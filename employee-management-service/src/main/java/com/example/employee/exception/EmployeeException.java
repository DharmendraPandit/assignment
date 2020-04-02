package com.example.employee.exception;

public class EmployeeException extends RuntimeException {
    private final int status;
    private final String message;
    private String errorCode;

    public EmployeeException(int status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public EmployeeException(int status, String message, String errorCode) {
        super(message);
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
