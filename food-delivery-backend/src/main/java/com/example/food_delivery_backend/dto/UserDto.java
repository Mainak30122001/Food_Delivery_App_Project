package com.example.food_delivery_backend.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String role;
}
