package com.example.assignment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.model.User;
import com.example.assignment.databinding.FragmentRegisterBinding;
import com.example.assignment.util.FragmentUtils;
import com.example.assignment.util.ImageUtils;

public class Register extends Fragment {

    private FragmentRegisterBinding binding;
    private UserViewModel vm;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        vm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        reset();
        binding.imgPhoto.setOnClickListener(v -> select());
        binding.btnRegister.setOnClickListener(v -> register());

        return binding.getRoot();
    }

    private void reset() {
        binding.edtEmail.getText().clear();
        binding.edtPassword.getText().clear();
        binding.edtName.getText().clear();
        binding.imgPhoto.setImageDrawable(null);
        binding.edtEmail.requestFocus();
    }

    private final androidx.activity.result.ActivityResultLauncher<String> getContent =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                binding.imgPhoto.setImageURI(uri);
            });

    private void select() {
        getContent.launch("image/*");
    }

    private void register() {
        String confirmPassword = binding.confirmedtPassword.getText().toString();

        if (confirmPassword.isEmpty()) {
            FragmentUtils.errorDialog(this, "请确认密码。");
            return;
        }
        if (!binding.edtPassword.getText().toString().equals(confirmPassword)) {
            FragmentUtils.errorDialog(this, "密码不匹配。");
            return;
        }

        User user = new User();
        user.setEmail(binding.edtEmail.getText().toString().trim());
        user.setPassword(binding.edtPassword.getText().toString().trim());
        user.setName(binding.edtName.getText().toString().trim());
        user.setPhoto(ImageUtils.imageViewToBytes(binding.imgPhoto, 300, 300));

        String e = vm.validate(user, true);
        if (!e.isEmpty()) {
            FragmentUtils.errorDialog(this, e);
            return;
        }

         vm.add(user);
        nav.navigateUp();
        FragmentUtils.infoDialog(this, "注册成功！\\n请登录");
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.hideBV();
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.showBV();
    }
}
