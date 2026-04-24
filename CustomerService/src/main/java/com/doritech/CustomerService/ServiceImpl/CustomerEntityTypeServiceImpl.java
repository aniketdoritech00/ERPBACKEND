package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.CustomerEntityTypeEntity;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.CustomerEntityTypeRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.CustomerEntityTypeRequest;
import com.doritech.CustomerService.Response.CustomerEntityTypeResponse;
import com.doritech.CustomerService.Service.CustomerEntityTypeService;

import jakarta.transaction.Transactional;

@Service
public class CustomerEntityTypeServiceImpl implements CustomerEntityTypeService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerEntityTypeServiceImpl.class);

	private final CustomerEntityTypeRepository repository;

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	public CustomerEntityTypeServiceImpl(CustomerEntityTypeRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public ResponseEntity saveCustomerEntityType(CustomerEntityTypeRequest request) {

		logger.info("Save/Update Customer Entity Type API called");

		boolean customerExists = customerMasterRepository.existsById(request.getCustomerId());

		if (!customerExists) {
			logger.error("Customer ID not found : {}", request.getCustomerId());
			throw new ResourceNotFoundException("Customer ID does not exist");
		}

		CustomerEntityTypeEntity entity;

		if (request.getCustEntityId() != null) {

			entity = repository.findById(request.getCustEntityId())
					.orElseThrow(() -> new ResourceNotFoundException("Customer Entity Type ID not found"));

			entity.setEntityType(request.getEntityType());
			entity.setIsActive(request.getIsActive());
			entity.setModifiedBy(request.getCreatedBy());
			entity.setModifiedOn(LocalDateTime.now());

			repository.save(entity);

			logger.info("Customer Entity Type updated successfully");

			return new ResponseEntity("Customer Entity Type Updated Successfully", 200, entity);

		} else {

			entity = new CustomerEntityTypeEntity();

			entity.setCustomerId(request.getCustomerId());
			entity.setEntityType(request.getEntityType());
			entity.setIsActive(request.getIsActive());
			entity.setCreatedBy(request.getCreatedBy());
			entity.setCreatedOn(LocalDateTime.now());

			repository.save(entity);

			logger.info("Customer Entity Type saved successfully");

			return new ResponseEntity("Customer Entity Type Saved Successfully", 200, entity);
		}
	}

	@Override
	public ResponseEntity getAllCustomerEntityTypes(int page, int size) {

		logger.info("Get All Customer Entity Types API called | page: {}, size: {}", page, size);

		Pageable pageable = PageRequest.of(page, size, Sort.by("custEntityId").descending());

		Page<CustomerEntityTypeEntity> entityPage = repository.findAll(pageable);

		if (entityPage.isEmpty()) {
			logger.warn("No Customer Entity Types found");
			throw new ResourceNotFoundException("No Customer Entity Types Found");
		}

		List<CustomerEntityTypeResponse> responseList = entityPage.getContent().stream().map(entity -> {

			CustomerEntityTypeResponse response = new CustomerEntityTypeResponse();

			response.setCustEntityId(entity.getCustEntityId());
			response.setCustomerId(entity.getCustomerId());

			if (entity.getCustomerId() != null) {
				CustomerMasterEntity customerMasterEntity = customerMasterRepository
						.findByCustomerId(entity.getCustomerId());
				response.setCustomerName(customerMasterEntity.getCustomerName());
				response.setCustomerCode(customerMasterEntity.getCustomerCode());
			}

			response.setEntityType(entity.getEntityType());
			response.setIsActive(entity.getIsActive());

			return response;

		}).toList();

		Map<String, Object> pagination = new HashMap<>();
		pagination.put("content", responseList);
		pagination.put("currentPage", entityPage.getNumber());
		pagination.put("totalItems", entityPage.getTotalElements());
		pagination.put("totalPages", entityPage.getTotalPages());
		pagination.put("pageSize", entityPage.getSize());

		logger.info("Customer Entity Types fetched successfully");

		return new ResponseEntity("Success", 200, pagination);
	}

	@Override
	public ResponseEntity getCustomerEntityTypeByCustomerId(Integer customerId) {

		logger.info("Get Customer Entity Types by customerId: {}", customerId);

		List<CustomerEntityTypeEntity> list = repository.findByCustomerId(customerId);

		if (list.isEmpty()) {
			throw new ResourceNotFoundException("Customer Entity Type Not Found");
		}

		List<CustomerEntityTypeResponse> response = list.stream().map(e -> {

			CustomerEntityTypeResponse r = new CustomerEntityTypeResponse();

			r.setCustEntityId(e.getCustEntityId());
			r.setCustomerId(e.getCustomerId());
			r.setEntityType(e.getEntityType());
			r.setIsActive(e.getIsActive());

			return r;

		}).toList();

		return new ResponseEntity("Success", 200, response);
	}

	@Override
	public ResponseEntity getCustomerEntityTypesByCustomerId(Integer customerId) {

		logger.info("Get Customer Entity Types by CustomerId API called");

		List<CustomerEntityTypeEntity> entityList = repository.findByCustomerId(customerId);

		if (entityList.isEmpty()) {
			logger.warn("No Customer Entity Types found for customerId: {}", customerId);
			throw new ResourceNotFoundException("No Entity Types Found for CustomerId: " + customerId);
		}

		List<CustomerEntityTypeResponse> responseList = entityList.stream().map(entity -> {

			CustomerEntityTypeResponse response = new CustomerEntityTypeResponse();

			response.setCustEntityId(entity.getCustEntityId());
			response.setCustomerId(entity.getCustomerId());
			response.setEntityType(entity.getEntityType());
			response.setIsActive(entity.getIsActive());

			return response;

		}).toList();

		logger.info("Customer Entity Types fetched successfully for customerId: {}", customerId);

		return new ResponseEntity("Success", 200, responseList);
	}
}