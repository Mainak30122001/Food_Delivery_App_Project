package com.example.food_delivery_backend.controller;

import com.example.food_delivery_backend.dto.RestaurantRequest;
import com.example.food_delivery_backend.dto.RestaurantResponse;
import com.example.food_delivery_backend.service.RestaurantServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantServiceImpl service;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@RequestBody RestaurantRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable Long id,
            @RequestBody RestaurantRequest req
    ) {
        return ResponseEntity.ok(service.update(id, req));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> delete(@PathVariable Long id) {
//        service.delete(id);
//        return ResponseEntity.ok("Restaurant deleted successfully");
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        service.deleteRestaurant(id);
        return ResponseEntity.ok("Restaurant deleted successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
