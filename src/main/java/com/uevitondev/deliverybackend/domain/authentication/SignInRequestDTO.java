package com.uevitondev.deliverybackend.domain.authentication;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignInRequestDTO(
        @NotEmpty(message = "email must not be empty")
        @Email(message = "email format invalid")
        String username,
        @NotEmpty(message = "password must not be empty")
        @Size(min = 6, max = 16, message = "password length min 6 and max 16 characters")
        String password
) {
}