package com.techolution.ecommerce.order.controller;

import com.techolution.ecommerce.order.dto.request.OrderRequestDto;
import com.techolution.ecommerce.order.dto.request.OrderUpdateRequestDto;
import com.techolution.ecommerce.order.model.Order;
import com.techolution.ecommerce.order.model.OrderStatus;
import com.techolution.ecommerce.order.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderServiceImpl orderService;

    private Order order;
    private UUID orderId;
    private OrderRequestDto validOrderRequest;
    private static final String CUSTOMER_NAME = "Dixit Dodiya";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        injectOrderService();
        validOrderRequest = createOrderRequest();
        orderId = UUID.randomUUID();
        order = createOrder(CUSTOMER_NAME, "Laptop", 1, 1500.00);
    }


    @Test
    void shouldCreateOrder() {
        when(orderService.createOrder(validOrderRequest)).thenReturn(order);
        ResponseEntity<Order> response = orderController.createOrder(validOrderRequest);
        verifyResponse(response, HttpStatus.CREATED, order);
        verify(orderService).createOrder(validOrderRequest);
    }

    @Test
    void shouldRetrieveOrders() {
        when(orderService.getOrders(null, null, null)).thenReturn(Collections.singletonList(order));
        ResponseEntity<List<Order>> response = orderController.getOrders(null, null, null);
        verifyResponse(response, HttpStatus.OK, Collections.singletonList(order));
        verify(orderService).getOrders(null, null, null);
    }

    @Test
    void shouldFilterOrdersByCustomerName() {
        when(orderService.getOrders(CUSTOMER_NAME, null, null)).thenReturn(Collections.singletonList(order));
        ResponseEntity<List<Order>> response = orderController.getOrders(CUSTOMER_NAME, null, null);
        verifyResponse(response, HttpStatus.OK, Collections.singletonList(order));
        verify(orderService).getOrders(CUSTOMER_NAME, null, null);
    }

    @Test
    void shouldFilterOrdersByStatus() {
        when(orderService.getOrders(null, "PENDING", null)).thenReturn(Collections.singletonList(order));
        ResponseEntity<List<Order>> response = orderController.getOrders(null, "PENDING", null);
        verifyResponse(response, HttpStatus.OK, Collections.singletonList(order));
        verify(orderService).getOrders(null, "PENDING", null);
    }

    @Test
    void shouldRetrieveOrderById() {
        when(orderService.getOrderById(orderId)).thenReturn(order);
        ResponseEntity<Order> response = orderController.getOrderById(orderId);
        verifyResponse(response, HttpStatus.OK, order);
        verify(orderService).getOrderById(orderId);
    }

    @Test
    void shouldUpdateOrder() {
        OrderUpdateRequestDto updateRequest = new OrderUpdateRequestDto();
        updateRequest.setStatus(OrderStatus.COMPLETED);
        when(orderService.updateOrder(orderId, updateRequest)).thenReturn(order);
        ResponseEntity<Order> response = orderController.updateOrder(orderId, updateRequest);
        verifyResponse(response, HttpStatus.OK, order);
        verify(orderService).updateOrder(orderId, updateRequest);
    }

    @Test
    void shouldDeleteOrder() {
        doNothing().when(orderService).deleteOrder(orderId);
        ResponseEntity<Void> response = orderController.deleteOrder(orderId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(orderService).deleteOrder(orderId);
    }

    @Test
    void shouldRetrieveOrderHistory() {
        when(orderService.getOrderHistory(CUSTOMER_NAME)).thenReturn(Collections.singletonList(order));
        ResponseEntity<List<Order>> response = orderController.getOrderHistory(CUSTOMER_NAME);
        verifyResponse(response, HttpStatus.OK, Collections.singletonList(order));
        verify(orderService).getOrderHistory(CUSTOMER_NAME);
    }

    private void injectOrderService() throws NoSuchFieldException, IllegalAccessException {
        Field serviceField = OrderController.class.getDeclaredField("orderService");
        serviceField.setAccessible(true);
        serviceField.set(orderController, orderService);
    }

    private OrderRequestDto createOrderRequest() {
        OrderRequestDto request = new OrderRequestDto();
        request.setCustomerName(CUSTOMER_NAME);
        request.setProductName("Laptop");
        request.setQuantity(1);
        request.setPrice(1500.00);
        return request;
    }

    private Order createOrder(String customerName, String productName, int quantity, double price) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setProductName(productName);
        order.setQuantity(quantity);
        order.setPrice(price);
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    private <T> void verifyResponse(ResponseEntity<T> response, HttpStatus expectedStatus, T expectedBody) {
        assertEquals(expectedStatus, response.getStatusCode());
        assertEquals(expectedBody, response.getBody());
    }

}
