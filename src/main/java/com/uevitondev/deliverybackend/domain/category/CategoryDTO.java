package com.uevitondev.deliverybackend.domain.category;

import java.util.UUID;

public record CategoryDTO(
        UUID id,
        String name
) {

    public CategoryDTO(Category category) {
        this(category.getId(), category.getName());
    }
}