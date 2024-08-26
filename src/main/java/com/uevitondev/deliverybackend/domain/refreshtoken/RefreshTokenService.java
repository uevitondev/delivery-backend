package com.uevitondev.deliverybackend.domain.refreshtoken;

import com.uevitondev.deliverybackend.domain.exception.RefreshTokenRevokedException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class RefreshTokenService {
    private final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);
    @Value("${security.jwt.refresh.token.expiration.time}")
    private long refreshTokenExpiresAt;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }


    boolean hasRefreshTokenFromUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );
        var optionalRefreshToken = refreshTokenRepository.findRefreshTokenByUserId(user.getId());
        return optionalRefreshToken.isPresent();
    }

    public RefreshToken generateRefreshTokenFromUserByUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );
        var refreshTokenEntity = new RefreshToken();
        var optionalRefreshTokenEntity = refreshTokenRepository.findRefreshTokenByUserId(user.getId());
        if (optionalRefreshTokenEntity.isPresent()) {
            refreshTokenEntity = optionalRefreshTokenEntity.get();
            refreshTokenEntity.setToken(UUID.randomUUID().toString());
            refreshTokenEntity.setUpdatedAt(Instant.now());
            refreshTokenEntity.setExpiryDate(Instant.now().plusSeconds(refreshTokenExpiresAt));
            log.info(
                    "[RefreshTokenService:generateRefreshTokenFromUserByUsername] Refresh Token (exist and updated) " +
                            "new token: {}", refreshTokenEntity.getToken()
            );
            return refreshTokenRepository.save(refreshTokenEntity);
        }
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setCreatedAt(Instant.now());
        refreshTokenEntity.setUpdatedAt(Instant.now());
        refreshTokenEntity.setExpiryDate(Instant.now().plusSeconds(refreshTokenExpiresAt));
        refreshTokenEntity.setUser(user);
        log.info("[RefreshTokenService:generateRefreshTokenFromUserByUsername] Refresh Token has been generated: {}", refreshTokenEntity.getToken());
        return refreshTokenRepository.save(refreshTokenEntity);
    }


    public RefreshToken validateTokenAndReturnRefreshToken(String token) {
        var refreshTokenEntity = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenRevokedException("refresh token dont exist"));

        if (refreshTokenEntity.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new RefreshTokenRevokedException("Refresh token is expired. Please make a new login..!");
        }
        return refreshTokenEntity;
    }

}
