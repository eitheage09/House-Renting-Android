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
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.model.User;
import com.example.assignment.databinding.FragmentMessageProfileInfoBinding;
import com.example.assignment.util.ImageUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageProfileInfo extends Fragment {
    private FragmentMessageProfileInfoBinding binding;
    private UserViewModel users;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageProfileInfoBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        users = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        String userMail = getArguments() != null ? getArguments().getString("userMail", "") : "";
        User user = users.get(userMail);

        if (user != null) {
            ImageUtils.setImageBytes(binding.profileInfoPicture, user.getPhoto());
            binding.ProfileName.setText(user.getName());
            binding.PfUsername.setText(user.getName());
            binding.PfEmail.setText(user.getEmail());
            binding.PfGender.setText(user.getGender().isEmpty() ? "请选择您的性别" : user.getGender());
            binding.PfAge.setText(user.getAge() == 0 ? "请输入您的年龄" : String.valueOf(user.getAge()));

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            binding.PfBirthday.setText(user.getBirthday() == 0L
                    ? "请选择您的出生日期" : df.format(new Date(user.getBirthday())));
            binding.PfMobile.setText(user.getMobile().isEmpty() ? "请输入您的手机号" : user.getMobile());
        }
        return binding.getRoot();
    }
}
