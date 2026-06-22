package com.example.assignment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.databinding.FragmentFirstBinding;

public class First extends Fragment {

    private FragmentFirstBinding binding;
    private NavController nav;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);

        binding.btnLogin.setOnClickListener(v -> nav.navigate(R.id.loginFragment));

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        AuthViewModel auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        new Thread(() -> {
            while (auth.getUser() == null) {
                try { Thread.sleep(40); } catch (InterruptedException e) { break; }
            }
            requireActivity().runOnUiThread(() -> {
                if (auth.getUser() != null) {
                    nav.navigate(R.id.homeFragment);
                }
            });
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity mainActivity = (MainActivity) requireActivity();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().hide();
        mainActivity.hideBV();
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity mainActivity = (MainActivity) requireActivity();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        mainActivity.showBV();
    }
}
