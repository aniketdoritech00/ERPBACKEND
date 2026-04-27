package com.doritech.CustomerService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Request.CustomerRequest;
import com.doritech.CustomerService.Service.CustomerService;
import com.doritech.CustomerService.ServiceImpl.CustomerServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/customer/api")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping("/createCustomer")
	public ResponseEntity createCustomer(@RequestBody CustomerRequest request,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest httpRequest) {
		logger.info("Create Customer API hit by user {}", userId);
		if (userId == null || userId.trim().isEmpty()) {
			throw new BadRequestException("X-User-Id header must not be empty");
		}
		Integer user = Integer.parseInt(userId);
		request.setCreatedBy(user);
		request.setModifiedBy(user);
		return customerService.createCustomer(request);
	}

	@DeleteMapping("/deleteCustomer")
	public ResponseEntity deleteCustomer(@RequestBody List<Integer> ids,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest httpRequest) {
		logger.info("Delete Customer API hit by user {} for ids {}", userId, ids);
		return customerService.deleteCustomer(ids);
	}

	@PutMapping("/updateCustomer/{id}")
	public ResponseEntity updateCustomer(@PathVariable Integer id,
			@RequestBody CustomerRequest request,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest httpRequest) {
		logger.info("Update Customer API hit by user {} for ID {}", userId, id);
		if (userId == null || userId.trim().isEmpty()) {
			throw new BadRequestException("X-User-Id header must not be empty");
		}
		request.setModifiedBy(Integer.parseInt(userId));
		return customerService.updateCustomer(id, request);
	}

	@GetMapping("/getCustomerById/{id}")
	public ResponseEntity getCustomerById(@PathVariable Integer id) {
		logger.info("Get Customer By ID API hit for {}", id);
		return customerService.getCustomerById(id);
	}

	@GetMapping("/getAllCustomers")
	public ResponseEntity getAllCustomers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		logger.info("Get All Customers API hit. Page {}, Size {}", page, size);
		return customerService.getAllCustomers(page, size);
	}

	@GetMapping("/getParentCustomerNames")
	public ResponseEntity getParentCustomerNames() {
		logger.info("Get Parent Customer Names API hit");
		return customerService.getParentCustomerNames();
	}

	@GetMapping("/filterForCustomer")
	public ResponseEntity filterCustomers(
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String entityType) {
		return customerService.filterCustomers(name, status, entityType);
	}

	@GetMapping("/getCustomerNames")
	public ResponseEntity getAllCustomerNames() {
		return customerService.getAllCustomerNames();
	}

	@GetMapping("getDistrict/{customerId}")
	public ResponseEntity getDistrict(@PathVariable Integer customerId) {
		return customerService.getCustomerDistrict(customerId);
	}

	@GetMapping("/getCustomerByHierarchy/{customerId}")
	public ResponseEntity getCustomerByHierarchy(@PathVariable Integer customerId) {
		logger.info("API called: getCustomersByCustomerHierarchy with customerId {}", customerId);
		return customerService.getCustomerByHierarchy(customerId);
	}

	@GetMapping("getCustomerDetailsByCustomerId/{customerId}")
	public ResponseEntity getCustomerDetailsByCustomerId(@PathVariable Integer customerId) {
		return customerService.getCustomerDetailsByCustomerId(customerId);
	}

	@GetMapping("/getAllCustomerNamesForFilter")
	public ResponseEntity getAllCustomerNamesForFilter() {
		return customerService.getAllCustomerNamesForFilter();
	}
}