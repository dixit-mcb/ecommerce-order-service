package com.techolution.ecommerce.order.exception;

public class InvalidOrderStatusException extends IllegalArgumentException {
    public InvalidOrderStatusException(String status) {
        super("Unknown order status: " + status);
    }
}
