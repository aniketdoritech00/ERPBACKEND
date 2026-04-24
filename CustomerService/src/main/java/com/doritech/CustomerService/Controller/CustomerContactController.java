package com.doritech.CustomerService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Request.CustomerContactRequest;
import com.doritech.CustomerService.Service.CustomerContactService;
import com.doritech.CustomerService.ServiceImpl.CustomerContactServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/contact")
public class CustomerContactController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerContactServiceImpl.class);

	private final CustomerContactService service;

	public CustomerContactController(CustomerContactService service) {
		this.service = service;
	}

	@PostMapping("/saveContact")
	public ResponseEntity saveCustomerContact(@RequestBody List<@Valid CustomerContactRequest> request,
			@RequestHeader("X-User-Id") String userId) {

		Integer user;
		try {
			user = Integer.parseInt(userId);
		} catch (NumberFormatException ex) {
			logger.error("saveCustomerContact failed: Invalid X-User-Id header value: {}", userId);
			throw new BadRequestException("Invalid X-User-Id header value: " + userId);
		}

		logger.info("Save Customer Contact API called by user {}", user);

		request.forEach(r -> {
			r.setCreatedBy(user);
			r.setModifiedBy(user);
		});

		return service.saveCustomerContact(request);
	}

	@GetMapping("/getAllCustomerContacts")
	public ResponseEntity getAllCustomerContacts(@RequestHeader("X-User-Id") String userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		logger.info("Get All Customer Contacts API called by user: {}, page: {}, size: {}", userId, page, size);
		return service.getAllCustomerContacts(page, size);
	}

	@GetMapping("/getContact/{customerId}")
	public ResponseEntity getCustomerContacts(@PathVariable Integer customerId,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get Customer Contacts for customerId {} called by user {}", customerId, userId);
		return service.getCustomerContacts(customerId);
	}

	@GetMapping("/getCustomerContactsDetails/{contactId}")
	public ResponseEntity getCustomerContactsDetails(@PathVariable Integer contactId,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get Customer Contacts for contactId {} called by user {}", contactId, userId);
		return service.getCustomerContactsDetails(contactId);
	}

	@DeleteMapping("/deleteBulkCustomerContact")
	public ResponseEntity deleteBulkCustomerContact(@RequestBody List<Integer> contactIds) {
		logger.info("deleteBulkContact endpoint hit with ids: {}", contactIds);
		return service.deleteBulkCustomerContact(contactIds);
	}
}