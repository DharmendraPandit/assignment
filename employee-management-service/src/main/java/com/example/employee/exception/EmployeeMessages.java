package com.example.employee.exception;

public enum EmployeeMessages {
    MANADATORY_FIELDS("name/salary/age is invalid"),
    SEARCH_FIELDS("name/age is invalid"),
    PAYROLL_ERROR("payroll service not responding"),
    NOT_FOUND("Employee not found"),
    INVALID_INPUT("Invalid input");

    private String message;

    EmployeeMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
