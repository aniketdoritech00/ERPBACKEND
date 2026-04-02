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
import com.doritech.CustomerService.Request.StockRequestDetailRequest;
import com.doritech.CustomerService.Service.StockRequestDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/stock/request/details")
public class StockRequestDetialsController {

	@Autowired
	private StockRequestDetailsService stockRequestDetailsService;

	@PostMapping("/saveStockRequestDetails")
	public ResponseEntity saveStockRequestDetails(
			@RequestBody @Valid StockRequestDetailRequest request,
			@RequestHeader("X-User-Id") String userId) {

		return stockRequestDetailsService.saveStockRequestDetails(request);
	}

	@GetMapping("/getAllStockRequestDetails")
	public ResponseEntity getAllStockRequestDetails(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return stockRequestDetailsService.getAllStockRequestDetails(page, size);
	}

	@GetMapping("/getStockRequestDetailById")
	public ResponseEntity getStockRequestDetailById(@RequestParam Integer id) {
		return stockRequestDetailsService.getStockRequestDetailsById(id);
	}

	@PutMapping("/updateStockRequestDetailsById")
	public ResponseEntity updateStockRequestDetailById(@RequestParam Integer id,
			@RequestBody @Valid StockRequestDetailRequest request,
			@RequestHeader("X-User-Id") String userId) {
		return stockRequestDetailsService.updateStockRequestDetails(id,
				request);
	}

	@DeleteMapping("/deleteStockRequestEntity")
	public ResponseEntity deleteStockRequestEntity(
			@RequestParam List<Integer> ids) {
		return stockRequestDetailsService.deleteMultipleStockDetails(ids);
	}

}
