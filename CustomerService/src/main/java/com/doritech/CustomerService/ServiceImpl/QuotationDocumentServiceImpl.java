package com.doritech.CustomerService.ServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.CustomerService.Entity.QuotationDocument;
import com.doritech.CustomerService.Entity.QuotationMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.QuotationDocumentMapper;
import com.doritech.CustomerService.Repository.QuotationDocumentRepository;
import com.doritech.CustomerService.Repository.QuotationMasterRepository;
import com.doritech.CustomerService.Request.QuotationDocumentRequest;
import com.doritech.CustomerService.Response.QuotationDocumentResponse;
import com.doritech.CustomerService.Service.QuotationDocumentService;

@Service
public class QuotationDocumentServiceImpl implements QuotationDocumentService {

	private static final Logger logger = LoggerFactory.getLogger(QuotationDocumentServiceImpl.class);

	private final QuotationDocumentRepository repository;
	private final QuotationMasterRepository quotationMasterRepository;
	private final QuotationDocumentMapper mapper;

	public QuotationDocumentServiceImpl(QuotationDocumentRepository repository,
			QuotationMasterRepository quotationMasterRepository, QuotationDocumentMapper mapper) {
		this.repository = repository;
		this.quotationMasterRepository = quotationMasterRepository;
		this.mapper = mapper;
	}

	@Override
	public ResponseEntity saveUpdateDocument(List<QuotationDocumentRequest> requests, List<MultipartFile> files) {

		logger.info("Save/Update Document service started");

		ResponseEntity response = new ResponseEntity();

		try {

			if (requests == null || requests.isEmpty()) {
				logger.error("Request list cannot be null or empty");
				response.setMessage("Request list cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (files == null || files.isEmpty()) {
				logger.error("Files list cannot be null or empty");
				response.setMessage("Files list cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (requests.size() != files.size()) {
				logger.error("Requests count {} does not match files count {}", requests.size(), files.size());
				response.setMessage("Number of requests and files must be equal");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			List<QuotationDocumentResponse> resultList = new ArrayList<>();
			int createCount = 0;
			int updateCount = 0;

			for (int i = 0; i < requests.size(); i++) {

				QuotationDocumentRequest request = requests.get(i);
				MultipartFile file = files.get(i);

				if (request == null) {
					logger.warn("Request at index {} is null, skipping", i);
					continue;
				}

				logger.info(
						"Processing document record [{}] - quotationDocumentId: {}, quotationId: {}, documentType: {}, documentSource: {}, documentDate: {}, isActive: {}",
						i, request.getQuotationDocumentId(), request.getQuotationId(), request.getDocumentType(),
						request.getDocumentSource(), request.getDocumentDate(), request.getIsActive());

				if (file == null || file.isEmpty()) {
					logger.error("File at index {} is null or empty", i);
					response.setMessage("File at index " + i + " is null or empty");
					response.setStatusCode(400);
					response.setPayload(null);
					return response;
				}

				byte[] fileBytes = file.getBytes();
				logger.info("File [{}] received - name: {}, type: {}, size: {} bytes", i, file.getOriginalFilename(),
						file.getContentType(), fileBytes.length);

				logger.info("Validating quotation with id {} for record [{}]", request.getQuotationId(), i);

				QuotationMaster quotation = quotationMasterRepository.findById(request.getQuotationId())
						.orElseThrow(() -> {
							logger.error("Quotation not found with id {}", request.getQuotationId());
							return new ResourceNotFoundException(
									"Quotation not found with id " + request.getQuotationId());
						});

				if (request.getQuotationDocumentId() == null) {

					logger.info("No quotationDocumentId found for record [{}], performing CREATE", i);

					QuotationDocument entity = new QuotationDocument();
					entity.setQuotationMaster(quotation);
					entity.setDocumentType(request.getDocumentType());
					entity.setDocumentSource(request.getDocumentSource());
					entity.setDocumentDate(request.getDocumentDate());
					entity.setDocument(fileBytes);
					entity.setIsActive(request.getIsActive());
					entity.setCreatedBy(request.getCreatedBy());
					entity.setCreatedOn(new Date());
					entity.setModifiedBy(null);
					entity.setModifiedOn(null);

					QuotationDocument saved = repository.save(entity);
					createCount++;
					logger.info("Record [{}] CREATED successfully with quotationDocumentId {}", i,
							saved.getQuotationDocumentId());

					resultList.add(mapper.toResponse(saved));

				} else {

					logger.info("quotationDocumentId {} found for record [{}], performing UPDATE",
							request.getQuotationDocumentId(), i);

					QuotationDocument entity = repository.findById(request.getQuotationDocumentId()).orElseThrow(() -> {
						logger.error("Document not found with id {}", request.getQuotationDocumentId());
						return new ResourceNotFoundException(
								"Document not found with id " + request.getQuotationDocumentId());
					});

					entity.setQuotationMaster(quotation);
					entity.setDocumentType(request.getDocumentType());
					entity.setDocumentSource(request.getDocumentSource());
					entity.setDocumentDate(request.getDocumentDate());
					entity.setDocument(fileBytes);
					entity.setIsActive(request.getIsActive());
					entity.setModifiedBy(request.getModifiedBy());
					entity.setModifiedOn(new Date());

					QuotationDocument updated = repository.save(entity);
					updateCount++;
					logger.info("Record [{}] UPDATED successfully with quotationDocumentId {}", i,
							updated.getQuotationDocumentId());

					resultList.add(mapper.toResponse(updated));
				}
			}

			logger.info("Save/Update document completed | Created: {}, Updated: {}", createCount, updateCount);

			response.setMessage("Documents saved successfully | Created: " + createCount + ", Updated: " + updateCount);
			response.setStatusCode(200);
			response.setPayload(resultList);

		} catch (IllegalArgumentException e) {
			logger.error("Validation error while processing documents: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while processing documents: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (DataIntegrityViolationException e) {
			logger.error("Data integrity violation while processing documents: {}", e.getMessage());
			response.setMessage("Invalid reference data: document could not be saved due to data integrity issue");
			response.setStatusCode(409);
			response.setPayload(null);
		} catch (IOException e) {
			logger.error("File read error while processing documents: {}", e.getMessage());
			response.setMessage("Unable to read uploaded file");
			response.setStatusCode(500);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error while processing documents: {}", e.getMessage(), e);
			response.setMessage("Unable to process documents");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getById(Integer id) {

		logger.info("Get Document by ID service started for id {}", id);

		ResponseEntity response = new ResponseEntity();

		try {

			if (id == null) {
				logger.error("Document id cannot be null");
				response.setMessage("Document id cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			QuotationDocument entity = repository.findById(id).orElseThrow(() -> {
				logger.error("Document not found with id {}", id);
				return new ResourceNotFoundException("Document not found with id " + id);
			});

			logger.info("Document fetched successfully with id {}", id);

			response.setMessage("Document fetched successfully");
			response.setStatusCode(200);
			response.setPayload(mapper.toResponse(entity));

		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while fetching document with id {}: {}", id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error while fetching document with id {}: {}", id, e.getMessage(), e);
			response.setMessage("Unable to fetch document");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllDocument(int page, int size) {

		logger.info("Get all documents service started with page {} size {}", page, size);

		ResponseEntity response = new ResponseEntity();

		try {

			if (page < 0) {
				logger.error("Invalid page value {}", page);
				response.setMessage("Page value must be 0 or greater");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (size <= 0) {
				logger.error("Invalid size value {}", size);
				response.setMessage("Size value must be greater than 0");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			Pageable pageable = PageRequest.of(page, size);
			Page<QuotationDocument> pageData = repository.findAll(pageable);

			if (pageData.isEmpty()) {
				logger.info("No documents found");
				response.setMessage("No documents available");
				response.setStatusCode(200);
				response.setPayload(Collections.emptyList());
				return response;
			}

			List<QuotationDocumentResponse> list = pageData.getContent().stream().map(mapper::toResponse).toList();

			Map<String, Object> result = new HashMap<>();
			result.put("content", list);
			result.put("page", pageData.getNumber());
			result.put("size", pageData.getSize());
			result.put("totalElements", pageData.getTotalElements());
			result.put("totalPages", pageData.getTotalPages());

			logger.info("Fetched {} documents on page {}", list.size(), page);

			response.setMessage("Documents fetched successfully");
			response.setStatusCode(200);
			response.setPayload(result);

		} catch (Exception e) {
			logger.error("Unexpected error while fetching all documents: {}", e.getMessage(), e);
			response.setMessage("Unable to fetch documents");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getByQuotationId(Integer quotationId) {

	    logger.info("Get documents by quotationId service started for quotationId {}", quotationId);

	    ResponseEntity response = new ResponseEntity();

	    try {

	        if (quotationId == null) {
	            logger.error("Quotation id cannot be null");
	            response.setMessage("Quotation id cannot be null");
	            response.setStatusCode(400);
	            response.setPayload(null);
	            return response;
	        }

	        boolean quotationExists = quotationMasterRepository.existsById(quotationId);
	        if (!quotationExists) {
	            logger.error("Quotation not found with id {}", quotationId);
	            response.setMessage("Quotation not found with id " + quotationId);
	            response.setStatusCode(404);
	            response.setPayload(null);
	            return response;
	        }

	        List<QuotationDocument> documents = repository.findByQuotationMasterQuotationId(quotationId);

	        if (documents.isEmpty()) {
	            logger.info("No documents found for quotationId {}", quotationId);
	            response.setMessage("No documents available for quotationId " + quotationId);
	            response.setStatusCode(200);
	            response.setPayload(Collections.emptyList());
	            return response;
	        }

	        List<QuotationDocumentResponse> list = documents.stream().map(mapper::toResponse).toList();

	        logger.info("Fetched {} documents for quotationId {}", list.size(), quotationId);

	        response.setMessage("Documents fetched successfully");
	        response.setStatusCode(200);
	        response.setPayload(list);

	    } catch (ResourceNotFoundException e) {
	        logger.error("Resource not found while fetching documents for quotationId {}: {}", quotationId, e.getMessage());
	        response.setMessage(e.getMessage());
	        response.setStatusCode(404);
	        response.setPayload(null);
	    } catch (Exception e) {
	        logger.error("Unexpected error while fetching documents for quotationId {}: {}", quotationId, e.getMessage(), e);
	        response.setMessage("Unable to fetch documents");
	        response.setStatusCode(500);
	        response.setPayload(null);
	    }

	    return response;
	}
	@Override
	public ResponseEntity deleteMultiple(List<Integer> ids) {

		logger.info("Delete multiple documents service started for ids {}", ids);

		ResponseEntity response = new ResponseEntity();

		try {

			if (ids == null || ids.isEmpty()) {
				logger.error("ID list cannot be null or empty");
				response.setMessage("ID list cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			List<QuotationDocument> documents = repository.findAllById(ids);

			if (documents.isEmpty()) {
				logger.error("No documents found for given ids {}", ids);
				response.setMessage("No documents found for given ids");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			if (documents.size() != ids.size()) {
				List<Integer> foundIds = documents.stream().map(QuotationDocument::getQuotationDocumentId).toList();
				List<Integer> notFoundIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();
				logger.warn("Some IDs not found: {}", notFoundIds);
			}

			repository.deleteAll(documents);

			logger.info("Documents deleted successfully for ids {}", ids);

			response.setMessage("Documents deleted successfully");
			response.setStatusCode(200);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Unexpected error while deleting documents for ids {}: {}", ids, e.getMessage(), e);
			response.setMessage("Unable to delete documents");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

}