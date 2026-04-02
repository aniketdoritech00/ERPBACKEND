package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "comp-site-mapping-service", url = "${company.service.url}")
public interface CompSiteMappingFeignClient {
	@GetMapping("/employee/api/comp-site-mapping/getAllCompSiteMappingByCompId/{compId}")
	ResponseEntity getAllCompSiteMappingByCompId(
			@PathVariable("compId") Integer compId);

}
