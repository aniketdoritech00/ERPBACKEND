package com.doritech.ItemService.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.ItemService.Entity.ResponseEntity;
import com.doritech.ItemService.Exception.ApiResponse;
import com.doritech.ItemService.Request.GodownTransferRequestDTO;
import com.doritech.ItemService.Service.GodownTransactionService;
import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item/api/godown-transaction")
public class GodownTransactionController {

	@Autowired
	private GodownTransactionService service;

	@PostMapping("/transferStock")
	public ResponseEntity transferStock(@RequestBody @Validated(OnCreate.class) GodownTransferRequestDTO dto,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {
		dto.setCreatedBy(Integer.parseInt(userId));
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Transfer Created");
		response.setData(service.transferStock(dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Transfer Created", 200, response);
	}

	@GetMapping("/getAllTransactionsByBatchId/{batchId}")
	public ResponseEntity getTransferByBatchId(@PathVariable Integer batchId,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Transfer fetched successfully");
		response.setData(service.getAllTransactionsByBatchId(batchId));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Transfer fetched successfully", 200, response);

	}

	@PutMapping("/updateTransactionByBatchId/{batchId}")
	public ResponseEntity updateTransactionById(@PathVariable Integer batchId,
			@Validated(OnUpdate.class) @RequestBody GodownTransferRequestDTO dto,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		// Set modifiedBy from X-User-Id header
		dto.setModifiedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Transaction update successfully");
		response.setData(service.updateTransactionById(batchId, dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Transaction update successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllTransactions")
	public ResponseEntity getAllTransactions(@RequestParam int page, @RequestParam int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("All Transaction fetched successfully");
		response.setData(service.getAllTransactions(page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("All Transaction fetched successfully", 200, response);

	}

	@PostMapping("/approveTransfer/{batchId}")
	public ResponseEntity approveTransfer(@PathVariable Integer batchId, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Transfer Approved");
		response.setData(service.approveTransfer(batchId));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Transfer Approved", 200, response);

	}

	@GetMapping("/filterTransactions")
	public ResponseEntity filterTransactions(

			@RequestParam(required = false) Integer batchId,
			@RequestParam(required = false) LocalDateTime transactionDate,
			@RequestParam(required = false) LocalDateTime fromDate,
			@RequestParam(required = false) LocalDateTime toDate,
			@RequestParam(required = false) String transactionType, @RequestParam(required = false) String status,
			@RequestParam(required = false) Integer godownId, @RequestParam(required = false) Integer itemId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Filtered transactions fetched successfully");

		response.setData(service.filterTransactions(batchId, transactionDate, fromDate, toDate, transactionType, status,
				godownId, itemId, page, size));

		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Filtered transactions fetched successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllTransactionsBatchWise")
	public ResponseEntity getAllTransactionsBatchWise(
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();

		response.setSuccess(true);
		response.setMessage("Transactions fetched successfully");
		response.setData(service.getAllTransactionsBatchWise());
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Transactions fetched successfully", HttpStatus.OK.value(), response);
	}
}