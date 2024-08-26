package com.uevitondev.deliverybackend.domain.authentication;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignInRequestDTO(
        @NotEmpty(message = "email must not be empty")
        @Email(message = "email format invalid")
        String username,
        @NotEmpty(message = "password must not be empty")
        String password
) {
}