 package com.example.assignment.repository;
 
 import androidx.lifecycle.MutableLiveData;
 
 import com.example.assignment.api.ApiClient;
 import com.example.assignment.model.Chat;
 import com.example.assignment.model.User;
 
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.Comparator;
 import java.util.List;
 
 public class ChatRepository {
 
     private final MutableLiveData<List<Chat>> chatListLD = new MutableLiveData<>();
     private List<Chat> cachedChats = new ArrayList<>();
 
     public ChatRepository() { loadAllChats(); }
     public MutableLiveData<List<Chat>> getChatLD() { return chatListLD; }
 
     public List<Chat> getAll() { return cachedChats; }
 
     public Chat get(String id) {
         for (Chat c : cachedChats) {
             if (c.getChatId() != null && c.getChatId().equals(id)) return c;
         }
         return null;
     }
 
     public MutableLiveData<List<Chat>> getAllByRoomId(String chatRoomId) {
         MutableLiveData<List<Chat>> filteredLD = new MutableLiveData<>();
         List<Chat> filtered = new ArrayList<>();
         for (Chat c : cachedChats) {
             if (c.getChatRoomId() != null && c.getChatRoomId().equals(chatRoomId)) {
                 filtered.add(c);
             }
         }
         Collections.sort(filtered, Comparator.comparingLong(Chat::getMessageTime));
         filteredLD.setValue(filtered);
         return filteredLD;
     }
 
     public List<Chat> getAllChatRooms(String roomId, String email) {
         List<Chat> result = new ArrayList<>();
         for (Chat c : cachedChats) {
             if (c.getChatRoomId() != null && c.getChatRoomId().equals(roomId)
                     && c.getSenderId() != null && !c.getSenderId().equals(email)) {
                 result.add(c);
             }
         }
         return result;
     }
 
     public void setRead(String roomId, String email) {
         List<Chat> chats = getAllChatRooms(roomId, email);
         for (Chat c : chats) {
             if (!c.isRead()) { c.setRead(true); set(c); }
         }
     }
 
     public void add(Chat chat) {
         if (chat == null) return;
         new Thread(() -> {
             try {
                 if (!idExists(chat.getChatId())) {
                     ApiClient.getInstance().post("chats", chat, Chat.class);
                     loadAllChats();
                 }
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void set(Chat chat) {
         if (chat == null || chat.getChatId() == null) return;
         new Thread(() -> {
             try {
                 ApiClient.getInstance().put("chats/" + chat.getChatId(), chat, Chat.class);
                 loadAllChats();
             } catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void delete(String id) {
         if (id == null) return;
         new Thread(() -> {
             try { ApiClient.getInstance().delete("chats/" + id); loadAllChats(); }
             catch (Exception e) { e.printStackTrace(); }
         }).start();
     }
 
     public void deleteAll() {
         for (Chat c : cachedChats) {
             if (c.getChatId() != null) delete(c.getChatId());
         }
     }
 
     public void cleanup() {}
 
     private void loadAllChats() {
         new Thread(() -> {
             try {
                 List<Chat> chats = ApiClient.getInstance().getList("chats", Chat.class);
                 cachedChats = (chats != null) ? chats : new ArrayList<>();
                 sortByTime();
             } catch (Exception e) {
                 e.printStackTrace();
                 chatListLD.postValue(new ArrayList<>());
             }
         }).start();
     }
 
    public void sortByTime() {
         Collections.sort(cachedChats, Comparator.comparingLong(Chat::getMessageTime));
         chatListLD.postValue(new ArrayList<>(cachedChats));
     }
 
     private boolean idExists(String id) {
         if (id == null) return false;
         for (Chat c : cachedChats) {
             if (c.getChatId() != null && c.getChatId().equals(id)) return true;
         }
         return false;
     }
 }
