 package com.rent.backend.entity;
 
 import jakarta.persistence.*;
 
 @Entity
 @Table(name = "chat_rooms")
 public class ChatRoomEntity {
     @Id private String chatRoomId;
     private String lastSender;
     @Column(columnDefinition = "TEXT") private String lastMessage;
     private long lastMessageTime;
     @Column(columnDefinition = "TEXT") private String userids;
     private boolean isRead;
 
     public ChatRoomEntity() {}
 
     public String getChatRoomId() { return chatRoomId; }
     public void setChatRoomId(String chatRoomId) { this.chatRoomId = chatRoomId; }
     public String getLastSender() { return lastSender; }
     public void setLastSender(String lastSender) { this.lastSender = lastSender; }
     public String getLastMessage() { return lastMessage; }
     public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
     public long getLastMessageTime() { return lastMessageTime; }
     public void setLastMessageTime(long lastMessageTime) { this.lastMessageTime = lastMessageTime; }
     public String getUserids() { return userids; }
     public void setUserids(String userids) { this.userids = userids; }
     public boolean isRead() { return isRead; }
     public void setRead(boolean read) { isRead = read; }
 }
