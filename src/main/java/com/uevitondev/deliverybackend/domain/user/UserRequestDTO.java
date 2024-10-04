package com.uevitondev.deliverybackend.domain.user;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String email,
        String password,
        String phoneNumber
) {


}