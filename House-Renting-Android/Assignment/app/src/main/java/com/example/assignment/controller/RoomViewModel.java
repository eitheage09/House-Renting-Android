package com.example.assignment.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment.model.Room;
import com.example.assignment.model.RoomFilterOptions;
import com.example.assignment.repository.RoomRepository;

public class RoomViewModel extends ViewModel {

    private RoomRepository roomRepository;

    public void setRoomRepository(RoomRepository repository) {
        this.roomRepository = repository;
    }

    public void init() {}

    public MutableLiveData<java.util.List<Room>> getRoomsLD() {
        return roomRepository != null ? roomRepository.getRoomsLD() : new MutableLiveData<>();
    }

    public LiveData<java.util.List<Room>> getAllRoomByUser(String email) {
        return roomRepository != null ? roomRepository.getAllRoomByUser(email) : new MutableLiveData<>();
    }

    public java.util.List<Room> getAll() {
        return roomRepository != null ? roomRepository.getAll() : null;
    }

    public Room get(String id) {
        return roomRepository != null ? roomRepository.get(id) : null;
    }

    public void add(Room room) { if (roomRepository != null) roomRepository.add(room); }

    public void delete(String id) { if (roomRepository != null) roomRepository.delete(id); }

    public void deleteAll() { if (roomRepository != null) roomRepository.deleteAll(); }

    public void set(Room room) { if (roomRepository != null) roomRepository.set(room); }

    public String validate(Room room, boolean insert) {
        return roomRepository != null ? roomRepository.validate(room, insert) : "";
    }

    public void searchByLocation(double latitude, double longitude) {
        if (roomRepository != null) roomRepository.searchByLocation(latitude, longitude);
    }

    public void clearSearch() { if (roomRepository != null) roomRepository.clearSearch(); }

    public void filterRooms(RoomFilterOptions filterOptions) {
        if (roomRepository != null) roomRepository.filterRooms(filterOptions);
    }
}
