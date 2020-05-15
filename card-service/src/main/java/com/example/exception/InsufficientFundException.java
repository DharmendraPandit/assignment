package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Insuffient fund")
public class InsufficientFundException extends Exception {

    public InsufficientFundException() {
        super();
    }

    public InsufficientFundException(String message) {
        super(message);
    }

}
