package com.doritech.CustomerService.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Service.DashboardService;
import com.doritech.CustomerService.ServiceImpl.CustomerServiceImpl;

@RestController
@RequestMapping("/customer/api/dashboard")
public class DashBoardController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("/dashboard-count")
	public ResponseEntity getCustomerCount() {
		logger.info("GET /dashboard/customer-count - Fetching customer count");
		return dashboardService.getCustomerCountSummary();
	}

	@GetMapping("/contract/monthly/{year}")
	public ResponseEntity getMonthlyData(@PathVariable int year) {
	    return dashboardService.getMonthlyContractSummary(year);
	}
}
