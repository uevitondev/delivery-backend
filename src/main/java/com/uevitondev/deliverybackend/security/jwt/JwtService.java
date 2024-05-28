package com.uevitondev.deliverybackend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.uevitondev.deliverybackend.security.jwt.exception.JwtBearerTokenException;
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
    @Value("${security.jwt.access.token.expiration.time}")
    private long jwtAccessTokenExpiration;

    public String generateJwtToken(String username) {
        try {
            log.info("[JwtService:generateJwtToken] Access Token Generate started for: {}", username);
            Algorithm algorithm = Algorithm.HMAC256(jwtAccessTokenSecretKey);
            var expiresAt = Instant.now().plusSeconds(jwtAccessTokenExpiration);
            var jwtToken = JWT.create()
                    .withIssuer("spring-security-jwt-access-token")
                    .withSubject(username)
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);
            log.info("[JwtService:generateJwtToken] Access Token has been generated: {}", jwtToken);
            return jwtToken;
        } catch (JWTCreationException e) {
            throw new JwtBearerTokenException(e.getMessage());
        }
    }

    public long getExpirationJwtToken() {
        return jwtAccessTokenExpiration;
    }

    public String validateJwtToken(String jwtToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtAccessTokenSecretKey);
            return JWT.require(algorithm)
                    .withIssuer("spring-security-jwt-access-token")
                    .build()
                    .verify(jwtToken)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new JwtBearerTokenException(e.getMessage());
        }

    }


}