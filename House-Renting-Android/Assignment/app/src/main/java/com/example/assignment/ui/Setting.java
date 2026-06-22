package com.example.assignment.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.model.User;
import com.example.assignment.databinding.FragmentSettingBinding;

public class Setting extends Fragment {
    private FragmentSettingBinding binding;
    private AuthViewModel auth;
    private UserViewModel vm;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        vm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        requireActivity().findViewById(R.id.bv).setVisibility(View.VISIBLE);

        auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.LayoutChangePsw.setVisibility(View.VISIBLE);
                binding.LayoutDltAccount.setVisibility(View.VISIBLE);
                binding.rltChangePsw.setVisibility(View.VISIBLE);
                binding.rltDltAccount.setVisibility(View.VISIBLE);
            } else {
                binding.rltChangePsw.setVisibility(View.GONE);
                binding.rltDltAccount.setVisibility(View.GONE);
                binding.LayoutChangePsw.setVisibility(View.GONE);
                binding.LayoutDltAccount.setVisibility(View.GONE);
            }
        });

        SharedPreferences sharedPref = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isNotificationsEnabled = sharedPref.getBoolean("notifications_enabled", true);
        binding.switchNotification.setChecked(isNotificationsEnabled);

        binding.switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPref.edit().putBoolean("notifications_enabled", isChecked).apply();
        });

        binding.LayoutChangePsw.setOnClickListener(v -> nav.navigate(R.id.changePasswordFragment));
        binding.LayoutDltAccount.setOnClickListener(v -> showDeleteAccountConfirmationDialog());

        return binding.getRoot();
    }

    private void showDeleteAccountConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("删除账户")
                .setMessage("您确定要删除您的账户吗？此操作无法撤销。")
                .setPositiveButton("Yes", (dialog, which) -> {
                    User u = auth.getUser();
                    if (u != null) vm.delete(u.getEmail());
                    nav.navigate(R.id.accountFragment);
                })
                .setNegativeButton("No", null)
                .show();
    }
}
