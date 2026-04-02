package com.doritech.CustomerService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.DispatchRequest;
import com.doritech.CustomerService.Service.DispatchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/dispatch/details")
public class DispatchController {

	@Autowired
	private DispatchService dispatchService;

	@PostMapping("/saveDispatchDetails")
	public ResponseEntity saveDispatchDetails(
			@RequestBody @Valid DispatchRequest request,
			@RequestHeader("X-User-Id") String userId) {

		request.setCreatedBy(Integer.parseInt(userId));
		return dispatchService.saveDispatchDeatils(request);
	}

	@GetMapping("/getAllDispatchDetails")
	public ResponseEntity getAllDispatchDetails(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return dispatchService.getAllDispatchDetails(page, size);
	}

	@GetMapping("/getDispatchDetailsById")
	public ResponseEntity getDispatchDetailsById(@RequestParam Integer id) {
		return dispatchService.getDispathchDetailsById(id);
	}

	@PutMapping("/updateDispatchDetails")
	public ResponseEntity updateDispatchDetails(@RequestParam Integer id,
			@RequestHeader("X-User-Id") String userId,
			@RequestBody @Valid DispatchRequest request) {
		request.setModifiedBy(Integer.parseInt(userId));
		return dispatchService.updateDispatchDeatils(id, request);
	}

	@DeleteMapping("/deleteMultipleDispatchDetails")
	public ResponseEntity deleteMultipleDispatchDetails(
			@RequestParam List<Integer> ids,
			@RequestHeader("X-User-Id") String userId) {
		return dispatchService.deleteMultipleDispatchDetails(ids);
	}
}
