package com.example.food_delivery_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String cuisine;

    private String image;   // Image URL or base64 string
}
