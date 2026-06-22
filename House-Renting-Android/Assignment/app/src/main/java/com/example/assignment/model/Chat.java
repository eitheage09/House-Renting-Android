 package com.example.assignment.model;
 
 public class Chat {
     private String chatId;
     private String chatRoomId;
     private String senderId;
     private String message;
     private long messageTime;
     private boolean isRead;
     private byte[] image;
     private transient String chatType;
     private transient User user;
 
     public Chat() {
         this.chatId = ""; this.chatRoomId = ""; this.senderId = "";
         this.message = ""; this.messageTime = System.currentTimeMillis();
         this.isRead = false; this.image = new byte[0];
         this.chatType = ""; this.user = new User();
     }
 
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
     public void setImage(byte[] image) { this.image = image != null ? image : new byte[0]; }
     public String getChatType() { return chatType; }
     public void setChatType(String chatType) { this.chatType = chatType; }
     public User getUser() { return user; }
     public void setUser(User user) { this.user = user; }
 }
