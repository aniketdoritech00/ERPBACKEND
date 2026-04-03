package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.DatabaseOperationException;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Projection.CustomerSummaryProjection;
import com.doritech.CustomerService.Projection.CustomerWithContactProjection;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.CustomerRequest;
import com.doritech.CustomerService.Response.CustomerContactResponse;
import com.doritech.CustomerService.Response.CustomerDetailsResponse;
import com.doritech.CustomerService.Response.CustomerResponse;
import com.doritech.CustomerService.Service.CustomerService;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomerServiceImpl.class);

	private final CustomerMasterRepository customerRepo;

	public CustomerServiceImpl(CustomerMasterRepository customerRepo) {
		this.customerRepo = customerRepo;
	}

	@Autowired
	private ValidationService validationService;

	@Override
	@Transactional
	public ResponseEntity createCustomer(CustomerRequest request) {

		logger.info("Create/Update Customer API called");

		try {

			if (request == null) {
				logger.error("Customer request is null");
				return new ResponseEntity("Invalid Request", 400, null);
			}

			if (request.getCustomerCode() == null
					|| request.getCustomerCode().trim().isEmpty()) {
				logger.error("Customer code is empty");
				return new ResponseEntity("Customer Code is required", 400,
						null);
			}

			CustomerMasterEntity customer;
			boolean isUpdate = false;

			if (request.getCustomerId() != null) {

				customer = customerRepo.findById(request.getCustomerId())
						.orElseThrow(() -> {
							logger.error("Customer not found with id {}",
									request.getCustomerId());
							return new ResourceNotFoundException(
									"Customer not found with id "
											+ request.getCustomerId());
						});

				isUpdate = true;

				Optional<CustomerMasterEntity> duplicate = customerRepo
						.findByCustomerCodeIgnoreCase(
								request.getCustomerCode());

				if (duplicate.isPresent() && !duplicate.get().getCustomerId()
						.equals(request.getCustomerId())) {
					logger.error("Customer code already exists: {}",
							request.getCustomerCode());
					return new ResponseEntity("Customer Code Already Exists",
							409, null);
				}

				customer.setModifiedBy(request.getModifiedBy());
				customer.setModifiedOn(LocalDateTime.now());
				logger.info("Updating existing customer with id {}",
						request.getCustomerId());

			} else {

				Optional<CustomerMasterEntity> existing = customerRepo
						.findByCustomerCodeIgnoreCase(
								request.getCustomerCode());

				if (existing.isPresent()) {
					logger.error("Customer code already exists: {}",
							request.getCustomerCode());
					return new ResponseEntity("Customer Code Already Exists",
							409, null);
				}

				customer = new CustomerMasterEntity();
				customer.setCreatedBy(request.getCreatedBy());
				customer.setCreatedOn(LocalDateTime.now());
				customer.setModifiedOn(null);
				customer.setModifiedBy(null);
				logger.info("Creating new customer with code {}",
						request.getCustomerCode());
			}

			if (request.getParentId() != null && request.getParentId() != 0) {

				customerRepo.findById(request.getParentId()).orElseThrow(() -> {
					logger.error("Parent customer not found with id {}",
							request.getParentId());
					return new ResourceNotFoundException(
							"Parent Customer not found with id "
									+ request.getParentId());
				});

				customer.setParentId(request.getParentId());
				logger.info("Parent customer set with id {}",
						request.getParentId());

			} else {
				customer.setParentId(null);
			}

			logger.info("Validating organization with id {}",
					request.getOrgId());
			validationService.validateAndGetOrganization(request.getOrgId());

			logger.info("Validating company with id {}", request.getCompId());
			validationService.validateAndGetCompany(request.getCompId());

			if (request.getHierarchyLevelId() != null) {
				logger.info("Validating hierarchy level with id {}",
						request.getHierarchyLevelId());
				validationService
						.validateAndGetHierarchy(request.getHierarchyLevelId());
			}

			customer.setCustomerName(request.getCustomerName());
			customer.setCustomerCode(request.getCustomerCode());
			customer.setOrgId(request.getOrgId());
			customer.setCompId(request.getCompId());
			customer.setAddress(request.getAddress());
			customer.setCity(request.getCity());
			customer.setDistrict(request.getDistrict());
			customer.setState(request.getState());
			customer.setCountry(request.getCountry());
			customer.setPostalCode(request.getPostalCode());
			customer.setGstin(request.getGstin());
			customer.setHierarchyLevelId(request.getHierarchyLevelId());
			customer.setIsActive(request.getIsActive() == null
					? "Y"
					: request.getIsActive());

			CustomerMasterEntity savedCustomer = customerRepo.save(customer);

			logger.info("Customer processed successfully with id {}",
					savedCustomer.getCustomerId());

			String message = isUpdate
					? "Customer Updated Successfully"
					: "Customer Saved Successfully";

			return new ResponseEntity(message, 200,
					buildCustomerResponse(savedCustomer));

		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while processing customer: {}",
					e.getMessage());
			return new ResponseEntity(e.getMessage(), 404, null);
		} catch (ExternalServiceException e) {
			logger.error("External service error while processing customer: {}",
					e.getMessage());
			return new ResponseEntity(e.getMessage(), 502, null);
		} catch (DataIntegrityViolationException e) {
			logger.error(
					"Data integrity violation while processing customer: {}",
					e.getMessage(), e);
			String exMsg = e.getRootCause() != null
					? e.getRootCause().getMessage()
					: e.getMessage();
			if (exMsg != null) {
				if (exMsg.contains("fk_customer_company")
						|| exMsg.contains("comp_id")) {
					return new ResponseEntity("Company ID Not Found", 404,
							null);
				} else if (exMsg.contains("fk_customer_org")
						|| exMsg.contains("org_id")) {
					return new ResponseEntity("Organization ID Not Found", 404,
							null);
				} else if (exMsg.contains("fk_customer_parent")
						|| exMsg.contains("parent_id")) {
					return new ResponseEntity("Parent Customer ID Not Found",
							404, null);
				} else if (exMsg.contains("fk_customer_hierarchy")
						|| exMsg.contains("hierarchy_level_id")) {
					return new ResponseEntity("Hierarchy Level ID Not Found",
							404, null);
				} else if (exMsg.contains("Duplicate")
						|| exMsg.contains("duplicate")) {
					return new ResponseEntity(
							"Duplicate Entry or Constraint Violation", 409,
							null);
				}
			}
			return new ResponseEntity("Duplicate Entry or Constraint Violation",
					409, null);
		} catch (TransactionSystemException e) {
			logger.error("Transaction failed while processing customer: {}",
					e.getMessage(), e);
			return new ResponseEntity("Transaction Failed", 500, null);
		} catch (DataAccessException e) {
			logger.error("Database access error while processing customer: {}",
					e.getMessage(), e);
			return new ResponseEntity("Database Operation Failed", 500, null);
		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument while processing customer: {}",
					e.getMessage(), e);
			return new ResponseEntity(e.getMessage(), 400, null);
		} catch (Exception e) {
			logger.error("Unexpected error while processing customer: {}",
					e.getMessage(), e);
			return new ResponseEntity("Internal Server Error", 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity updateCustomer(Integer id, CustomerRequest request) {

		logger.info("Update API called for ID {}", id);

		try {

			if (id == null) {
				logger.error("Customer ID is null in updateCustomer");
				throw new BadRequestException("Customer ID is required");
			}

			if (request == null) {
				logger.error("Customer request is null for ID {}", id);
				throw new BadRequestException("Request body is required");
			}

			CustomerMasterEntity customer = customerRepo.findById(id)
					.orElseThrow(() -> {
						logger.error("Customer not found with ID {}", id);
						return new ResourceNotFoundException(
								"Customer Not Found with ID " + id);
					});

			customer.setCustomerName(request.getCustomerName());
			customer.setAddress(request.getAddress());
			customer.setCity(request.getCity());
			customer.setDistrict(request.getDistrict());
			customer.setState(request.getState());
			customer.setCountry(request.getCountry());
			customer.setPostalCode(request.getPostalCode());
			customer.setGstin(request.getGstin());
			customer.setModifiedBy(request.getModifiedBy());
			customer.setIsActive(request.getIsActive());
			BeanUtils.copyProperties(request, customer);

			CustomerMasterEntity updated = customerRepo.save(customer);

			logger.info("Customer updated successfully for ID {}", id);

			return new ResponseEntity("Updated Successfully", 200, updated);

		} catch (BadRequestException | ResourceNotFoundException e) {
			logger.warn("Validation error in updateCustomer for ID {}: {}", id,
					e.getMessage());
			throw e;
		} catch (DataIntegrityViolationException e) {
			logger.error(
					"Data integrity violation in updateCustomer for ID {}: {}",
					id, e.getMessage(), e);
			return new ResponseEntity("Duplicate Entry or Constraint Violation",
					409, null);
		} catch (TransactionSystemException e) {
			logger.error("Transaction failed in updateCustomer for ID {}: {}",
					id, e.getMessage(), e);
			return new ResponseEntity("Transaction Failed", 500, null);
		} catch (DataAccessException e) {
			logger.error(
					"Database access error in updateCustomer for ID {}: {}", id,
					e.getMessage(), e);
			return new ResponseEntity("Database Operation Failed", 500, null);
		} catch (Exception e) {
			logger.error("Unexpected error in updateCustomer for ID {}: {}", id,
					e.getMessage(), e);
			return new ResponseEntity("Internal Server Error", 500, null);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllCustomers(int page, int size) {

		logger.info("Get All Customers called for page {} and size {}", page,
				size);

		if (page < 0) {
			logger.error("Invalid page number: {}", page);
			throw new BadRequestException("Page number cannot be negative");
		}

		if (size <= 0) {
			logger.error("Invalid page size: {}", size);
			throw new BadRequestException(
					"Page size must be greater than zero");
		}

		try {

			long totalItems = customerRepo.countAllCustomers();

			if (totalItems == 0) {
				logger.warn("No customers found");
				throw new ResourceNotFoundException("No Customers Found");
			}

			Pageable pageable = PageRequest.of(page, size,
					Sort.by("customerId").ascending());

			List<CustomerSummaryProjection> rows = customerRepo
					.findAllCustomersSummary(pageable);

			List<CustomerResponse> responseList = rows.stream().map(row -> {
				CustomerResponse res = new CustomerResponse();
				res.setCustomerId(row.getCustomerId());
				res.setCustomerName(row.getCustomerName());
				res.setCustomerCode(row.getCustomerCode());
				res.setOrgId(row.getOrgId());
				res.setCompId(row.getCompId());
				res.setAddress(row.getAddress());
				res.setCity(row.getCity());
				res.setDistrict(row.getDistrict());
				res.setState(row.getState());
				res.setCountry(row.getCountry());
				res.setPostalCode(row.getPostalCode());
				res.setGstin(row.getGstin());
				res.setParentId(row.getParentId());
				res.setHierarchyLevelId(row.getHierarchyLevelId());
				res.setIsActive(row.getIsActive());
				res.setCreatedOn(row.getCreatedOn());
				res.setModifiedOn(row.getModifiedOn());
				res.setCreatedBy(row.getCreatedBy());
				res.setModifiedBy(row.getModifiedBy());
				res.setOrgName(row.getOrgName());
				res.setCompanyName(row.getCompanyName());
				res.setCompanyCode(row.getCompanyCode());
				res.setHierarchyName(row.getHierarchyName());
				return res;
			}).toList();

			long totalPages = (long) Math.ceil((double) totalItems / size);

			Map<String, Object> map = new HashMap<>();
			map.put("content", responseList);
			map.put("currentPage", page);
			map.put("pageSize", size);
			map.put("totalItems", totalItems);
			map.put("totalPages", totalPages);

			logger.info("Customers fetched successfully for page {}. Count: {}",
					page, responseList.size());

			return new ResponseEntity("Success", 200, map);

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn("Validation error while fetching customers: {}",
					ex.getMessage());
			throw ex;
		} catch (DataAccessException ex) {
			logger.error(
					"Database access error while fetching all customers: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to fetch customers");
		} catch (Exception ex) {
			logger.error("Unexpected error while fetching all customers: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to fetch customers");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getCustomerById(Integer id) {

		logger.info("Get Customer By ID {}", id);

		if (id == null) {
			logger.error("Customer ID is null in getCustomerById");
			throw new BadRequestException("Customer ID is required");
		}

		try {

			List<CustomerWithContactProjection> rows = customerRepo
					.findCustomerWithDetailsById(id);

			if (rows == null || rows.isEmpty()) {
				logger.error("Customer not found with id {}", id);
				throw new ResourceNotFoundException(
						"Customer Not Found with ID " + id);
			}

			CustomerDetailsResponse res = buildDetailsFromProjection(rows);

			logger.info("Customer fetched successfully for id {}", id);

			return new ResponseEntity("Success", 200, res);

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn("Validation error while fetching customer ID {}: {}",
					id, ex.getMessage());
			throw ex;
		} catch (DataAccessException ex) {
			logger.error(
					"Database access error while fetching customer ID {}: {}",
					id, ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to fetch customer");
		} catch (Exception ex) {
			logger.error(
					"Unexpected error while fetching customer for id {}: {}",
					id, ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to fetch customer");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity filterCustomers(String name, String status,
			String entityType) {

		logger.info(
				"Filter Customers API called with name={}, status={}, entityType={}",
				name, status, entityType);

		try {

			List<CustomerWithContactProjection> rows = customerRepo
					.findCustomersWithDetailsByFilter(name, status, entityType);

			if (rows == null || rows.isEmpty()) {
				logger.warn(
						"No customers found with filters: name={}, status={}, entityType={}",
						name, status, entityType);
				throw new ResourceNotFoundException("No Customers Found");
			}

			Map<Integer, CustomerDetailsResponse> grouped = new LinkedHashMap<>();

			for (CustomerWithContactProjection row : rows) {

				Integer customerId = row.getCustomerId();

				CustomerDetailsResponse res = grouped
						.computeIfAbsent(customerId, cid -> {
							CustomerDetailsResponse d = new CustomerDetailsResponse();
							d.setCustomerId(row.getCustomerId());
							d.setCustomerName(row.getCustomerName());
							d.setCustomerCode(row.getCustomerCode());
							d.setOrgId(row.getOrgId());
							d.setCompId(row.getCompId());
							d.setAddress(row.getAddress());
							d.setCity(row.getCity());
							d.setDistrict(row.getDistrict());
							d.setState(row.getState());
							d.setCountry(row.getCountry());
							d.setPostalCode(row.getPostalCode());
							d.setGstin(row.getGstin());
							d.setParentId(row.getParentId());
							d.setHierarchyLevelId(row.getHierarchyLevelId());
							d.setIsActive(row.getIsActive());
							d.setCreatedBy(row.getCreatedBy());
							d.setOrgName(row.getOrgName());
							d.setCompanyName(row.getCompanyName());
							d.setCompanyCode(row.getCompanyCode());
							d.setHierarchyName(row.getHierarchyName());
							d.setCustEntityId(row.getCustEntityId());
							d.setEntityType(row.getEntityType());
							d.setEntityTypeIsActive(
									row.getEntityTypeIsActive());
							d.setContacts(new ArrayList<>());
							return d;
						});

				if (row.getCustContId() != null) {
					res.getContacts()
							.add(buildContactResponse(row, customerId));
				}
			}

			List<CustomerDetailsResponse> responseList = new ArrayList<>(
					grouped.values());

			logger.info("Filter customers returned {} results",
					responseList.size());

			return new ResponseEntity("Success", 200, responseList);

		} catch (ResourceNotFoundException ex) {
			logger.warn("No customers found during filter: {}",
					ex.getMessage());
			throw ex;
		} catch (DataAccessException ex) {
			logger.error("Database access error in filterCustomers: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to filter customers");
		} catch (Exception ex) {
			logger.error("Unexpected error in filterCustomers: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to filter customers");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getParentCustomerNames() {

		logger.info("Get Parent Customer Names API called");

		try {

			List<CustomerMasterEntity> customers = customerRepo
					.findAllActiveParentCustomers();

			if (customers == null || customers.isEmpty()) {
				logger.warn("No parent customers found");
				throw new ResourceNotFoundException(
						"No Parent Customers Found");
			}

			List<CustomerResponse> responseList = customers.stream().map(c -> {
				CustomerResponse response = new CustomerResponse();
				response.setCustomerId(c.getCustomerId());
				response.setCustomerName(c.getCustomerName());
				response.setCustomerCode(c.getCustomerCode());
				return response;
			}).toList();

			logger.info("Parent customers fetched successfully. Count: {}",
					responseList.size());

			return new ResponseEntity("Success", 200, responseList);

		} catch (ResourceNotFoundException ex) {
			logger.warn("No parent customers found: {}", ex.getMessage());
			throw ex;
		} catch (DataAccessException ex) {
			logger.error(
					"Database access error while fetching parent customer names: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException(
					"Failed to fetch parent customer names");
		} catch (Exception ex) {
			logger.error(
					"Unexpected error while fetching parent customer names: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException(
					"Failed to fetch parent customer names");
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteCustomer(List<Integer> ids) {

		logger.info("Delete Customer API called for IDs {}", ids);

		if (ids == null || ids.isEmpty()) {
			logger.error(
					"Customer IDs list is null or empty in deleteCustomer");
			throw new BadRequestException("Customer IDs are required");
		}

		try {

			int deleteCount = 0;

			for (Integer id : ids) {
				try {
					CustomerMasterEntity customer = customerRepo.findById(id)
							.orElseThrow(() -> {
								logger.error(
										"Customer not found with ID {} during delete",
										id);
								return new ResourceNotFoundException(
										"Customer Not Found with ID " + id);
							});

					if (!"N".equalsIgnoreCase(customer.getIsActive())) {
						customer.setIsActive("N");
						customerRepo.save(customer);
						deleteCount++;
						logger.info("Customer soft deleted for ID {}", id);
					} else {
						logger.warn(
								"Customer with ID {} is already inactive, skipping",
								id);
					}

				} catch (ResourceNotFoundException ex) {
					logger.warn("Skipping delete for ID {}: {}", id,
							ex.getMessage());
					throw ex;
				}
			}

			logger.info("Customers soft deleted successfully. Count: {}",
					deleteCount);

			return new ResponseEntity(
					deleteCount + " Customer(s) Deleted Successfully", 200,
					null);

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn("Validation error in deleteCustomer: {}",
					ex.getMessage());
			throw ex;
		} catch (DataIntegrityViolationException ex) {
			logger.error("Data integrity violation during delete: {}",
					ex.getMessage(), ex);
			return new ResponseEntity(
					"Cannot delete customer due to existing references", 409,
					null);
		} catch (DataAccessException ex) {
			logger.error("Database access error during deleteCustomer: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to delete customers");
		} catch (Exception ex) {
			logger.error("Unexpected error during deleteCustomer: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to delete customers");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllCustomerNames() {

		logger.info("Get All Customer Names API called");

		try {

			List<CustomerMasterEntity> customers = customerRepo.findAll();

			if (customers.isEmpty()) {
				logger.warn("No customers found in getAllCustomerNames");
				throw new ResourceNotFoundException("No Customers Found");
			}

			List<CustomerResponse> responseList = customers.stream().filter(c -> "Y".equalsIgnoreCase(c.getIsActive()))
					.map(c -> {
						CustomerResponse res = new CustomerResponse();
						res.setCustomerId(c.getCustomerId());
						res.setCustomerName(c.getCustomerName());
						res.setCustomerCode(c.getCustomerCode());
						return res;
					}).toList();

			logger.info("Total customers fetched: {}", responseList.size());

			return new ResponseEntity("Success", 200, responseList);

		} catch (ResourceNotFoundException ex) {
			logger.warn("No customers found: {}", ex.getMessage());
			throw ex;
		} catch (DataAccessException ex) {
			logger.error("Database access error in getAllCustomerNames: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException(
					"Failed to fetch customer names");
		} catch (Exception ex) {
			logger.error("Unexpected error in getAllCustomerNames: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException(
					"Failed to fetch customer names");
		}
	}

	private CustomerResponse buildCustomerResponse(CustomerMasterEntity saved) {
		CustomerResponse response = new CustomerResponse();
		response.setCustomerId(saved.getCustomerId());
		response.setCustomerName(saved.getCustomerName());
		response.setCustomerCode(saved.getCustomerCode());
		response.setOrgId(saved.getOrgId());
		response.setCompId(saved.getCompId());
		response.setAddress(saved.getAddress());
		response.setCity(saved.getCity());
		response.setDistrict(saved.getDistrict());
		response.setState(saved.getState());
		response.setCountry(saved.getCountry());
		response.setPostalCode(saved.getPostalCode());
		response.setGstin(saved.getGstin());
		response.setParentId(saved.getParentId());
		response.setHierarchyLevelId(saved.getHierarchyLevelId());
		response.setIsActive(saved.getIsActive());
		response.setCreatedOn(saved.getCreatedOn());
		response.setModifiedOn(saved.getModifiedOn());
		response.setCreatedBy(saved.getCreatedBy());
		response.setModifiedBy(saved.getModifiedBy());
		return response;
	}

	private CustomerDetailsResponse buildDetailsFromProjection(
			List<CustomerWithContactProjection> rows) {

		CustomerWithContactProjection first = rows.get(0);

		CustomerDetailsResponse res = new CustomerDetailsResponse();
		res.setCustomerId(first.getCustomerId());
		res.setCustomerName(first.getCustomerName());
		res.setCustomerCode(first.getCustomerCode());
		res.setOrgId(first.getOrgId());
		res.setCompId(first.getCompId());
		res.setAddress(first.getAddress());
		res.setCity(first.getCity());
		res.setDistrict(first.getDistrict());
		res.setState(first.getState());
		res.setCountry(first.getCountry());
		res.setPostalCode(first.getPostalCode());
		res.setGstin(first.getGstin());
		res.setParentId(first.getParentId());
		res.setHierarchyLevelId(first.getHierarchyLevelId());
		res.setIsActive(first.getIsActive());
		res.setCreatedBy(first.getCreatedBy());
		res.setOrgName(first.getOrgName());
		res.setCompanyName(first.getCompanyName());
		res.setCompanyCode(first.getCompanyCode());
		res.setHierarchyName(first.getHierarchyName());
		res.setCustEntityId(first.getCustEntityId());
		res.setEntityType(first.getEntityType());
		res.setEntityTypeIsActive(first.getEntityTypeIsActive());

		List<CustomerContactResponse> contacts = new ArrayList<>();

		for (CustomerWithContactProjection row : rows) {
			if (row.getCustContId() != null) {
				contacts.add(buildContactResponse(row, first.getCustomerId()));
			}
		}

		res.setContacts(contacts);
		return res;
	}

	private CustomerContactResponse buildContactResponse(
			CustomerWithContactProjection row, Integer customerId) {
		CustomerContactResponse contactRes = new CustomerContactResponse();
		contactRes.setCustContId(row.getCustContId());
		contactRes.setCustomerId(customerId);
		contactRes.setContactPerson(row.getContactPerson());
		contactRes.setEmail(row.getEmail());
		contactRes.setPhone(row.getPhone());
		contactRes.setDesignation(row.getDesignation());
		contactRes.setRole(row.getRole());
		contactRes.setDepartment(row.getDepartment());
		contactRes.setIsActive(row.getContactIsActive());
		contactRes.setCreatedOn(row.getContactCreatedOn());
		contactRes.setModifiedOn(row.getContactModifiedOn());
		contactRes.setCreatedBy(row.getContactCreatedBy());
		contactRes.setModifiedBy(row.getContactModifiedBy());
		return contactRes;
	}

	public ResponseEntity getCustomerDistrict(Integer customerId) {
		if (customerId == null) {
			return new ResponseEntity("Customer ID must not be null", 400,
					null);
		}

		CustomerMasterEntity customer = customerRepo
				.findByCustomerId(customerId);
		if (customer == null) {
			return new ResponseEntity("Customer not found", 404, null);
		}

		String district = customer.getDistrict();
		return new ResponseEntity("District fetched successfully", 200,
				district);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getCustomerByHierarchy(Integer customerId) {

		logger.info("Get Customers By CustomerId called: {}", customerId);

		if (customerId == null) {
			logger.error("CustomerId cannot be null");
			throw new BadRequestException("CustomerId is required");
		}

		try {

			CustomerMasterEntity customer = customerRepo.findById(customerId)
					.orElseThrow(() -> {
						logger.error("Customer not found for id {}",
								customerId);
						return new ResourceNotFoundException(
								"Customer not found");
					});

			Integer orgId = customer.getOrgId();
			Integer levelId = customer.getHierarchyLevelId();

			logger.info("Fetched orgId {} and hierarchyLevelId {}", orgId,
					levelId);

			List<CustomerSummaryProjection> rows = customerRepo
					.findByOrgAndHierarchy(orgId, levelId);

			if (rows.isEmpty()) {
				logger.warn("No customers found for orgId {} and level {}",
						orgId, levelId);
				throw new ResourceNotFoundException("No customers found");
			}

			List<CustomerResponse> responseList = rows.stream().map(row -> {
				CustomerResponse res = new CustomerResponse();
				res.setCustomerId(row.getCustomerId());
				res.setCustomerName(row.getCustomerName());
				res.setCustomerCode(row.getCustomerCode());
				res.setOrgId(row.getOrgId());
				res.setCompId(row.getCompId());
				res.setAddress(row.getAddress());
				res.setCity(row.getCity());
				res.setDistrict(row.getDistrict());
				res.setState(row.getState());
				res.setCountry(row.getCountry());
				res.setPostalCode(row.getPostalCode());
				res.setGstin(row.getGstin());
				res.setParentId(row.getParentId());
				res.setHierarchyLevelId(row.getHierarchyLevelId());
				res.setIsActive(row.getIsActive());
				res.setCreatedOn(row.getCreatedOn());
				res.setModifiedOn(row.getModifiedOn());
				res.setCreatedBy(row.getCreatedBy());
				res.setModifiedBy(row.getModifiedBy());
				return res;
			}).toList();

			logger.info("Filtered customers count: {}", responseList.size());

			return new ResponseEntity("Success", 200, responseList);

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn("Validation error: {}", ex.getMessage());
			throw ex;
		} catch (DataAccessException ex) {
			logger.error("Database error: {}", ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to fetch customers");
		} catch (Exception ex) {
			logger.error("Unexpected error: {}", ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to fetch customers");
		}
	}

}