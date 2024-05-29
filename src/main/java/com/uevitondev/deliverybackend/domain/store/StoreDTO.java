package com.uevitondev.deliverybackend.domain.store;

import java.io.Serializable;
import java.util.UUID;

public class StoreDTO implements Serializable {
    private UUID id;
    private String name;

    public StoreDTO(Store store) {
        this.id = store.getId();
        this.name = store.getName();
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