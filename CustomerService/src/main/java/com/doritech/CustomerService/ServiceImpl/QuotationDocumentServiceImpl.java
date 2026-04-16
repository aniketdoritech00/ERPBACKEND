package com.doritech.CustomerService.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.CustomerService.Entity.QuotationDocument;
import com.doritech.CustomerService.Entity.QuotationMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.InternalServerException;
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

        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Request list cannot be null or empty");
        }

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Files list cannot be null or empty");
        }

        if (requests.size() != files.size()) {
            throw new IllegalArgumentException("Number of requests and files must be equal. Requests: "
                    + requests.size() + ", Files: " + files.size());
        }

        List<QuotationDocumentResponse> resultList = new ArrayList<>();
        int createCount = 0;
        int updateCount = 0;

        for (int i = 0; i < requests.size(); i++) {

            QuotationDocumentRequest request = requests.get(i);
            MultipartFile file = files.get(i);

            if (request == null) {
                throw new IllegalArgumentException("Request at index " + i + " cannot be null");
            }

            if (request.getQuotationId() == null) {
                throw new IllegalArgumentException("Quotation id cannot be null at index " + i);
            }

            logger.info(
                    "Processing document record [{}] - quotationDocumentId: {}, quotationId: {}, documentType: {}, documentSource: {}, documentDate: {}, isActive: {}",
                    i, request.getQuotationDocumentId(), request.getQuotationId(), request.getDocumentType(),
                    request.getDocumentSource(), request.getDocumentDate(), request.getIsActive());

            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("File at index " + i + " is null or empty");
            }

            byte[] fileBytes;
            try {
                fileBytes = file.getBytes();
            } catch (Exception e) {
                logger.error("Failed to read file at index {}: {}", i, e.getMessage());
                throw new InternalServerException("Unable to read uploaded file at index " + i + ": " + e.getMessage());
            }

            logger.info("File [{}] received - name: {}, type: {}, size: {} bytes", i,
                    file.getOriginalFilename(), file.getContentType(), fileBytes.length);

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

                QuotationDocument entity = repository.findById(request.getQuotationDocumentId())
                        .orElseThrow(() -> {
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

        ResponseEntity response = new ResponseEntity();
        response.setMessage("Documents saved successfully | Created: " + createCount + ", Updated: " + updateCount);
        response.setStatusCode(200);
        response.setPayload(resultList);
        return response;
    }

    @Override
    public ResponseEntity getById(Integer id) {

        logger.info("Get Document by ID service started for id {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Document id cannot be null");
        }

        QuotationDocument entity = repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Document not found with id {}", id);
                    return new ResourceNotFoundException("Document not found with id " + id);
                });

        logger.info("Document fetched successfully with id {}", id);

        ResponseEntity response = new ResponseEntity();
        response.setMessage("Document fetched successfully");
        response.setStatusCode(200);
        response.setPayload(mapper.toResponse(entity));
        return response;
    }

    @Override
    public ResponseEntity getAllDocument(int page, int size) {

        logger.info("Get all documents service started with page {} size {}", page, size);

        if (page < 0) {
            throw new IllegalArgumentException("Page value must be 0 or greater");
        }

        if (size <= 0) {
            throw new IllegalArgumentException("Size value must be greater than 0");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<QuotationDocument> pageData = repository.findAll(pageable);

        if (pageData.isEmpty()) {
            logger.info("No documents found");
            ResponseEntity response = new ResponseEntity();
            response.setMessage("No documents available");
            response.setStatusCode(200);
            response.setPayload(Collections.emptyList());
            return response;
        }

        List<QuotationDocumentResponse> list = pageData.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", list);
        result.put("page", pageData.getNumber());
        result.put("size", pageData.getSize());
        result.put("totalElements", pageData.getTotalElements());
        result.put("totalPages", pageData.getTotalPages());

        logger.info("Fetched {} documents on page {}", list.size(), page);

        ResponseEntity response = new ResponseEntity();
        response.setMessage("Documents fetched successfully");
        response.setStatusCode(200);
        response.setPayload(result);
        return response;
    }

    @Override
    public ResponseEntity getByQuotationId(Integer quotationId) {

        logger.info("Get documents by quotationId service started for quotationId {}", quotationId);

        if (quotationId == null) {
            throw new IllegalArgumentException("Quotation id cannot be null");
        }

        if (!quotationMasterRepository.existsById(quotationId)) {
            logger.error("Quotation not found with id {}", quotationId);
            throw new ResourceNotFoundException("Quotation not found with id " + quotationId);
        }

        List<QuotationDocument> documents = repository.findByQuotationMasterQuotationId(quotationId);

        if (documents.isEmpty()) {
            logger.info("No documents found for quotationId {}", quotationId);
            ResponseEntity response = new ResponseEntity();
            response.setMessage("No documents available for quotationId " + quotationId);
            response.setStatusCode(200);
            response.setPayload(Collections.emptyList());
            return response;
        }

        List<QuotationDocumentResponse> list = documents.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        logger.info("Fetched {} documents for quotationId {}", list.size(), quotationId);

        ResponseEntity response = new ResponseEntity();
        response.setMessage("Documents fetched successfully");
        response.setStatusCode(200);
        response.setPayload(list);
        return response;
    }

    @Override
    public ResponseEntity deleteMultiple(List<Integer> ids) {

        logger.info("Delete multiple documents service started for ids {}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("ID list cannot be null or empty");
        }

        if (ids.stream().anyMatch(id -> id == null)) {
            throw new IllegalArgumentException("ID list must not contain null values");
        }

        List<QuotationDocument> documents = repository.findAllById(ids);

        if (documents.isEmpty()) {
            logger.error("No documents found for given ids {}", ids);
            throw new ResourceNotFoundException("No documents found for given ids: " + ids);
        }

        if (documents.size() != ids.size()) {
            List<Integer> foundIds = documents.stream()
                    .map(QuotationDocument::getQuotationDocumentId)
                    .collect(Collectors.toList());
            List<Integer> notFoundIds = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            logger.warn("Some document IDs not found: {}", notFoundIds);
            throw new ResourceNotFoundException("Document IDs not found: " + notFoundIds);
        }

        repository.deleteAll(documents);

        logger.info("Documents deleted successfully for ids {}", ids);

        ResponseEntity response = new ResponseEntity();
        response.setMessage("Documents deleted successfully");
        response.setStatusCode(200);
        response.setPayload(null);
        return response;
    }
}