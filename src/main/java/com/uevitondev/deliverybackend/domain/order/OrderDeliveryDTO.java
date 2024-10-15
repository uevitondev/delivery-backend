package com.uevitondev.deliverybackend.domain.order;

import java.util.UUID;

public record OrderDeliveryDTO(
        UUID id,
        String deliveryName,
        String deliveryPhoneNumber,
        String deliveryStreet,
        Integer deliveryNumber,
        String deliveryDistrict,
        String deliveryCity,
        String deliveryUf,
        String deliveryComplement,
        String deliveryZipCode
) { }
