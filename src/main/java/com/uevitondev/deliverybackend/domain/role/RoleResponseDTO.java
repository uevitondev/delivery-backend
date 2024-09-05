package com.uevitondev.deliverybackend.domain.role;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoleResponseDTO(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public RoleResponseDTO(Role role) {
        this(
                role.getId(),
                role.getName(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }
}
