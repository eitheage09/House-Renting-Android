package com.example.assignment.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment.model.User;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.service.UserService;

import java.util.List;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;

    public void setUserRepository(UserRepository repository) {
        this.userRepository = repository;
    }

    public void init() {}

     public void add(User user) { if (userRepository != null) userRepository.add(user); }
 
    public MutableLiveData<java.util.List<User>> getUsersLD() {
        return userRepository != null ? userRepository.getUsersLD() : new MutableLiveData<>();
    }

    public java.util.List<User> getAll() { return userRepository != null ? userRepository.getAll() : null; }

    public User get(String email) { return userRepository != null ? userRepository.get(email) : null; }

    public LiveData<List<User>> getUserUpdate(String email) {
        return userRepository != null ? userRepository.getUserUpdate(email) : new MutableLiveData<>();
    }

    public void set(User user) { if (userRepository != null) userRepository.set(user); }

    public void delete(String email) { if (userRepository != null) userRepository.delete(email); }

    public void deleteAll() { if (userRepository != null) userRepository.deleteAll(); }

    public void updatePassword(String email, String newPassword, UserService.UserServiceCallback callback) {
        if (userRepository != null) {
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
    }

    public String validate(User user, boolean insert) {
        return userRepository != null ? userRepository.validate(user, insert) : "";
    }
}
