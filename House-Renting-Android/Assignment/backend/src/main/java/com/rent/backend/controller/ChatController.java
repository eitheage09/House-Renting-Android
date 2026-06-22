 package com.rent.backend.controller;
 
 import com.rent.backend.entity.ChatEntity;
 import com.rent.backend.repository.ChatRepo;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 
 import java.util.List;
 
 @RestController
 @RequestMapping("/api/chats")
 public class ChatController {
     @Autowired private ChatRepo chatRepo;
 
     @GetMapping
     public List<ChatEntity> getAll() { return chatRepo.findAll(); }
 
     @GetMapping("/{id}")
     public ResponseEntity<ChatEntity> get(@PathVariable String id) {
         return chatRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
     }
 
     @PostMapping
     public ChatEntity create(@RequestBody ChatEntity chat) { return chatRepo.save(chat); }
 
     @PutMapping("/{id}")
     public ResponseEntity<ChatEntity> update(@PathVariable String id, @RequestBody ChatEntity chat) {
         return chatRepo.findById(id).map(existing -> {
             if (chat.getChatRoomId() != null) existing.setChatRoomId(chat.getChatRoomId());
             if (chat.getSenderId() != null) existing.setSenderId(chat.getSenderId());
             if (chat.getMessage() != null) existing.setMessage(chat.getMessage());
             existing.setMessageTime(chat.getMessageTime());
             existing.setRead(chat.isRead());
             if (chat.getImage() != null) existing.setImage(chat.getImage());
             return ResponseEntity.ok(chatRepo.save(existing));
         }).orElse(ResponseEntity.notFound().build());
     }
 
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable String id) {
         if (chatRepo.existsById(id)) { chatRepo.deleteById(id); return ResponseEntity.ok().build(); }
         return ResponseEntity.notFound().build();
     }
 }
