package com.uevitondev.deliverybackend.domain.refreshtoken;

import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_user_refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, length = 10000)
    private String token;
    @Column
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(String token, boolean revoked, User user) {
        this.token = token;
        this.revoked = revoked;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
