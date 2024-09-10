package com.uevitondev.deliverybackend.domain.passwordresettoken;

import com.uevitondev.deliverybackend.domain.exception.InvalidTokenVerificationException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PasswordResetTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetTokenService.class);

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }


    public PasswordResetToken findPasswordResetTokenByToken(String token) {
        return passwordResetTokenRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("password reset token not found by token")
        );

    }

    public PasswordResetToken findPasswordResetTokenByUser(UUID userId) {
        return passwordResetTokenRepository.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("password reset token not found by user")
        );
    }

    @Transactional
    public PasswordResetToken validateAndConfirmPasswordResetTokenByToken(String token) {
        var passwordResetToken = findPasswordResetTokenByToken(token);
        if (passwordResetToken.isExpired()) {
            throw new InvalidTokenVerificationException("password reset token expired");
        }
        passwordResetToken.setConfirmedAt(LocalDateTime.now());
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Transactional
    public PasswordResetToken updatePasswordResetToken(PasswordResetToken passwordResetToken) {
        passwordResetToken.setToken();
        passwordResetToken.setUpdatedAt(LocalDateTime.now());
        passwordResetToken.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        LOGGER.info("update exists password reset token");
        return passwordResetTokenRepository.save(passwordResetToken);
    }

    @Transactional
    public void createNewPasswordResetTokenForUser(User user) {
        LOGGER.info("create new password reset by user: {}", user.getUsername());
        passwordResetTokenRepository.save(new PasswordResetToken(user));
    }


}
