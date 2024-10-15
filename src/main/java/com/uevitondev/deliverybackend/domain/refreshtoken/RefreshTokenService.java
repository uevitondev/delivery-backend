package com.uevitondev.deliverybackend.domain.refreshtoken;

import com.uevitondev.deliverybackend.domain.exception.RefreshTokenRevokedException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${security.jwt.refresh.token.expiration.time}")
    private long refreshTokenExpiresAt;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository

    ) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public void generateRefreshTokenFromUser(User user) {
        var refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setCreatedAt(LocalDateTime.now());
        refreshTokenEntity.setUpdatedAt(LocalDateTime.now());
        refreshTokenEntity.setExpiredAt(LocalDateTime.now().plusSeconds(refreshTokenExpiresAt));
        refreshTokenEntity.setUser(user);
        LOGGER.info("refresh token has been generated");
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public RefreshToken validateAndConfirmRefreshTokenByToken(String token) {
        var refreshTokenEntity = refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("refresh token not found")
        );

        if (refreshTokenEntity.isExpired()) {
            throw new RefreshTokenRevokedException("refresh token expired");
        }
        return refreshTokenEntity;
    }

    @Transactional
    public RefreshToken updateRefreshTokenByUser(User user) {
        var refreshToken = refreshTokenRepository.findByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("refresh token not found")
        );
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUpdatedAt(LocalDateTime.now());
        refreshToken.setExpiredAt(LocalDateTime.now().plusSeconds(refreshTokenExpiresAt));
        refreshToken = refreshTokenRepository.save(refreshToken);
        LOGGER.info("refresh token has been updated new refresh token");
        return refreshToken;
    }


}
