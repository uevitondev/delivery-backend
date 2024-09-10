package com.uevitondev.deliverybackend.domain.passwordresettoken;

import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "tb_password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public PasswordResetToken() {
    }

    public PasswordResetToken(User user) {
        this.setToken();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
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

    public void setToken() {
        this.token = Integer.toString(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public boolean isExpired() {
        return this.expiredAt.isBefore(LocalDateTime.now());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
