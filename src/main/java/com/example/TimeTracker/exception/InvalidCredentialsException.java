package com.example.TimeTracker.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(final String msg) {
        super(msg);
    }
}
