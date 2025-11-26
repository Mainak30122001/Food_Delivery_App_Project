package com.example.food_delivery_backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDTO {
    private Long id;
    private String name;
    private Double price;
    private Boolean veg;
    private String image;
    private Long restaurantId;  // restaurant this menu item belongs to
}
