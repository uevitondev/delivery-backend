package com.uevitondev.deliverybackend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.uevitondev.deliverybackend.domain.dto.AuthResponseDTO;
import com.uevitondev.deliverybackend.domain.enums.TokenType;
import com.uevitondev.deliverybackend.security.jwt.exception.JwtBearerTokenException;
import com.uevitondev.deliverybackend.security.userimpl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {
    private final Logger log = LoggerFactory.getLogger(JwtService.class);
    @Value("${security.jwt.access.token.secret.key}")
    private String jwtAccessTokenSecretKey;
    @Value("${security.jwt.refresh.token.secret.key}")
    private String jwtRefreshTokenSecretKey;
    @Value("${security.jwt.access.token.expiration.time}")
    private long jwtAccessTokenExpiration;
    @Value("${security.jwt.refresh.token.expiration.time}")
    private long jwtRefreshTokenExpiration;

    public AuthResponseDTO generateAccessToken(UserDetailsImpl userDetails) {
        try {
            log.info("[JwtService:generateAccessToken] Token Creation Started for:{}", userDetails.getUsername());
            Algorithm algorithm = Algorithm.HMAC256(jwtAccessTokenSecretKey);
            var expiresAt = Instant.now().plusSeconds(jwtAccessTokenExpiration);
            var accessToken = JWT.create()
                    .withIssuer("spring-security-jwt-access-token")
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(Instant.now().plusSeconds(20))
                    .sign(algorithm);
            log.info("[JwtService:generateAccessToken] Authorities: {} ", userDetails.getAuthorities());
            return new AuthResponseDTO(TokenType.Bearer.name(), accessToken, expiresAt, userDetails.getName(), userDetails.getUsername(), userDetails.getAuthorities());
        } catch (JWTCreationException e) {
            throw new JwtBearerTokenException(e.getMessage());
        }
    }

    public String generateRefreshToken(UserDetailsImpl userDetails) {
        try {
            log.info("[JwtServiceConfig:generateRefreshToken] Token Creation Started for:{}", userDetails.getUsername());
            Algorithm algorithm = Algorithm.HMAC256(jwtRefreshTokenSecretKey);
            var expiresAt = Instant.now().plusSeconds(jwtRefreshTokenExpiration);
            var refreshToken = JWT.create()
                    .withIssuer("spring-security-jwt-refresh-token")
                    .withSubject(userDetails.getUsername())
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);
            log.info("[JwtService:generateRefreshToken] RefreshToken for user:{}, has been generated", userDetails.getUsername());
            return refreshToken;
        } catch (JWTCreationException e) {
            throw new JwtBearerTokenException(e.getMessage());
        }
    }

    public String validateAccessToken(String accessToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtAccessTokenSecretKey);
            return JWT.require(algorithm)
                    .withIssuer("spring-security-jwt-access-token")
                    .build()
                    .verify(accessToken)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new JwtBearerTokenException(e.getMessage());
        }

    }

    public String validateRefreshToken(String refreshToken) {
        Algorithm algorithm = Algorithm.HMAC256(jwtRefreshTokenSecretKey);
        return JWT.require(algorithm)
                .withIssuer("spring-security-jwt-access-token")
                .build()
                .verify(refreshToken)
                .getSubject();
    }


}