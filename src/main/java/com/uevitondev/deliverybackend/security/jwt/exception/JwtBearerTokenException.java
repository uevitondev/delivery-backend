package com.uevitondev.deliverybackend.security.jwt.exception;

public class JwtBearerTokenException extends RuntimeException {

    public JwtBearerTokenException(String msg) {
        super(msg);
    }

}