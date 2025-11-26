package com.example.food_delivery_backend.controller;

import com.example.food_delivery_backend.dto.LoginRequest;
import com.example.food_delivery_backend.dto.LoginResponse;
import com.example.food_delivery_backend.dto.RegisterRequest;
import com.example.food_delivery_backend.service.UserService;
import com.example.food_delivery_backend.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final UserServiceImpl service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        try {
            String result = service.register(req);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        try {
            // Assume login() returns a LoginResponse with token, name, role
            LoginResponse response = service.login(req);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // In case of invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse());
        }
    }
}
