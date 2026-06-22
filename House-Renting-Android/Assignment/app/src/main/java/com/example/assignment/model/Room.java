 package com.example.assignment.model;
 
import java.util.ArrayList;
import java.util.List;
 import com.google.gson.annotations.JsonAdapter;
 import com.example.assignment.util.HomeAmenitiesAdapter;
 
 public class Room {
     private String roomId;
     private String roomName;
     private String roomPlace;
     private String roomDetail;
     private String photoJson;
     private List<Integer> imageList;
     private double roomSize;
     private String roomType;
     private int numOfBed;
     private int numOfBath;
     @JsonAdapter(HomeAmenitiesAdapter.class)
     private List<String> homeAmenities;
     private double roomPrice;
     private double latitude;
     private double longitude;
     private String userId;
     private transient boolean isFavorite;
 
     public Room() {
         this.roomId = ""; this.roomName = ""; this.roomPlace = ""; this.roomDetail = "";
         this.photoJson = "[]"; this.imageList = imageList; this.roomSize = 0.0; this.roomType = "";
         this.numOfBed = 0; this.numOfBath = 0;
         this.homeAmenities = new ArrayList<>(); this.roomPrice = 0.0;
         this.latitude = 0.0; this.longitude = 0.0; this.userId = ""; this.isFavorite = false;
     }
 
     public String getRoomId() { return roomId; }
     public void setRoomId(String roomId) { this.roomId = roomId; }
     public String getRoomName() { return roomName; }
     public void setRoomName(String roomName) { this.roomName = roomName; }
     public String getRoomPlace() { return roomPlace; }
     public void setRoomPlace(String roomPlace) { this.roomPlace = roomPlace; }
     public String getRoomDetail() { return roomDetail; }
     public void setRoomDetail(String roomDetail) { this.roomDetail = roomDetail; }
     public String getPhotoJson() { return photoJson; }
     public void setPhotoJson(String photoJson) { this.photoJson = photoJson; }
     public List<Integer> getImageList() {
         return imageList;
     }
     public double getRoomSize() { return roomSize; }
     public void setRoomSize(double roomSize) { this.roomSize = roomSize; }
     public String getRoomType() { return roomType; }
     public void setRoomType(String roomType) { this.roomType = roomType; }
     public int getNumOfBed() { return numOfBed; }
     public void setNumOfBed(int numOfBed) { this.numOfBed = numOfBed; }
     public int getNumOfBath() { return numOfBath; }
     public void setNumOfBath(int numOfBath) { this.numOfBath = numOfBath; }
     public List<String> getHomeAmenities() { return homeAmenities; }
     public void setHomeAmenities(List<String> homeAmenities) { this.homeAmenities = homeAmenities; }
     public double getRoomPrice() { return roomPrice; }
     public void setRoomPrice(double roomPrice) { this.roomPrice = roomPrice; }
     public double getLatitude() { return latitude; }
     public void setLatitude(double latitude) { this.latitude = latitude; }
     public double getLongitude() { return longitude; }
     public void setLongitude(double longitude) { this.longitude = longitude; }
     public String getUserId() { return userId; }
     public void setUserId(String userId) { this.userId = userId; }
     public boolean isFavorite() { return isFavorite; }
     public void setFavorite(boolean favorite) { isFavorite = favorite; }
 }
