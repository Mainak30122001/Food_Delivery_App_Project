package com.example.food_delivery_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantResponse {
    private Long id;
    private String name;
    private String address;
    private String cuisine;
    private String image;
}
