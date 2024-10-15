package com.uevitondev.deliverybackend.domain.payment;

import java.util.UUID;

public record PaymentMethodDTO(
        UUID id,
        String name,
        String description
) {
    public PaymentMethodDTO(PaymentMethod paymentMethod) {
        this(paymentMethod.getId(), paymentMethod.getName().toString(), paymentMethod.getDescription());
    }
}

