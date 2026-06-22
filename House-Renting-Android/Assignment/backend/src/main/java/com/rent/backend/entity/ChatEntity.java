 package com.rent.backend.entity;
 
 import jakarta.persistence.*;
 
 @Entity
 @Table(name = "chats")
 public class ChatEntity {
     @Id private String chatId;
     private String chatRoomId;
     private String senderId;
     @Column(columnDefinition = "TEXT") private String message;
     private long messageTime;
     private boolean isRead;
     @Lob private byte[] image;
 
     public ChatEntity() {}
 
     public String getChatId() { return chatId; }
     public void setChatId(String chatId) { this.chatId = chatId; }
     public String getChatRoomId() { return chatRoomId; }
     public void setChatRoomId(String chatRoomId) { this.chatRoomId = chatRoomId; }
     public String getSenderId() { return senderId; }
     public void setSenderId(String senderId) { this.senderId = senderId; }
     public String getMessage() { return message; }
     public void setMessage(String message) { this.message = message; }
     public long getMessageTime() { return messageTime; }
     public void setMessageTime(long messageTime) { this.messageTime = messageTime; }
     public boolean isRead() { return isRead; }
     public void setRead(boolean read) { isRead = read; }
     public byte[] getImage() { return image; }
     public void setImage(byte[] image) { this.image = image; }
 }
