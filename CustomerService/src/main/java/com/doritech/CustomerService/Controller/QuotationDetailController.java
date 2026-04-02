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

	private static final Logger logger = LoggerFactory
			.getLogger(QuotationDetailController.class);

	private final QuotationDetailService service;

	public QuotationDetailController(QuotationDetailService service) {
		this.service = service;
	}

	@PostMapping("/saveAndUpdateQuotationDetails/{quotationId}")
	public ResponseEntity saveAndUpdateQuotationDetails(
			@RequestHeader("X-User-Id") String userId,
			@PathVariable Integer quotationId,
			@Valid @RequestBody List<QuotationDetailRequest> requests) {

		int userIdInt = Integer.parseInt(userId);
		requests.forEach(r -> {
			r.setCreatedBy(userIdInt);
			r.setModifiedBy(userIdInt);
		});

		return service.saveAndUpdateQuotationDetails(quotationId, requests);
	}

	@DeleteMapping("/deleteQuotationDetails")
	public ResponseEntity deleteMultiple(@RequestBody List<Integer> ids) {
		logger.info("API called: deleteQuotationDetails for ids {}", ids);
		try {
			return service.deleteQuotationDetails(ids);
		} catch (Exception e) {
			logger.error("Error in deleteQuotationDetails API for ids {}", ids,
					e);
			throw e;
		}
	}

	@GetMapping("/getQuotationDetailById/{id}")
	public ResponseEntity getById(@PathVariable Integer id) {
		logger.info("API called: getQuotationDetailById for id {}", id);
		try {
			return service.getQuotationDetailById(id);
		} catch (Exception e) {
			logger.error("Error in getQuotationDetailById API for id {}", id,
					e);
			throw e;
		}
	}

	@GetMapping("/getAllQuotationDetails")
	public ResponseEntity getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		logger.info("API called: getAllQuotationDetails - page: {}, size: {}",
				page, size);
		try {
			return service.getAllQuotationDetails(page, size);
		} catch (Exception e) {
			logger.error("Error in getAllQuotationDetails API", e);
			throw e;
		}
	}

	@GetMapping("/getByQuotationId/{quotationId}")
	public ResponseEntity getByQuotationId(@PathVariable Integer quotationId) {
		logger.info("API called: getByQuotationId for quotationId {}",
				quotationId);
		try {
			return service.getByQuotationId(quotationId);
		} catch (Exception e) {
			logger.error("Error in getByQuotationId API for quotationId {}",
					quotationId, e);
			throw e;
		}
	}
}