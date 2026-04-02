package com.doritech.CustomerService.Controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerEmployeeAllocationRequest;
import com.doritech.CustomerService.Response.CustomerEmployeeAllocationResponse;
import com.doritech.CustomerService.Service.CustomerEmployeeAllocationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/customer-employee-allocation")
public class CustomerEmployeeAllocationController {

	@Autowired
	private CustomerEmployeeAllocationService service;

	@PostMapping("/allocate")
	public ResponseEntity allocateEmployee(
			@RequestHeader("X-User-Id") String userId,
			@Valid @RequestBody CustomerEmployeeAllocationRequest request,
			HttpServletRequest httpRequest) {

		Integer user = Integer.parseInt(userId);

		request.setCreatedBy(user);
		request.setModifiedBy(user);

		CustomerEmployeeAllocationResponse response = service
				.allocateEmployee(request);

		return new ResponseEntity("All Update", HttpStatus.OK.value(),
				response);
	}

	@GetMapping("/active")
	public ResponseEntity getAllActive() {
		return new ResponseEntity("All record Fetch Successfully",
				HttpStatus.OK.value(), service.getAllActive());
	}

	@GetMapping("/getAllAllocationWithPagination")
	public ResponseEntity getAllActiveAllocationWithPagination(
			@RequestParam Integer page, @RequestParam Integer size) {

		return new ResponseEntity("All record Fetch Successfully",
				HttpStatus.OK.value(),
				service.getAllAllocationWithPagination(page, size));
	}

	@GetMapping("/active/{customerId}")
	public ResponseEntity getActiveByCustomerId(
			@PathVariable Integer customerId) {
		return new ResponseEntity("Fetch Data successfully",
				HttpStatus.OK.value(),
				service.getActiveByCustomerId(customerId));
	}

	@GetMapping("/filterAllocations")
	public ResponseEntity filterAllocations(
			@RequestParam(required = false) Integer customerId,
			@RequestParam(required = false) Integer employeeId,
			@RequestParam(required = false) String isActive,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,

			// ✅ Pagination params
			@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size) {

		// ✅ Validation
		if (page < 0) {
			throw new IllegalArgumentException("Page must be >= 0");
		}

		if (size <= 0 || size > 100) {
			throw new IllegalArgumentException(
					"Size must be between 1 and 100");
		}

		return new ResponseEntity("Filter Data Sucessfully",
				HttpStatus.OK.value(), service.filterAllocations(customerId,
						employeeId, isActive, fromDate, toDate, page, size));
	}
}