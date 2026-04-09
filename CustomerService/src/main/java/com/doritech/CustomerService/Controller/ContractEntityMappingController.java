package com.doritech.CustomerService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.doritech.CustomerService.Request.ContractEntityMappingRequest;
import com.doritech.CustomerService.Service.ContractEntityMappingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/contract-entity-mapping")
public class ContractEntityMappingController {

	private static final Logger logger = LoggerFactory
			.getLogger(ContractEntityMappingController.class);

	@Autowired
	private ContractEntityMappingService service;

	@PostMapping("/saveOrUpdateContractEntity")
	public ResponseEntity saveOrUpdateMappings(
			@Valid @RequestBody List<ContractEntityMappingRequest> requests,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("SaveOrUpdate Mapping API hit for {} mappings by user: {}",
				requests.size(), userId);

		Integer user = Integer.parseInt(userId);

		// set createdBy & modifiedBy for each request
		for (ContractEntityMappingRequest req : requests) {
			req.setCreatedBy(user);
			req.setModifiedBy(user);
		}

		return service.saveOrUpdateMappings(requests);
	}

	@GetMapping("/getContractEntity/{id}")
	public ResponseEntity getMapping(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("getContractEntity Mapping API hit by user: {}", userId);
		return service.getMappingById(id);
	}

	@GetMapping("/getAllContractEntity")
	public ResponseEntity getAllMappings(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("getAllMappings API hit with page {} and size {}", page,
				size);

		return service.getAllMappings(page, size);
	}

	@GetMapping("/getAllContractEntityMappings")
	public ResponseEntity getAllContractEntityMappings(@RequestParam String contractType, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		logger.info("getAllContractEntityMappings API hit with contract type {}", contractType);

		return service.getAllContractEntityMappings(contractType,page,size);
	}

	@DeleteMapping("/deactivateContractEntity/{id}")
	public ResponseEntity deactivateMapping(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("deactivateMapping API hit by user: {}", userId);
		return service.deactivateMapping(id);
	}

	@GetMapping("/getMappingByContractId/{id}")
	public ResponseEntity getMappingByContractId(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("getMappingByContractId API hit by user: {}", userId);
		return service.getMappingByContractId(id);
	}

	@DeleteMapping("/deactivateBulkContractEntity/{ids}")
	public ResponseEntity deactivateBulkContractEntity(
	        @PathVariable List<Integer> ids,
	        @RequestHeader("X-User-Id") String userId) {

	    logger.info("deactivateBulkContractEntity API hit by user: {}", userId);
	    return service.deactivateBulkContractEntity(ids);
	}

}