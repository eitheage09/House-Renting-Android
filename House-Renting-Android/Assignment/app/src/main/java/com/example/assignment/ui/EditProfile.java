package com.example.assignment.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment.R;
import com.example.assignment.controller.AuthViewModel;
import com.example.assignment.controller.UserViewModel;
import com.example.assignment.model.User;
import com.example.assignment.databinding.FragmentEditProfileBinding;
import com.example.assignment.util.FragmentUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditProfile extends Fragment implements DatePickerDialog.OnDateSetListener {
    private FragmentEditProfileBinding binding;
    private AuthViewModel auth;
    private UserViewModel vm;
    private NavController nav;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        nav = NavHostFragment.findNavController(this);
        auth = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        vm = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        requireActivity().findViewById(R.id.bv).setVisibility(View.GONE);

        binding.buttonSave.setOnClickListener(v -> {
            try {
                save();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        binding.btnDatePicker.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(requireContext(), this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        auth.getUserLD().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.editTextEmail.setText(user.getEmail());
                binding.editTextName.setText(user.getName().isEmpty() ? "" : user.getName());
                binding.editTextName.setHint(user.getName().isEmpty() ? "请输入用户名" : "");

                if (!user.getGender().isEmpty()) {
                    if ("Male".equals(user.getGender())) binding.radioGroupGender.check(R.id.radioMale);
                    else if ("Female".equals(user.getGender())) binding.radioGroupGender.check(R.id.radioFemale);
                }
                binding.editTextAge.setText(user.getAge() == 0 ? "" : String.valueOf(user.getAge()));
                binding.editTextAge.setHint(user.getAge() == 0 ? "请输入年龄" : "");
                binding.editTextBirthday.setText(user.getBirthday() == 0L ? "" : dateFormat.format(new Date(user.getBirthday())));
                binding.editTextBirthday.setHint(user.getBirthday() == 0L ? "请选择出生日期" : "");
                binding.editTextMobile.setText(user.getMobile().isEmpty() ? "" : user.getMobile());
                binding.editTextMobile.setHint(user.getMobile().isEmpty() ? "请输入手机号" : "");
            }
        });

        return binding.getRoot();
    }

    private void save() throws ParseException {
        String username = binding.editTextName.getText().toString().trim();
        if (username.isEmpty()) { FragmentUtils.errorDialog(this, "用户名不能为空"); return; }
        if (username.length() > 15) { FragmentUtils.errorDialog(this, "用户名长度不能超过15个字符"); return; }

        String selectedGender = "";
        if (binding.radioGroupGender.getCheckedRadioButtonId() == R.id.radioMale) selectedGender = "Male";
        else if (binding.radioGroupGender.getCheckedRadioButtonId() == R.id.radioFemale) selectedGender = "Female";

        String pickBirthday = binding.editTextBirthday.getText().toString().trim();
        long birthdayTimestamp = pickBirthday.isEmpty() ? 0L
                : java.util.Objects.requireNonNull(dateFormat.parse(pickBirthday)).getTime();

        int age = 0;
        if (!binding.editTextAge.getText().toString().trim().isEmpty()) {
            age = Integer.parseInt(binding.editTextAge.getText().toString().trim());
        }

        User currentUser = auth.getUserLD().getValue();
        if (currentUser != null) {
            User updated = new User();
            updated.setPhoto(currentUser.getPhoto());
            updated.setPassword(currentUser.getPassword());
            updated.setName(username);
            updated.setEmail(currentUser.getEmail());
            updated.setGender(selectedGender);
            updated.setAge(age);
            updated.setBirthday(birthdayTimestamp);
            updated.setMobile(binding.editTextMobile.getText().toString().trim());

            vm.set(updated);
            auth.setUser(updated);
        }
        nav.navigateUp();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        binding.editTextBirthday.setText(dateFormat.format(calendar.getTime()));
    }
}
