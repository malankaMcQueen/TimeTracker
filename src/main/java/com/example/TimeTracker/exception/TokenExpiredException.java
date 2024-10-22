package com.example.TimeTracker.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(final String msg) {
        super(msg);
    }
}

