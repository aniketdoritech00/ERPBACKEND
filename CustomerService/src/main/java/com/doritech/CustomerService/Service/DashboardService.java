package com.doritech.CustomerService.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;

public interface DashboardService  {

	ResponseEntity getCustomerCountSummary();

	ResponseEntity getMonthlyContractSummary(int year);

}
