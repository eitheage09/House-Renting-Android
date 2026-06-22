 package com.rent.backend.repository;
 
 import com.rent.backend.entity.UserEntity;
 import org.springframework.data.jpa.repository.JpaRepository;
 
 public interface UserRepo extends JpaRepository<UserEntity, String> {}
