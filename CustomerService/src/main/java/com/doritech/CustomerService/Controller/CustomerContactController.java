package com.doritech.CustomerService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerContactRequest;
import com.doritech.CustomerService.Service.CustomerContactService;
import com.doritech.CustomerService.ServiceImpl.CustomerContactServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/contact")
public class CustomerContactController {
	private static final Logger logger = LoggerFactory
			.getLogger(CustomerContactServiceImpl.class);

	private final CustomerContactService service;

	public CustomerContactController(CustomerContactService service) {
		this.service = service;
	}

	@PostMapping("/saveContact")
	public ResponseEntity saveCustomerContact(
			@RequestBody List<@Valid CustomerContactRequest> request,
			@RequestHeader("X-User-Id") String userId) {

		Integer user = Integer.parseInt(userId);
		logger.info("Save Customer Contact API called by user {}", user);

		// Set createdBy/modifiedBy for all requests
		request.forEach(r -> {
			r.setCreatedBy(user);
			r.setModifiedBy(user);
		});

		return service.saveCustomerContact(request);
	}
	@GetMapping("/getAllCustomerContacts")
	public ResponseEntity getAllCustomerContacts(
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get All Customer Contacts API called by user {}", userId);
		return service.getAllCustomerContacts();
	}
	@GetMapping("/getContact/{customerId}")
	public ResponseEntity getCustomerContacts(@PathVariable Integer customerId,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Get Customer Contacts for customerId {} called by user {}",
				customerId, userId);
		return service.getCustomerContacts(customerId);
	}
}