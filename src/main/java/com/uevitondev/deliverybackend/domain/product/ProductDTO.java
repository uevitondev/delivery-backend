package com.uevitondev.deliverybackend.domain.product;

import java.util.UUID;

public record ProductDTO(
        UUID id,
        String imgUrl,
        String name,
        String description,
        Double price
) {

    public ProductDTO(Product product) {
        this(
                product.getId(),
                product.getImgUrl(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
    }
}



