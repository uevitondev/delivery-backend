package com.uevitondev.deliverybackend.domain.tokenverification;

import com.uevitondev.deliverybackend.domain.exception.InvalidTokenVerificationException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class TokenVerificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenVerificationService.class);

    private final TokenVerificationRepository tokenVerificationRepository;

    public TokenVerificationService(
            TokenVerificationRepository tokenVerificationRepository
    ) {
        this.tokenVerificationRepository = tokenVerificationRepository;
    }

    @Transactional
    public TokenVerification generateTokenVerificationByUser(User user) {
        var tokenVerification = new TokenVerification(user);
        LOGGER.info("token verification success generated");
        return tokenVerificationRepository.save(tokenVerification);
    }

    @Transactional
    public TokenVerification validateAndConfirmTokenVerificationByToken(String token) {
        var tokenVerification = tokenVerificationRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("token verification not found")
        );
        if (tokenVerification.isExpired()) {
            throw new InvalidTokenVerificationException("token verification expired");
        }
        tokenVerification.setConfirmedAt(LocalDateTime.now());
        LOGGER.info("token verification success validation and confirmed");
        return tokenVerificationRepository.save(tokenVerification);
    }


    @Transactional
    public TokenVerification updateTokenVerificationByUser(User user) {
        var tokenVerification = tokenVerificationRepository.findByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("token not found for user")
        );
        tokenVerification.setToken();
        tokenVerification.setUpdatedAt(LocalDateTime.now());
        tokenVerification.setExpiredAt(LocalDateTime.now().plusMinutes(3));
        LOGGER.info("token verification success updated");
        return tokenVerificationRepository.save(tokenVerification);
    }


}
