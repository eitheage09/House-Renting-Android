 package com.rent.backend.repository;
 
 import com.rent.backend.entity.FavoriteEntity;
 import org.springframework.data.jpa.repository.JpaRepository;
 
 public interface FavoriteRepo extends JpaRepository<FavoriteEntity, String> {}
