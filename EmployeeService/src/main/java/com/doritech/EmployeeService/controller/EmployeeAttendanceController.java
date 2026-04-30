package com.doritech.EmployeeService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.AttendanceCheckInRequest;
import com.doritech.EmployeeService.request.AttendanceCheckOutRequest;
import com.doritech.EmployeeService.service.EmployeeAttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api/employee-attendance")
public class EmployeeAttendanceController {

	@Autowired
	EmployeeAttendanceService attendanceService;

	@PostMapping("/check-in")
	public ResponseEntity markEmployeeCheckIn(@Valid @RequestBody AttendanceCheckInRequest request,
			@RequestHeader("X-User-Id") Integer userId) {
		request.setCreatedBy(userId);
		return attendanceService.markCheckIn(request);
	}

	@PutMapping("/check-out")
	public ResponseEntity markEmployeeCheckOut(@Valid @RequestBody AttendanceCheckOutRequest request,
			@RequestHeader("X-User-Id") Integer userId) {
		request.setCreatedBy(userId);
		return attendanceService.markCheckOut(request);
	}

	@GetMapping("/getAllEmployeeAttendanceRecord")
	public ResponseEntity getAllEmployeeAttendanceRecord(@RequestParam(required = false) String month) {
		return attendanceService.getAllEmployeeAttendanceRecord(month);
	}

	@GetMapping("/getEmployeeAttendanceRecord")
	public ResponseEntity getEmployeeAttendanceRecord(@RequestParam(required = false) String month,
			@RequestHeader("X-User-Id") Integer userId) {
		return attendanceService.getEmployeeAttendanceRecord(month, userId);
	}

}
