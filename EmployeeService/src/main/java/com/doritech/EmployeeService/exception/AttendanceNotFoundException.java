package com.doritech.EmployeeService.exception;

public class AttendanceNotFoundException extends RuntimeException {
    public AttendanceNotFoundException(String message) {
        super(message);
    }
}