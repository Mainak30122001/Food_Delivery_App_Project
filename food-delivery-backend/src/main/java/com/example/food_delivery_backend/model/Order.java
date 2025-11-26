package com.example.food_delivery_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store menu item IDs and quantities as JSON
    @Column(name = "menu_items", columnDefinition = "json")
    @Convert(converter = HashMapConverter.class)
    private Map<Long, Integer> menuItems; // key = menuItemId, value = quantity

    private Double totalPrice;

    private String status;

    private Long deliveryPartnerId; // optional, no foreign key

    private Long customerId; // foreign key
}
