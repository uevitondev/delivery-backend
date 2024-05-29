package com.uevitondev.deliverybackend.domain.category;

import java.io.Serializable;
import java.util.UUID;

public class CategoryDTO implements Serializable {
    private UUID id;
    private String name;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}