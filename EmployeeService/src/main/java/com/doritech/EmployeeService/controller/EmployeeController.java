package com.doritech.EmployeeService.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.EmployeeRequest;
import com.doritech.EmployeeService.response.EmployeeResponse;
import com.doritech.EmployeeService.service.EmployeeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/saveEmployee")
	public ResponseEntity saveEmployee(@Valid @RequestBody EmployeeRequest request,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest servletRequest) {
		request.setCreatedBy(Integer.parseInt(userId));
		EmployeeResponse response = employeeService.saveEmployee(request);
		return new ResponseEntity("Employee saved successfully", HttpStatus.CREATED.value(), response);
	}

	@PutMapping("/updateEmployee/{id}")
	public ResponseEntity updateEmployee(@PathVariable("id") Integer employeeId,
			@Valid @RequestBody EmployeeRequest request, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest servletRequest) {
		request.setModifiedBy(Integer.parseInt(userId));
		EmployeeResponse response = employeeService.updateEmployee(employeeId, request);
		return new ResponseEntity("Employee updated successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getEmployee/{id}")
	public ResponseEntity getEmployeeById(@PathVariable("id") Integer employeeId,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest servletRequest) {
		EmployeeResponse response = employeeService.getEmployeeById(employeeId);
		return new ResponseEntity("Employee retrieved successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllEmployees")
	public ResponseEntity getAllEmployees(@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest servletRequest) {
		List<EmployeeResponse> list = employeeService.getAllEmployees();
		return new ResponseEntity("All employees retrieved successfully", HttpStatus.OK.value(), list);
	}

	@GetMapping("/getEmployeesWithPagination")
	public ResponseEntity getEmployeesWithPagination(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest servletRequest) {
		Page<EmployeeResponse> pagedData = employeeService.getEmployeesWithPagination(page, size);
		return new ResponseEntity("Paginated employees retrieved successfully", HttpStatus.OK.value(), pagedData);
	}

	@GetMapping("/filterEmployees")
	public ResponseEntity filterEmployees(@RequestParam(required = false) String employeeName,
			@RequestParam(required = false) String employeeCode, @RequestParam(required = false) String isActive,
			@RequestParam(required = false) String email, @RequestParam(required = false) String phone,
			@RequestParam(required = false) String department, @RequestParam(required = false) String designation,
			@RequestParam(required = false) String role, @RequestParam(required = false) Integer companyId,
			@RequestParam(required = false) Integer siteId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<EmployeeResponse> employees = employeeService.filterEmployees(employeeName, employeeCode, isActive, email,
				phone, department, designation, role, companyId, siteId, page, size);

		return new ResponseEntity("Filtered Employees fetched successfully", 200, employees);
	}

	@GetMapping("/getCompanyIdName")
	public ResponseEntity getCompanyIdName(@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest servletRequest) {
		return employeeService.getCompanyIdAndName();
	}

	@GetMapping("/getSiteIdName")
	public ResponseEntity getSiteIdName(@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest servletRequest) {
		return employeeService.getSiteIdAndName();
	}

	@GetMapping("/getParentIdName")
	public ResponseEntity getParentIdName(@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest servletRequest) {
		List<Map<String, Object>> list = employeeService.getEmployeeAsParentList();
		return new ResponseEntity("Parent ID & Name fetched successfully", 200, list);
	}

	@GetMapping("/getAllEmployeeByCompanyId/{compId}")
	public ResponseEntity getAllEmployeeByCompanyId(
	        @PathVariable Integer compId,
	        @RequestHeader(value = "X-User-Id", required = false) String userId,
	        HttpServletRequest servletRequest) {

	    List<Map<String, Object>> list = employeeService.getAllEmployeeByCompanyId(compId);

	    return new ResponseEntity("Parent ID & Name fetched successfully", 200, list);
	}

	@GetMapping("/getAllActiveEmployees")
	public ResponseEntity getAllActiveEmployees(@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest servletRequest) {
		return employeeService.getAllActiveEmployees();
	}

	@GetMapping("/getAllAssociateFa")
	public ResponseEntity getAllAssociateFa(@RequestParam Integer siteId) {
		return employeeService.getAllAssociateFa(siteId);
	}

	@GetMapping("/getAllFa")
	public ResponseEntity getAllFa() {
		return employeeService.getAllFa();
	}

	@GetMapping("/getDistrictByEmployee/{employeeId}")
	public ResponseEntity getDistrictByEmployee(@PathVariable Integer employeeId) {
		return employeeService.getEmployeeDistrict(employeeId);
	}

	@PostMapping("/uploadEmployeeExcel")
	public org.springframework.http.ResponseEntity<?> uploadEmployeeExcel(@RequestHeader("X-User-Id") String userId,
			@RequestParam("file") MultipartFile file) {

		Integer userIdInt = Integer.parseInt(userId);

		ResponseEntity response = employeeService.uploadEmployeeExcel(file, userIdInt);

		Integer statusCode = response.getStatusCode() != null ? Integer.parseInt(response.getStatusCode().toString())
				: 500;

		if (statusCode == 206) {
			return org.springframework.http.ResponseEntity.status(statusCode)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_error.xlsx")
					.header("X-Message", response.getMessage()).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(response.getPayload());
		}

		return org.springframework.http.ResponseEntity.status(statusCode).body(response.getMessage());
	}

	@GetMapping("/downloadEmployeeTemplate")
	public org.springframework.http.ResponseEntity<?> downloadEmployeeTemplate() {

		byte[] file = employeeService.generateEmployeeTemplate();

		return org.springframework.http.ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_template.xlsx")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(file);
	}
}