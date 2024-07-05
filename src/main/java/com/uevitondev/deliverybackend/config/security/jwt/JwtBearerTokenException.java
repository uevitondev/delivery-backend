package com.uevitondev.deliverybackend.config.security.jwt;

public class JwtBearerTokenException extends RuntimeException {

    public JwtBearerTokenException(String msg) {
        super(msg);
    }

}