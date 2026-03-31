package com.doritech.EmployeeService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
import com.doritech.EmployeeService.exception.ApiResponse;
import com.doritech.EmployeeService.exception.BusinessException;
import com.doritech.EmployeeService.request.OrganizationRequestDTO;
import com.doritech.EmployeeService.service.OnCreate;
import com.doritech.EmployeeService.service.OnUpdate;
import com.doritech.EmployeeService.service.OrganizationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/employee/api/organization")
@Validated
public class OrganizationController {

	@Autowired
	private OrganizationService service;

	@PostMapping("/saveOrganization")
	public ResponseEntity saveOrganization(
			@Validated(OnCreate.class) @RequestBody OrganizationRequestDTO dto,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		System.out.println("User id is" + userId);

		dto.setCreatedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Organization created successfully");
		response.setData(service.saveOrganization(dto));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Organization created successfully",
				HttpStatus.CREATED.value(), response);
	}

	@PutMapping("/updateOrganization/{id}")
	public ResponseEntity updateOrganization(@PathVariable @Positive Integer id,
			@Validated(OnUpdate.class) @RequestBody OrganizationRequestDTO dto,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setModifiedBy(Integer.parseInt(userId));
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Organization updated successfully");
		response.setData(service.updateOrganization(id, dto));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Organization updated successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getByOrganizationId/{id}")
	public ResponseEntity getByOrganizationId(
			@PathVariable @Positive Integer id, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Organization fetched successfully");
		response.setData(service.getByOrganizationId(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Organization fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllOrganization")
	public ResponseEntity getAllOrganization(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Organizations fetched successfully");
		response.setData(service.getAllOrganization(page, size));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Organizations fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteOrganizationById/{id}")
	public ResponseEntity deleteOrganization(@PathVariable @Positive Integer id,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		System.out.println(
				"Organization with ID: " + id + " deleted by user: " + userId);
		service.deleteOrganization(id);
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Organization deleted successfully");
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Organization deleted successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/filterOrganization")
	public ResponseEntity filterOrganization(
			@RequestParam(required = false) String orgName,
			@RequestParam(required = false) String active,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Organizations fetched successfully");
		response.setData(
				service.filterOrganization(orgName, active, page, size));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Organizations fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/activeOrganizations")
	public ResponseEntity getAllActiveOrganizations(
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Active organizations fetched successfully");
		response.setData(service.getAllActiveOrganizations());
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Active organizations fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteMultipleOrganizations")
	public ResponseEntity deleteMultipleOrganizations(
			@RequestBody List<Integer> ids,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		if (ids == null || ids.isEmpty()) {
			throw new BusinessException("ID list cannot be empty");
		}
		System.out.println("Organizations with IDs: " + ids
				+ " deleted by user: " + userId);
		service.deleteMultiple(ids);
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Organizations deleted successfully");
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Organizations deleted successfully",
				HttpStatus.OK.value(), response);
	}

}
