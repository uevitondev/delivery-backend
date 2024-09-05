package com.uevitondev.deliverybackend.domain.user;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String phoneNumber,
        String username,
        String password
) {


}