package com.uevitondev.deliverybackend.domain.authentication;

import java.util.List;

public record AuthResponseDTO(
        String tokenType,
        String accessToken,
        Long expiresAt,
        String authName,
        String username,
        List<String> roles
) {
}









