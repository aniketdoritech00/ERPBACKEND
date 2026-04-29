package com.doritech.CustomerService.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.AmcExpenseRequest;

public interface AmcExpenseService {

	public ResponseEntity saveOrUpdateAmcExpense(AmcExpenseRequest request);

	List<Map<String, Object>> getAllAssignmentExpense(Integer employeeId, Integer siteId, LocalDateTime startDate,
			LocalDateTime endDate);
}
