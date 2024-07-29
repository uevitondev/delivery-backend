package com.uevitondev.deliverybackend.domain.orderitem;

import java.util.UUID;

public record OrderItemDTO(
        UUID id,
        String imgUrl,
        String name,
        String description,
        Double price,
        Integer quantity,
        Double totalPrice
) {


    public OrderItemDTO(OrderItem orderItem) {
        this(
                orderItem.getId(),
                orderItem.getImgUrl(),
                orderItem.getName(),
                orderItem.getDescription(),
                orderItem.getPrice(),
                orderItem.getQuantity(),
                orderItem.getTotalPrice()
        );

    }


}


