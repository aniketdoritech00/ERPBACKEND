package com.doritech.CustomerService.Service;

import java.time.LocalDate;
import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerAllocationRequest;


public interface CustomerAllocationService {
	
	ResponseEntity saveCustomerAllocation(CustomerAllocationRequest request);
	
	ResponseEntity getAllCustomerAllocation(int page, int size);
	
	ResponseEntity getCustomerAllocationById(Integer Id);
	
	ResponseEntity updateCustomerAllocationById(Integer Id, CustomerAllocationRequest request);
	
	ResponseEntity deleteMultipleCustomerAllocation(List<Integer> ids);

	ResponseEntity filterCustomerAllocation(Integer customerId, Integer employeeId, LocalDate fromDate,
			Integer createdBy, Integer modifiedBy, String isActive, int page, int size);

}
