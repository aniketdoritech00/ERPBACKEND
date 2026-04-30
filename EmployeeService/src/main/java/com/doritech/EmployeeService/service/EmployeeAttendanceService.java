package com.doritech.EmployeeService.service;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.AttendanceCheckInRequest;
import com.doritech.EmployeeService.request.AttendanceCheckOutRequest;

import jakarta.validation.Valid;

public interface EmployeeAttendanceService {

	ResponseEntity markCheckIn(@Valid AttendanceCheckInRequest request);

	ResponseEntity markCheckOut(@Valid AttendanceCheckOutRequest request);

	ResponseEntity getAllEmployeeAttendanceRecord(String month);

	ResponseEntity getEmployeeAttendanceRecord(String month, Integer userId);



}
