package com.uevitondev.deliverybackend.domain.orderitem;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record ShoppingCartDTO(
        @NotNull
        UUID addressId,
        @NotNull
        UUID storeId,
        @NotNull
        String paymentMethod,
        @NotEmpty
        Set<CartItemDTO> cartItems
) {
}




