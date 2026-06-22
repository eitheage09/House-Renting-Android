package com.example.assignment.service;

import com.example.assignment.model.Room;
import com.example.assignment.repository.RoomRepository;

public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public String validate(Room room, boolean insert) {
        return roomRepository.validate(room, insert);
    }
}
