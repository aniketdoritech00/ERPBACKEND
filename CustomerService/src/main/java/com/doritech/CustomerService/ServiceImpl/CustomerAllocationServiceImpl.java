package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.CustomerAllocationEntity;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.CustomerAllocationRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.CustomerAllocationRequest;
import com.doritech.CustomerService.Response.CustomerAllocationResponse;
import com.doritech.CustomerService.Response.EmployeeDTO;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Service.CustomerAllocationService;
import com.doritech.CustomerService.Specification.CustomerAllocationSpecification;
import com.doritech.CustomerService.ValidationService.ValidationService;

import jakarta.transaction.Transactional;

@Service
public class CustomerAllocationServiceImpl implements CustomerAllocationService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerAllocationServiceImpl.class);

	@Autowired
	private CustomerAllocationRepository customerAllocationRepository;

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	@Autowired
	private ValidationService validationService;

	@Override
	@Transactional
	public ResponseEntity saveCustomerAllocation(CustomerAllocationRequest request) {

		logger.info("Save customer allocation service started");

		ResponseEntity response = new ResponseEntity();

		try {

			if (request == null) {
				logger.error("Request body cannot be null");
				response.setStatusCode(400);
				response.setMessage("Request body cannot be null");
				return response;
			}

			if (request.getCustomerId() == null || request.getEmployeeId() == null) {
				logger.error("Customer ID and Employee ID are required");
				response.setStatusCode(400);
				response.setMessage("Customer ID and Employee ID are required");
				return response;
			}

			List<CustomerAllocationEntity> existingAllocations = customerAllocationRepository
					.findByCustomer_CustomerId(request.getCustomerId());

			if (existingAllocations != null && !existingAllocations.isEmpty()) {

				boolean sameEmpSameDateExists = existingAllocations.stream()
						.anyMatch(e -> e.getEmployeeId().equals(request.getEmployeeId()) && e.getFromDate() != null
								&& e.getFromDate().equals(request.getFromDate()));

				if (sameEmpSameDateExists) {
					logger.error("Customer, Employee and FromDate combination already exists");
					response.setStatusCode(409);
					response.setMessage("Customer, Employee and FromDate combination already exists");
					return response;
				}

				LocalDate latestDate = existingAllocations.stream().map(CustomerAllocationEntity::getFromDate)
						.filter(Objects::nonNull).max(LocalDate::compareTo)
						.orElseThrow(() -> new IllegalStateException("Unable to determine latest date"));

				if (request.getFromDate() == null) {
					throw new IllegalArgumentException("From date is required");
				}

				if (!request.getFromDate().isAfter(latestDate)) {
					throw new IllegalArgumentException("From date must be greater than " + latestDate);
				}
			}

			logger.info("Validating employee with id {}", request.getEmployeeId());
			EmployeeDTO emp = validationService.validateEmployeeExists(request.getEmployeeId());

			logger.info("Validating customer with id {}", request.getCustomerId());
			CustomerMasterEntity customer = customerMasterRepository.findById(request.getCustomerId()).orElseThrow(
					() -> new ResourceNotFoundException("Customer not found with id " + request.getCustomerId()));

			CustomerAllocationEntity entity = toEntity(request, customer);
			entity.setEmployeeId(emp.getEmployeeId());
			entity.setCreatedOn(LocalDateTime.now());
			entity.setModifiedOn(null);
			entity.setModifiedBy(null);

			CustomerAllocationEntity saved = customerAllocationRepository.save(entity);

			logger.info("Customer allocation saved successfully with id {}", saved.getAllocationId());

			response.setStatusCode(201);
			response.setMessage("Data Saved Successfully");
			response.setPayload(toResponse(saved));

		} catch (IllegalArgumentException e) {
			logger.error("Validation error while saving customer allocation: {}", e.getMessage());
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
			response.setPayload(null);
		} catch (IllegalStateException e) {
			logger.error("State error while saving customer allocation: {}", e.getMessage());
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while saving customer allocation: {}", e.getMessage());
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("External service error while saving customer allocation: {}", e.getMessage());
			response.setStatusCode(502);
			response.setMessage(e.getMessage());
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error while saving customer allocation: {}", e.getMessage(), e);
			response.setStatusCode(500);
			response.setMessage("Internal Server Error");
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllCustomerAllocation(int page, int size) {

		logger.info("Fetching all customer allocations. Page: {}, Size: {}", page, size);

		ResponseEntity response = new ResponseEntity();

		try {

			Pageable pageable = PageRequest.of(page, size);
			Page<CustomerAllocationEntity> pageData = customerAllocationRepository.findAll(pageable);

			List<CustomerAllocationResponse> list = pageData.getContent().stream().map(entity -> {

				CustomerAllocationResponse res = new CustomerAllocationResponse();
				res.setAllocationId(entity.getAllocationId());
				res.setCustomerId(entity.getCustomer().getCustomerId());
				res.setCustomerName(entity.getCustomer().getCustomerName());
				res.setEmployeeId(entity.getEmployeeId());

				try {
					EmployeeDTO employee = validationService.validateEmployeeExists(entity.getEmployeeId());
					res.setEmployeeName(employee.getEmployeeName());
				} catch (Exception ex) {
					logger.warn("Could not fetch employee name for employeeId {}: {}", entity.getEmployeeId(),
							ex.getMessage());
				}

				res.setFromDate(entity.getFromDate());
				res.setCreatedBy(entity.getCreatedBy());
				res.setModifiedBy(entity.getModifiedBy());
				res.setCreatedOn(entity.getCreatedOn());
				res.setModifiedOn(entity.getModifiedOn());
				res.setIsActive(entity.getIsActive());
				return res;

			}).toList();

			PageResponse<CustomerAllocationResponse> pageResponse = new PageResponse<>();
			pageResponse.setContent(list);
			pageResponse.setPageNumber(pageData.getNumber());
			pageResponse.setPageSize(pageData.getSize());
			pageResponse.setTotalElements(pageData.getTotalElements());
			pageResponse.setTotalPages(pageData.getTotalPages());
			pageResponse.setLastPage(pageData.isLast());

			logger.info("Customer allocations fetched successfully. Total: {}", pageData.getTotalElements());

			response.setMessage("Data fetched successfully");
			response.setPayload(pageResponse);
			response.setStatusCode(200);

		} catch (Exception e) {
			logger.error("Unexpected error while fetching all customer allocations: {}", e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getCustomerAllocationById(Integer id) {

		logger.info("Fetching customer allocation by id {}", id);

		ResponseEntity response = new ResponseEntity();

		try {

			if (id == null) {
				logger.error("Allocation id cannot be null");
				response.setStatusCode(400);
				response.setMessage("Allocation id cannot be null");
				return response;
			}

			CustomerAllocationEntity entity = customerAllocationRepository.findById(id).orElseThrow(() -> {
				logger.error("Customer allocation not found with id {}", id);
				return new ResourceNotFoundException("Customer allocation not found with id " + id);
			});

			CustomerAllocationResponse data = new CustomerAllocationResponse();
			data.setAllocationId(entity.getAllocationId());
			data.setCustomerId(entity.getCustomer().getCustomerId());
			data.setCustomerName(entity.getCustomer().getCustomerName());
			data.setEmployeeId(entity.getEmployeeId());

			try {
				EmployeeDTO employee = validationService.validateEmployeeExists(entity.getEmployeeId());
				data.setEmployeeName(employee.getEmployeeName());
			} catch (Exception ex) {
				logger.warn("Could not fetch employee name for employeeId {}: {}", entity.getEmployeeId(),
						ex.getMessage());
			}

			data.setFromDate(entity.getFromDate());
			data.setCreatedBy(entity.getCreatedBy());
			data.setIsActive(entity.getIsActive());
			data.setModifiedBy(entity.getModifiedBy());
			data.setCreatedOn(entity.getCreatedOn());
			data.setModifiedOn(entity.getModifiedOn());

			logger.info("Customer allocation fetched successfully with id {}", id);

			response.setMessage("Data fetched successfully");
			response.setPayload(data);
			response.setStatusCode(200);

		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while fetching customer allocation with id {}: {}", id, e.getMessage());
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error while fetching customer allocation with id {}: {}", id, e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	@Transactional
	public ResponseEntity updateCustomerAllocationById(Integer id, CustomerAllocationRequest request) {

		logger.info("Update customer allocation service started for id {}", id);

		ResponseEntity response = new ResponseEntity();

		try {

			if (id == null) {
				logger.error("Allocation id cannot be null");
				response.setStatusCode(400);
				response.setMessage("Allocation id cannot be null");
				return response;
			}

			if (request == null) {
				logger.error("Request body cannot be null");
				response.setStatusCode(400);
				response.setMessage("Request body cannot be null");
				return response;
			}

			if (request.getCustomerId() == null || request.getEmployeeId() == null) {
				logger.error("Customer ID and Employee ID are required");
				response.setStatusCode(400);
				response.setMessage("Customer ID and Employee ID are required");
				return response;
			}

			CustomerAllocationEntity entity = customerAllocationRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Customer allocation not found with id " + id));

			List<CustomerAllocationEntity> existingAllocations = customerAllocationRepository
					.findByCustomer_CustomerId(request.getCustomerId());

			if (existingAllocations != null && !existingAllocations.isEmpty()) {

				boolean sameEmpSameDateExists = existingAllocations.stream()
						.filter(e -> !e.getAllocationId().equals(id))
						.anyMatch(e -> e.getEmployeeId().equals(request.getEmployeeId()) && e.getFromDate() != null
								&& e.getFromDate().equals(request.getFromDate()));

				if (sameEmpSameDateExists) {
					logger.error("Customer, Employee and FromDate combination already exists");
					response.setStatusCode(409);
					response.setMessage("Customer, Employee and FromDate combination already exists");
					return response;
				}

				LocalDate latestDate = existingAllocations.stream().filter(e -> !e.getAllocationId().equals(id))
						.map(CustomerAllocationEntity::getFromDate).filter(Objects::nonNull).max(LocalDate::compareTo)
						.orElse(null);

				if (latestDate != null) {

					if (request.getFromDate() == null) {
						throw new IllegalArgumentException("From date is required");
					}

					if (!request.getFromDate().isAfter(latestDate)) {
						throw new IllegalArgumentException("From date must be greater than " + latestDate);
					}
				}
			}

			logger.info("Validating employee with id {}", request.getEmployeeId());
			EmployeeDTO emp = validationService.validateEmployeeExists(request.getEmployeeId());

			logger.info("Validating customer with id {}", request.getCustomerId());
			CustomerMasterEntity customer = customerMasterRepository.findById(request.getCustomerId()).orElseThrow(
					() -> new ResourceNotFoundException("Customer not found with id " + request.getCustomerId()));

			entity.setEmployeeId(emp.getEmployeeId());
			entity.setCustomer(customer);
			entity.setIsActive(request.getIsActive());
			entity.setModifiedBy(request.getModifiedBy());
			entity.setModifiedOn(LocalDateTime.now());
			entity.setFromDate(request.getFromDate());

			CustomerAllocationEntity saved = customerAllocationRepository.save(entity);

			logger.info("Customer allocation updated successfully with id {}", id);

			response.setMessage("Data Updated Successfully");
			response.setPayload(toResponse(saved));
			response.setStatusCode(200);

		} catch (IllegalArgumentException e) {
			logger.error("Validation error while updating customer allocation with id {}: {}", id, e.getMessage());
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while updating customer allocation with id {}: {}", id, e.getMessage());
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("External service error while updating customer allocation with id {}: {}", id,
					e.getMessage());
			response.setStatusCode(502);
			response.setMessage(e.getMessage());
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error while updating customer allocation with id {}: {}", id, e.getMessage(), e);
			response.setStatusCode(500);
			response.setMessage("Internal Server Error");
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity deleteMultipleCustomerAllocation(List<Integer> ids) {

		logger.info("Delete multiple customer allocations service started");

		ResponseEntity response = new ResponseEntity();

		try {

			if (ids == null || ids.isEmpty()) {
				logger.error("Id list is empty");
				response.setStatusCode(400);
				response.setMessage("Please provide at least one id to delete");
				return response;
			}

			List<CustomerAllocationEntity> entities = customerAllocationRepository.findAllById(ids);

			if (entities.isEmpty()) {
				logger.error("No records found for provided ids: {}", ids);
				response.setStatusCode(404);
				response.setMessage("No records found for provided ids");
				return response;
			}

			List<Integer> foundIds = entities.stream().map(CustomerAllocationEntity::getAllocationId).toList();
			List<Integer> missingIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

			customerAllocationRepository.deleteAll(entities);

			logger.info("{} customer allocation(s) deleted successfully", entities.size());

			if (missingIds.isEmpty()) {
				response.setMessage(entities.size() + " record(s) deleted successfully");
			} else {
				logger.warn("Some ids not found during delete: {}", missingIds);
				response.setMessage(entities.size() + " record(s) deleted. Some ids not found: " + missingIds);
			}

			response.setStatusCode(200);

		} catch (Exception e) {
			logger.error("Unexpected error while deleting customer allocations: {}", e.getMessage(), e);
			response.setStatusCode(500);
			response.setMessage("Internal Server Error");
		}

		return response;
	}

	@Override
	public ResponseEntity filterCustomerAllocation(Integer customerId, Integer employeeId, LocalDate fromDate,
			Integer createdBy, Integer modifiedBy, String isActive, int page, int size) {

		logger.info("Filter customer allocation service started");

		ResponseEntity response = new ResponseEntity();

		try {

			Pageable pageable = PageRequest.of(page, size);

			Page<CustomerAllocationEntity> pageData = customerAllocationRepository
					.findAll(CustomerAllocationSpecification.filter(customerId, employeeId, fromDate, createdBy,
							modifiedBy, isActive), pageable);

			List<CustomerAllocationResponse> list = pageData.getContent().stream().map(this::toResponse).toList();

			if (list.isEmpty()) {
				logger.warn("No data found for given filter criteria");
				response.setMessage("No data found");
				response.setStatusCode(404);
				response.setPayload(null);
			} else {

				PageResponse<CustomerAllocationResponse> pageResponse = new PageResponse<>();
				pageResponse.setContent(list);
				pageResponse.setPageNumber(pageData.getNumber());
				pageResponse.setPageSize(pageData.getSize());
				pageResponse.setTotalElements(pageData.getTotalElements());
				pageResponse.setTotalPages(pageData.getTotalPages());
				pageResponse.setLastPage(pageData.isLast());

				logger.info("Filtered customer allocations fetched successfully. Total: {}",
						pageData.getTotalElements());

				response.setMessage("Filtered data fetched successfully");
				response.setStatusCode(200);
				response.setPayload(pageResponse);
			}

		} catch (Exception e) {
			logger.error("Unexpected error while filtering customer allocations: {}", e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	public CustomerAllocationEntity toEntity(CustomerAllocationRequest request, CustomerMasterEntity customer) {
		CustomerAllocationEntity entity = new CustomerAllocationEntity();
		entity.setCustomer(customer);
		entity.setEmployeeId(request.getEmployeeId());
		entity.setIsActive(request.getIsActive());
		entity.setFromDate(request.getFromDate());
		entity.setCreatedBy(request.getCreatedBy());
		return entity;
	}

	public CustomerAllocationResponse toResponse(CustomerAllocationEntity entity) {
		CustomerAllocationResponse response = new CustomerAllocationResponse();
		response.setAllocationId(entity.getAllocationId());
		response.setCustomerId(entity.getCustomer().getCustomerId());
		response.setEmployeeId(entity.getEmployeeId());
		response.setCustomerName(entity.getCustomer().getCustomerName());
		response.setFromDate(entity.getFromDate());
		response.setIsActive(entity.getIsActive());
		response.setCreatedBy(entity.getCreatedBy());
		response.setModifiedBy(entity.getModifiedBy());
		response.setCreatedOn(entity.getCreatedOn());
		response.setModifiedOn(entity.getModifiedOn());
		return response;
	}
}