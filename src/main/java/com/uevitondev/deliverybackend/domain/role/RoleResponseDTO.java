package com.uevitondev.deliverybackend.domain.role;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class RoleResponseDTO implements Serializable {

    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public RoleResponseDTO() {
    }

    public RoleResponseDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.createdAt = role.getCreatedAt();
        this.updateAt = role.getUpdateAt();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
