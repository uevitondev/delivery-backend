package com.uevitondev.deliverybackend.domain.dto;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

public class AuthResponseDTO implements Serializable {
    private String tokenType;
    private String accessToken;
    private Instant expiresAt;
    private String userName;
    private String userEmail;
    private Collection<? extends GrantedAuthority> authorities;


    public AuthResponseDTO() {

    }

    public AuthResponseDTO(String tokenType, String accessToken, Instant expiresAt, String userName, String userEmail, Collection<? extends GrantedAuthority> authorities) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.userName = userName;
        this.userEmail = userEmail;
        this.authorities = authorities;
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

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
