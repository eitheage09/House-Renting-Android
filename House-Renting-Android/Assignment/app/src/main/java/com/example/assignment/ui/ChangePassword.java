package com.example.assignment.ui;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.example.assignment.databinding.FragmentChangePasswordBinding;
import com.example.assignment.model.User;
import com.example.assignment.util.FragmentUtils;

public class ChangePassword extends Fragment {
    private FragmentChangePasswordBinding binding;
    private AuthViewModel auth;
    private UserViewModel vm;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        vm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        requireActivity().findViewById(R.id.bv).setVisibility(View.GONE);

        binding.btnVerifyCurrentPsw.setOnClickListener(v -> verifyCurrentPassword());
        binding.btnConfirm.setOnClickListener(v -> confirmNewPassword());
        binding.imgViewChangeNewPsw.setOnClickListener(v -> toggleVisibility(binding.edtChangeNewPsw, binding.imgViewChangeNewPsw));
        binding.imgViewConfirmChangeNewPsw.setOnClickListener(v -> toggleVisibility(binding.edtConfirmChangeNewPsw, binding.imgViewConfirmChangeNewPsw));

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

    private void verifyCurrentPassword() {
        String currentPsw = binding.edtCurrentPsw.getText().toString().trim();
        if (currentPsw.isEmpty()) { FragmentUtils.errorDialog(this, "当前密码不能为空。"); return; }

        auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
            if (user != null && user.getPassword().equals(currentPsw)) {
                binding.lblChangeNewPsw.setVisibility(View.VISIBLE);
                binding.edtChangeNewPsw.setVisibility(View.VISIBLE);
                binding.lblConfirmChangeNewPsw.setVisibility(View.VISIBLE);
                binding.edtConfirmChangeNewPsw.setVisibility(View.VISIBLE);
                binding.imgViewChangeNewPsw.setVisibility(View.VISIBLE);
                binding.imgViewConfirmChangeNewPsw.setVisibility(View.VISIBLE);
                binding.btnConfirm.setVisibility(View.VISIBLE);
                binding.edtChangeNewPsw.requestFocus();
                binding.edtCurrentPsw.setEnabled(false);
                binding.btnVerifyCurrentPsw.setEnabled(false);
            } else {
                FragmentUtils.errorDialog(this, "当前密码不正确。");
            }
        });
    }

    private void confirmNewPassword() {
        String newPsw = binding.edtChangeNewPsw.getText().toString().trim();
        String confirmPsw = binding.edtConfirmChangeNewPsw.getText().toString().trim();

        if (newPsw.isEmpty() || confirmPsw.isEmpty()) { FragmentUtils.errorDialog(this, "密码字段不能为空。"); return; }
        if (newPsw.length() < 6) { FragmentUtils.errorDialog(this, "密码太短（至少6个字符）。"); return; }
        if (!newPsw.equals(confirmPsw)) { FragmentUtils.errorDialog(this, "新密码和确认密码不匹配。"); return; }

        auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                User updated = new User();
                updated.setEmail(user.getEmail());
                updated.setPassword(newPsw);
                updated.setName(user.getName());
                updated.setPhoto(user.getPhoto());
                vm.set(updated);
                FragmentUtils.toast(this, "密码修改成功！");
                nav.navigateUp();
            }
        });
    }
}
