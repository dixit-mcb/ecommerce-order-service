package com.techolution.ecommerce.order.repository;

import com.techolution.ecommerce.order.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private final Map<UUID, Order> orders = new HashMap<>();

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public Optional<Order> findById(UUID orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public Order save(Order order) {
        orders.put(order.getOrderId(), order);
        return order;
    }

    public void deleteById(UUID orderId) {
        orders.remove(orderId);
    }

}
