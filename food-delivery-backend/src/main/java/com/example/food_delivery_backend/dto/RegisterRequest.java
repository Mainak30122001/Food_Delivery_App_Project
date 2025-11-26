package com.example.food_delivery_backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role; // USER / ADMIN
}
