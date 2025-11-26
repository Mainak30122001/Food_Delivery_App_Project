package com.example.food_delivery_backend.Repository;

import com.example.food_delivery_backend.dto.CustomerOrderStats;
import com.example.food_delivery_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatusNot(String status);
    @Query("SELECT o.customerId, COUNT(o) FROM Order o GROUP BY o.customerId")
    List<Object[]> aggregateOrdersByCustomer();


}
