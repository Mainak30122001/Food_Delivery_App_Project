package com.example.food_delivery_backend.service;

import com.example.food_delivery_backend.dto.RestaurantRequest;
import com.example.food_delivery_backend.dto.RestaurantResponse;
import java.util.List;

public interface RestaurantService {

    RestaurantResponse create(RestaurantRequest request);

    RestaurantResponse update(Long id, RestaurantRequest request);

    void deleteRestaurant(Long id);

    RestaurantResponse getById(Long id);

    List<RestaurantResponse> getAll();
}
