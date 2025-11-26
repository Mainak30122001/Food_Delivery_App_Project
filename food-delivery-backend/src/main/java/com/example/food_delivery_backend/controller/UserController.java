package com.example.food_delivery_backend.controller;

import com.example.food_delivery_backend.model.User;
import com.example.food_delivery_backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    // Accept name as path variable
    @GetMapping("/by-name/{name}")
    public ResponseEntity<Map<String, Long>> getUserIdByName(@PathVariable String name) {
        User user = userService.getUserByName(name);
        if (user != null) {
            // return only the id
            return ResponseEntity.ok(Map.of("id", user.getId()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
