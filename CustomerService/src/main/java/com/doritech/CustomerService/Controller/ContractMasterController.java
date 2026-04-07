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
import com.doritech.CustomerService.Request.ContractMasterRequest;
import com.doritech.CustomerService.Service.ContractMasterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/contract")
public class ContractMasterController {

	private static final Logger logger = LoggerFactory
			.getLogger(ContractMasterController.class);
	@Autowired
	private ContractMasterService contractService;

	@PostMapping("/saveOrUpdateContract")
	public ResponseEntity createContract(
			@RequestParam(required = false) Integer id,
			@Valid @RequestBody ContractMasterRequest request,
			@RequestHeader("X-User-Id") String userId) {

		Integer user = Integer.parseInt(userId);

		logger.info("Create/Update Contract API hit by user {}", userId);

		request.setCreatedBy(user);
		request.setModifiedBy(user);

		return contractService.saveOrUpdateContract(id, request);
	}


	@GetMapping("/getContractById/{id}")
	public ResponseEntity getContractById(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get Contract By Id API hit {} by user {}", id, userId);
		return contractService.getContractById(id);
	}

	@GetMapping("/getAllContracts")
	public ResponseEntity getAllContracts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Get All Contracts API hit page {} size {} by user {}",
				page, size, userId);

		return contractService.getAllContracts(page, size);
	}

	@DeleteMapping("/deactivateContract")
	public ResponseEntity deactivateContract(@RequestParam List<Integer> ids,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Deactivate Contract API hit for ids: {} by user {}", ids,
				userId);

		return contractService.deactivateContracts(ids);
	}

	@GetMapping("/contracts")
	public ResponseEntity getContracts(
			@RequestParam(required = false) String contractNo,
			@RequestParam(required = false) Integer customerId,
			@RequestParam(required = false) String contractType,
			@RequestParam(required = false) String isActive,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Get Contracts API hit by user {}", userId);

		return contractService.filterContracts(contractNo, customerId,
				contractType, isActive);
	}

	@GetMapping("/getContractNamesAndIds")
	public ResponseEntity getContractNamesAndIds(
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get Contract Names And Ids API hit by user {}", userId);
		return contractService.getContractNamesAndIds();
	}

	@GetMapping("/getContractDetailsByType")
	public ResponseEntity getContractDetailsByType(@RequestParam String type,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get Contract Details By Type API hit {} by user {}", type,
				userId);
		return contractService.getContractDetailsByType(type);
	}

		@GetMapping("/getContractNamesAndIdsForFillter")
	public ResponseEntity getContractNamesAndIdsForFillter(
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Get Contract Names And Ids API hit by user {}", userId);
		return contractService.getContractNamesAndIdsForFillter();
	}

}