package com.uevitondev.deliverybackend.domain.refreshtoken;

import com.uevitondev.deliverybackend.domain.exception.RefreshTokenRevokedException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${security.jwt.refresh.token.expiration.time}")
    private long refreshTokenExpiresAt;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository
    ) {
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

    @Transactional
    public RefreshToken generateRefreshTokenFromUserByUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );
        var refreshTokenEntity = new RefreshToken();
        var optionalRefreshTokenEntity = refreshTokenRepository.findRefreshTokenByUserId(user.getId());
        if (optionalRefreshTokenEntity.isPresent()) {
            refreshTokenEntity = optionalRefreshTokenEntity.get();
            refreshTokenEntity.setToken(UUID.randomUUID().toString());
            refreshTokenEntity.setUpdatedAt(LocalDateTime.now());
            refreshTokenEntity.setExpiredAt(LocalDateTime.now().plusSeconds(refreshTokenExpiresAt));
            LOGGER.info("user has been generated new refresh token: {}", refreshTokenEntity.getToken()
            );
            return refreshTokenRepository.save(refreshTokenEntity);
        }

        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setCreatedAt(LocalDateTime.now());
        refreshTokenEntity.setUpdatedAt(LocalDateTime.now());
        refreshTokenEntity.setExpiredAt(LocalDateTime.now().plusSeconds(refreshTokenExpiresAt));
        refreshTokenEntity.setUser(user);
        LOGGER.info("user has been generated new refresh token: {}", refreshTokenEntity.getToken());
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public RefreshToken validateTokenAndReturnRefreshToken(String token) {
        var refreshTokenEntity = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("refresh token not found"));

        if (refreshTokenEntity.isExpired()) {
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new RefreshTokenRevokedException("token expired");
        }
        return refreshTokenEntity;
    }

}
