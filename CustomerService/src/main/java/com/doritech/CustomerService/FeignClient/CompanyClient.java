package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "company-service", url = "${company.service.url}")
public interface CompanyClient {

	@GetMapping("/employee/api/company/getCompanyById/{compId}")
	ResponseEntity getCompanyById(@PathVariable Integer compId);
}