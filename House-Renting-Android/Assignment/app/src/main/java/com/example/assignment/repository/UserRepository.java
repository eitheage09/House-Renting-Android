 package com.example.assignment.repository;
 
 import androidx.lifecycle.MutableLiveData;
 
 import com.example.assignment.api.ApiClient;
 import com.example.assignment.model.User;
 
 import java.util.ArrayList;
 import java.util.List;
 
 public class UserRepository {
 
     private final MutableLiveData<List<User>> usersLD = new MutableLiveData<>();
     private List<User> cachedUsers = new ArrayList<>();
 
     public UserRepository() { loadAllUsers(); }
 
     public MutableLiveData<List<User>> getUsersLD() { return usersLD; }
 
     public User get(String email) {
         for (User u : cachedUsers) {
             if (u.getEmail() != null && u.getEmail().equals(email)) return u;
         }
         return null;
     }
 
     public void set(User user) {
         if (user == null || user.getEmail() == null) return;
         new Thread(() -> {
             try {
                 ApiClient.getInstance().put("users/" + user.getEmail(), user, User.class);
                 loadAllUsers(); // refresh cache
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void add(User user) {
         if (user == null || user.getEmail() == null) return;
         new Thread(() -> {
             try {
                 ApiClient.getInstance().post("users", user, User.class);
                 loadAllUsers();
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void delete(String email) {
         if (email == null) return;
         new Thread(() -> {
             try {
                 ApiClient.getInstance().delete("users/" + email);
                 loadAllUsers();
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
    public void cleanup() { /* no-op for API-based repo */ }

    public java.util.List<User> getAll() { return new java.util.ArrayList<>(cachedUsers); }

    public androidx.lifecycle.MutableLiveData<java.util.List<User>> getUserUpdate(String email) {
        androidx.lifecycle.MutableLiveData<java.util.List<User>> ld = new androidx.lifecycle.MutableLiveData<>();
        User u = get(email);
        ld.setValue(u != null ? java.util.Collections.singletonList(u) : new java.util.ArrayList<>());
        return ld;
    }

    public void deleteAll() {
        for (User u : cachedUsers) if (u.getEmail() != null) delete(u.getEmail());
    }

    public String validate(User user, boolean insert) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) return "Email required";
        if (user.getPassword() == null || user.getPassword().isEmpty()) return "Password required";
        if (insert && get(user.getEmail()) != null) return "Email already exists";
        return "";
    }

    private void loadAllUsers() {
         new Thread(() -> {
             try {
                 List<User> users = ApiClient.getInstance().getList("users", User.class);
                 cachedUsers = (users != null) ? users : new ArrayList<>();
                 usersLD.postValue(new ArrayList<>(cachedUsers));
             } catch (Exception e) {
                 e.printStackTrace();
                 usersLD.postValue(new ArrayList<>());
             }
         }).start();
     }
 }
