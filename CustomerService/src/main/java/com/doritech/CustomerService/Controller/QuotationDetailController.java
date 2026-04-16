package com.doritech.CustomerService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.doritech.CustomerService.Request.QuotationDetailRequest;
import com.doritech.CustomerService.Service.QuotationDetailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/quotation-detail")
public class QuotationDetailController {

	private static final Logger logger = LoggerFactory.getLogger(QuotationDetailController.class);

	private final QuotationDetailService service;

	public QuotationDetailController(QuotationDetailService service) {
		this.service = service;
	}

	@PostMapping("/saveAndUpdateQuotationDetails/{quotationId}")
	public ResponseEntity saveAndUpdateQuotationDetails(@RequestHeader("X-User-Id") String userId,
			@PathVariable Integer quotationId, @Valid @RequestBody List<QuotationDetailRequest> requests) {

		logger.info("API called: saveAndUpdateQuotationDetails for quotationId {}", quotationId);

		ResponseEntity response = new ResponseEntity();

		if (userId == null || userId.isBlank()) {
			logger.error("X-User-Id header is missing or blank");
			response.setMessage("X-User-Id header is required");
			response.setStatusCode(400);
			response.setPayload(null);
			return response;
		}

		int userIdInt;
		try {
			userIdInt = Integer.parseInt(userId.trim());
		} catch (NumberFormatException e) {
			logger.error("Invalid X-User-Id header value: {}", userId);
			response.setMessage("X-User-Id header must be a valid integer, received: " + userId);
			response.setStatusCode(400);
			response.setPayload(null);
			return response;
		}

		if (requests == null || requests.isEmpty()) {
			logger.error("Request body is null or empty for quotationId {}", quotationId);
			response.setMessage("Request body cannot be null or empty");
			response.setStatusCode(400);
			response.setPayload(null);
			return response;
		}

		for (int i = 0; i < requests.size(); i++) {
			QuotationDetailRequest r = requests.get(i);
			if (r == null) {
				logger.warn("Request at index {} is null, skipping userId assignment", i);
				continue;
			}
			r.setCreatedBy(userIdInt);
			r.setModifiedBy(userIdInt);
		}

		return service.saveAndUpdateQuotationDetails(quotationId, requests);
	}

	@DeleteMapping("/deleteQuotationDetails")
	public ResponseEntity deleteMultiple(@RequestBody List<Integer> ids) {

		logger.info("API called: deleteQuotationDetails for ids {}", ids);

		ResponseEntity response = new ResponseEntity();

		if (ids == null || ids.isEmpty()) {
			logger.error("Delete request received with null or empty ids list");
			response.setMessage("ids list cannot be null or empty");
			response.setStatusCode(400);
			response.setPayload(null);
			return response;
		}

		return service.deleteQuotationDetails(ids);
	}

	@GetMapping("/getQuotationDetailById/{id}")
	public ResponseEntity getById(@PathVariable Integer id) {

		logger.info("API called: getQuotationDetailById for id {}", id);

		ResponseEntity response = new ResponseEntity();

		if (id == null) {
			logger.error("getQuotationDetailById called with null id");
			response.setMessage("id path variable cannot be null");
			response.setStatusCode(400);
			response.setPayload(null);
			return response;
		}

		return service.getQuotationDetailById(id);
	}

	@GetMapping("/getAllQuotationDetails")
	public ResponseEntity getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("API called: getAllQuotationDetails - page: {}, size: {}", page, size);

		ResponseEntity response = new ResponseEntity();

		if (page < 0) {
			logger.error("Invalid page number: {}", page);
			response.setMessage("page must be >= 0, received: " + page);
			response.setStatusCode(400);
			response.setPayload(null);
			return response;
		}

		if (size <= 0) {
			logger.error("Invalid page size: {}", size);
			response.setMessage("size must be > 0, received: " + size);
			response.setStatusCode(400);
			response.setPayload(null);
			return response;
		}

		return service.getAllQuotationDetails(page, size);
	}

	@GetMapping("/getByQuotationId/{quotationId}")
	public ResponseEntity getByQuotationId(@PathVariable Integer quotationId) {

		logger.info("API called: getByQuotationId for quotationId {}", quotationId);

		ResponseEntity response = new ResponseEntity();

		if (quotationId == null) {
			logger.error("getByQuotationId called with null quotationId");
			response.setMessage("quotationId path variable cannot be null");
			response.setStatusCode(400);
			response.setPayload(null);
			return response;
		}

		return service.getByQuotationId(quotationId);
	}
	
	
	
}