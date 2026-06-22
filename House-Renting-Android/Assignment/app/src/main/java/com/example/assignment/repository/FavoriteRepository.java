 package com.example.assignment.repository;
 
 import androidx.lifecycle.MutableLiveData;
 
 import com.example.assignment.api.ApiClient;
 import com.example.assignment.model.Favorite;
 import com.example.assignment.model.Room;
 import com.example.assignment.model.User;
 
 import java.util.ArrayList;
 import java.util.List;
 
 public class FavoriteRepository {
 
     private final MutableLiveData<List<Favorite>> favoritesLD = new MutableLiveData<>();
     private List<Favorite> cached = new ArrayList<>();
 
     public FavoriteRepository() { loadAll(); }
     public MutableLiveData<List<Favorite>> getFavoritesLD() { return favoritesLD; }
 
     public Favorite get(String id) {
         for (Favorite f : cached) {
             if (f.getFavoriteId() != null && f.getFavoriteId().equals(id)) return f;
         }
         return null;
     }
 
     public List<Favorite> getByUser(String userId) {
         List<Favorite> result = new ArrayList<>();
         for (Favorite f : cached) {
             if (f.getUserId() != null && f.getUserId().equals(userId)) result.add(f);
         }
         return result;
     }
 
     public void add(Favorite favorite) {
         if (favorite == null) return;
         new Thread(() -> {
             try { ApiClient.getInstance().post("favorites", favorite, Favorite.class); loadAll(); }
             catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void delete(String id) {
         if (id == null) return;
         new Thread(() -> {
             try { ApiClient.getInstance().delete("favorites/" + id); loadAll(); }
             catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void addToFavorites(User user, Room room) {
         if (user == null || room == null) return;
         Favorite fav = new Favorite(user.getEmail(), room.getRoomId());
         add(fav);
     }
 
     public void removeFromFavorites(User user, Room room) {
         if (user == null || room == null) return;
         for (Favorite f : cached) {
             if (f.getUserId() != null && f.getUserId().equals(user.getEmail())
                     && f.getRoomId() != null && f.getRoomId().equals(room.getRoomId())) {
                 delete(f.getFavoriteId());
                 return;
             }
         }
     }
 
    public void cleanup() {}

    public java.util.List<Favorite> getAll() { return new java.util.ArrayList<>(cached); }
    public void deleteByRoomId(String roomId) {
        for (Favorite f : cached) {
            if (f.getRoomId() != null && f.getRoomId().equals(roomId) && f.getFavoriteId() != null) { delete(f.getFavoriteId()); return; }
        }
    }
    public void clearSearch() { loadAll(); }
    public void filterRooms(com.example.assignment.model.RoomFilterOptions filterOptions, java.util.List<Favorite> favRoom) {
        favoritesLD.postValue(new java.util.ArrayList<>(cached));
    }

    private void loadAll() {
         new Thread(() -> {
             try {
                 List<Favorite> list = ApiClient.getInstance().getList("favorites", Favorite.class);
                 cached = (list != null) ? list : new ArrayList<>();
                 favoritesLD.postValue(new ArrayList<>(cached));
             } catch (Exception e) { e.printStackTrace(); favoritesLD.postValue(new ArrayList<>()); }
         }).start();
     }
 }
