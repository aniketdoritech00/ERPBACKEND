package com.doritech.CustomerService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.ContractDocumentRequest;
import com.doritech.CustomerService.Service.ContractDocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/contract-document")
public class ContractDocumentController {

	private static final Logger logger = LoggerFactory
			.getLogger(ContractDocumentController.class);

	@Autowired
	private ContractDocumentService service;

	@PostMapping("/saveOrUpdateDocument")
	public ResponseEntity saveOrUpdateDocument(
			@ModelAttribute @Valid ContractDocumentRequest request,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("SaveOrUpdate Document Controller called");

		Integer user = Integer.parseInt(userId);

		request.setCreatedBy(user);
		request.setModifiedBy(user);

		return service.saveOrUpdateDocument(request);
	}

	/*
	 * @PutMapping("/updateDocument") public ResponseEntity
	 * updateDocument(@ModelAttribute @Valid ContractDocumentRequest request,
	 * 
	 * @RequestHeader("X-User-Id") String userId) {
	 * logger.info("Update Document Controller called"); Integer user =
	 * Integer.parseInt(userId); request.setModifiedBy(user); return
	 * service.updateDocument(request); }
	 */

	@GetMapping("/getDocument/{documentId}")
	public ResponseEntity getDocument(@PathVariable Integer documentId,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Get document API called for id: {} by user: {}",
				documentId, userId);

		if (documentId <= 0) {
			logger.error("Invalid document id: {}", documentId);
			return new ResponseEntity("Invalid Document Id", 400, null);
		}

		return service.getDocument(documentId);
	}

	@GetMapping("/getDocumentByContractId")
	public ResponseEntity getDocumentByContractId(
			@RequestParam Integer contractId) {
		logger.info(
				"Get Document By ContractId Controller called for contractId: {}",
				contractId);
		return service.getDocumentByContractId(contractId);
	}

	@DeleteMapping("/deleteDocument/{documentId}")
	public ResponseEntity deleteDocument(@PathVariable Integer documentId,
			@RequestHeader("X-User-Id") String userId) {

		logger.info("Delete document API called for id: {} by user: {}",
				documentId, userId);

		if (documentId <= 0) {
			logger.error("Invalid document id: {}", documentId);
			return new ResponseEntity("Invalid Document Id", 400, null);
		}

		return service.deleteDocument(documentId);
	}

	@GetMapping("/getAllDocuments")
	public ResponseEntity getAllDocuments() {
		logger.info("Get all documents API called");
		return service.getAllDocuments();
	}

	@DeleteMapping("/deleteBulkDocument")
	public ResponseEntity deleteBulkDocument(
			@RequestBody List<Integer> documentIds,
			@RequestHeader("X-User-Id") String userId) {
		logger.info("Bulk delete document API called for ids: {} by user: {}", documentIds, userId);
		if (documentIds == null || documentIds.isEmpty()) {
			logger.error("Invalid document ids: {}", documentIds);
			return new ResponseEntity("Document IDs cannot be null or empty", 400, null);
		}
		return service.deleteBulkDocument(documentIds);
	}
}