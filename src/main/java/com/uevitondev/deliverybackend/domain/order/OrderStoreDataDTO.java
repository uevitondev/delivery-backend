package com.uevitondev.deliverybackend.domain.order;

import java.util.UUID;

public record OrderStoreDataDTO(
        UUID id,
        String logoUrl,
        String name,
        String phoneNumber,
        String type
) {
}
