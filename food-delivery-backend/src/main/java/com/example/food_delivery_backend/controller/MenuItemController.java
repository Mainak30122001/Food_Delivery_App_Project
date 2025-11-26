package com.example.food_delivery_backend.controller;

import com.example.food_delivery_backend.dto.MenuItemDTO;
import com.example.food_delivery_backend.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")  // allow Angular app
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping("/by-ids")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByIds(ids));
    }



    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }


    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDTO>> getMenuByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.getAllMenuItemsByRestaurant(restaurantId));
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO> addMenuItem(@RequestBody MenuItemDTO dto) {
        return ResponseEntity.ok(menuItemService.addMenuItem(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDTO dto) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok("MenuItem deleted successfully");
    }
}
