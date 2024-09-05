package com.uevitondev.deliverybackend.config.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    @Value("${security.jwt.access.token.secret.key}")
    private String jwtAccessTokenSecretKey;
    @Value("${security.jwt.access.token.expiration.time}")
    private long jwtAccessTokenExpiration;

    public String generateJwtToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtAccessTokenSecretKey);
            var expiresAt = Instant.now().plusSeconds(jwtAccessTokenExpiration);
            var jwtToken = JWT.create()
                    .withIssuer("spring-security-jwt-access-token")
                    .withSubject(username)
                    .withExpiresAt(expiresAt)
                    .sign(algorithm);
            LOGGER.info("Access Token has been generated for user: {}, value: {}", username, jwtToken);
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