package com.techolution.ecommerce.order.service;

import com.techolution.ecommerce.order.dto.request.OrderRequestDto;
import com.techolution.ecommerce.order.dto.request.OrderUpdateRequestDto;
import com.techolution.ecommerce.order.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(OrderRequestDto orderRequestDto);

    List<Order> getOrders(String customerName, String status, String sort);

    Order getOrderById(UUID orderId);

    Order updateOrder(UUID orderId, OrderUpdateRequestDto updateRequest);

    void deleteOrder(UUID orderId);

    List<Order> getOrderHistory(String customerName);
}
