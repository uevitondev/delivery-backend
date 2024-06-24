package com.uevitondev.deliverybackend.domain.store;

import java.io.Serializable;
import java.util.UUID;

public class StoreDTO implements Serializable {
    private UUID id;
    private String logoUrl;
    private String name;
    private String phoneNumber;
    private String type;

    public StoreDTO(Store store) {
        this.id = store.getId();
        this.logoUrl = store.getLogoUrl();
        this.name = store.getName();
        this.phoneNumber = store.getPhoneNumber();
        this.type = store.getType();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}