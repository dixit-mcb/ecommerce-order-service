package com.techolution.ecommerce.order.service;

import com.techolution.ecommerce.order.dto.request.OrderRequestDto;
import com.techolution.ecommerce.order.dto.request.OrderUpdateRequestDto;
import com.techolution.ecommerce.order.exception.OrderNotFoundException;
import com.techolution.ecommerce.order.model.Order;
import com.techolution.ecommerce.order.model.OrderStatusFactory;
import com.techolution.ecommerce.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PriorityQueue<Order> orderQueue = new PriorityQueue<>(Comparator.comparing(Order::getDateCreated));
    private static volatile OrderServiceImpl instance;

    private OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Singleton instance retrieval
    public static OrderServiceImpl getInstance(OrderRepository repo) {
        if (instance == null) {
            synchronized (OrderServiceImpl.class) {
                if (instance == null) {
                    instance = new OrderServiceImpl(repo);
                }
            }
        }
        return instance;
    }

    // Create a new order
    public Order createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order(orderRequestDto.getCustomerName(),
                orderRequestDto.getProductName(),
                orderRequestDto.getQuantity(),
                orderRequestDto.getPrice()
        );
        orderRepository.save(order);
        orderQueue.offer(order);
        return order;
    }

    // Retrieve orders with optional filters and sorting
    public List<Order> getOrders(String customerName, String status, String sort) {
        return orderRepository.findAll().stream()
                .filter(order -> status == null || order.getStatus().equals(OrderStatusFactory.createOrderStatus(status)))
                .filter(order -> customerName == null || order.getCustomerName().equalsIgnoreCase(customerName))
                .sorted("desc".equalsIgnoreCase(sort) ?
                        Comparator.comparing(Order::getDateCreated).reversed() :
                        Comparator.comparing(Order::getDateCreated))
                .collect(Collectors.toList());
    }

    // Get a specific order by ID
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
    }

    // Update the status of an existing order
    public Order updateOrder(UUID orderId, OrderUpdateRequestDto updateRequest) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatusFactory.createOrderStatus(updateRequest.getStatus().toString()));
        orderRepository.save(order);
        orderQueue.remove(order);
        orderQueue.offer(order);
        return order;
    }

    // Delete an order by its ID
    public void deleteOrder(UUID orderId) {
        Order order = getOrderById(orderId);
        orderRepository.deleteById(orderId);
        orderQueue.remove(order);
    }

    // Retrieve order history for a specific customer
    public List<Order> getOrderHistory(String customerName) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCustomerName().equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
    }

}
