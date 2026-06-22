 package com.rent.backend.controller;
 
 import com.rent.backend.entity.ChatRoomEntity;
 import com.rent.backend.repository.ChatRoomRepo;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 
 import java.util.List;
 
 @RestController
 @RequestMapping("/api/chat-rooms")
 public class ChatRoomController {
     @Autowired private ChatRoomRepo chatRoomRepo;
 
     @GetMapping
     public List<ChatRoomEntity> getAll() { return chatRoomRepo.findAll(); }
 
     @GetMapping("/{id}")
     public ResponseEntity<ChatRoomEntity> get(@PathVariable String id) {
         return chatRoomRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
     }
 
     @PostMapping
     public ChatRoomEntity create(@RequestBody ChatRoomEntity e) { return chatRoomRepo.save(e); }
 
     @PutMapping("/{id}")
     public ResponseEntity<ChatRoomEntity> update(@PathVariable String id, @RequestBody ChatRoomEntity e) {
         return chatRoomRepo.findById(id).map(existing -> {
             if (e.getLastSender() != null) existing.setLastSender(e.getLastSender());
             if (e.getLastMessage() != null) existing.setLastMessage(e.getLastMessage());
             existing.setLastMessageTime(e.getLastMessageTime());
             if (e.getUserids() != null) existing.setUserids(e.getUserids());
             existing.setRead(e.isRead());
             return ResponseEntity.ok(chatRoomRepo.save(existing));
         }).orElse(ResponseEntity.notFound().build());
     }
 
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable String id) {
         if (chatRoomRepo.existsById(id)) { chatRoomRepo.deleteById(id); return ResponseEntity.ok().build(); }
         return ResponseEntity.notFound().build();
     }
 }
