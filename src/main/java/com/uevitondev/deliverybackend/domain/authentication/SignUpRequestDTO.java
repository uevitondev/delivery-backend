package com.uevitondev.deliverybackend.domain.authentication;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
        @NotEmpty(message = "firstName must not be empty")
        @Size(min = 2, max = 25, message = "firstName length min 2 and max 25 characters")
        String firstName,
        @NotEmpty(message = "lastName must not be empty")
        @Size(min = 2, max = 60, message = "lastName length min 2 an max 60 characters")
        String lastName,
        @NotEmpty(message = "email must not be empty")
        @Email(message = "email format invalid")
        String email,
        @NotEmpty(message = "password must not be empty")
        @Size(min = 6, max = 16, message = "password length min 6 and max 16 characters")
        String password
) {
}