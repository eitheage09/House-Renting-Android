 package com.example.assignment.model;
 
 public class Favorite {
     private String favoriteId;
     private String userId;
     private String roomId;
     private transient Room room;
 
     public Favorite() { this.favoriteId = ""; this.userId = ""; this.roomId = ""; this.room = new Room(); }
     public Favorite(String userId, String roomId) { this(); this.userId = userId; this.roomId = roomId; }
 
     public String getFavoriteId() { return favoriteId; }
     public void setFavoriteId(String favoriteId) { this.favoriteId = favoriteId; }
     public String getUserId() { return userId; }
     public void setUserId(String userId) { this.userId = userId; }
     public String getRoomId() { return roomId; }
     public void setRoomId(String roomId) { this.roomId = roomId; }
     public Room getRoom() { return room; }
     public void setRoom(Room room) { this.room = room; }
 }
