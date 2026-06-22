 package com.rent.backend.repository;
 
 import com.rent.backend.entity.ChatEntity;
 import org.springframework.data.jpa.repository.JpaRepository;
 
 public interface ChatRepo extends JpaRepository<ChatEntity, String> {}
