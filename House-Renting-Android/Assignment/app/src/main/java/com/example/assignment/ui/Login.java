 package com.example.assignment.ui;
 
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 
 import androidx.fragment.app.Fragment;
 import androidx.lifecycle.ViewModelProvider;
 import androidx.navigation.NavController;
 import androidx.navigation.fragment.NavHostFragment;
 
 import com.example.assignment.MainActivity;
 import com.example.assignment.R;
 import com.example.assignment.controller.AuthViewModel;
 import com.example.assignment.databinding.FragmentLoginBinding;
 import com.example.assignment.util.FragmentUtils;
 
 public class Login extends Fragment {
 
     private FragmentLoginBinding binding;
     private AuthViewModel auth;
     private NavController nav;
 
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         binding = FragmentLoginBinding.inflate(inflater, container, false);
         nav = NavHostFragment.findNavController(this);
 
         auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
 
         reset();
 
         binding.btnLogin.setOnClickListener(v -> login());
         binding.goRegister.setOnClickListener(v -> nav.navigate(R.id.registerFragment));
         binding.lblForgetPassword.setOnClickListener(v -> nav.navigate(R.id.emailFragement));
 
         return binding.getRoot();
     }
 
     private void reset() {
         binding.edtEmail.getText().clear();
         binding.edtPassword.getText().clear();
         binding.chkRemember.setChecked(false);
         binding.edtEmail.requestFocus();
     }
 
     private void login() {
         String email = binding.edtEmail.getText().toString().trim();
         String password = binding.edtPassword.getText().toString().trim();
         boolean remember = binding.chkRemember.isChecked();
 
         if (email.isEmpty()) { FragmentUtils.errorDialog(this, "请输入邮箱"); return; }
         if (password.isEmpty()) { FragmentUtils.errorDialog(this, "请输入密码"); return; }
 
         boolean success = auth.login(email, password, remember);
         if (success && auth.getUser() != null) {
             nav.navigate(R.id.homeFragment);
         } else {
             // Login via Firestore is async - observe for result
             auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
                 if (user != null) {
                     nav.navigate(R.id.homeFragment);
                 }
             });
             binding.btnLogin.postDelayed(() -> {
                 if (auth.getUser() == null) {
                     FragmentUtils.errorDialog(this, "登录凭据无效。");
                 }
             }, 3000);
         }
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
