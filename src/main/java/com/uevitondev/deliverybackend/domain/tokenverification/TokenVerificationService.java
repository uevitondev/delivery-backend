package com.uevitondev.deliverybackend.domain.tokenverification;

import com.uevitondev.deliverybackend.domain.exception.RefreshTokenRevokedException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenVerificationService {

    private final TokenVerificationRepository tokenVerificationRepository;

    public TokenVerificationService(TokenVerificationRepository tokenVerificationRepository) {
        this.tokenVerificationRepository = tokenVerificationRepository;
    }

    @Transactional
    public TokenVerification generateTokenVerificationByUser(User user) {
        var tokenVerification = new TokenVerification(user);
        return tokenVerificationRepository.save(tokenVerification);
    }

    public TokenVerification getTokenVerificationByToken(String token) {
        return tokenVerificationRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("token verification is invalid "));
    }

    @Transactional
    public void updateTokenVerificationById(UUID tokenVerificationId) {
        var tokenVerification = tokenVerificationRepository.getReferenceById(tokenVerificationId);
        tokenVerification.setConfirmedAt(LocalDateTime.now());
        tokenVerificationRepository.save(tokenVerification);
    }


}
