package com.uevitondev.deliverybackend.domain.store;

import java.util.UUID;

public record StoreDTO(
        UUID id,
        String logoUrl,
        String name,
        String phoneNumber,
        String type
) {
    public StoreDTO(Store store) {
        this(
                store.getId(),
                store.getLogoUrl(),
                store.getName(),
                store.getPhoneNumber(),
                store.getType()
        );
    }
}