package com.example.food_delivery_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    @Column(name = "is_veg")
    private Boolean veg;  // true = veg, false = non-veg

    private String image;   // Image URL or base64 string

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
