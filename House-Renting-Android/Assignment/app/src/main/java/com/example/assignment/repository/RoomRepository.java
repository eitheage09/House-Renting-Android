 package com.example.assignment.repository;
 
 import androidx.lifecycle.MutableLiveData;
 
 import com.example.assignment.api.ApiClient;
 import com.example.assignment.model.Room;
 
 import java.util.ArrayList;
 import java.util.List;
 
 public class RoomRepository {
 
     private final MutableLiveData<List<Room>> roomsLD = new MutableLiveData<>();
     private List<Room> cachedRooms = new ArrayList<>();
     private String currentFilter = "";
 
     public RoomRepository() { loadAllRooms(); }
     public MutableLiveData<List<Room>> getRoomsLD() { return roomsLD; }
 
     public void init() { loadAllRooms(); }
     public void clearSearch() { currentFilter = ""; roomsLD.postValue(new ArrayList<>(cachedRooms)); }
 
     public Room get(String roomId) {
         for (Room r : cachedRooms) {
             if (r.getRoomId() != null && r.getRoomId().equals(roomId)) return r;
         }
         return null;
     }
 
     public void add(Room room) {
         if (room == null) return;
         new Thread(() -> {
             try {
                 ApiClient.getInstance().post("rooms", room, Room.class);
                 loadAllRooms();
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void set(Room room) {
         if (room == null || room.getRoomId() == null) return;
         new Thread(() -> {
             try {
                 ApiClient.getInstance().put("rooms/" + room.getRoomId(), room, Room.class);
                 loadAllRooms();
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void delete(String roomId) {
         if (roomId == null) return;
         new Thread(() -> {
             try {
                 ApiClient.getInstance().delete("rooms/" + roomId);
                 loadAllRooms();
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void searchByLocation(double lat, double lng) {
         currentFilter = "location";
         new Thread(() -> {
             List<Room> filtered = new ArrayList<>();
             for (Room r : cachedRooms) {
                 double d = calculateDistance(lat, lng, r.getLatitude(), r.getLongitude());
                 if (d <= 1.0) filtered.add(r); // within 1km
             }
             roomsLD.postValue(filtered);
         }).start();
     }
 
    public void cleanup() {}

    public java.util.List<Room> getAll() { return new java.util.ArrayList<>(cachedRooms); }
    public androidx.lifecycle.MutableLiveData<java.util.List<Room>> getAllRoomByUser(String email) {
        androidx.lifecycle.MutableLiveData<java.util.List<Room>> ld = new androidx.lifecycle.MutableLiveData<>();
        java.util.List<Room> filtered = new java.util.ArrayList<>();
        for (Room r : cachedRooms) { if (r.getUserId() != null && r.getUserId().equals(email)) filtered.add(r); }
        ld.setValue(filtered); return ld;
    }
    public void deleteAll() { for (Room r : cachedRooms) if (r.getRoomId() != null) delete(r.getRoomId()); }
    public String validate(Room room, boolean insert) {
        if (room.getRoomName() == null || room.getRoomName().isEmpty()) return "Room name required";
        if (room.getRoomPrice() <= 0) return "Price required"; return "";
    }
    public void filterRooms(com.example.assignment.model.RoomFilterOptions filterOptions) {
        roomsLD.postValue(new java.util.ArrayList<>(cachedRooms));
    }

    private void loadAllRooms() {
         new Thread(() -> {
             try {
                 List<Room> rooms = ApiClient.getInstance().getList("rooms", Room.class);
                 cachedRooms = (rooms != null) ? rooms : new ArrayList<>();
                 roomsLD.postValue(new ArrayList<>(cachedRooms));
             } catch (Exception e) {
                 e.printStackTrace();
                 roomsLD.postValue(new ArrayList<>());
             }
         }).start();
     }
 
     private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
         double R = 6371;
         double dLat = Math.toRadians(lat2 - lat1);
         double dLng = Math.toRadians(lng2 - lng1);
         double a = Math.sin(dLat/2)*Math.sin(dLat/2) + 
                    Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*
                    Math.sin(dLng/2)*Math.sin(dLng/2);
         return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
     }
 }
