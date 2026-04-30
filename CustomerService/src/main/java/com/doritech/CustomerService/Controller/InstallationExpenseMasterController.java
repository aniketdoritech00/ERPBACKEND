package com.doritech.CustomerService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.InstallationExpenseMasterRequest;
import com.doritech.CustomerService.Service.InstallationExpenseMasterService;

@RestController
@RequestMapping("/customer/api/installation-expense")
public class InstallationExpenseMasterController {

	@Autowired
	private InstallationExpenseMasterService service;

	@PostMapping("/saveUpdateInstallationExpense")
	public ResponseEntity saveOrUpdate(@RequestBody InstallationExpenseMasterRequest request,
			@RequestHeader("X-User-Id") String userId) {
		return service.saveOrUpdate(request);
	}

	@GetMapping("/calculateinstallationExpense")
	public ResponseEntity getCompletedAssignmentsExpense(@RequestHeader("X-User-Id") String userId) {
		return service.getCompletedAssignmentsExpense();
	}

}