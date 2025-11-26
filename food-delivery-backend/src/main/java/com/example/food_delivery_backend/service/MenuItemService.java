package com.example.food_delivery_backend.service;

import com.example.food_delivery_backend.dto.MenuItemDTO;
import com.example.food_delivery_backend.model.MenuItem;
import com.example.food_delivery_backend.model.Restaurant;
import com.example.food_delivery_backend.Repository.MenuItemRepository;
import com.example.food_delivery_backend.Repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getMenuItemsByIds(List<Long> ids) {
        return menuItemRepository.findAllById(ids)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }



    public List<MenuItemDTO> getAllMenuItemsByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MenuItemDTO addMenuItem(MenuItemDTO dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        MenuItem menuItem = MenuItem.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .veg(dto.getVeg())
                .image(dto.getImage())
                .restaurant(restaurant)
                .build();

        MenuItem saved = menuItemRepository.save(menuItem);
        return toDTO(saved);
    }

    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO dto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItem not found"));

        menuItem.setName(dto.getName());
        menuItem.setPrice(dto.getPrice());
        menuItem.setVeg(dto.getVeg());
        menuItem.setImage(dto.getImage());

        // optionally update restaurant
        if (!menuItem.getRestaurant().getId().equals(dto.getRestaurantId())) {
            Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            menuItem.setRestaurant(restaurant);
        }

        return toDTO(menuItemRepository.save(menuItem));
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }

    private MenuItemDTO toDTO(MenuItem menuItem) {
        return MenuItemDTO.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .veg(menuItem.getVeg())
                .image(menuItem.getImage())
                .restaurantId(menuItem.getRestaurant().getId())
                .build();
    }
}
