package com.uevitondev.deliverybackend.domain.user;

import com.uevitondev.deliverybackend.domain.role.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserRequestDTO implements Serializable {

    private UUID id;
    @NotBlank(message = "email: is mandatory")
    @Email(message = "email: is invalid")
    private String email;
    @NotBlank(message = "password: is mandatory")
    private String password;
    @NotEmpty(message = "rolesId: is empty")
    private final Set<UUID> rolesId = new HashSet<>();

    public UserRequestDTO() {
    }

    public UserRequestDTO(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public UserRequestDTO(User user, Set<Role> roles) {
        this.id = user.getId();
        this.email = user.getUsername();
        this.password = user.getPassword();
        roles.forEach(role -> this.rolesId.add(role.getId()));
    }

    public UserRequestDTO(User user) {
        this.id = user.getId();
        this.email = user.getUsername();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UUID> getRolesId() {
        return rolesId;
    }
}