package com.doritech.CustomerService.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.QuotationMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.DuplicateResourceException;
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
	public ResponseEntity createQuotation(QuotationMasterRequest request) {

		logger.info("Create quotation service started");

		
		if (request == null) {
			throw new IllegalArgumentException("Request body cannot be null");
		}

		if (request.getCustomerId() == null) {
			throw new IllegalArgumentException("Customer id cannot be null");
		}

		if (request.getCreatedBy() == null || request.getCreatedBy().toString().isBlank()) {
			throw new IllegalArgumentException("CreatedBy cannot be null or empty");
		}

		if (request.getQuotationCode() == null || request.getQuotationCode().isBlank()) {
			throw new IllegalArgumentException("Quotation code cannot be null or empty");
		}

		if (repository.existsByQuotationCode(request.getQuotationCode())) {
			logger.warn("Quotation code already exists: {}", request.getQuotationCode());
			throw new DuplicateResourceException("Quotation code already exists: " + request.getQuotationCode());
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

		QuotationMaster saved = repository.save(entity);

		logger.info("Quotation saved successfully with id {}", saved.getQuotationId());

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Quotation created successfully");
		response.setStatusCode(201);
		response.setPayload(mapper.toResponse(saved));
		return response;
	}

	

	@Override
	public ResponseEntity getQuotationById(Integer id) {

		logger.info("Fetching quotation by id {}", id);

		if (id == null) {
			throw new IllegalArgumentException("Quotation id cannot be null");
		}

		QuotationMaster entity = repository.findById(id).orElseThrow(() -> {
			logger.error("Quotation not found with id {}", id);
			return new ResourceNotFoundException("Quotation not found with id " + id);
		});

		QuotationMasterResponse dto = mapper.toResponse(entity);

		if (entity.getCustomer() != null && entity.getCustomer().getCustomerId() != null) {
			customerRepository.findById(entity.getCustomer().getCustomerId()).ifPresent(customer -> {
				dto.setCustomerName(customer.getCustomerName());
				dto.setCustomerCode(customer.getCustomerCode());
			});
		}

		if (entity.getContract() != null && entity.getContract().getContractId() != null) {
			contractRepository.findById(entity.getContract().getContractId()).ifPresent(contract -> {
				dto.setContractName(contract.getContractName());
				dto.setContractCode(contract.getContractNo());
			});
		}

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Quotation fetched successfully");
		response.setStatusCode(200);
		response.setPayload(dto);
		return response;
	}

	

	@Override
	public ResponseEntity getAllQuotation(int page, int size) {

		logger.info("Fetching all quotations page {} size {}", page, size);

		if (page < 0 || size <= 0) {
			throw new IllegalArgumentException("Invalid pagination params: page must be >= 0 and size must be > 0");
		}

		Pageable pageable = PageRequest.of(page, size);
		Page<QuotationMaster> data = repository.findAll(pageable);

		if (data.isEmpty()) {
			Map<String, Object> emptyResult = new HashMap<>();
			emptyResult.put("data", Collections.emptyList());
			emptyResult.put("currentPage", page);
			emptyResult.put("pageSize", size);
			emptyResult.put("totalElements", 0);
			emptyResult.put("totalPages", 0);

			ResponseEntity response = new ResponseEntity();
			response.setMessage("No quotations available");
			response.setStatusCode(200);
			response.setPayload(emptyResult);
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

			if (q.getContract() != null && q.getContract().getContractId() != null) {
				contractRepository.findById(q.getContract().getContractId()).ifPresent(contract -> {
					dto.setContractName(contract.getContractName());
					dto.setContractCode(contract.getContractNo());
				});
			}

			list.add(dto);
		}

		Map<String, Object> result = new HashMap<>();
		result.put("data", list);
		result.put("currentPage", data.getNumber());
		result.put("pageSize", data.getSize());
		result.put("totalElements", data.getTotalElements());
		result.put("totalPages", data.getTotalPages());

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Quotation list fetched successfully");
		response.setStatusCode(200);
		response.setPayload(result);
		return response;
	}

	

	@Override
	public ResponseEntity updateQuotation(Integer id, QuotationMasterRequest request) {

		logger.info("Updating quotation with id {}", id);

		if (id == null) {
			throw new IllegalArgumentException("Quotation id cannot be null");
		}

		if (request == null) {
			throw new IllegalArgumentException("Request body cannot be null");
		}

		QuotationMaster entity = repository.findById(id).orElseThrow(() -> {
			logger.error("Quotation not found with id {}", id);
			return new ResourceNotFoundException("Quotation not found with id " + id);
		});

		if (request.getQuotationCode() != null
				&& repository.existsByQuotationCodeAndQuotationIdNot(request.getQuotationCode(), id)) {
			logger.warn("Quotation code already exists: {}", request.getQuotationCode());
			throw new DuplicateResourceException("Quotation code already exists: " + request.getQuotationCode());
		}

		if (request.getCustomerId() != null) {
			CustomerMasterEntity customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
					() -> new ResourceNotFoundException("Customer not found with id " + request.getCustomerId()));
			entity.setCustomer(customer);
		}

		if (request.getContractId() != null) {
			ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(
					() -> new ResourceNotFoundException("Contract not found with id " + request.getContractId()));
			entity.setContract(contract);
		}

		entity.setQuotationCode(request.getQuotationCode());
		entity.setStatus(request.getStatus());
		entity.setPoNo(request.getPoNo());
		entity.setSalesOrderNo(request.getSalesOrderNo());
		entity.setIsActive(request.getIsActive());
		entity.setModifiedBy(request.getModifiedBy());

		QuotationMaster updated = repository.save(entity);

		logger.info("Quotation updated successfully with id {}", updated.getQuotationId());

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Quotation updated successfully");
		response.setStatusCode(200);
		response.setPayload(mapper.toResponse(updated));
		return response;
	}

	
	@Override
	@Transactional
	public ResponseEntity deleteQuotationBulk(List<Integer> ids) {

		logger.info("Bulk deleting quotations with ids {}", ids);

		if (ids == null || ids.isEmpty()) {
			throw new IllegalArgumentException("Quotation id list cannot be null or empty");
		}

		if (ids.stream().anyMatch(i -> i == null)) {
			throw new IllegalArgumentException("Quotation id list must not contain null values");
		}

		List<QuotationMaster> quotations = repository.findAllById(ids);

		if (quotations.isEmpty()) {
			logger.error("No quotations found for given ids {}", ids);
			throw new ResourceNotFoundException("No quotations found for given ids: " + ids);
		}

		if (quotations.size() != ids.size()) {
			List<Integer> foundIds = quotations.stream().map(QuotationMaster::getQuotationId)
					.collect(Collectors.toList());
			List<Integer> notFoundIds = ids.stream().filter(i -> !foundIds.contains(i)).collect(Collectors.toList());
			logger.warn("Some quotation IDs not found: {}", notFoundIds);
			throw new ResourceNotFoundException("Quotation IDs not found: " + notFoundIds);
		}

		repository.deleteAll(quotations);

		logger.info("Bulk delete successful for {} quotations", quotations.size());

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Bulk delete successful");
		response.setStatusCode(200);
		response.setPayload(null);
		return response;
	}

	

	@Override
	public ResponseEntity getQuotations(String quotationCode, Integer customerId, Integer contractId, String status,
			String isActive, int page, int size) {

		logger.info(
				"Fetching quotations with filters: quotationCode={}, customerId={}, contractId={}, status={}, isActive={}, page={}, size={}",
				quotationCode, customerId, contractId, status, isActive, page, size);

		if (page < 0 || size <= 0) {
			throw new IllegalArgumentException("Invalid pagination params: page must be >= 0 and size must be > 0");
		}

		Specification<QuotationMaster> spec = QuotationSpecification.filterQuotations(quotationCode, customerId,
				contractId, status, isActive);

		Pageable pageable = PageRequest.of(page, size);
		Page<QuotationMaster> pageResult = repository.findAll(spec, pageable);

		List<QuotationMasterResponse> responseList = pageResult.getContent().stream().map(this::toResponse)
				.collect(Collectors.toList());

		Map<String, Object> pagination = new HashMap<>();
		pagination.put("content", responseList);
		pagination.put("currentPage", pageResult.getNumber());
		pagination.put("totalItems", pageResult.getTotalElements());
		pagination.put("totalPages", pageResult.getTotalPages());
		pagination.put("pageSize", pageResult.getSize());

		logger.info("Quotations fetched successfully: totalItems={}", pageResult.getTotalElements());

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Quotations fetched successfully");
		response.setStatusCode(200);
		response.setPayload(pagination);
		return response;
	}

	

	@Override
	public ResponseEntity getAllQuotationIdAndCode() {

		logger.info("Get All Quotation Id And Code API called");

		List<QuotationMaster> master = repository.findAll();

		if (master == null || master.isEmpty()) {
			throw new ResourceNotFoundException("No quotations available");
		}

		List<QuotationMasterResponse> responseList = master.stream().map(entity -> {
			QuotationMasterResponse dto = new QuotationMasterResponse();
			dto.setQuotationId(entity.getQuotationId());
			dto.setQuotationCode(entity.getQuotationCode());
			return dto;
		}).collect(Collectors.toList());

		logger.info("Successfully fetched {} quotations", responseList.size());

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Quotation list fetched successfully");
		response.setPayload(responseList);
		response.setStatusCode(200);
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