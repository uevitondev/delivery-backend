package com.uevitondev.deliverybackend.domain.tokenverification;

import com.uevitondev.deliverybackend.domain.exception.InvalidTokenVerificationException;
import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import com.uevitondev.deliverybackend.domain.user.User;
import com.uevitondev.deliverybackend.domain.user.UserRepository;
import com.uevitondev.deliverybackend.domain.utils.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TokenVerificationService {

    private final Logger log = LoggerFactory.getLogger(TokenVerificationService.class);

    private final TokenVerificationRepository tokenVerificationRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public TokenVerificationService(
            TokenVerificationRepository tokenVerificationRepository,
            UserRepository userRepository,
            MailService mailService
    ) {
        this.tokenVerificationRepository = tokenVerificationRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Transactional
    public TokenVerification generateTokenVerificationByUser(User user) {
        var tokenVerification = new TokenVerification(user);
        return tokenVerificationRepository.save(tokenVerification);
    }

    public void validateUserTokenVerificationByToken(String token) {
        var tokenVerificationEntity = tokenVerificationRepository.findByToken(token)
                .filter(tokenVerification -> !tokenVerification.isExpired())
                .orElseThrow(() -> {
                    log.error("[TokenVerificationService:validateUserTokenVerificationByToken] Token invalid or expired");
                    return new InvalidTokenVerificationException("token expired or invalid");
                });

        enableUserByTokenVerification(tokenVerificationEntity);
        tokenVerificationEntity.setConfirmedAt(LocalDateTime.now());
        tokenVerificationRepository.save(tokenVerificationEntity);
    }

    public void enableUserByTokenVerification(TokenVerification tokenVerification) {
        var user = tokenVerification.getUser();
        user.isEnabled(true);
        userRepository.save(user);
        log.error("[TokenVerificationService:enableUserByTokenVerification] User enabled by token verification");
    }


    @Transactional
    public void updateTokenVerificationByUsername(String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );
        var tokenVerification = tokenVerificationRepository.findByUser(user).orElseThrow(
                () -> new ResourceNotFoundException("token verification not found")
        );
        tokenVerification.setToken();
        tokenVerification = tokenVerificationRepository.save(tokenVerification);

        var emailDto = new MailService.EmailDTO(
                user.getUsername(),
                user.getFirstName(),
                "Email de Verificação",
                tokenVerification.getToken(),
                "token-verification-email.html"
        );
        mailService.sendEmail(emailDto);
    }


}
