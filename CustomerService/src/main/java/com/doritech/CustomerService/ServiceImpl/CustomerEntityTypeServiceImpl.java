package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.CustomerEntityTypeEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.InternalServerException;
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

	public CustomerEntityTypeServiceImpl(CustomerEntityTypeRepository repository) {
		this.repository = repository;
	}

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	@Override
	@Transactional
	public ResponseEntity saveCustomerEntityType(CustomerEntityTypeRequest request) {

		logger.info("Save/Update Customer Entity Type API called");

		try {

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

		} catch (ResourceNotFoundException ex) {
			logger.error("Resource not found", ex);
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while saving/updating Customer Entity Type", ex);
			throw new InternalServerException("Something went wrong while processing Customer Entity Type");
		}
	}

	@Override
	@Transactional
	public ResponseEntity getAllCustomerEntityTypes() {

		logger.info("Get All Customer Entity Types API called");

		try {

			List<CustomerEntityTypeEntity> entityList = repository.findAll();

			if (entityList.isEmpty()) {
				logger.warn("No Customer Entity Types found");
				return new ResponseEntity("No Customer Entity Types Found", 404, null);
			}

			List<CustomerEntityTypeResponse> responseList = new ArrayList<>();

			for (CustomerEntityTypeEntity entity : entityList) {

				CustomerEntityTypeResponse response = new CustomerEntityTypeResponse();

				response.setCustEntityId(entity.getCustEntityId());
				response.setCustomerId(entity.getCustomerId());
				response.setEntityType(entity.getEntityType());
				response.setIsActive(entity.getIsActive());
			//	response.setCreatedBy(entity.getCreatedBy());
				//response.setCreatedOn(entity.getCreatedOn());
				//response.setModifiedBy(entity.getModifiedBy());
				//response.setModifiedOn(entity.getModifiedOn());

				responseList.add(response);
			}

			logger.info("Customer Entity Types fetched successfully");

			return new ResponseEntity("Success", 200, responseList);

		} catch (Exception ex) {

			logger.error("Error while fetching Customer Entity Types", ex);
			throw new InternalServerException("Something went wrong while fetching Customer Entity Types");
		}
	}

	@Override
	public ResponseEntity getCustomerEntityTypeByCustomerId(Integer customerId) {

		logger.info("Get Customer Entity Types {}", customerId);

		List<CustomerEntityTypeEntity> list = repository.findByCustomerId(customerId);

		if (list.isEmpty())
			throw new ResourceNotFoundException("Customer Entity Type Not Found");

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
	@Transactional
	public ResponseEntity getCustomerEntityTypesByCustomerId(Integer customerId) {

	    logger.info("Get Customer Entity Types by CustomerId API called");

	    try {

	        List<CustomerEntityTypeEntity> entityList = repository.findByCustomerId(customerId);

	        if (entityList.isEmpty()) {
	            logger.warn("No Customer Entity Types found for customerId: {}", customerId);
	            return new ResponseEntity("No Entity Types Found for CustomerId: " + customerId, 404, null);
	        }

	        List<CustomerEntityTypeResponse> responseList = new ArrayList<>();

	        for (CustomerEntityTypeEntity entity : entityList) {

	            CustomerEntityTypeResponse response = new CustomerEntityTypeResponse();

	            response.setCustEntityId(entity.getCustEntityId());
	            response.setCustomerId(entity.getCustomerId());
	            response.setEntityType(entity.getEntityType());
	            response.setIsActive(entity.getIsActive());

	            responseList.add(response);
	        }

	        logger.info("Customer Entity Types fetched successfully for customerId: {}", customerId);

	        return new ResponseEntity("Success", 200, responseList);

	    } catch (Exception ex) {

	        logger.error("Error while fetching Customer Entity Types", ex);
	        throw new InternalServerException("Something went wrong while fetching Customer Entity Types");
	    }
	}
}