package com.doritech.CustomerService.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.AmcExpenseRequest;
import com.doritech.CustomerService.Service.AmcExpenseService;

@RestController
@RequestMapping("/customer/api/amc")
public class AmcExpenseController {

	@Autowired
	private AmcExpenseService amcExpenseService;

	@GetMapping("/getAllAssignmentExpense")
	public ResponseEntity getExpense(@RequestHeader("X-User-Id") String userId,
			@RequestParam(required = false) Integer employeeId, @RequestParam(required = false) Integer siteId,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {

		LocalDateTime start = null;
		LocalDateTime end = null;

		try {
			if (startDate != null && !startDate.isEmpty()) {
				start = LocalDateTime.parse(startDate);
			}

			if (endDate != null && !endDate.isEmpty()) {
				end = LocalDateTime.parse(endDate);
			}
		} catch (Exception e) {
			return new ResponseEntity("Invalid date format. Use ISO format", 400, null);
		}

		List<Map<String, Object>> data = amcExpenseService.getAllAssignmentExpense(employeeId, siteId, start, end);

		return new ResponseEntity("AMC expense calculated successfully", 200, data);
	}

	@PostMapping("/saveOrUpdate")
	public ResponseEntity saveOrUpdate(@RequestHeader("X-User-Id") String userId,
			@RequestBody AmcExpenseRequest request) {

		return amcExpenseService.saveOrUpdateAmcExpense(request);
	}
}