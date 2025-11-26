package com.example.food_delivery_backend.service;

import com.example.food_delivery_backend.dto.LoginRequest;
import com.example.food_delivery_backend.dto.LoginResponse;
import com.example.food_delivery_backend.dto.RegisterRequest;
import com.example.food_delivery_backend.model.User;

public interface UserService {
    String register(RegisterRequest request);

    LoginResponse login(LoginRequest request); // <-- change return type

    User getUserByName(String name);
}
