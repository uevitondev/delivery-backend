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

    public RefreshToken generateRefreshTokenAndSave(String username) {
        log.info("[RefreshTokenService:generateSaveRefreshToken] Refresh Token Generate Started for: {}", username);
        var user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("not found"));
        var optionalRefreshToken = refreshTokenRepository.findRefreshTokenByUserId(user.getId());
        if (optionalRefreshToken.isPresent()) {
            var refreshToken = optionalRefreshToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setUpdatedAt(Instant.now());
            refreshToken.setExpiryDate(Instant.now().plusSeconds(refreshTokenExpiresAt));
            log.info("[RefreshTokenService:generateSaveRefreshToken] Refresh Token (exist and updated) new token: {}", refreshToken.getToken());
            return refreshTokenRepository.save(refreshToken);
        }

        var refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                Instant.now().plusSeconds(refreshTokenExpiresAt),
                userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("not found"))
        );
        log.info("[RefreshTokenService:generateSaveRefreshToken] Refresh Token has been generated: {}", refreshToken.getToken());
        return refreshTokenRepository.save(refreshToken);
    }


    public String getUsernameRefreshTokenByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenRevokedException("refresh token dont exist"))
                .getUser()
                .getUsername();
    }

    public RefreshToken verifyRefreshTokenExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenRevokedException("Refresh token is expired. Please make a new login..!");
        }
        return refreshToken;
    }

}
