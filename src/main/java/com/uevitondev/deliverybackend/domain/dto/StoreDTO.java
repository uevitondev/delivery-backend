package com.uevitondev.deliverybackend.domain.dto;

import com.uevitondev.deliverybackend.domain.model.Store;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

public class StoreDTO implements Serializable {
    private UUID id;
    @NotBlank(message = "name: is mandatory")
    private String name;

    public StoreDTO() {
    }

    public StoreDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public StoreDTO(Store pizzeria) {
        this.id = pizzeria.getId();
        this.name = pizzeria.getName();
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