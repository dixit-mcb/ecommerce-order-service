package com.techolution.ecommerce.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Order {
    private UUID orderId;
    private String customerName;
    private String productName;
    private int quantity;
    private double price;
    private OrderStatus status;
    private LocalDateTime dateCreated;

    public Order() {
        this.orderId = UUID.randomUUID();
        this.status = OrderStatus.PENDING;
        this.dateCreated = LocalDateTime.now();
    }

    public Order(String customerName, String productName, int quantity, double price) {
        this();
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

}
