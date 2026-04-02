package com.doritech.CustomerService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.AmcDetailFilterRequest;
import com.doritech.CustomerService.Request.AmcDetailRequest;
import com.doritech.CustomerService.Service.AmcDetailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/amc-details")
public class AmcDetailController {

	@Autowired
	private AmcDetailService service;

	@PostMapping("/createAmcDetail")
	public ResponseEntity createAmcDetail(
			@Valid @RequestBody AmcDetailRequest request,
			@RequestHeader("X-User-Id") String userId) {

		Integer userIdInt = Integer.parseInt(userId);
		request.setCreatedBy(userIdInt);

		return new ResponseEntity("AMC Detail created successfully",
				HttpStatus.CREATED.value(), service.create(request));
	}

	@PostMapping("/createMultipleAmcDetails")
	public ResponseEntity createMultipleAmcDetails(
			@Valid @RequestBody List<AmcDetailRequest> requests,
			@RequestHeader("X-User-Id") String userId) {

		Integer userIdInt = Integer.parseInt(userId);

		requests.forEach(req -> req.setCreatedBy(userIdInt));

		return new ResponseEntity("AMC Details created successfully",
				HttpStatus.CREATED.value(), service.createBulk(requests));
	}

	@PutMapping("/updateAmcDetail/{id}")
	public ResponseEntity updateAmcDetail(@PathVariable Integer id,
			@Valid @RequestBody AmcDetailRequest request,
			@RequestHeader("X-User-Id") String userId) {

		Integer userIdInt = Integer.parseInt(userId);
		request.setModifiedBy(userIdInt);

		return new ResponseEntity("AMC Detail updated successfully",
				HttpStatus.OK.value(), service.update(id, request));
	}

	// @PutMapping("/updateMultipleAmcDetails")
	// public ResponseEntity updateMultipleAmcDetails(
	// @Valid @RequestBody List<AmcDetailRequest> requests,
	// @RequestHeader("X-User-Id") String userId) {
	//
	// Integer userIdInt = Integer.parseInt(userId);
	//
	// // 🔥 Set modifiedBy for all
	// requests.forEach(req -> req.setModifiedBy(userIdInt));
	//
	// return new ResponseEntity("AMC Details updated successfully",
	// HttpStatus.OK.value(), service.updateBulk(requests));
	// }

	@GetMapping("/getAmcDetailById/{id}")
	public ResponseEntity getAmcDetailById(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {

		return new ResponseEntity("AMC Detail fetched successfully",
				HttpStatus.OK.value(), service.getById(id));
	}

	@GetMapping("/getAmcDetailsByAmcId/{amcId}")
	public ResponseEntity getAmcDetailsByAmcId(@PathVariable Integer amcId,
			@RequestHeader("X-User-Id") String userId) {

		return new ResponseEntity("AMC Details fetched successfully",
				HttpStatus.OK.value(), service.getByAmcId(amcId));
	}

	@DeleteMapping("/deleteAmcDetail/{id}")
	public ResponseEntity deleteAmcDetail(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId) {

		service.deleteById(id);

		return new ResponseEntity("AMC Detail deleted successfully",
				HttpStatus.OK.value(), null);
	}

	@PostMapping("/filterAmcDetails")
	public ResponseEntity filterAmcDetails(
			@RequestBody AmcDetailFilterRequest request,
			@RequestHeader("X-User-Id") String userId) {

		return new ResponseEntity("AMC Details fetched successfully",
				HttpStatus.OK.value(), service.filter(request));
	}
}