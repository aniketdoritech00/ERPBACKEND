package com.doritech.CustomerService.ServiceImpl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ContractDocuments;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Repository.ContractDocumentRepository;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Request.ContractDocumentRequest;
import com.doritech.CustomerService.Service.ContractDocumentService;

@Service
public class ContractDocumentServiceImpl implements ContractDocumentService {

	private static final Logger logger = LoggerFactory.getLogger(ContractDocumentServiceImpl.class);

	@Autowired
	private ContractMasterRepository contractRepository;

	@Autowired
	private ContractDocumentRepository repository;

	@Override
	public ResponseEntity saveOrUpdateDocument(ContractDocumentRequest request) {

		logger.info("SaveOrUpdate Document API called");

		try {

			if (request == null) {
				logger.error("Request body is null");
				return new ResponseEntity("Invalid request", 400, null);
			}

			if (request.getContractId() == null) {
				logger.error("ContractId is missing");
				return new ResponseEntity("Contract Id is required", 400, null);
			}

			boolean contractExists = contractRepository.existsById(request.getContractId());

			if (!contractExists) {
				logger.error("Contract not found for id: {}", request.getContractId());
				return new ResponseEntity("Contract does not exist", 404, null);
			}

			ContractDocuments entity;

			if (request.getDocumentId() != null) {

				Optional<ContractDocuments> optional = repository.findById(request.getDocumentId());

				if (!optional.isPresent()) {
					logger.error("Document not found with id: {}", request.getDocumentId());
					return new ResponseEntity("Document not found", 404, null);
				}

				entity = optional.get();
				entity.setModifiedBy(request.getModifiedBy());
				entity.setModifiedOn(new Date());

			} else {

				entity = new ContractDocuments();
				entity.setCreatedBy(request.getCreatedBy());
				entity.setCreatedOn(new Date());
				entity.setUploadDate(new Date());
			}

			entity.setContractId(request.getContractId());
			entity.setDocumentType(request.getDocumentType());
			entity.setIsActive(request.getIsActive());

			if (request.getDocument() != null && !request.getDocument().isEmpty()) {
				entity.setDocumentName(request.getDocument().getOriginalFilename());
				entity.setDocument(request.getDocument().getBytes());
			}

			repository.save(entity);

			logger.info("Document saved/updated successfully. Id: {}", entity.getDocumentId());

			return new ResponseEntity("Success", 200, entity);

		} catch (IOException e) {
			logger.error("Error while reading document file", e);
			return new ResponseEntity("Error reading document", 500, null);

		} catch (DataAccessException e) {
			logger.error("Database error while saving document", e);
			return new ResponseEntity("Database error", 500, null);

		} catch (Exception e) {
			logger.error("Unexpected error", e);
			return new ResponseEntity("Unexpected error occurred", 500, null);
		}
	}

	@Override
	public ResponseEntity getDocument(Integer documentId) {

		logger.info("Get Document API called for documentId: {}", documentId);

		ResponseEntity response = new ResponseEntity();

		try {

			Optional<ContractDocuments> optional = repository.findById(documentId);

			if (!optional.isPresent()) {
				logger.error("Document not found with id: {}", documentId);
				response.setMessage("Document not found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			response.setMessage("Document fetched successfully");
			response.setStatusCode(200);
			response.setPayload(optional.get());

		} catch (DataAccessException e) {
			logger.error("Database error while fetching document", e);
			response.setMessage("Database error while fetching document");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Error while fetching document", e);
			response.setMessage("Unable to fetch document");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllDocuments() {

		logger.info("Get All Documents API called");

		ResponseEntity response = new ResponseEntity();

		try {

			List<ContractDocuments> list = repository.findAll();

			if (list == null || list.isEmpty()) {
				logger.warn("No documents found");
				response.setMessage("No documents found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			logger.info("Total documents fetched: {}", list.size());

			response.setMessage("Documents fetched successfully");
			response.setStatusCode(200);
			response.setPayload(list);

		} catch (DataAccessException e) {
			logger.error("Database error while fetching all documents", e);
			response.setMessage("Database error while fetching documents");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Error while fetching all documents", e);
			response.setMessage("Unable to fetch documents");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity updateDocument(ContractDocumentRequest request) {

		logger.info("Update Document API called");

		ResponseEntity response = new ResponseEntity();

		try {

			if (request == null) {
				logger.error("Request body is null");
				response.setMessage("Invalid request");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (request.getDocumentId() == null) {
				logger.error("DocumentId is required for update");
				response.setMessage("DocumentId is required");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			Optional<ContractDocuments> optional = repository.findById(request.getDocumentId());

			if (!optional.isPresent()) {
				logger.error("Document not found with id: {}", request.getDocumentId());
				response.setMessage("Document not found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			ContractDocuments entity = optional.get();

			if (request.getContractId() != null) {

				boolean contractExists = contractRepository.existsById(request.getContractId());

				if (!contractExists) {
					logger.error("Contract not found with id: {}", request.getContractId());
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

			logger.info("Document updated successfully for id: {}", request.getDocumentId());

			response.setMessage("Document updated successfully");
			response.setStatusCode(200);
			response.setPayload(entity);

		} catch (IOException e) {
			logger.error("Error while reading updated document", e);
			response.setMessage("Error reading updated document");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error("Database error while updating document", e);
			response.setMessage("Database error while updating document");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Unexpected error while updating document", e);
			response.setMessage("Unexpected error occurred");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity deleteDocument(Integer documentId) {

		logger.info("Delete Document API called for id: {}", documentId);

		ResponseEntity response = new ResponseEntity();

		try {

			Optional<ContractDocuments> optional = repository.findById(documentId);

			if (!optional.isPresent()) {
				logger.error("Document not found for delete id: {}", documentId);
				response.setMessage("Document not found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			repository.delete(optional.get());

			logger.info("Document deleted successfully id: {}", documentId);

			response.setMessage("Document deleted successfully");
			response.setStatusCode(200);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error("Database error while deleting document", e);
			response.setMessage("Database error while deleting document");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Unexpected error while deleting document", e);
			response.setMessage("Unable to delete document");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getDocumentByContractId(Integer contractId) {

		logger.info("Get Document By ContractId API called {}", contractId);

		ResponseEntity response = new ResponseEntity();

		try {

			if (contractId == null) {
				logger.error("ContractId cannot be null");
				response.setMessage("ContractId cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			List<ContractDocuments> documents = repository.findByContractId(contractId);

			if (documents == null || documents.isEmpty()) {
				logger.warn("No documents found for contractId {}", contractId);
				response.setMessage("No documents found for contractId " + contractId);
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			logger.info("Documents fetched successfully for contractId {}", contractId);

			response.setMessage("Success");
			response.setStatusCode(200);
			response.setPayload(documents);

		} catch (DataAccessException ex) {
			logger.error("Database error while fetching documents for contractId {}", contractId, ex);
			response.setMessage("Database error while fetching documents");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception ex) {
			logger.error("Unexpected error while fetching documents for contractId {}", contractId, ex);
			response.setMessage("Something went wrong");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity deleteBulkDocument(List<Integer> documentIds) {

		logger.info("Delete Bulk Document API called for ids: {}", documentIds);

		ResponseEntity response = new ResponseEntity();

		try {

			if (documentIds == null || documentIds.isEmpty()) {
				logger.error("Invalid document ids: {}", documentIds);
				response.setMessage("Document IDs cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			List<ContractDocuments> documents = repository.findAllById(documentIds);

			if (documents.isEmpty()) {
				logger.error("No documents found for ids: {}", documentIds);
				response.setMessage("Document not found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			repository.deleteAll(documents);

			logger.info("Documents deleted successfully for ids: {}", documentIds);

			response.setMessage("Document deleted successfully");
			response.setStatusCode(200);
			response.setPayload(null);

		} catch (DataAccessException e) {

			logger.error("Database error while deleting documents for ids {}", documentIds, e);
			response.setMessage("Database error while deleting document");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {

			logger.error("Unexpected error while deleting documents for ids {}", documentIds, e);
			response.setMessage("Unable to delete document");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}
}