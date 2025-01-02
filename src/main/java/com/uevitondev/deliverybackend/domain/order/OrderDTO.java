package com.uevitondev.deliverybackend.domain.order;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDTO(
        UUID id,
        Integer number,
        String status,
        String paymentMethod,
        Double total,
        LocalDateTime createdAt,
        LocalDateTime updatedAt


) {
    public OrderDTO(Order order) {
        this(
                order.getId(),
                order.getNumber(),
                order.getStatus().toString(),
                order.getPaymentMethod().toString(),
                order.getTotal(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );

    }
}
