package com.techolution.ecommerce.order.service;

import com.techolution.ecommerce.order.dto.request.OrderRequestDto;
import com.techolution.ecommerce.order.dto.request.OrderUpdateRequestDto;
import com.techolution.ecommerce.order.model.Order;
import com.techolution.ecommerce.order.model.OrderStatus;
import com.techolution.ecommerce.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    private Order order;
    private UUID orderId;
    private OrderRequestDto validOrderRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderId = UUID.randomUUID();
        validOrderRequestDto = new OrderRequestDto("Dixit Dodiya", "Laptop", 1500.00, 1);
        order = Order.builder().customerName("Dixit Dodiya").productName("Laptop").quantity(1).price(1500.00).status(OrderStatus.PENDING).build();
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    void shouldCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Order createdOrder = orderService.createOrder(validOrderRequestDto);
        assertNotNull(createdOrder);
        assertEquals(order.getCustomerName(), createdOrder.getCustomerName());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void shouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        assertOrders(orderService.getOrders(null, null, null), 1);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    void shouldReturnOrdersByCustomerName() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        assertOrders(orderService.getOrders("Dixit Dodiya", null, null), 1);
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    void shouldReturnOrdersByStatus() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        assertOrders(orderService.getOrders(null, "PENDING", null), 1);
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    void shouldSortOrdersByDateCreatedAsc() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        orderService.getOrders(null, null, "asc");
        verify(orderRepository).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    void shouldSortOrdersByDateCreatedDesc() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        orderService.getOrders(null, null, "desc");
        verify(orderRepository).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    void shouldReturnOrderById() {
        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
        Order foundOrder = orderService.getOrderById(orderId);
        assertNotNull(foundOrder);
        assertEquals(order, foundOrder);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    void shouldUpdateOrder() {
        Order updatedOrder = Order.builder().customerName(order.getCustomerName()).productName(order.getProductName()).quantity(order.getQuantity()).price(order.getPrice()).status(order.getStatus()).build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);
        OrderUpdateRequestDto updateRequest = new OrderUpdateRequestDto(OrderStatus.COMPLETED);
        Order result = orderService.updateOrder(orderId, updateRequest);
        assertEquals(OrderStatus.COMPLETED, result.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    void shouldReturnOrderHistory() {
        String customerName = "Dixit Dodiya";
        Order order = Order.builder().customerName(customerName).quantity(1).price(1500).status(OrderStatus.PENDING).build();
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));
        List<Order> orderHistory = orderService.getOrderHistory(customerName);
        assertFalse(orderHistory.isEmpty());
        assertEquals(customerName, orderHistory.get(0).getCustomerName());
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    void shouldDeleteOrder() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(Order.builder().orderId(orderId).build()));
        orderService.deleteOrder(orderId);
        verify(orderRepository).deleteById(orderId);
    }

    private void assertOrder(Order order) {
        assertNotNull(order);
        assertEquals(this.order, order);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    private void assertOrders(List<Order> orders, int expectedSize) {
        assertFalse(orders.isEmpty());
        assertEquals(expectedSize, orders.size());
        verify(orderRepository).findAll();
    }
}
