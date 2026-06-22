package com.example.assignment.controller;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.assignment.model.Favorite;
import com.example.assignment.model.RoomFilterOptions;
import com.example.assignment.model.User;
import com.example.assignment.model.Room;
import com.example.assignment.repository.FavoriteRepository;

public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteRepository favoriteRepository;

    public FavoriteViewModel(Application application) {
        super(application);
    }

    public void setFavoriteRepository(FavoriteRepository repository) {
        this.favoriteRepository = repository;
    }

    public void init() {}

    public MutableLiveData<java.util.List<Favorite>> getFavoritesLD() {
        return favoriteRepository != null ? favoriteRepository.getFavoritesLD() : new MutableLiveData<>();
    }

    public java.util.List<Favorite> getAll() {
        return favoriteRepository != null ? favoriteRepository.getAll() : new java.util.ArrayList<>();
    }

    public Favorite get(String id) { return favoriteRepository != null ? favoriteRepository.get(id) : null; }

    public void deleteByRoomId(String roomId) {
        if (favoriteRepository != null) favoriteRepository.deleteByRoomId(roomId);
    }

    public void addToFavorites(User user, Room room) {
        if (favoriteRepository != null) favoriteRepository.addToFavorites(user, room);
    }

    public void removeFromFavorites(User user, Room room) {
        if (favoriteRepository != null) favoriteRepository.removeFromFavorites(user, room);
    }

    public void clearSearch() { if (favoriteRepository != null) favoriteRepository.clearSearch(); }

    public void filterRooms(RoomFilterOptions filterOptions, java.util.List<Favorite> favRoom) {
        if (favoriteRepository != null) favoriteRepository.filterRooms(filterOptions, favRoom);
    }
}
