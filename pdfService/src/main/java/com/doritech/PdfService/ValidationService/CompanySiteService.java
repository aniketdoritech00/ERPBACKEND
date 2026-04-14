package com.doritech.PdfService.ValidationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doritech.PdfService.FeignClient.CustomerFeignClient;
import com.doritech.PdfService.FeignClient.EmployeeAssignmentFeignClient;
import com.doritech.PdfService.Response.CompanySiteResponse;
import com.doritech.PdfService.Response.ResponseEntity;

@Service
public class CompanySiteService {

	private static final Logger log = LoggerFactory.getLogger(CompanySiteService.class);

	@Autowired
	private CustomerFeignClient feignClient;

	@Autowired
	private EmployeeAssignmentFeignClient customerFeignClient;

	

	public ResponseEntity fetchCustomer(Integer assignmentId) {
		log.info("Calling Feign client for assignmentId={}", assignmentId);
		if (assignmentId == null) {
			throw new IllegalArgumentException("Assignment ID must not be null");
		}
		try {
			ResponseEntity response = customerFeignClient.getCustomerDetails(assignmentId);
			if (response == null) {
				throw new RuntimeException("Customer service returned null response");
			}
			if (response.getStatusCode() == null || !response.getStatusCode().toString().equals("200")) {
				return new ResponseEntity(response.getMessage(), response.getStatusCode(), null);
			}
			if (response.getPayload() == null) {
				throw new RuntimeException("Customer payload is empty");
			}
			return response;
		} catch (IllegalArgumentException ex) {
			log.error("Invalid input for assignmentId={}", assignmentId, ex);
			throw ex;
		} catch (Exception ex) {
			log.error("Exception while calling customer service for assignmentId={}", assignmentId, ex);
			throw new RuntimeException("Failed to fetch customer via Feign", ex);
		}
	}

	public ResponseEntity getCustomerDetailsByCustomerId(Integer customerId) {
		log.info("Calling Feign client for customerId={}", customerId);
		ResponseEntity finalResponse = new ResponseEntity();
		if (customerId == null) {
			log.error("Customer ID is null");
			finalResponse.setStatusCode(400);
			finalResponse.setMessage("Customer ID must not be null");
			finalResponse.setPayload(null);
			return finalResponse;
		}
		try {
			ResponseEntity feignResponse = feignClient.getCustomerDetailsByCustomerId(customerId);
			if (feignResponse == null) {
				log.error("Received null response from Customer Service");
				finalResponse.setStatusCode(500);
				finalResponse.setMessage("Customer service returned null response");
				finalResponse.setPayload(null);
				return finalResponse;
			}
			if (feignResponse.getStatusCode() == null || !Integer.valueOf(200).equals(feignResponse.getStatusCode())) {
				log.warn("Customer service returned error: {}", feignResponse.getMessage());
				finalResponse.setStatusCode(feignResponse.getStatusCode());
				finalResponse.setMessage(feignResponse.getMessage());
				finalResponse.setPayload(null);
				return finalResponse;
			}
			if (feignResponse.getPayload() == null) {
				log.error("Customer payload is empty");
				finalResponse.setStatusCode(404);
				finalResponse.setMessage("Customer data not found");
				finalResponse.setPayload(null);
				return finalResponse;
			}
			log.info("Customer details fetched successfully for customerId={}", customerId);
			return feignResponse;
		} catch (Exception ex) {
			log.error("Exception while calling customer service for customerId={}", customerId, ex);
			finalResponse.setStatusCode(500);
			finalResponse.setMessage("Failed to fetch customer details from external service");
			finalResponse.setPayload(null);
			return finalResponse;
		}
	}

	public void updateStatusAfterPdfGenerate(Integer scheduleVisitId) {
		log.info("Calling updateStatusAfterPdfGenerate for scheduleVisitId={}", scheduleVisitId);
		if (scheduleVisitId == null) {
			log.warn("scheduleVisitId is null, skipping status update");
			return;
		}
		try {
			ResponseEntity response = customerFeignClient.updateStatusAfterPdfGenerate(scheduleVisitId);
			if (response == null) {
				log.error("Received null response from updateStatusAfterPdfGenerate API for scheduleVisitId={}", scheduleVisitId);
				return;
			}
			if (response.getStatusCode() == null || !Integer.valueOf(200).equals(response.getStatusCode())) {
				log.warn("updateStatusAfterPdfGenerate returned non-200 for scheduleVisitId={}. Status: {}, Message: {}",
						scheduleVisitId, response.getStatusCode(), response.getMessage());
				return;
			}
			log.info("Status updated successfully after PDF generation for scheduleVisitId={}", scheduleVisitId);
		} catch (feign.FeignException fe) {
			log.error("Feign error calling updateStatusAfterPdfGenerate for scheduleVisitId={}: {}",
					scheduleVisitId, fe.getMessage());
		} catch (Exception ex) {
			log.error("Exception while calling updateStatusAfterPdfGenerate for scheduleVisitId={}: {}",
					scheduleVisitId, ex.getMessage(), ex);
		}
	}
}