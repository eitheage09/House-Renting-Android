package com.example.assignment.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.model.User;
import com.example.assignment.databinding.FragmentAccountBinding;
import com.example.assignment.util.FragmentUtils;
import com.example.assignment.util.ImageUtils;

public class Account extends Fragment {

    private FragmentAccountBinding binding;
    private AuthViewModel auth;
    private UserViewModel vm;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);

        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        vm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        requireActivity().findViewById(R.id.bv).setVisibility(View.VISIBLE);

        binding.LayoutProfileInfo.setOnClickListener(v -> nav.navigate(R.id.profileInfoFragment));
        binding.LayoutHelpCentre.setOnClickListener(v -> nav.navigate(R.id.helpCentreFragment));
        binding.LayoutSetting.setOnClickListener(v -> nav.navigate(R.id.settingFragment));
        binding.LayoutLogin.setOnClickListener(v -> nav.navigate(R.id.loginFragment));
        binding.LayoutLogout.setOnClickListener(v -> ToLogout());
        binding.btnChangePicture.setOnClickListener(v -> select());

        SharedPreferences sharedPref = requireContext().getSharedPreferences("Mode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        boolean nightMode = sharedPref.getBoolean("night", false);
        binding.switchNight.setChecked(nightMode);
        binding.switchNight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("night", true).apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("night", false).apply();
            }
        });

        auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                updateUI(user);
            } else {
                logoutState();
            }
        });

        return binding.getRoot();
    }

    private void updateUI(User user) {
        binding.LblUsername.setText(user.getName());
        ImageUtils.setImageBytes(binding.profilePicture, user.getPhoto());
        binding.relativeProfileInfo.setVisibility(View.VISIBLE);
        binding.LayoutProfileInfo.setVisibility(View.VISIBLE);
        binding.relativeLogin.setVisibility(View.GONE);
        binding.LayoutLogin.setVisibility(View.GONE);
        binding.relativeLogout.setVisibility(View.VISIBLE);
        binding.LayoutLogout.setVisibility(View.VISIBLE);
        binding.changephotolayout.setVisibility(View.VISIBLE);
    }

    private void logoutState() {
        binding.LblUsername.setText("游客");
        android.graphics.Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
        binding.profilePicture.setImageBitmap(bitmap);
        binding.relativeProfileInfo.setVisibility(View.GONE);
        binding.LayoutProfileInfo.setVisibility(View.GONE);
        binding.relativeLogout.setVisibility(View.GONE);
        binding.LayoutLogout.setVisibility(View.GONE);
        binding.relativeLogin.setVisibility(View.VISIBLE);
        binding.LayoutLogin.setVisibility(View.VISIBLE);
        binding.changephotolayout.setVisibility(View.GONE);
    }

    private boolean ToLogout() {
        auth.updateUserStatus("offline");
        new AlertDialog.Builder(requireContext())
                .setTitle("确认退出")
                .setMessage("您确定要退出登录吗？")
                .setPositiveButton("Yes", (dialog, which) -> {
                    auth.logout();
                    FragmentUtils.infoDialog(this, "退出登录成功！");
                    nav.navigate(R.id.firstFragment);
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
        return true;
    }

    private final androidx.activity.result.ActivityResultLauncher<String> getContent =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                binding.profilePicture.setImageURI(uri);
                changePhoto();
            });

    private void select() {
        getContent.launch("image/*");
    }

    private void changePhoto() {
        User currentUser = auth.getUserLD().getValue();
        if (currentUser != null) {
            User updatedUser = new User();
            updatedUser.setPhoto(ImageUtils.imageViewToBytes(binding.profilePicture, 300, 300));
            updatedUser.setPassword(currentUser.getPassword());
            updatedUser.setName(currentUser.getName());
            updatedUser.setEmail(currentUser.getEmail());
            updatedUser.setGender(currentUser.getGender());
            updatedUser.setAge(currentUser.getAge());
            updatedUser.setBirthday(currentUser.getBirthday());
            updatedUser.setMobile(currentUser.getMobile());

            vm.set(updatedUser);
            auth.setUser(updatedUser);
        }
    }
}
