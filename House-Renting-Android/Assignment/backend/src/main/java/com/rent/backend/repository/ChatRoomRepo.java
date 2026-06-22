 package com.rent.backend.repository;
 
 import com.rent.backend.entity.ChatRoomEntity;
 import org.springframework.data.jpa.repository.JpaRepository;
 
 public interface ChatRoomRepo extends JpaRepository<ChatRoomEntity, String> {}
