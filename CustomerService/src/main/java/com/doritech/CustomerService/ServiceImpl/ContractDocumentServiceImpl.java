package com.doritech.CustomerService.ServiceImpl;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ContractDocuments;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Repository.ContractDocumentRepository;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Request.ContractDocumentRequest;
import com.doritech.CustomerService.Response.ContractDocumentResponse;
import com.doritech.CustomerService.Service.ContractDocumentService;

import jakarta.transaction.Transactional;

@Service
public class ContractDocumentServiceImpl implements ContractDocumentService {

	private static final Logger logger = LoggerFactory.getLogger(ContractDocumentServiceImpl.class);

	@Autowired
	private ContractMasterRepository contractRepository;

	@Autowired
	private ContractDocumentRepository repository;

	private ContractDocumentResponse mapToResponse(ContractDocuments doc) {

		ContractDocumentResponse dto = new ContractDocumentResponse();

		dto.setDocumentId(doc.getDocumentId());
		dto.setContractId(doc.getContractId());
		dto.setDocumentType(doc.getDocumentType());
		dto.setDocumentName(doc.getDocumentName());
		dto.setIsActive(doc.getIsActive());
		dto.setUploadDate(doc.getUploadDate());
		dto.setCreatedBy(doc.getCreatedBy());
		dto.setCreatedOn(doc.getCreatedOn());
		dto.setModifiedBy(doc.getModifiedBy());
		dto.setModifiedOn(doc.getModifiedOn());

		if (doc.getContractId() != null) {
			contractRepository.findById(doc.getContractId()).ifPresentOrElse(contract -> {
				dto.setContractName(contract.getContractName());
				dto.setContractNo(contract.getContractNo());
				logger.debug("mapToResponse: Contract name '{}' mapped for contractId: {}", contract.getContractName(),
						doc.getContractId());
			}, () -> logger.warn("mapToResponse: No contract found for contractId: {}", doc.getContractId()));
		}

		return dto;
	}

	@Override
	public ResponseEntity saveOrUpdateDocument(ContractDocumentRequest request) {

		logger.info("saveOrUpdateDocument API called");

		try {

			if (request == null) {
				logger.error("saveOrUpdateDocument failed: Request body is null");
				return new ResponseEntity("Invalid request", 400, null);
			}

			if (request.getContractId() == null) {
				logger.error("saveOrUpdateDocument failed: ContractId is missing");
				return new ResponseEntity("Contract Id is required", 400, null);
			}

			if (!contractRepository.existsById(request.getContractId())) {
				logger.error("saveOrUpdateDocument failed: Contract not found for id: {}", request.getContractId());
				return new ResponseEntity("Contract does not exist", 404, null);
			}

			ContractDocuments entity;

			if (request.getDocumentId() != null) {

				Optional<ContractDocuments> optional = repository.findById(request.getDocumentId());

				if (!optional.isPresent()) {
					logger.error("saveOrUpdateDocument failed: Document not found with id: {}",
							request.getDocumentId());
					return new ResponseEntity("Document not found", 404, null);
				}

				entity = optional.get();
				entity.setModifiedBy(request.getModifiedBy());
				entity.setModifiedOn(new Date());

				logger.info("saveOrUpdateDocument: Updating existing document with id: {}", request.getDocumentId());

			} else {

				entity = new ContractDocuments();
				entity.setCreatedBy(request.getCreatedBy());
				entity.setCreatedOn(new Date());
				entity.setUploadDate(new Date());

				logger.info("saveOrUpdateDocument: Creating new document for contractId: {}", request.getContractId());
			}

			entity.setContractId(request.getContractId());
			entity.setDocumentType(request.getDocumentType());
			entity.setIsActive(request.getIsActive());

			if (request.getDocument() != null && !request.getDocument().isEmpty()) {
				entity.setDocumentName(request.getDocument().getOriginalFilename());
				entity.setDocument(request.getDocument().getBytes());
			}

			repository.save(entity);

			logger.info("saveOrUpdateDocument: Document saved/updated successfully with id: {}",
					entity.getDocumentId());

			return new ResponseEntity("Success", 200, mapToResponse(entity));

		} catch (IOException e) {
			logger.error("saveOrUpdateDocument: Error while reading document file: {}", e.getMessage(), e);
			return new ResponseEntity("Error reading document", 500, null);

		} catch (DataAccessException e) {
			logger.error("saveOrUpdateDocument: Database error while saving document: {}", e.getMessage(), e);
			return new ResponseEntity("Database error", 500, null);

		} catch (Exception e) {
			logger.error("saveOrUpdateDocument: Unexpected error: {}", e.getMessage(), e);
			return new ResponseEntity("Unexpected error occurred", 500, null);
		}
	}

	@Override
	@Transactional()
	public ResponseEntity getDocumentById(Integer documentId) {

		logger.info("getDocumentById API called with documentId: {}", documentId);

		try {

			if (documentId == null) {
				logger.error("getDocumentById failed: DocumentId is null");
				return new ResponseEntity("Document Id is required", 400, null);
			}

			Optional<ContractDocuments> optional = repository.findById(documentId);

			if (!optional.isPresent()) {
				logger.error("getDocumentById failed: Document not found for id: {}", documentId);
				return new ResponseEntity("Document not found", 404, null);
			}

			ContractDocuments entity = optional.get();

			ContractDocumentResponse response = new ContractDocumentResponse();

			response.setDocumentId(entity.getDocumentId());
			response.setContractId(entity.getContractId());
			response.setDocumentName(entity.getDocumentName());
			response.setDocumentType(entity.getDocumentType());
			response.setUploadDate(entity.getUploadDate());
			response.setIsActive(entity.getIsActive());
			response.setCreatedBy(entity.getCreatedBy());
			response.setCreatedOn(entity.getCreatedOn());
			response.setModifiedBy(entity.getModifiedBy());
			response.setModifiedOn(entity.getModifiedOn());

			if (entity.getDocument() != null) {
				String base64 = Base64.getEncoder().encodeToString(entity.getDocument());
				response.setDocument(base64);
			}

			logger.info("getDocumentById: Document fetched successfully for id: {}", documentId);

			return new ResponseEntity("Success", 200, response);

		} catch (DataAccessException e) {
			logger.error("getDocumentById: Database error: {}", e.getMessage(), e);
			return new ResponseEntity("Database error", 500, null);

		} catch (Exception e) {
			logger.error("getDocumentById: Unexpected error: {}", e.getMessage(), e);
			return new ResponseEntity("Unexpected error occurred", 500, null);
		}
	}

	@Override
	public ResponseEntity getAllDocuments(int page, int size) {

		logger.info("getAllDocuments API called with page: {}, size: {}", page, size);

		ResponseEntity response = new ResponseEntity();

		try {

			if (page < 0) {
				logger.error("getAllDocuments failed: Page index must not be less than zero");
				response.setMessage("Page index must not be less than zero");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (size <= 0) {
				logger.error("getAllDocuments failed: Page size must not be less than one");
				response.setMessage("Page size must not be less than one");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			Pageable pageable = PageRequest.of(page, size);
			Page<ContractDocuments> documentPage = repository.findAll(pageable);

			if (documentPage.isEmpty()) {
				logger.warn("getAllDocuments: No documents found");
				response.setMessage("No documents found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			List<ContractDocumentResponse> responseList = documentPage.getContent().stream().map(this::mapToResponse)
					.toList();

			logger.info("getAllDocuments: Fetched {} records on page {}/{}", responseList.size(), page + 1,
					documentPage.getTotalPages());

			response.setMessage("Documents fetched successfully");
			response.setStatusCode(200);
			response.setPayload(responseList);

		} catch (DataAccessException e) {
			logger.error("getAllDocuments: Database error: {}", e.getMessage(), e);
			response.setMessage("Database error while fetching documents");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("getAllDocuments: Unexpected error: {}", e.getMessage(), e);
			response.setMessage("Unable to fetch documents");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity updateDocument(ContractDocumentRequest request) {

		logger.info("updateDocument API called");

		ResponseEntity response = new ResponseEntity();

		try {

			if (request == null) {
				logger.error("updateDocument failed: Request body is null");
				response.setMessage("Invalid request");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (request.getDocumentId() == null) {
				logger.error("updateDocument failed: DocumentId is required");
				response.setMessage("DocumentId is required");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			Optional<ContractDocuments> optional = repository.findById(request.getDocumentId());

			if (!optional.isPresent()) {
				logger.error("updateDocument failed: Document not found with id: {}", request.getDocumentId());
				response.setMessage("Document not found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			ContractDocuments entity = optional.get();

			if (request.getContractId() != null) {

				if (!contractRepository.existsById(request.getContractId())) {
					logger.error("updateDocument failed: Contract not found with id: {}", request.getContractId());
					response.setMessage("Contract does not exist");
					response.setStatusCode(404);
					response.setPayload(null);
					return response;
				}

				entity.setContractId(request.getContractId());
			}

			entity.setDocumentType(request.getDocumentType());
			entity.setIsActive(request.getIsActive());
			entity.setModifiedBy(request.getModifiedBy());
			entity.setModifiedOn(new Date());

			if (request.getDocument() != null && !request.getDocument().isEmpty()) {
				entity.setDocumentName(request.getDocument().getOriginalFilename());
				entity.setDocument(request.getDocument().getBytes());
			}

			repository.save(entity);

			logger.info("updateDocument: Document updated successfully for id: {}", request.getDocumentId());

			response.setMessage("Document updated successfully");
			response.setStatusCode(200);
			response.setPayload(mapToResponse(entity));

		} catch (IOException e) {
			logger.error("updateDocument: Error while reading document file: {}", e.getMessage(), e);
			response.setMessage("Error reading updated document");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error("updateDocument: Database error: {}", e.getMessage(), e);
			response.setMessage("Database error while updating document");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("updateDocument: Unexpected error: {}", e.getMessage(), e);
			response.setMessage("Unexpected error occurred");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity deleteDocument(Integer documentId) {

		logger.info("deleteDocument API called for id: {}", documentId);

		ResponseEntity response = new ResponseEntity();

		try {

			if (documentId == null) {
				logger.error("deleteDocument failed: DocumentId is null");
				response.setMessage("DocumentId cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			Optional<ContractDocuments> optional = repository.findById(documentId);

			if (!optional.isPresent()) {
				logger.error("deleteDocument failed: Document not found for id: {}", documentId);
				response.setMessage("Document not found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			repository.delete(optional.get());

			logger.info("deleteDocument: Document deleted successfully for id: {}", documentId);

			response.setMessage("Document deleted successfully");
			response.setStatusCode(200);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error("deleteDocument: Database error for id {}: {}", documentId, e.getMessage(), e);
			response.setMessage("Database error while deleting document");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("deleteDocument: Unexpected error for id {}: {}", documentId, e.getMessage(), e);
			response.setMessage("Unable to delete document");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getDocumentByContractId(Integer contractId) {

		logger.info("getDocumentByContractId API called for contractId: {}", contractId);

		ResponseEntity response = new ResponseEntity();

		try {

			if (contractId == null) {
				logger.error("getDocumentByContractId failed: ContractId is null");
				response.setMessage("ContractId cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (!contractRepository.existsById(contractId)) {
				logger.error("getDocumentByContractId failed: Contract not found for id: {}", contractId);
				response.setMessage("Contract not found for id: " + contractId);
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			List<ContractDocuments> documents = repository.findByContractId(contractId);

			if (documents == null || documents.isEmpty()) {
				logger.warn("getDocumentByContractId: No documents found for contractId: {}", contractId);
				response.setMessage("No documents found for contractId: " + contractId);
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			List<ContractDocumentResponse> responseList = documents.stream().map(this::mapToResponse).toList();

			logger.info("getDocumentByContractId: {} documents fetched for contractId: {}", responseList.size(),
					contractId);

			response.setMessage("Success");
			response.setStatusCode(200);
			response.setPayload(responseList);

		} catch (DataAccessException ex) {
			logger.error("getDocumentByContractId: Database error for contractId {}: {}", contractId, ex.getMessage(),
					ex);
			response.setMessage("Database error while fetching documents");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception ex) {
			logger.error("getDocumentByContractId: Unexpected error for contractId {}: {}", contractId, ex.getMessage(),
					ex);
			response.setMessage("Something went wrong");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity deleteBulkDocument(List<Integer> documentIds) {

		logger.info("deleteBulkDocument API called for ids: {}", documentIds);

		ResponseEntity response = new ResponseEntity();

		try {

			if (documentIds == null || documentIds.isEmpty()) {
				logger.error("deleteBulkDocument failed: Document IDs are null or empty");
				response.setMessage("Document IDs cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			List<ContractDocuments> documents = repository.findAllById(documentIds);

			if (documents.isEmpty()) {
				logger.error("deleteBulkDocument failed: No documents found for ids: {}", documentIds);
				response.setMessage("No documents found for given IDs");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			repository.deleteAll(documents);

			logger.info("deleteBulkDocument: {} documents deleted successfully for ids: {}", documents.size(),
					documentIds);

			response.setMessage("Documents deleted successfully");
			response.setStatusCode(200);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error("deleteBulkDocument: Database error for ids {}: {}", documentIds, e.getMessage(), e);
			response.setMessage("Database error while deleting documents");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("deleteBulkDocument: Unexpected error for ids {}: {}", documentIds, e.getMessage(), e);
			response.setMessage("Unable to delete documents");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}
}