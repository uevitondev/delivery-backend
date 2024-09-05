package com.uevitondev.deliverybackend.domain.order;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDTO(
        UUID id,
        OrderStatus status,
        OrderPayment paymentMethod,
        Double total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt


) {
    public OrderDTO(Order order) {
        this(
                order.getId(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );

    }
}
