package com.doritech.CustomerService.Controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerAllocationRequest;
import com.doritech.CustomerService.Service.CustomerAllocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/allocation")
public class CustomerAllocationController {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomerAllocationController.class);

	@Autowired
	private CustomerAllocationService customerAllocationService;

	@PostMapping("/saveCustomerAllocation")
	public ResponseEntity saveCustomerAllocation(
			@RequestBody @Valid CustomerAllocationRequest request,
			@RequestHeader("X-User-Id") String userId) {

		Integer user = Integer.parseInt(userId);
		logger.info("Save CustomerAllocation API called by user {}", user);

		request.setCreatedBy(user);

		return customerAllocationService.saveCustomerAllocation(request);
	}

	@GetMapping("/getAllCustomerAllocations")
	public ResponseEntity getAllCustomerAllocations(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Get All CustomerAllocations API called by user {}",
				userId);
		return customerAllocationService.getAllCustomerAllocation(page, size);
	}

	@GetMapping("/getCustomerAllocationById")
	public ResponseEntity getCustomerAllocationById(@RequestParam Integer id,
			@RequestHeader("X-User-Id") String userId) {

		logger.info(
				"Get CustomerAllocation By Id API called for id {} by user {}",
				id, userId);
		return customerAllocationService.getCustomerAllocationById(id);
	}

	@PutMapping("/updateCustomerAllocationById")
	public ResponseEntity updateCustomerAllocationById(@RequestParam Integer id,
			@RequestBody @Valid CustomerAllocationRequest request,
			@RequestHeader("X-User-Id") String userId) {

		Integer user = Integer.parseInt(userId);
		logger.info(
				"Update CustomerAllocation By Id API called for id {} by user {}",
				id, user);

		request.setModifiedBy(user);
		return customerAllocationService.updateCustomerAllocationById(id,
				request);
	}

	@DeleteMapping("/deleteMultipleCustomerAllocation")
	public ResponseEntity deleteMultipleCustomerAllocation(
			@RequestParam List<Integer> ids,
			@RequestHeader("X-User-Id") String userId) {

		logger.info(
				"Delete Multiple CustomerAllocations API called for ids {} by user {}",
				ids, userId);
		return customerAllocationService.deleteMultipleCustomerAllocation(ids);
	}

	@GetMapping("/filterCustomerAllocation")
	public ResponseEntity filterCustomerAllocation(
			@RequestParam(required = false) Integer customerId,
			@RequestParam(required = false) Integer employeeId,
			@RequestParam(required = false) LocalDate fromDate,
			@RequestParam(required = false) Integer createdBy,
			@RequestParam(required = false) Integer modifiedBy,
			@RequestParam(required = false) String isActive,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Filter CustomerAllocations API called by user {}", userId);
		return customerAllocationService.filterCustomerAllocation(customerId,
				employeeId, fromDate, createdBy, modifiedBy, isActive, page,
				size);
	}
}