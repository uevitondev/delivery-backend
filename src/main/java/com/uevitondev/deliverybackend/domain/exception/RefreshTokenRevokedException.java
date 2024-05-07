package com.uevitondev.deliverybackend.domain.exception;

public class RefreshTokenRevokedException extends RuntimeException {
    public RefreshTokenRevokedException(String msg) {
        super(msg);
    }
}
