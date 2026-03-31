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
import com.doritech.EmployeeService.request.RoleMasterRequest;
import com.doritech.EmployeeService.service.RoleMasterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api")
public class RoleMasterController {

	@Autowired
	private RoleMasterService roleService;

	@PostMapping("/createRole")
	public ResponseEntity createRole(@Valid @RequestBody RoleMasterRequest requestBody,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		requestBody.setCreatedBy(Integer.parseInt(userId));
		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		return roleService.saveRole(requestBody);
	}

	@PutMapping("/updateRole/{id}")
	public ResponseEntity updateRole(@PathVariable Integer id, @Valid @RequestBody RoleMasterRequest requestBody,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		requestBody.setModifiedBy(Integer.parseInt(userId));
		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		return roleService.updateRole(id, requestBody);
	}

	@GetMapping("getRoleById/{id}")
	public ResponseEntity getRoleById(@PathVariable Integer id) {
		return roleService.getRoleById(id);
	}

	@GetMapping("/getAllRoles")
	public ResponseEntity getAllRoles(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return roleService.getAllRoles(page, size);
	}

	@DeleteMapping("/deleteRole/{id}")
	public ResponseEntity deleteRole(@PathVariable Integer id, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		return roleService.deleteRole(id);
	}

	@GetMapping("/filterRoles")
	public ResponseEntity filterRoles(@RequestParam(required = false) String roleName,
			@RequestParam(required = false) String isActive, @RequestParam(required = false) Integer createdBy,
			@RequestParam(required = false) Integer modifiedBy, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return roleService.filterRoles(roleName, isActive, createdBy, modifiedBy, page, size);
	}

	@DeleteMapping("/deleteRoles")
	public ResponseEntity deleteMultipleRoles(@RequestParam List<Integer> roleIds,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {
		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());
		return roleService.deleteMultipleRoles(roleIds);
	}

	@GetMapping("/getAllRoleNameAndId")
	public ResponseEntity getAllRoles() {
		return roleService.getAllRoles();
	}
}
