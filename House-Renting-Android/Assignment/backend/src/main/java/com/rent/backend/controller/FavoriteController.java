 package com.rent.backend.controller;
 
 import com.rent.backend.entity.FavoriteEntity;
 import com.rent.backend.repository.FavoriteRepo;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 
 import java.util.List;
 
 @RestController
 @RequestMapping("/api/favorites")
 public class FavoriteController {
     @Autowired private FavoriteRepo favoriteRepo;
 
     @GetMapping
     public List<FavoriteEntity> getAll() { return favoriteRepo.findAll(); }
 
     @GetMapping("/{id}")
     public ResponseEntity<FavoriteEntity> get(@PathVariable String id) {
         return favoriteRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
     }
 
     @PostMapping
     public FavoriteEntity create(@RequestBody FavoriteEntity e) { return favoriteRepo.save(e); }
 
     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable String id) {
         if (favoriteRepo.existsById(id)) { favoriteRepo.deleteById(id); return ResponseEntity.ok().build(); }
         return ResponseEntity.notFound().build();
     }
 }
