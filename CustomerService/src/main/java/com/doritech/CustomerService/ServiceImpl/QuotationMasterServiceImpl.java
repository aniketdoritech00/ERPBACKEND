package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.QuotationMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.QuotationMasterMapper;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Repository.QuotationMasterRepository;
import com.doritech.CustomerService.Request.QuotationMasterRequest;
import com.doritech.CustomerService.Response.QuotationMasterResponse;
import com.doritech.CustomerService.Service.QuotationMasterService;
import com.doritech.CustomerService.Specification.QuotationSpecification;

import jakarta.transaction.Transactional;

@Service
public class QuotationMasterServiceImpl implements QuotationMasterService {

	private static final Logger logger = LoggerFactory.getLogger(QuotationMasterServiceImpl.class);

	@Autowired
	private QuotationMasterRepository repository;

	@Autowired
	private QuotationMasterMapper mapper;

	@Autowired
	private CustomerMasterRepository customerRepository;

	@Autowired
	private ContractMasterRepository contractRepository;

	@Override
	public com.doritech.CustomerService.Entity.ResponseEntity createQuotation(QuotationMasterRequest request) {

		logger.info("Create quotation service started");

		com.doritech.CustomerService.Entity.ResponseEntity response = new com.doritech.CustomerService.Entity.ResponseEntity();

		try {

			if (request == null) {
				logger.error("Request body cannot be null");
				response.setMessage("Request body cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			logger.info("Request received for quotation code {}", request.getQuotationCode());

			if (request.getCustomerId() == null) {
				logger.error("Customer id cannot be null");
				response.setMessage("Customer id cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (request.getCreatedBy() == null || request.getCreatedBy().toString().isBlank()) {
				logger.error("CreatedBy cannot be null or empty");
				response.setMessage("CreatedBy cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (request.getQuotationCode() == null || request.getQuotationCode().isBlank()) {
				logger.error("Quotation code cannot be null or empty");
				response.setMessage("Quotation code cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (repository.existsByQuotationCode(request.getQuotationCode())) {
				logger.warn("Quotation code already exists: {}", request.getQuotationCode());
				response.setMessage("Quotation code already exists: " + request.getQuotationCode());
				response.setStatusCode(409);
				response.setPayload(null);
				return response;
			}

			CustomerMasterEntity customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
					() -> new ResourceNotFoundException("Customer not found with id " + request.getCustomerId()));

			ContractMaster contract = null;
			if (request.getContractId() != null) {
				contract = contractRepository.findById(request.getContractId()).orElseThrow(
						() -> new ResourceNotFoundException("Contract not found with id " + request.getContractId()));
			}

			QuotationMaster entity = mapper.toEntity(request);
			entity.setCustomer(customer);
			entity.setContract(contract);
			//entity.setCreatedOn(LocalDateTime.now());

			QuotationMaster saved = repository.save(entity);

			logger.info("Quotation saved successfully with id {}", saved.getQuotationId());

			response.setMessage("Quotation created successfully");
			response.setStatusCode(201);
			response.setPayload(mapper.toResponse(saved));

		} catch (ResourceNotFoundException e) {

			logger.error("Reference data not found while creating quotation: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);

		} catch (DataIntegrityViolationException e) {

			logger.error("Data integrity violation while creating quotation: {}", e.getMessage());
			response.setMessage("Duplicate or invalid data: " + e.getMostSpecificCause().getMessage());
			response.setStatusCode(409);
			response.setPayload(null);

		} catch (Exception e) {

			logger.error("Unexpected error while saving quotation: {}", e.getMessage(), e);
			response.setMessage("Unable to save quotation");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public com.doritech.CustomerService.Entity.ResponseEntity getQuotationById(Integer id) {

		logger.info("Fetching quotation by id {}", id);

		com.doritech.CustomerService.Entity.ResponseEntity response = new com.doritech.CustomerService.Entity.ResponseEntity();

		if (id == null) {
			logger.error("Quotation id cannot be null");
			throw new IllegalArgumentException("Quotation id cannot be null");
		}

		try {

			QuotationMaster entity = repository.findById(id).orElseThrow(() -> {
				logger.error("Quotation not found with id {}", id);
				return new ResourceNotFoundException("Quotation not found with id " + id);
			});

			QuotationMasterResponse dto = mapper.toResponse(entity);

			if (entity.getCustomer().getCustomerId() != null) {
				CustomerMasterEntity customer = customerRepository.findById(entity.getCustomer().getCustomerId())
						.orElse(null);
				if (customer != null) {
					dto.setCustomerName(customer.getCustomerName());
					dto.setCustomerCode(customer.getCustomerCode());
				}
			}

			if (entity.getContract() != null && entity.getContract().getContractId() != null) {

				ContractMaster contract = contractRepository.findById(entity.getContract().getContractId())
						.orElse(null);

				if (contract != null) {
					dto.setContractName(contract.getContractName());
					dto.setContractCode(contract.getContractNo());
				}
			}
			response.setMessage("Quotation fetched successfully");
			response.setStatusCode(200);
			response.setPayload(dto);

		} catch (ResourceNotFoundException e) {
			logger.error("Quotation not found: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error while fetching quotation with id {}: {}", id, e.getMessage(), e);
			throw new RuntimeException("Unable to fetch quotation");
		}

		return response;
	}

	@Override
	public com.doritech.CustomerService.Entity.ResponseEntity getAllQuotation(int page, int size) {

		logger.info("Fetching all quotations page {} size {}", page, size);

		com.doritech.CustomerService.Entity.ResponseEntity response = new com.doritech.CustomerService.Entity.ResponseEntity();

		try {

			if (page < 0 || size <= 0) {
				logger.error("Invalid pagination params: page={}, size={}", page, size);
				throw new IllegalArgumentException("Invalid page or size value");
			}

			Pageable pageable = PageRequest.of(page, size);
			Page<QuotationMaster> data = repository.findAll(pageable);

			if (data == null || data.isEmpty()) {

				Map<String, Object> result = new HashMap<>();
				result.put("data", Collections.emptyList());
				result.put("currentPage", page);
				result.put("pageSize", size);
				result.put("totalElements", 0);
				result.put("totalPages", 0);

				response.setMessage("No quotations available");
				response.setStatusCode(200);
				response.setPayload(result);
				return response;
			}

			List<QuotationMasterResponse> list = new ArrayList<>();

			for (QuotationMaster q : data.getContent()) {

				if (q == null)
					continue;

				QuotationMasterResponse dto = mapper.toResponse(q);

				if (q.getCustomer() != null && q.getCustomer().getCustomerId() != null) {
					customerRepository.findById(q.getCustomer().getCustomerId()).ifPresent(customer -> {
						dto.setCustomerName(customer.getCustomerName());
						dto.setCustomerCode(customer.getCustomerCode());
					});
				}

				if (q.getContract() != null) {
					Integer contractId = q.getContract().getContractId();

					if (contractId != null) {
						contractRepository.findById(contractId).ifPresent(contract -> {
							dto.setContractName(contract.getContractName());
							dto.setContractCode(contract.getContractNo());
						});
					}
				}

				list.add(dto);
			}

			Map<String, Object> result = new HashMap<>();
			result.put("data", list);
			result.put("currentPage", data.getNumber());
			result.put("pageSize", data.getSize());
			result.put("totalElements", data.getTotalElements());
			result.put("totalPages", data.getTotalPages());

			response.setMessage("Quotation list fetched successfully");
			response.setStatusCode(200);
			response.setPayload(result);

		} catch (IllegalArgumentException ex) {
			logger.error("Validation error: {}", ex.getMessage());
			throw ex;

		} catch (Exception e) {
			logger.error("Unexpected error while fetching quotations", e);
			throw new RuntimeException("Unable to fetch quotations");
		}

		return response;
	}

	@Override
	public com.doritech.CustomerService.Entity.ResponseEntity updateQuotation(Integer id,
			QuotationMasterRequest request) {

		logger.info("Updating quotation with id {}", id);

		com.doritech.CustomerService.Entity.ResponseEntity response = new com.doritech.CustomerService.Entity.ResponseEntity();

		try {

			if (id == null) {
				logger.error("Quotation id cannot be null");

				response.setMessage("Quotation id cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (request == null) {
				logger.error("Request body cannot be null");

				response.setMessage("Request body cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			QuotationMaster entity = repository.findById(id).orElseThrow(() -> {
				logger.error("Quotation not found with id {}", id);
				return new ResourceNotFoundException("Quotation not found with id " + id);
			});

			if (request.getQuotationCode() != null
					&& repository.existsByQuotationCodeAndQuotationIdNot(request.getQuotationCode(), id)) {

				logger.warn("Quotation code already exists: {}", request.getQuotationCode());

				response.setMessage("Quotation code already exists: " + request.getQuotationCode());
				response.setStatusCode(409);
				response.setPayload(null);
				return response;
			}

			if (request.getCustomerId() != null) {

				CustomerMasterEntity customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> {
					logger.error("Customer not found with id {}", request.getCustomerId());
					return new ResourceNotFoundException("Customer not found with id " + request.getCustomerId());
				});

				entity.setCustomer(customer);
			}

			if (request.getContractId() != null) {

				ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(() -> {
					logger.error("Contract not found with id {}", request.getContractId());
					return new ResourceNotFoundException("Contract not found with id " + request.getContractId());
				});

				entity.setContract(contract);
			}

			entity.setQuotationCode(request.getQuotationCode());
			entity.setStatus(request.getStatus());
			entity.setPoNo(request.getPoNo());
			entity.setSalesOrderNo(request.getSalesOrderNo());
			entity.setIsActive(request.getIsActive());
			entity.setModifiedBy(request.getModifiedBy());
			//entity.setModifiedOn(LocalDateTime.now());

			QuotationMaster updated = repository.save(entity);

			logger.info("Quotation updated successfully with id {}", updated.getQuotationId());

			response.setMessage("Quotation updated successfully");
			response.setStatusCode(200);
			response.setPayload(mapper.toResponse(updated));

		} catch (ResourceNotFoundException e) {

			logger.error("Reference data not found while updating quotation: {}", e.getMessage());

			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);

		} catch (Exception e) {

			logger.error("Unexpected error while updating quotation with id {}: {}", id, e.getMessage(), e);

			response.setMessage("Unable to update quotation");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	@Transactional
	public com.doritech.CustomerService.Entity.ResponseEntity deleteQuotationBulk(List<Integer> ids) {

		logger.info("Bulk deleting quotations with ids {}", ids);

		com.doritech.CustomerService.Entity.ResponseEntity response = new com.doritech.CustomerService.Entity.ResponseEntity();

		try {

			if (ids == null || ids.isEmpty()) {
				logger.error("Quotation id list cannot be null or empty");
				throw new IllegalArgumentException("Quotation id list cannot be null or empty");
			}

			List<QuotationMaster> quotations = repository.findAllById(ids);

			if (quotations.isEmpty()) {
				logger.error("No quotations found for given ids {}", ids);
				throw new ResourceNotFoundException("No quotations found for given ids");
			}

			if (quotations.size() != ids.size()) {
				logger.warn("Some quotation IDs not found. Requested: {}, Found: {}", ids.size(), quotations.size());
			}

			repository.deleteAll(quotations);

			logger.info("Bulk delete successful for {} quotations", quotations.size());

			response.setMessage("Bulk delete successful");
			response.setStatusCode(200);
			response.setPayload(null);

		} catch (IllegalArgumentException e) {
			logger.error("Validation error in bulk delete: {}", e.getMessage());
			throw e;
		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found in bulk delete: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("Unexpected error in bulk delete: {}", e.getMessage(), e);
			throw new RuntimeException("Unable to delete quotations in bulk");
		}

		return response;
	}

	@Override
	public ResponseEntity getQuotations(String quotationCode, Integer customerId, Integer contractId, String status,
			String isActive, int page, int size) {

		logger.info(
				"Fetching quotations with filters: quotationCode={}, customerId={}, contractId={}, status={}, isActive={}, page={}, size={}",
				quotationCode, customerId, contractId, status, isActive, page, size);

		ResponseEntity response = new ResponseEntity();

		try {
			Specification<QuotationMaster> spec = QuotationSpecification.filterQuotations(quotationCode, customerId,
					contractId, status, isActive);

			Pageable pageable = PageRequest.of(page, size);

			Page<QuotationMaster> pageResult = repository.findAll(spec, pageable);

			List<QuotationMasterResponse> responseList = pageResult.getContent().stream().map(this::toResponse)
					.toList();

			Map<String, Object> pagination = new HashMap<>();
			pagination.put("content", responseList);
			pagination.put("currentPage", pageResult.getNumber());
			pagination.put("totalItems", pageResult.getTotalElements());
			pagination.put("totalPages", pageResult.getTotalPages());
			pagination.put("pageSize", pageResult.getSize());

			response.setMessage("Quotations fetched successfully");
			response.setStatusCode(200);
			response.setPayload(pagination);

			logger.info("Quotations fetched successfully: totalItems={}", pageResult.getTotalElements());

		} catch (Exception e) {
			logger.error("Unexpected error while fetching quotations: {}", e.getMessage(), e);
			throw new RuntimeException("Unable to fetch quotations");
		}

		return response;
	}

	public QuotationMasterResponse toResponse(QuotationMaster entity) {
		if (entity == null)
			return null;

		QuotationMasterResponse response = new QuotationMasterResponse();

		response.setQuotationId(entity.getQuotationId());
		response.setQuotationCode(entity.getQuotationCode());

		if (entity.getContract() != null) {
			response.setContractId(entity.getContract().getContractId());
			response.setContractName(entity.getContract().getContractName());
		}

		if (entity.getCustomer() != null) {
			response.setCustomerId(entity.getCustomer().getCustomerId());
			response.setCustomerName(entity.getCustomer().getCustomerName());
		}

		response.setQuotationDate(entity.getQuotationDate());
		response.setStatus(entity.getStatus());
		response.setPoNo(entity.getPoNo());
		response.setSalesOrderNo(entity.getSalesOrderNo());
		response.setCommunicationMode(entity.getCommunicationMode());
		response.setIsActive(entity.getIsActive());

		response.setCreatedOn(entity.getCreatedOn());
		response.setModifiedOn(entity.getModifiedOn());
		response.setCreatedBy(entity.getCreatedBy());
		response.setModifiedBy(entity.getModifiedBy());

		return response;
	}

}