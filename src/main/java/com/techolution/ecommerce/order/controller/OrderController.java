package com.techolution.ecommerce.order.controller;

import com.techolution.ecommerce.order.dto.request.OrderRequestDto;
import com.techolution.ecommerce.order.dto.request.OrderUpdateRequestDto;
import com.techolution.ecommerce.order.model.Order;
import com.techolution.ecommerce.order.repository.OrderRepository;
import com.techolution.ecommerce.order.service.OrderService;
import com.techolution.ecommerce.order.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderService = OrderServiceImpl.getInstance(orderRepository);
    }

    /**
     * Create a new order.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequestDto));
    }

    /**
     * Update an existing order by ID.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrder(@PathVariable UUID id,
                                             @RequestBody OrderUpdateRequestDto updateRequest) {
        return ResponseEntity.ok(orderService.updateOrder(id, updateRequest));
    }

    /**
     * Get all orders with optional filters.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@RequestParam(required = false) String customerName,
                                                 @RequestParam(required = false) String status,
                                                 @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(orderService.getOrders(customerName, status, sort));
    }

    /**
     * Get order by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    /**
     * Delete an order by ID.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get order history for a specific customer.
     */
    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable String customerName) {
        return ResponseEntity.ok(orderService.getOrderHistory(customerName));
    }

}
