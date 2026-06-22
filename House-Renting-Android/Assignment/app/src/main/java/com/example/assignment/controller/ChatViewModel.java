package com.example.assignment.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment.model.Chat;
import com.example.assignment.repository.ChatRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private ChatRepository chatRepository;

    public void setChatRepository(ChatRepository repository) {
        this.chatRepository = repository;
    }

    public void init() {}

    public MutableLiveData<List<Chat>> getChatLD() {
        return chatRepository != null ? chatRepository.getChatLD() : new MutableLiveData<>();
    }

    public List<Chat> getAll() { return chatRepository != null ? chatRepository.getAll() : null; }

    public LiveData<List<Chat>> getAllByRoomId(String chatRoomId) {
        return chatRepository != null ? chatRepository.getAllByRoomId(chatRoomId) : new MutableLiveData<>();
    }

    public void sortByTime() { if (chatRepository != null) chatRepository.sortByTime(); }

    public List<Chat> getAllChatRooms(String roomId, String email) {
        return chatRepository != null ? chatRepository.getAllChatRooms(roomId, email) : new ArrayList<>();
    }

    public void setRead(String roomId, String email) {
        if (chatRepository != null) chatRepository.setRead(roomId, email);
    }

    public Chat get(String id) { return chatRepository != null ? chatRepository.get(id) : null; }

    public void add(Chat chat) { if (chatRepository != null) chatRepository.add(chat); }

    public void set(Chat chat) { if (chatRepository != null) chatRepository.set(chat); }

    public void delete(String id) { if (chatRepository != null) chatRepository.delete(id); }

    public void deleteAll() { if (chatRepository != null) chatRepository.deleteAll(); }
}
