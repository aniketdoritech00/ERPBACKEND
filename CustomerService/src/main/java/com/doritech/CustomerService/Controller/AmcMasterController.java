package com.doritech.CustomerService.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.AmcMasterRequest;
import com.doritech.CustomerService.Request.OnCreate;
import com.doritech.CustomerService.Request.OnUpdate;
import com.doritech.CustomerService.Service.AmcMasterService;

@RestController
@RequestMapping("/customer/api/amc")
public class AmcMasterController {

	@Autowired
	private AmcMasterService service;

	@PostMapping("/createAMC")
	public ResponseEntity createAMC(
			@Validated(OnCreate.class) @RequestBody AmcMasterRequest request,
			@RequestHeader("X-User-Id") String userId) {

		Integer user = Integer.parseInt(userId);

		request.setCreatedBy(user);
		return new ResponseEntity("AMC created Successfully",
				HttpStatus.CREATED.value(), service.create(request));
	}

	@PutMapping("/updateAMC/{id}")
	public ResponseEntity updateAMC(@PathVariable Integer id,
			@Validated(OnUpdate.class) @RequestBody AmcMasterRequest request,
			@RequestHeader("X-User-Id") String userId) {

		Integer user = Integer.parseInt(userId);

		request.setModifiedBy(user);

		return new ResponseEntity("AMC updated Successfully",
				HttpStatus.OK.value(), service.update(id, request));
	}

	@DeleteMapping("/deleteMultiple")
	public ResponseEntity deleteMultiple(@RequestBody List<Integer> ids) {

		service.deleteMultipleByIds(ids);

		return new ResponseEntity("AMC deleted Successfully",
				HttpStatus.OK.value(), null);
	}

	@GetMapping("/getByAmcNumber/{amcNumber}")
	public ResponseEntity getByAmcNumber(@PathVariable String amcNumber) {

		return new ResponseEntity("Fetch Successful", HttpStatus.OK.value(),
				service.getByAmcNumber(amcNumber));
	}

	@GetMapping("/filter")
	public ResponseEntity filter(
			@RequestParam(required = false) String amcStatus,
			@RequestParam(required = false) Integer customerId,
			@RequestParam(required = false) String amcCategory,
			@RequestParam(required = false) String amcName,
			@RequestParam(required = false) LocalDate startDate,
			@RequestParam(required = false) LocalDate endDate,
			@RequestParam(required = false) BigDecimal minValue,
			@RequestParam(required = false) BigDecimal maxValue,
			@RequestParam(required = false) Integer createdBy,
			@RequestParam(required = false, defaultValue = "0") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer size) {

		return new ResponseEntity("Fetch Successful", HttpStatus.OK.value(),
				service.filter(amcStatus, customerId, amcCategory, amcName,
						startDate, endDate, minValue, maxValue, createdBy, page,
						size));
	}

	@GetMapping("/getAMCById/{id}")
	public ResponseEntity getAMCById(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {
		return new ResponseEntity("Fetch Data Sucessful", HttpStatus.OK.value(),
				service.getById(id));
	}
}