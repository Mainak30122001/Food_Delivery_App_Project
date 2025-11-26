package com.example.food_delivery_backend.service;

import com.example.food_delivery_backend.dto.CustomerOrderStats;
import com.example.food_delivery_backend.dto.OrderDTO;
import com.example.food_delivery_backend.model.Order;
import com.example.food_delivery_backend.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // Create Order
    public OrderDTO createOrder(OrderDTO dto) {
        Order order = Order.builder()
                .menuItems(dto.getMenuItems())
                .totalPrice(dto.getTotalPrice())
                .status(dto.getStatus())
                .deliveryPartnerId(dto.getDeliveryPartnerId())
                .customerId(dto.getCustomerId())
                .build();
        Order saved = orderRepository.save(order);
        dto.setId(saved.getId());
        return dto;
    }

    // Get all orders
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get orders by customer
    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get all pending orders
    public List<OrderDTO> getPendingOrders() {
        return orderRepository.findByStatusNot("DELIVERED")
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Update order status
    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);

        return toDTO(updated);
    }

    public List<CustomerOrderStats> getOrdersByCustomerStats() {
        List<Object[]> rows = orderRepository.aggregateOrdersByCustomer();

        return rows.stream().map(r -> {
            CustomerOrderStats dto = new CustomerOrderStats();
            dto.setCustomerId((Long) r[0]);
            dto.setTotalOrders((Long) r[1]);
            dto.setCustomerName("Customer " + r[0]); // optional until join added
            return dto;
        }).toList();
    }




    // Helper to map entity -> DTO
    private OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .menuItems(order.getMenuItems())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .deliveryPartnerId(order.getDeliveryPartnerId())
                .customerId(order.getCustomerId())
                .build();
    }

}
