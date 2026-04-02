package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "employee-service", url = "${company.service.url}")
public interface EmployeeFeignClient {
	@GetMapping("/employee/api/getEmployee/{id}")
	ResponseEntity getEmployeeById(@PathVariable("id") Integer id);

	@GetMapping("/employee/api/getAllEmployees")
	ResponseEntity getAllEmployees();

}