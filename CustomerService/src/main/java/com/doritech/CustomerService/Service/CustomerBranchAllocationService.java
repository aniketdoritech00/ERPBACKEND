package com.doritech.CustomerService.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerBranchAllocationRequest;

public interface CustomerBranchAllocationService {

	ResponseEntity createOrUpdateAllocation(
			CustomerBranchAllocationRequest request);

	ResponseEntity getActiveByCustomerId(Integer customerId);

	ResponseEntity getCustomerBranchAllocationById(Integer allocationId);

	ResponseEntity getAllCustomerBranchAllocations(int page, int size);

	ResponseEntity filterCustomerBranchAllocations(Integer customerId, Integer siteId, String isActive, String fromDate,
			int page, int size);

}