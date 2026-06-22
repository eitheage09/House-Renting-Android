 package com.example.assignment.service;
 
 import android.content.Context;
 import android.content.SharedPreferences;
 
 import androidx.lifecycle.MutableLiveData;
 
 import com.example.assignment.api.ApiClient;
 import com.example.assignment.model.User;
 
 import java.util.List;
 
 public class AuthService {
 
     private final MutableLiveData<User> userLD = new MutableLiveData<>();
     private final Context context;
 
     public AuthService(Context context) {
         this.context = context;
         userLD.setValue(null);
     }
 
     public MutableLiveData<User> getUserLD() { return userLD; }
     public User getUser() { return userLD.getValue(); }
     public void setUser(User user) { userLD.setValue(user); }
 
     public boolean login(String email, String password, boolean remember) {
         String cachedEmail = getPreferences().getString("email_" + email, null);
         String cachedPassword = getPreferences().getString("password_" + email, null);
         if (email.equals(cachedEmail) && password.equals(cachedPassword)) {
             User cachedUser = loadUserFromPrefs(email);
             if (cachedUser != null) {
                 userLD.setValue(cachedUser);
                 if (remember) getPreferences().edit().putString("email", email).putString("password", password).apply();
                 return true;
             }
         }
         new Thread(() -> {
             try {
                 List<User> users = ApiClient.getInstance().getList("users", User.class);
                 if (users != null) for (User u : users) {
                     if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                         userLD.postValue(u); cacheUserLocal(u);
                         if (remember) getPreferences().edit().putString("email", email).putString("password", password).apply();
                         return;
                     }
                 }
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
         return true;
     }
 
     public void logout() {
         userLD.setValue(null);
         getPreferences().edit().remove("email").remove("password").apply();
     }
 
     public void loginFromPreferences() {
         String email = getPreferences().getString("email", null);
         String password = getPreferences().getString("password", null);
         if (email != null && password != null) login(email, password, false);
     }
 
     public void updateUserStatus(String status) {
         User user = userLD.getValue();
         if (user != null) {
             user.setStatus(status);
             if ("offline".equals(status)) user.setLastLoginTime(System.currentTimeMillis());
             User finalUser = user;
             new Thread(() -> {
                 try { ApiClient.getInstance().put("users/" + finalUser.getEmail(), finalUser, User.class); }
                 catch (Exception e) { e.printStackTrace(); }
             }).start();
         }
     }
 
     public void cacheUserLocal(User user) {
         if (user == null || user.getEmail() == null) return;
         getPreferences().edit()
             .putString("email_" + user.getEmail(), user.getEmail())
             .putString("password_" + user.getEmail(), user.getPassword() != null ? user.getPassword() : "")
             .putString("name_" + user.getEmail(), user.getName() != null ? user.getName() : "")
             .putString("gender_" + user.getEmail(), user.getGender() != null ? user.getGender() : "")
             .putInt("age_" + user.getEmail(), user.getAge())
             .putString("mobile_" + user.getEmail(), user.getMobile() != null ? user.getMobile() : "")
             .apply();
     }
 
     private User loadUserFromPrefs(String email) {
         String savedEmail = getPreferences().getString("email_" + email, null);
         if (savedEmail == null) return null;
         User user = new User();
         user.setEmail(savedEmail);
         user.setPassword(getPreferences().getString("password_" + email, ""));
         user.setName(getPreferences().getString("name_" + email, ""));
         user.setGender(getPreferences().getString("gender_" + email, ""));
         user.setAge(getPreferences().getInt("age_" + email, 0));
         user.setMobile(getPreferences().getString("mobile_" + email, ""));
         return user;
     }
 
     private SharedPreferences getPreferences() {
         return context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
     }
 }
