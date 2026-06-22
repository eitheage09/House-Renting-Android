package com.example.assignment.ui;

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
import com.example.assignment.databinding.FragmentProfileInfoBinding;
import com.example.assignment.util.ImageUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class profileInfo extends Fragment {
    private FragmentProfileInfoBinding binding;
    private AuthViewModel auth;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileInfoBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        requireActivity().findViewById(R.id.bv).setVisibility(View.GONE);
        binding.cvEditProfile.setOnClickListener(v -> nav.navigate(R.id.editProfileFragment));

        auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                ImageUtils.setImageBytes(binding.profileInfoPicture, user.getPhoto());
                binding.ProfileName.setText(user.getName());
                binding.PfUsername.setText(user.getName());
                binding.PfEmail.setText(user.getEmail());
                binding.PfGender.setText(user.getGender().isEmpty() ? "请选择您的性别" : user.getGender());
                binding.PfAge.setText(user.getAge() == 0 ? "请输入您的年龄" : String.valueOf(user.getAge()));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                if (user.getBirthday() == 0L) {
                    binding.PfBirthday.setText("请选择您的出生日期");
                } else {
                    binding.PfBirthday.setText(dateFormat.format(new Date(user.getBirthday())));
                }
                binding.PfMobile.setText(user.getMobile().isEmpty() ? "请输入您的手机号" : user.getMobile());
            }
        });
        return binding.getRoot();
    }
}
