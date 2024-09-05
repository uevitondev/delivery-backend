package com.uevitondev.deliverybackend.domain.product;

import java.util.UUID;

public record NewProductDTO(
        UUID id,
        String name,
        String imgUrl,
        String description,
        Double price,
        UUID categoryId,
        UUID storeId
) {


}
