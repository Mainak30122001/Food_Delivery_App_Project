package com.example.food_delivery_backend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String name;
    private String role;
}
