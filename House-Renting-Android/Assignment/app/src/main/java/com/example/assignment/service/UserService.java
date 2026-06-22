package com.example.assignment.service;

import android.util.Log;

import com.example.assignment.model.User;
import com.example.assignment.repository.UserRepository;

public class UserService {

    private static final String TAG = "UserService";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String validate(User user, boolean insert) {
        return userRepository.validate(user, insert);
    }

    public void updatePassword(String email, String newPassword, UserServiceCallback callback) {
        Log.d(TAG, "Trying to update password for email: " + email);
        User user = userRepository.get(email);
        if (user != null) {
            user.setPassword(newPassword);
            try {
                userRepository.set(user);
                callback.onResult(true, "Password updated successfully. Please try to login");
            } catch (Exception e) {
                callback.onResult(false, "Failed to update password.");
            }
        } else {
            callback.onResult(false, "User not found.");
        }
    }

    public interface UserServiceCallback {
        void onResult(boolean success, String message);
    }
}
