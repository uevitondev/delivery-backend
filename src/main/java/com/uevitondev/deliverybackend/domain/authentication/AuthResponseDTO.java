package com.uevitondev.deliverybackend.domain.authentication;

import java.util.List;

public record AuthResponseDTO(
        String name,
        List<String> roles,
        String tokenType,
        String accessToken,
        Long expiresAt
) {
}









