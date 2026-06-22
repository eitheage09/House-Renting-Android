package com.example.assignment.ui;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.databinding.FragmentEmailBinding;
import com.example.assignment.util.FragmentUtils;
import com.example.assignment.util.SimpleEmail;

public class Email extends Fragment {
    private FragmentEmailBinding binding;
    private AuthViewModel auth;
    private UserViewModel vm;
    private NavController nav;

    private String verificationCode;
    private int verificationAttempts = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmailBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        vm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        requireActivity().findViewById(R.id.bv).setVisibility(View.GONE);

        binding.EmailCode.requestFocus();
        binding.btnSend.setOnClickListener(v -> send());
        binding.btnVerify.setOnClickListener(v -> verify());
        binding.btnSubmitPassword.setOnClickListener(v -> submitPassword());
        binding.imgViewedtNewPassword.setOnClickListener(v -> toggleVisibility(binding.edtNewPassword, binding.imgViewedtNewPassword));
        binding.imgViewedtConfirmPassword.setOnClickListener(v -> toggleVisibility(binding.edtConfirmPassword, binding.imgViewedtConfirmPassword));

        return binding.getRoot();
    }

    private void toggleVisibility(EditText editText, ImageView imageView) {
        if (editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageView.setImageResource(R.drawable.ic_visibility_off);
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageView.setImageResource(R.drawable.ic_visibility_on);
        }
        editText.setSelection(editText.getText().length());
    }

    private void send() {
        FragmentUtils.hideKeyboard(this);
        String email = binding.EmailCode.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FragmentUtils.errorDialog(this, "无效的邮箱。");
            binding.EmailCode.requestFocus();
            return;
        }
        sendVerificationCode(email);
    }

    private void sendVerificationCode(String email) {
        verificationCode = String.format("%04d", (int)(Math.random() * 10000));
        String subject = "重置密码验证码 - " + verificationCode;
        String content = "<p>Your <b>Reset Password Code</b> is:</p>" +
                "<h1 style=\"color: red\">" + verificationCode + "</h1><p>Thank you.</p>";

        FragmentUtils.snackbar(this, "正在发送邮件...");
        binding.btnSend.setEnabled(false);

        new SimpleEmail()
                .to(email)
                .subject(subject)
                .content(content)
                .isHtml(true)
                .send(() -> {
                    requireActivity().runOnUiThread(() -> {
                        FragmentUtils.snackbar(this, "邮件已发送...");
                        binding.lblVerify.setVisibility(View.VISIBLE);
                        binding.edtVerifyCode.setVisibility(View.VISIBLE);
                        binding.btnVerify.setVisibility(View.VISIBLE);
                        binding.EmailCode.setEnabled(false);
                        binding.btnSend.setEnabled(false);
                        binding.edtVerifyCode.requestFocus();
                        verificationAttempts = 0;
                    });
                });
    }

    private void verify() {
        FragmentUtils.hideKeyboard(this);
        String enteredCode = binding.edtVerifyCode.getText().toString().trim();
        if (enteredCode.equals(verificationCode)) {
            FragmentUtils.snackbar(this, "验证成功！");
            binding.lblVerify.setVisibility(View.GONE);
            binding.edtVerifyCode.setVisibility(View.GONE);
            binding.btnVerify.setVisibility(View.GONE);
            binding.lblNewPassword.setVisibility(View.VISIBLE);
            binding.edtNewPassword.setVisibility(View.VISIBLE);
            binding.lblConfirmNewPassword.setVisibility(View.VISIBLE);
            binding.edtConfirmPassword.setVisibility(View.VISIBLE);
            binding.imgViewedtNewPassword.setVisibility(View.VISIBLE);
            binding.imgViewedtConfirmPassword.setVisibility(View.VISIBLE);
            binding.btnSubmitPassword.setVisibility(View.VISIBLE);
        } else {
            verificationAttempts++;
            if (verificationAttempts >= 3) {
                verificationAttempts = 0;
                binding.edtVerifyCode.setText("");
                binding.lblVerify.setVisibility(View.GONE);
                binding.edtVerifyCode.setVisibility(View.GONE);
                binding.btnVerify.setVisibility(View.GONE);
                binding.EmailCode.setEnabled(true);
                binding.btnSend.setEnabled(true);
                FragmentUtils.snackbar(this, "请重新输入您的邮箱并点击发送。");
            } else {
                FragmentUtils.errorDialog(this, "无效的验证码。");
                binding.edtVerifyCode.requestFocus();
            }
        }
    }

    private void submitPassword() {
        FragmentUtils.hideKeyboard(this);
        String newPassword = binding.edtNewPassword.getText().toString().trim();
        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();
        String email = binding.EmailCode.getText().toString().trim();

        if (email.isEmpty()) { FragmentUtils.errorDialog(this, "邮箱字段不能为空。"); return; }
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) { FragmentUtils.errorDialog(this, "密码字段不能为空。"); return; }
        if (newPassword.length() < 6) { FragmentUtils.errorDialog(this, "密码太短（至少6个字符）。"); return; }
        if (!newPassword.equals(confirmPassword)) { FragmentUtils.errorDialog(this, "密码不匹配。"); return; }

        vm.updatePassword(email, newPassword, (success, message) -> {
            requireActivity().runOnUiThread(() -> {
                if (success) {
                    FragmentUtils.snackbar(this, message);
                    nav.popBackStack();
                } else {
                    FragmentUtils.errorDialog(this, message);
                }
            });
        });
    }
}
