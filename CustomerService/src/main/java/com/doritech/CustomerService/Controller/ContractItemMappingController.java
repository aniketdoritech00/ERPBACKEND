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
import com.doritech.CustomerService.Request.ContractItemMappingRequest;
import com.doritech.CustomerService.Service.ContractItemMappingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/contract-item-mapping")
public class ContractItemMappingController {

	private static final Logger logger = LoggerFactory.getLogger(ContractItemMappingController.class);

	@Autowired
	private ContractItemMappingService service;

	// @PostMapping("/saveOrUpdateContractItem")
	// public ResponseEntity saveOrUpdate(
	// 		@Valid @RequestBody List<ContractItemMappingRequest> requests,
	// 		@RequestHeader("X-User-Id") String userId) {

	// 	logger.info("SaveOrUpdate ContractItemMapping API hit for {} requests by user {}", requests.size(), userId);

	// 	Integer user = Integer.parseInt(userId);

	// 	for (ContractItemMappingRequest req : requests) {
	// 		req.setCreatedBy(user);
	// 		req.setModifiedBy(user);
	// 	}

	// 	return service.saveOrUpdateItemMapping(requests);
	// }

	@PostMapping("/saveOrUpdateContractItem")
	public ResponseEntity saveOrUpdate(
			@Valid @RequestBody List<ContractItemMappingRequest> requests,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("SaveOrUpdate ContractItemMapping API hit for {} requests by user {}", requests.size(), userId);

		Integer user = Integer.parseInt(userId);

		for (ContractItemMappingRequest req : requests) {
			req.setCreatedBy(user);
			req.setModifiedBy(user);
		}

		return service.saveOrUpdateItemMapping(requests);
	}

	@GetMapping("/getContractItemById/{id}")
	public ResponseEntity getById(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get ContractItemMapping By Id API hit with id {} by user {}", id, userId);
		return service.getItemMappingById(id);
	}

	@GetMapping("/getAllContractItem")
	public ResponseEntity getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		logger.info("Get All ContractItemMapping API hit with page {} and size {}", page, size);
		return service.getAllItemMappings(page, size);
	}

	@DeleteMapping("/deleteContractItem/{id}")
	public ResponseEntity delete(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Delete ContractItemMapping API hit with id {} by user {}", id, userId);
		return service.deleteItemMapping(id);
	}

	@GetMapping("/getContractItemByContractId/{id}")
	public ResponseEntity getContractItemByContractId(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get ContractItemMapping By ContractId API hit with id {} by user {}", id, userId);
		return service.getContractItemByContractId(id);
	}

	@GetMapping("/getItemByContractId/{id}")
	public ResponseEntity getItemByContractId(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get ContractItemMapping By Contract Id API hit with id {} by user {}", id, userId);
		return service.getItemByContractId(id);
	}

	@GetMapping("/getPackageMapedItem")
	public ResponseEntity getPackageItemsByContractAndItem(
			@RequestParam Integer contractId, @RequestParam Integer itemId) {
		return service.getPackageItemsByContractAndItem(contractId, itemId);
	}

	@DeleteMapping("/contract-item-mappings")
	public ResponseEntity deactivateContractItemMappings(
			@RequestBody List<Integer> mappingIds,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Deactivate ContractItemMappings API invoked by user {} for ids {}", userId, mappingIds);
		return service.deactivateContractItemMappings(mappingIds, userId);
	}
}