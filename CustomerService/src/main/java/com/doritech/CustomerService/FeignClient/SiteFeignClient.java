package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "site-service", url = "${site.service.url}")
public interface SiteFeignClient {

	@GetMapping("/employee/api/companies-site/exists/{siteId}")
	Boolean siteExists(@PathVariable Integer siteId);

	@GetMapping("/employee/api/companies-site/getCompanySiteById")
	ResponseEntity getSiteById(@RequestParam("id") Integer id);
	@GetMapping("/employee/api/companies-site/feign-all-sites")
	ResponseEntity getAllSites();
}