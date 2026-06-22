package com.example.assignment.controller;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment.model.ChatRoom;
import com.example.assignment.repository.ChatRoomRepository;

import java.util.List;

public class ChatRoomViewModel extends ViewModel {

    private ChatRoomRepository chatRoomRepository;

    public void setChatRoomRepository(ChatRoomRepository repository) {
        this.chatRoomRepository = repository;
    }

    public void init() {}

    public MutableLiveData<List<ChatRoom>> getChatRoomLD() {
        return chatRoomRepository != null ? chatRoomRepository.getChatRoomsLD() : new MutableLiveData<>();
    }

    public List<ChatRoom> getAll() { return chatRoomRepository != null ? chatRoomRepository.getAll() : null; }

    public MutableLiveData<List<ChatRoom>> getAllByEmail(String email) {
        return chatRoomRepository != null ? chatRoomRepository.getAllByEmail(email) : new MutableLiveData<>();
    }

    public ChatRoom getRoomByUser(String sender, String receiver) {
        return chatRoomRepository != null ? chatRoomRepository.getRoomByUser(sender, receiver) : null;
    }

    public void sortByTime() { if (chatRoomRepository != null) chatRoomRepository.sortByTime(); }

    public ChatRoom get(String id) { return chatRoomRepository != null ? chatRoomRepository.get(id) : null; }

    public void add(ChatRoom chatRoom) { if (chatRoomRepository != null) chatRoomRepository.add(chatRoom); }

    public void set(ChatRoom chatRoom) { if (chatRoomRepository != null) chatRoomRepository.set(chatRoom); }

    public void delete(String id) { if (chatRoomRepository != null) chatRoomRepository.delete(id); }

    public void deleteAll() { if (chatRoomRepository != null) chatRoomRepository.deleteAll(); }

    public void search(String name) { if (chatRoomRepository != null) chatRoomRepository.search(name); }
}
