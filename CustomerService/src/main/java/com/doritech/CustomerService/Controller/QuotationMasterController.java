package com.doritech.CustomerService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.doritech.CustomerService.Request.QuotationMasterRequest;
import com.doritech.CustomerService.Service.QuotationMasterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/quotation")
public class QuotationMasterController {

	private static final Logger logger = LoggerFactory
			.getLogger(QuotationMasterController.class);

	@Autowired
	private QuotationMasterService service;

	@PostMapping("/createQuotation")
	public ResponseEntity createQuotation(
			@RequestHeader("X-User-Id") String userId,
			@Valid @RequestBody QuotationMasterRequest request) {

		int userIdInt = Integer.parseInt(userId);
		request.setCreatedBy(userIdInt);

		logger.info("Create quotation API called by user {}", userIdInt);

		return service.createQuotation(request);
	}

	@GetMapping("/getQuotationById/{id}")
	public ResponseEntity getQuotationById(@PathVariable Integer id) {

		logger.info("Get quotation by id API called {}", id);

		return service.getQuotationById(id);
	}

	@GetMapping("/getAllQuotation")
	public ResponseEntity getAllQuotation(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("Get all quotation API called page {} size {}", page, size);

		return service.getAllQuotation(page, size);
	}

	@PutMapping("/updateQuotation/{id}")
	public ResponseEntity updateQuotation(
			@RequestHeader("X-User-Id") String userId, @PathVariable Integer id,
			@Valid @RequestBody QuotationMasterRequest request) {

		int userIdInt = Integer.parseInt(userId);
		request.setModifiedBy(userIdInt);

		logger.info("Update quotation API called for id {} by user {}", id,
				userIdInt);

		return service.updateQuotation(id, request);
	}

	@DeleteMapping("/deleteQuotationBulk")
	public ResponseEntity deleteQuotationBulk(@RequestBody List<Integer> ids,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Bulk delete quotation API called with ids {}", ids);

		return service.deleteQuotationBulk(ids);
	}
	@GetMapping("/filter")
	public ResponseEntity getQuotations(
			@RequestParam(required = false) String quotationCode,
			@RequestParam(required = false) Integer customerId,
			@RequestParam(required = false) Integer contractId,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String isActive,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return service.getQuotations(quotationCode, customerId, contractId,
				status, isActive, page, size);
	}
	
	@GetMapping("/getAllQuotationIdAndCode")
	public ResponseEntity getAllQuotationIdAndCode() {
		logger.info("Get All Quotation Id And Code");
		return service.getAllQuotationIdAndCode();
	}
}