package com.example.food_delivery_backend.dto;

import lombok.Data;

@Data
public class RestaurantRequest {
    private String name;
    private String address;
    private String cuisine;
    private String image;
}
