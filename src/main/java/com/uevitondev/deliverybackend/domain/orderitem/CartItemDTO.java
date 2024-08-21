package com.uevitondev.deliverybackend.domain.orderitem;

import com.uevitondev.deliverybackend.domain.product.ProductDTO;

public record CartItemDTO(ProductDTO product, Integer quantity, String note) {}