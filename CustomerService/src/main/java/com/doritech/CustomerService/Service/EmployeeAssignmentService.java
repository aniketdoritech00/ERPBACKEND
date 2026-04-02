package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.EmployeeAssignmentRequest;
import com.doritech.CustomerService.Response.EmployeeAssignmentResponse;
import com.doritech.CustomerService.Response.PageResponse;

public interface EmployeeAssignmentService {

	EmployeeAssignmentResponse saveEmployeeAssignment(
			EmployeeAssignmentRequest request);

	List<EmployeeAssignmentResponse> saveBulkEmployeeAssignment(
			List<EmployeeAssignmentRequest> requests);

	PageResponse<EmployeeAssignmentResponse> getEmployeeAssignments(
			Integer employeeId, int page, int size, String sortBy,
			String sortDir);

	ResponseEntity getCustomerDetailsByAssignmentId(Integer assignmentId);

}
