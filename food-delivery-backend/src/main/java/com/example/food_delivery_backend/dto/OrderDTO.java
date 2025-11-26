package com.example.food_delivery_backend.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;
    private Map<Long, Integer> menuItems; // key = menuItemId, value = quantity
    private Double totalPrice;
    private String status;
    private Long deliveryPartnerId;
    private Long customerId;
}
