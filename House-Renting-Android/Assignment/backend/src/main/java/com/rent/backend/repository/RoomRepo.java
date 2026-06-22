 package com.rent.backend.repository;
 
 import com.rent.backend.entity.RoomEntity;
 import org.springframework.data.jpa.repository.JpaRepository;
 
 public interface RoomRepo extends JpaRepository<RoomEntity, String> {}
