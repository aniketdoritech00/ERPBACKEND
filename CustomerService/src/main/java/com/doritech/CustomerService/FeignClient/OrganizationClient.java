package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "organization-service", url = "${org.service.url}")
public interface OrganizationClient {

	@GetMapping("/employee/api/organization/getOrgById/{id}")
	  ResponseEntity getOrganization(@PathVariable("id") Integer id);

}