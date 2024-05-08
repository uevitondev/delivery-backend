package com.uevitondev.deliverybackend.domain.authentication;

import com.uevitondev.deliverybackend.domain.role.Role;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.*;

public class AuthResponseDTO implements Serializable {
    private String tokenType;
    private String accessToken;
    private Long expiresAt;
    private String authName;
    private String username;
    private List<String> roles = new ArrayList<>();


    public AuthResponseDTO() {

    }

    public AuthResponseDTO(String tokenType, String accessToken, Long expiresAt, String authName, String username, List<String> roles) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.authName = authName;
        this.username = username;
        this.roles = roles;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
