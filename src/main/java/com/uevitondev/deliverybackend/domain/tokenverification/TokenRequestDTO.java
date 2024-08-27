package com.uevitondev.deliverybackend.domain.tokenverification;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TokenRequestDTO(
        @NotEmpty(message = "token must not be empty")
        @Size(max = 6)
        String token
) {
}