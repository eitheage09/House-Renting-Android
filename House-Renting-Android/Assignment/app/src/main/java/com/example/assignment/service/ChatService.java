package com.example.assignment.service;

import com.example.assignment.model.Room;
import com.example.assignment.model.User;
import com.example.assignment.repository.ChatRepository;
import com.example.assignment.repository.ChatRoomRepository;
import com.example.assignment.repository.UserRepository;
import com.example.assignment.model.Chat;
import com.example.assignment.model.ChatRoom;

public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ChatService(ChatRepository chatRepository, ChatRoomRepository chatRoomRepository,
                       UserRepository userRepository, NotificationService notificationService) {
        this.chatRepository = chatRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public void sendMessage(Chat newMsg, String chatRoomID, String senderEmail,
                            String receiverEmail, String messageText) {
        ChatRoom room = chatRoomRepository.get(chatRoomID);
        newMsg.setChatRoomId(chatRoomID);
        newMsg.setSenderId(senderEmail);
        newMsg.setMessage(messageText);
        chatRepository.add(newMsg);

        User sender = userRepository.get(senderEmail);
        notificationService.sendNotification(messageText, chatRoomID, sender);

        if (room != null) {
            User receiver = userRepository.get(receiverEmail);
            if (receiver != null) room.setUser(receiver);
            room.setLastSender(senderEmail);
            room.setLastMessage(messageText);
            room.setLastMessageTime(newMsg.getMessageTime());
            room.setRead(false);
            chatRoomRepository.set(room);
        }
    }

    public void createChatRoom(String userMail, String email) {
        if (chatRoomRepository.getRoomByUser(userMail, email) == null) {
            ChatRoom newChatRoom = new ChatRoom();
            newChatRoom.setUserId(java.util.Arrays.asList(userMail, email));
            chatRoomRepository.add(newChatRoom);
        }
    }
}
