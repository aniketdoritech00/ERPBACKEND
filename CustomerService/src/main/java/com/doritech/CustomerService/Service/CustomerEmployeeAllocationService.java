package com.doritech.CustomerService.Service;

import java.time.LocalDate;
import java.util.List;

import com.doritech.CustomerService.Request.CustomerEmployeeAllocationRequest;
import com.doritech.CustomerService.Response.CustomerEmployeeAllocationResponse;
import com.doritech.CustomerService.Response.PageResponse;

public interface CustomerEmployeeAllocationService {
	CustomerEmployeeAllocationResponse allocateEmployee(
			CustomerEmployeeAllocationRequest request);

	CustomerEmployeeAllocationResponse getActiveByCustomerId(
			Integer customerId);

	List<CustomerEmployeeAllocationResponse> getAllActive();

	PageResponse<CustomerEmployeeAllocationResponse> getAllAllocationWithPagination(
			Integer page, Integer size);

	PageResponse<CustomerEmployeeAllocationResponse> filterAllocations(
			Integer customerId, Integer employeeId, String isActive,
			LocalDate fromDate, LocalDate toDate, Integer page, Integer size);
}