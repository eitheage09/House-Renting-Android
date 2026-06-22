 package com.rent.backend.controller;
 
 import com.rent.backend.entity.RoomEntity;
 import com.rent.backend.repository.RoomRepo;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 
 import java.util.List;
 
 @RestController
 @RequestMapping("/api/rooms")
 public class RoomController {
 
     @Autowired
     private RoomRepo roomRepo;
 
     @GetMapping
     public List<RoomEntity> getAll() { return roomRepo.findAll(); }
 
     @GetMapping("/{id}")
     public ResponseEntity<RoomEntity> get(@PathVariable String id) {
         return roomRepo.findById(id)
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
     }
 
     @PostMapping
     public RoomEntity create(@RequestBody RoomEntity room) { return roomRepo.save(room); }
 
     @PutMapping("/{id}")
     public ResponseEntity<RoomEntity> update(@PathVariable String id, @RequestBody RoomEntity room) {
         return roomRepo.findById(id)
                 .map(existing -> {
                     if (room.getRoomName() != null) existing.setRoomName(room.getRoomName());
                     if (room.getRoomPlace() != null) existing.setRoomPlace(room.getRoomPlace());
                     if (room.getRoomDetail() != null) existing.setRoomDetail(room.getRoomDetail());
                     if (room.getPhotoJson() != null) existing.setPhotoJson(room.getPhotoJson());
                     existing.setRoomSize(room.getRoomSize());
                     if (room.getRoomType() != null) existing.setRoomType(room.getRoomType());
                     existing.setNumOfBed(room.getNumOfBed());
                     existing.setNumOfBath(room.getNumOfBath());
                     if (room.getHomeAmenities() != null) existing.setHomeAmenities(room.getHomeAmenities());
                     existing.setRoomPrice(room.getRoomPrice());
                     existing.setLatitude(room.getLatitude());
                     existing.setLongitude(room.getLongitude());
                     if (room.getUserId() != null) existing.setUserId(room.getUserId());
                     return ResponseEntity.ok(roomRepo.save(existing));
                 })
                 .orElse(ResponseEntity.notFound().build());
     }
 
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable String id) {
         if (roomRepo.existsById(id)) { roomRepo.deleteById(id); return ResponseEntity.ok().build(); }
         return ResponseEntity.notFound().build();
     }
 }
