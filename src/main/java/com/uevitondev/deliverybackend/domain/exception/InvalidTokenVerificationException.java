package com.uevitondev.deliverybackend.domain.exception;

public class InvalidTokenVerificationException extends RuntimeException {
    public InvalidTokenVerificationException(String msg) {
        super(msg);
    }
}

