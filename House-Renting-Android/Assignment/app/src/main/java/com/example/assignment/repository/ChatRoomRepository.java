 package com.example.assignment.repository;
 
 import androidx.lifecycle.MutableLiveData;
 
 import com.example.assignment.api.ApiClient;
 import com.example.assignment.model.ChatRoom;
 
 import java.util.ArrayList;
 import java.util.List;
 
 public class ChatRoomRepository {
 
     private final MutableLiveData<List<ChatRoom>> chatRoomsLD = new MutableLiveData<>();
     private List<ChatRoom> cached = new ArrayList<>();
 
     public ChatRoomRepository() { loadAll(); }
     public MutableLiveData<List<ChatRoom>> getChatRoomsLD() { return chatRoomsLD; }
 
     public ChatRoom get(String id) {
         for (ChatRoom c : cached) {
             if (c.getChatRoomId() != null && c.getChatRoomId().equals(id)) return c;
         }
         return null;
     }
 
     public void add(ChatRoom chatRoom) {
         if (chatRoom == null) return;
         new Thread(() -> {
             try { ApiClient.getInstance().post("chat-rooms", chatRoom, ChatRoom.class); loadAll(); }
             catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void set(ChatRoom chatRoom) {
         if (chatRoom == null || chatRoom.getChatRoomId() == null) return;
         new Thread(() -> {
             try {
                 ApiClient.getInstance().put("chat-rooms/" + chatRoom.getChatRoomId(), chatRoom, ChatRoom.class);
                 loadAll();
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
    public void cleanup() {}

    public java.util.List<ChatRoom> getAll() { return new java.util.ArrayList<>(cached); }
    public androidx.lifecycle.MutableLiveData<java.util.List<ChatRoom>> getAllByEmail(String email) {
        androidx.lifecycle.MutableLiveData<java.util.List<ChatRoom>> ld = new androidx.lifecycle.MutableLiveData<>();
        java.util.List<ChatRoom> filtered = new java.util.ArrayList<>();
        for (ChatRoom cr : cached) { if (cr.getUserId() != null && cr.getUserId().contains(email)) filtered.add(cr); }
        ld.setValue(filtered); return ld;
    }
    public ChatRoom getRoomByUser(String sender, String receiver) {
        for (ChatRoom cr : cached) { if (cr.getUserId() != null && cr.getUserId().contains(sender) && cr.getUserId().contains(receiver)) return cr; }
        return null;
    }
    public void sortByTime() {
        java.util.Collections.sort(cached, (a, b) -> Long.compare(b.getLastMessageTime(), a.getLastMessageTime()));
        chatRoomsLD.postValue(new java.util.ArrayList<>(cached));
    }
    public void delete(String id) {
        new Thread(() -> { try { com.example.assignment.api.ApiClient.getInstance().delete("chat-rooms/" + id); loadAll(); }
        catch (Exception e) { e.printStackTrace(); } }).start();
    }
    public void deleteAll() { for (ChatRoom cr : cached) if (cr.getChatRoomId() != null) delete(cr.getChatRoomId()); }
    public void search(String name) {
        java.util.List<ChatRoom> filtered = new java.util.ArrayList<>();
        for (ChatRoom cr : cached) { if (cr.getLastMessage() != null && cr.getLastMessage().toLowerCase().contains(name.toLowerCase())) filtered.add(cr); }
        chatRoomsLD.postValue(filtered);
    }

    private void loadAll() {
         new Thread(() -> {
             try {
                 List<ChatRoom> list = ApiClient.getInstance().getList("chat-rooms", ChatRoom.class);
                 cached = (list != null) ? list : new ArrayList<>();
                 chatRoomsLD.postValue(new ArrayList<>(cached));
             } catch (Exception e) { e.printStackTrace(); chatRoomsLD.postValue(new ArrayList<>()); }
         }).start();
     }
 }
