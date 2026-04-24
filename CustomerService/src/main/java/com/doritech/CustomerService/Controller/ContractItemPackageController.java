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
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Request.ContractItemPackageRequest;
import com.doritech.CustomerService.Service.ContractItemPackageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/contractItemPackage")
public class ContractItemPackageController {

	private static final Logger logger = LoggerFactory.getLogger(ContractItemPackageController.class);

	@Autowired
	private ContractItemPackageService service;

	@PostMapping("/saveOrUpdateItemPackage")
	public ResponseEntity saveOrUpdatePackage(
			@Valid @RequestBody List<ContractItemPackageRequest> requests,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("SaveOrUpdate ContractItemPackage API called for list");

		if (userId == null || userId.isBlank()) {
			throw new BadRequestException("X-User-Id header cannot be null or blank");
		}

		Integer user = Integer.parseInt(userId);

		requests.forEach(req -> {
			if (req != null) {
				req.setCreatedBy(user);
				req.setModifiedBy(user);
			}
		});

		return service.saveOrUpdatePackageList(requests);
	}

	@GetMapping("/getPackageById/{id}")
	public ResponseEntity getPackageById(
			@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Get ContractItemPackage by id API called {} by user {}", id, userId);

		return service.getPackageById(id);
	}

	@GetMapping("/getAllPackages")
	public ResponseEntity getAllPackages(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Get all ContractItemPackages API called with page {} and size {} by user {}", page, size, userId);

		return service.getAllPackages(page, size);
	}

	@DeleteMapping("/deletePackage/{id}")
	public ResponseEntity deletePackage(
			@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Delete ContractItemPackage API called for id {} by user {}", id, userId);

		return service.deletePackage(id);
	}

	@GetMapping("/getPackageByContractId/{contractId}")
	public ResponseEntity getPackageByContractId(
			@PathVariable Integer contractId,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Get packages by contractId API called {} by user {}", contractId, userId);

		return service.getPackageByContractId(contractId);
	}

	@DeleteMapping("/deleteBulkPackage/{ids}")
	public ResponseEntity deleteBulkPackage(
	        @PathVariable List<Integer> ids,
	        @RequestHeader("X-User-Id") String userId) {

	    logger.info(
	            "Delete Bulk ContractItemPackage API called for ids {} by user {}",
	            ids, userId);

	    return service.deleteBulkPackage(ids);
	}
}