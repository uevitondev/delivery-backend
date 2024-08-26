package com.uevitondev.deliverybackend.domain.authentication;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignUpRequestDTO(
        @NotEmpty(message = "firstName must not be empty")
        String firstName,
        @NotEmpty(message = "lastName must not be empty")
        String lastName,
        @NotEmpty(message = "email must not be empty")
        @Email(message = "email format invalid")
        String email,
        @NotEmpty(message = "password must not be empty")
        String password
) {
}