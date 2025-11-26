package com.example.food_delivery_backend.Repository;

import com.example.food_delivery_backend.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
