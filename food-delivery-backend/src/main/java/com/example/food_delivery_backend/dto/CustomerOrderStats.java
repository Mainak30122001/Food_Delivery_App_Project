package com.example.food_delivery_backend.dto;

import lombok.Data;

@Data
public class CustomerOrderStats {
    private String customerName;
    private Long customerId;
    private Long totalOrders;
}
