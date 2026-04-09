package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "user-service", url = "${company.service.url}")
public interface UserClient {

	@GetMapping("/employee/api/getUserById/{id}")
	ResponseEntity getUserById(@PathVariable Integer id);
}