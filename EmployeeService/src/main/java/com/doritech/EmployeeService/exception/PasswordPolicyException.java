package com.doritech.EmployeeService.exception;

import java.util.Map;

public class PasswordPolicyException extends RuntimeException {

    private final Map<String, String> errors;

    public PasswordPolicyException(Map<String, String> errors) {
        super("Password validation failed");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
