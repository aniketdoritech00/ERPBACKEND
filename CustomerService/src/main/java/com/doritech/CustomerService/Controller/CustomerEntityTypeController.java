package com.doritech.CustomerService.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerEntityTypeRequest;
import com.doritech.CustomerService.Service.CustomerEntityTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/entity-type")
public class CustomerEntityTypeController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerEntityTypeController.class);

	private final CustomerEntityTypeService service;

	public CustomerEntityTypeController(CustomerEntityTypeService service) {
		this.service = service;
	}

	@PostMapping("/saveEntity-type")
	public ResponseEntity saveCustomerEntityType(@RequestHeader("X-User-Id") String userId,
			@Valid @RequestBody CustomerEntityTypeRequest request, HttpServletRequest httpRequest) {

		Integer user = Integer.parseInt(userId);

		request.setCreatedBy(user);
		request.setModifiedBy(user);

		return service.saveCustomerEntityType(request);
	}

	@GetMapping("/getEntity-type/{customerId}")
	public ResponseEntity getCustomerEntityType(@PathVariable Integer customerId) {
		return service.getCustomerEntityTypeByCustomerId(customerId);
	}

	@GetMapping("/getAllCustomerEntityTypes")
	public ResponseEntity getAllCustomerEntityTypes(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("Get All Customer Entity Types API hit | page: {}, size: {}", page, size);

		return service.getAllCustomerEntityTypes(page, size);
	}

	@GetMapping("/getCustomerEntityTypes/{customerId}")
	public ResponseEntity getCustomerEntityTypes(@PathVariable Integer customerId) {
		logger.info("Get Customer Entity Types API hit for customerId: {}", customerId);
		return service.getCustomerEntityTypesByCustomerId(customerId);
	}
}