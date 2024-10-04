package com.uevitondev.deliverybackend.domain.orderitem;

import com.uevitondev.deliverybackend.domain.product.ProductDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CartItemDTO(
        @NotNull
        ProductDTO product,
        @NotNull
        Integer quantity,
        @Size(max = 120)
        String note
) {
}