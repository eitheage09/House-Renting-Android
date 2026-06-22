 package com.example.assignment.model;
 
 import java.util.ArrayList;
 import java.util.List;
 
 public class ChatRoom {
     private String chatRoomId;
     private String lastSender;
     private String lastMessage;
     private long lastMessageTime;
     private List<String> userId;
     private boolean isRead;
     private transient User user;
 
     public ChatRoom() {
         this.chatRoomId = ""; this.lastSender = ""; this.lastMessage = "";
         this.lastMessageTime = System.currentTimeMillis();
         this.userId = new ArrayList<>(); this.isRead = false;
         this.user = new User();
     }
 
     public String getChatRoomId() { return chatRoomId; }
     public void setChatRoomId(String chatRoomId) { this.chatRoomId = chatRoomId; }
     public String getLastSender() { return lastSender; }
     public void setLastSender(String lastSender) { this.lastSender = lastSender; }
     public String getLastMessage() { return lastMessage; }
     public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
     public long getLastMessageTime() { return lastMessageTime; }
     public void setLastMessageTime(long lastMessageTime) { this.lastMessageTime = lastMessageTime; }
     public List<String> getUserId() { return userId; }
     public void setUserId(List<String> userId) { this.userId = userId; }
     public boolean isRead() { return isRead; }
     public void setRead(boolean read) { isRead = read; }
     public User getUser() { return user; }
     public void setUser(User user) { this.user = user; }
 }
