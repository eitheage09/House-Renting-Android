 package com.rent.backend.entity;
 
 import jakarta.persistence.*;
 
 @Entity
 @Table(name = "users")
 public class UserEntity {
     @Id private String email;
     private String password;
     private String name;
     @Lob private byte[] photo;
     private String gender;
     private int age;
     private long birthday;
     private String mobile;
     private String status;
     private long lastLoginTime;
     private String fcmToken;
 
     public UserEntity() {}
 
     public String getEmail() { return email; }
     public void setEmail(String email) { this.email = email; }
     public String getPassword() { return password; }
     public void setPassword(String password) { this.password = password; }
     public String getName() { return name; }
     public void setName(String name) { this.name = name; }
     public byte[] getPhoto() { return photo; }
     public void setPhoto(byte[] photo) { this.photo = photo; }
     public String getGender() { return gender; }
     public void setGender(String gender) { this.gender = gender; }
     public int getAge() { return age; }
     public void setAge(int age) { this.age = age; }
     public long getBirthday() { return birthday; }
     public void setBirthday(long birthday) { this.birthday = birthday; }
     public String getMobile() { return mobile; }
     public void setMobile(String mobile) { this.mobile = mobile; }
     public String getStatus() { return status; }
     public void setStatus(String status) { this.status = status; }
     public long getLastLoginTime() { return lastLoginTime; }
     public void setLastLoginTime(long lastLoginTime) { this.lastLoginTime = lastLoginTime; }
     public String getFcmToken() { return fcmToken; }
     public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; }
 }
