package com.uevitondev.deliverybackend.domain.order;

public record OrderCustomerDataDTO(
        String firstName,
        String lastName,
        String phoneNumber
) {
}
