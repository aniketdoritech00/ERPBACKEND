package com.doritech.EmployeeService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.UserMasterRequest;
import com.doritech.EmployeeService.service.UserMasterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api")
public class UserMasterController {

	@Autowired
	private UserMasterService userService;

	@PostMapping("/saveUser")
	public ResponseEntity createUser(@Valid @RequestBody UserMasterRequest requestBody, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest servletRequest) {
		requestBody.setCreatedBy(Integer.parseInt(userId));
		return userService.saveUser(requestBody);
	}

	@PutMapping("/updateUser/{id}")
	public ResponseEntity updateUser(@PathVariable Integer id, @Valid @RequestBody UserMasterRequest requestBody,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		requestBody.setModifiedBy(Integer.parseInt(userId));
		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		return userService.updateUser(id, requestBody);
	}

	@GetMapping("getUserById/{id}")
	public ResponseEntity getUserById(@PathVariable Integer id) {
		return userService.getUserById(id);
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return userService.getAllUsers(page, size);
	}

	@DeleteMapping("/deleteUserById/{id}")
	public ResponseEntity deleteUserById(@PathVariable Integer id, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		return userService.deleteUserById(id);
	}

	@GetMapping("/filterUsers")
	public ResponseEntity filterUsers(@RequestParam(required = false) String loginId,
			@RequestParam(required = false) String userType, @RequestParam(required = false) Integer sourceId,
			@RequestParam(required = false) Integer roleId, @RequestParam(required = false) String isActive,

			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		return userService.filterUsers(loginId, userType, sourceId, roleId, isActive, page, size);
	} 

	@DeleteMapping("/deleteUsers")
	public ResponseEntity deleteMultipleUsers(@RequestParam List<Integer> userIds,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		return userService.deleteMultipleUsers(userIds);
	}

	@GetMapping("/getAllEmployeeNameAndId")
	public ResponseEntity getAllEmployeeNames() {
		return userService.getAllEmployeeNames();
	}

}
