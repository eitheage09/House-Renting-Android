 package com.rent.backend.controller;
 
 import com.rent.backend.entity.UserEntity;
 import com.rent.backend.repository.UserRepo;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 
 import java.util.List;
 
 @RestController
 @RequestMapping("/api/users")
 public class UserController {
 
     @Autowired
     private UserRepo userRepo;
 
     @GetMapping
     public List<UserEntity> getAll() {
         return userRepo.findAll();
     }
 
     @GetMapping("/{email}")
     public ResponseEntity<UserEntity> get(@PathVariable String email) {
         return userRepo.findById(email)
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
     }
 
     @PostMapping
     public UserEntity create(@RequestBody UserEntity user) {
         return userRepo.save(user);
     }
 
     @PutMapping("/{email}")
     public ResponseEntity<UserEntity> update(@PathVariable String email, @RequestBody UserEntity user) {
         return userRepo.findById(email)
                 .map(existing -> {
                     if (user.getPassword() != null && !user.getPassword().isEmpty()) existing.setPassword(user.getPassword());
                     if (user.getName() != null) existing.setName(user.getName());
                     if (user.getPhoto() != null) existing.setPhoto(user.getPhoto());
                     if (user.getGender() != null) existing.setGender(user.getGender());
                     existing.setAge(user.getAge());
                     existing.setBirthday(user.getBirthday());
                     if (user.getMobile() != null) existing.setMobile(user.getMobile());
                     if (user.getStatus() != null) existing.setStatus(user.getStatus());
                     existing.setLastLoginTime(user.getLastLoginTime());
                     if (user.getFcmToken() != null) existing.setFcmToken(user.getFcmToken());
                     return ResponseEntity.ok(userRepo.save(existing));
                 })
                 .orElse(ResponseEntity.notFound().build());
     }
 
     @DeleteMapping("/{email}")
     public ResponseEntity<Void> delete(@PathVariable String email) {
         if (userRepo.existsById(email)) {
             userRepo.deleteById(email);
             return ResponseEntity.ok().build();
         }
         return ResponseEntity.notFound().build();
     }
 }
