package com.doritech.CustomerService.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerBranchAllocationRequest;
import com.doritech.CustomerService.Service.CustomerBranchAllocationService;

@RestController
@RequestMapping("/customer/api/customer-branch-allocation")
public class CustomerBranchAllocationController {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomerBranchAllocationController.class);

	@Autowired
	private CustomerBranchAllocationService allocationService;

	@PostMapping("/create-or-update")
	public ResponseEntity createOrUpdate(
			@Validated(CustomerBranchAllocationRequest.Create.class) @RequestBody CustomerBranchAllocationRequest request,
			@RequestHeader("X-User-Id") String userId) {

		Integer user = Integer.parseInt(userId);
		logger.info(
				"Create/Update CustomerBranchAllocation API called by user {}",
				user);

		request.setCreatedBy(user);
		request.setModifiedBy(user);

		return allocationService.createOrUpdateAllocation(request);
	}

	@GetMapping("/active/{customerId}")
	public ResponseEntity getActiveAllocation(
			@PathVariable Integer customerId) {
		return allocationService.getActiveByCustomerId(customerId);
	}

	@GetMapping("/{allocationId}")
	public ResponseEntity getById(@PathVariable Integer allocationId) {
		return allocationService.getCustomerBranchAllocationById(allocationId);
	}

	@GetMapping("/all")
	public ResponseEntity getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return allocationService.getAllCustomerBranchAllocations(page, size);
	}

	@GetMapping("/filter")
	public ResponseEntity filter(
			@RequestParam(required = false) Integer customerId,
			@RequestParam(required = false) Integer siteId,
			@RequestParam(required = false) String isActive,
			@RequestParam(required = false) String fromDate,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return allocationService.filterCustomerBranchAllocations(customerId,
				siteId, isActive, fromDate, page, size);
	}
}