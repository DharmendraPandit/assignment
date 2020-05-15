package com.example.exception;

public class InvalidCardException extends RuntimeException {
    private final int status;
    private final String message;

    public InvalidCardException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
