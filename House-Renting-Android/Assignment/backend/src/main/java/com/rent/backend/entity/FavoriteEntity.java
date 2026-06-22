 package com.rent.backend.entity;
 
 import jakarta.persistence.*;
 
 @Entity
 @Table(name = "favorites")
 public class FavoriteEntity {
     @Id private String favoriteId;
     private String userId;
     private String roomId;
 
     public FavoriteEntity() {}
 
     public String getFavoriteId() { return favoriteId; }
     public void setFavoriteId(String favoriteId) { this.favoriteId = favoriteId; }
     public String getUserId() { return userId; }
     public void setUserId(String userId) { this.userId = userId; }
     public String getRoomId() { return roomId; }
     public void setRoomId(String roomId) { this.roomId = roomId; }
 }
