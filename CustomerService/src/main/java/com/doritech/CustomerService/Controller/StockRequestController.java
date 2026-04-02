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
import com.doritech.CustomerService.Request.StockRequestRequest;
import com.doritech.CustomerService.Service.StockRequestService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/customer/api/stock/request")
public class StockRequestController {

	@Autowired
	private StockRequestService stockRequestService;

	@PostMapping("/saveStockRequest")
	public ResponseEntity saveStockRequest(
			@RequestBody @Valid StockRequestRequest request,
			@RequestHeader("X-User-Id") String userId) {

		request.setCreatedBy(Integer.parseInt(userId));
		return stockRequestService.saveStockRequest(request);
	}

	@GetMapping("/getAllStockRequest")
	public ResponseEntity getAllStockRequest(
			@RequestHeader("X-User-Id") String userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return stockRequestService.getAllStockRequest(page, size);
	}

	@GetMapping("/getStockRequestById")
	public ResponseEntity getStockRequestById(
			@RequestHeader("X-User-Id") String userId,
			@RequestParam Integer id) {

		return stockRequestService.getStockRequestById(id);
	}

	@PutMapping("/updateStockRequestById")
	public ResponseEntity updateStockRequestById(
			@RequestHeader("X-User-Id") String userId, @RequestParam Integer id,
			@RequestBody @Valid StockRequestRequest request) {

		request.setModifiedBy(Integer.parseInt(userId));
		return stockRequestService.updateStockRequest(id, request);
	}

	@DeleteMapping("/deleteMultipleStockRequest")
	public ResponseEntity deleteMultipleStockRequest(
			@RequestHeader("X-User-Id") String userId,
			@RequestParam List<Integer> ids) {

		return stockRequestService.deleteMultipleStockRequest(ids);
	}

	@PutMapping("/approveStockRequest")
	public ResponseEntity approveStockRequest(
			@RequestHeader("X-User-Id") String userId, @RequestParam Integer id,
			@RequestBody StockRequestRequest request) {

		request.setModifiedBy(Integer.parseInt(userId));
		return stockRequestService.approveStockRequest(id, request);
	}

	@GetMapping("/filterStockRequests")
	public ResponseEntity filterStockRequests(
			@RequestHeader("X-User-Id") String userId,
			@RequestParam(required = false) Integer sourceSiteId,
			@RequestParam(required = false) Integer requestedSiteId,
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return stockRequestService.filterStockRequests(sourceSiteId,
				requestedSiteId, status, page, size);
	}

	@GetMapping("/getAllApprovedStockRequest")
	public ResponseEntity getAllApprovedStockRequest(
			@RequestHeader("X-User-Id") String userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return stockRequestService.getAllApprovedStockRequests(page, size);
	}

	@GetMapping("/getApprovedStockWithoutChallan")
	public ResponseEntity getApprovedStockWithoutChallan(
			@RequestHeader("X-User-Id") String userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return stockRequestService.getApprovedStockWithoutChallan(page, size);
	}
}