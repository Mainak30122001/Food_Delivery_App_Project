package com.example.food_delivery_backend.controller;

import com.example.food_delivery_backend.dto.CustomerOrderStats;
import com.example.food_delivery_backend.dto.OrderDTO;
import com.example.food_delivery_backend.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }

    // Get Pending Orders
    @GetMapping("/pending")
    public List<OrderDTO> getPendingOrders() {
        return orderService.getPendingOrders();
    }

    // Update Order Status
    @PutMapping("/{orderId}/status")
    public OrderDTO updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @GetMapping("/analysis/orders-by-customer")
    public ResponseEntity<List<CustomerOrderStats>> getOrdersByCustomerStats() {
        return ResponseEntity.ok(orderService.getOrdersByCustomerStats());
    }

}
