package com.techolution.ecommerce.order.model;

import com.techolution.ecommerce.order.exception.InvalidOrderStatusException;

public class OrderStatusFactory {

    public static OrderStatus createOrderStatus(String status) {
        switch (status.toUpperCase()) {
            case "PENDING":
                return OrderStatus.PENDING;
            case "COMPLETED":
                return OrderStatus.COMPLETED;
            case "CANCELLED":
                return OrderStatus.CANCELLED;
            default:
                throw new InvalidOrderStatusException(status);
        }
    }
}
