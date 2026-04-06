package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.CustomerService.Entity.ContractEntityMapping;
import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.CustomerBranchAllocation;
import com.doritech.CustomerService.Entity.CustomerEmployeeAllocation;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.DatabaseOperationException;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.ContractEntityMappingMapper;
import com.doritech.CustomerService.Repository.ContractEntityMappingRepository;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.CustomerBranchAllocationRepository;
import com.doritech.CustomerService.Repository.CustomerEmployeeAllocationRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.ContractEntityMappingRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.ContractEntityMappingResponse;
import com.doritech.CustomerService.Service.ContractEntityMappingService;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
public class ContractEntityMappingServiceImpl
		implements
		ContractEntityMappingService {

	private static final Logger logger = LoggerFactory
			.getLogger(ContractEntityMappingServiceImpl.class);

	@Autowired
	private ContractEntityMappingRepository repository;

	@Autowired
	private ContractMasterRepository contractRepository;

	@Autowired
	private CustomerMasterRepository customerRepository;

	@Autowired
	private ContractEntityMappingMapper mapper;

	@Autowired
	private ValidationService validationService;

	@Autowired
	private CustomerBranchAllocationRepository customerBranchAllocationRepository;

	@Autowired
	private CustomerEmployeeAllocationRepository customerEmployeeAllocationRepository;

	@Override
	@Transactional
	public ResponseEntity saveOrUpdateMappings(
			List<ContractEntityMappingRequest> requests) {

		logger.info(
				"SaveOrUpdate ContractEntityMapping API called for {} requests",
				requests.size());

		if (requests == null || requests.isEmpty()) {
			logger.error("Request list is empty");
			throw new BadRequestException("Request list cannot be empty");
		}

		List<ContractEntityMappingResponse> responseList = new ArrayList<>();

		try {

			for (ContractEntityMappingRequest request : requests) {
				if (request.getMappingId() != null
						&& !repository.existsById(request.getMappingId())) {
					logger.error("Mapping not found with id {}",
							request.getMappingId());
					throw new ResourceNotFoundException(
							"Mapping not found with id "
									+ request.getMappingId());
				}
			}

			for (ContractEntityMappingRequest request : requests) {

				logger.info(
						"Processing request — mappingId: {}, contractId: {}, customerId: {}, siteId: {}",
						request.getMappingId(), request.getContractId(),
						request.getCustomerId(), request.getSiteId());

				ContractMaster contract = contractRepository
						.findById(request.getContractId()).orElseThrow(() -> {
							logger.error("Contract not found with id {}",
									request.getContractId());
							return new ResourceNotFoundException(
									"Contract not found with id "
											+ request.getContractId());
						});

				CustomerMasterEntity customer = customerRepository
						.findById(request.getCustomerId()).orElseThrow(() -> {
							logger.error("Customer not found with id {}",
									request.getCustomerId());
							return new ResourceNotFoundException(
									"Customer not found with id "
											+ request.getCustomerId());
						});

				logger.info("Validating site with id {}", request.getSiteId());
				validationService.validateSiteExists(request.getSiteId());

				logger.info(
						"Checking CustomerBranchAllocation for customerId {} and siteId {} with isActive Y",
						request.getCustomerId(), request.getSiteId());
				boolean branchAllocationExists = customerBranchAllocationRepository
						.existsByCustomer_CustomerIdAndSiteIdAndIsActive(
								request.getCustomerId(), request.getSiteId(),
								"Y");

				if (!branchAllocationExists) {
					logger.info(
							"No active CustomerBranchAllocation found for customerId {} and siteId {}, creating new entry",
							request.getCustomerId(), request.getSiteId());
					CustomerBranchAllocation branchAllocation = new CustomerBranchAllocation();
					branchAllocation.setCustomer(customer);
					branchAllocation.setSiteId(request.getSiteId());
					branchAllocation.setFromDate(request.getSiteFromDate());
					branchAllocation.setIsActive("Y");
					branchAllocation.setCreatedBy(request.getCreatedBy());
					customerBranchAllocationRepository.save(branchAllocation);
					logger.info(
							"CustomerBranchAllocation saved successfully for customerId {} and siteId {}",
							request.getCustomerId(), request.getSiteId());
				} else {
					logger.info(
							"Active CustomerBranchAllocation already exists for customerId {} and siteId {}, skipping",
							request.getCustomerId(), request.getSiteId());
				}

				logger.info("Validating employee with id {}",
						request.getEmployeeId());
				validationService
						.validateEmployeeExists(request.getEmployeeId());

				logger.info(
						"Checking CustomerEmployeeAllocation for customerId {} and employeeId {} with isActive Y",
						request.getCustomerId(), request.getEmployeeId());
				boolean employeeAllocationExists = customerEmployeeAllocationRepository
						.existsByCustomer_CustomerIdAndEmployeeIdAndIsActive(
								request.getCustomerId(),
								request.getEmployeeId(), "Y");

				if (!employeeAllocationExists) {
					logger.info(
							"No active CustomerEmployeeAllocation found for customerId {} and employeeId {}, creating new entry",
							request.getCustomerId(), request.getEmployeeId());
					CustomerEmployeeAllocation employeeAllocation = new CustomerEmployeeAllocation();
					employeeAllocation.setCustomer(customer);
					employeeAllocation.setEmployeeId(request.getEmployeeId());
					employeeAllocation
							.setFromDate(request.getEmployeeFromDate());
					employeeAllocation.setIsActive("Y");
					employeeAllocation.setCreatedBy(request.getCreatedBy());
					customerEmployeeAllocationRepository
							.save(employeeAllocation);
					logger.info(
							"CustomerEmployeeAllocation saved successfully for customerId {} and employeeId {}",
							request.getCustomerId(), request.getEmployeeId());
				} else {
					logger.info(
							"Active CustomerEmployeeAllocation already exists for customerId {} and employeeId {}, skipping",
							request.getCustomerId(), request.getEmployeeId());
				}

				if (request.getMappingId() != null
						&& repository.existsById(request.getMappingId())) {

					logger.info("Updating existing mapping with id {}",
							request.getMappingId());

					ContractEntityMapping mapping = repository
							.findById(request.getMappingId())
							.orElseThrow(() -> {
								logger.error("Mapping not found with id {}",
										request.getMappingId());
								return new ResourceNotFoundException(
										"Mapping not found with id "
												+ request.getMappingId());
							});

					mapping.setContract(contract);
					mapping.setCustomer(customer);
					mapping.setSiteId(request.getSiteId());
					mapping.setMinNoVisits(request.getMinNoVisits());
					mapping.setVisitsFrequency(request.getVisitsFrequency());
					mapping.setVisitsPaid(request.getVisitsPaid());
					mapping.setIsActive(request.getIsActive());
					mapping.setModifiedBy(request.getModifiedBy());
					mapping.setModifiedOn(LocalDateTime.now());

					repository.save(mapping);

					logger.info("Mapping updated successfully with id {}",
							request.getMappingId());
					responseList.add(mapper.toResponse(mapping));

				} else {

					logger.info("Creating new mapping for contractId {}",
							request.getContractId());

					ContractEntityMapping mapping = new ContractEntityMapping();
					mapping.setContract(contract);
					mapping.setCustomer(customer);
					mapping.setSiteId(request.getSiteId());
					mapping.setMinNoVisits(request.getMinNoVisits());
					mapping.setVisitsFrequency(request.getVisitsFrequency());
					mapping.setVisitsPaid(request.getVisitsPaid());
					mapping.setIsActive(request.getIsActive());
					mapping.setCreatedBy(request.getCreatedBy());
					mapping.setCreatedOn(LocalDateTime.now());
					mapping.setModifiedBy(null);
					mapping.setModifiedOn(null);

					ContractEntityMapping saved;
					try {
						saved = repository.save(mapping);
					} catch (Exception ex) {
						logger.error(
								"DB error while saving mapping for contractId {}: {}",
								request.getContractId(), ex.getMessage(), ex);
						throw new DatabaseOperationException(
								"Failed to save mapping");
					}

					logger.info("Mapping created successfully with id {}",
							saved.getMappingId());
					responseList.add(mapper.toResponse(saved));
				}
			}

			logger.info("Processed total {} mappings", responseList.size());
			return new ResponseEntity("Mappings saved/updated successfully",
					200, responseList);

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn("Client error in saveOrUpdateMappings: {}",
					ex.getMessage());
			throw ex;
		} catch (ExternalServiceException | DatabaseOperationException ex) {
			logger.error("Service/DB error in saveOrUpdateMappings: {}",
					ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Unexpected error in saveOrUpdateMappings: {}",
					ex.getMessage(), ex);
			throw new DatabaseOperationException(
					"Failed to save or update mappings");
		}
	}

	@Override
	@Transactional
	public ResponseEntity updateMapping(Integer id,
			ContractEntityMappingRequest request) {

		logger.info("Update Mapping API called for id {}", id);

		if (id == null) {
			logger.error("Mapping id cannot be null");
			throw new BadRequestException("Mapping id cannot be null");
		}

		if (request == null) {
			logger.error("Request body cannot be null");
			throw new BadRequestException("Request body cannot be null");
		}

		try {

			ContractEntityMapping mapping = repository.findById(id)
					.orElseThrow(() -> {
						logger.error("Mapping not found with id {}", id);
						return new ResourceNotFoundException(
								"Mapping not found with id " + id);
					});

			ContractMaster contract = contractRepository
					.findById(request.getContractId()).orElseThrow(() -> {
						logger.error("Contract not found with id {}",
								request.getContractId());
						return new ResourceNotFoundException(
								"Contract not found with id "
										+ request.getContractId());
					});

			CustomerMasterEntity customer = customerRepository
					.findById(request.getCustomerId()).orElseThrow(() -> {
						logger.error("Customer not found with id {}",
								request.getCustomerId());
						return new ResourceNotFoundException(
								"Customer not found with id "
										+ request.getCustomerId());
					});

			logger.info("Validating site with id {}", request.getSiteId());
			validationService.validateSiteExists(request.getSiteId());

			mapping.setContract(contract);
			mapping.setCustomer(customer);
			mapping.setSiteId(request.getSiteId());
			mapping.setMinNoVisits(request.getMinNoVisits());
			mapping.setVisitsFrequency(request.getVisitsFrequency());
			mapping.setVisitsPaid(request.getVisitsPaid());
			mapping.setIsActive(request.getIsActive());
			mapping.setModifiedBy(request.getModifiedBy());
			mapping.setModifiedOn(LocalDateTime.now());

			repository.save(mapping);

			logger.info("Mapping updated successfully with id {}", id);

			return new ResponseEntity("Mapping updated successfully", 200,
					mapper.toResponse(mapping));

		} catch (BadRequestException ex) {
			logger.warn(
					"BadRequestException while updating mapping with id {}: {}",
					id, ex.getMessage());
			throw ex;
		} catch (ResourceNotFoundException ex) {
			logger.warn(
					"ResourceNotFoundException while updating mapping with id {}: {}",
					id, ex.getMessage());
			throw ex;
		} catch (ExternalServiceException ex) {
			logger.error(
					"ExternalServiceException while updating mapping with id {}: {}",
					id, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error(
					"Unexpected error while updating mapping with id {}: {}",
					id, ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to update mapping");
		}
	}

	@Override
	public ResponseEntity getMappingById(Integer id) {

		logger.info("Get Mapping By ID API called {}", id);

		if (id == null) {
			logger.error("Mapping id cannot be null");
			throw new BadRequestException("Mapping id cannot be null");
		}

		try {

			ContractEntityMapping mapping = repository.findById(id)
					.orElseThrow(() -> {
						logger.error("Mapping not found with id {}", id);
						return new ResourceNotFoundException(
								"Mapping not found with id " + id);
					});

			ContractEntityMappingResponse response = mapper.toResponse(mapping);

			try {
				logger.info("Fetching site details for siteId {}",
						mapping.getSiteId());

				CompSiteResponse site = validationService
						.validateAndGetSite(mapping.getSiteId(), "Source");

				response.setSiteId(site.getSiteId());
				response.setSiteName(site.getSiteName());

			} catch (Exception ex) {
				logger.warn("Unable to fetch site details for siteId {}: {}",
						mapping.getSiteId(), ex.getMessage());
				response.setSiteName("Unknown Site");
			}

			try {

				if (mapping.getCustomer() != null) {

					Integer customerId = mapping.getCustomer().getCustomerId();

					logger.info("Fetching customer details for customerId {}",
							customerId);

					CustomerMasterEntity customer = customerRepository
							.findById(customerId)
							.orElseThrow(() -> new ResourceNotFoundException(
									"Customer not found with id "
											+ customerId));

					response.setCustomerId(customer.getCustomerId());
					response.setCustomerName(customer.getCustomerName());

				} else {
					logger.warn("Customer is null in mapping id {}", id);
					response.setCustomerName("Unknown Customer");
				}

			} catch (ResourceNotFoundException ex) {
				logger.warn("Customer not found: {}", ex.getMessage());
				response.setCustomerName("Unknown Customer");

			} catch (Exception ex) {
				logger.error("Error fetching customer", ex);
				response.setCustomerName("Unknown Customer");
			}

			logger.info("Mapping fetched successfully {}", id);

			return new ResponseEntity("Success", 200, response);

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn("Error in getMappingById: {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {
			logger.error("Unexpected error in getMappingById for id {}", id,
					ex);
			throw new DatabaseOperationException("Failed to fetch mapping");
		}
	}

	@Override
	public ResponseEntity getAllMappings(int page, int size) {

		try {
			logger.info("getAllMappings API hit with page {} and size {}", page,
					size);

			Pageable pageable = PageRequest.of(page, size,
					Sort.by("mappingId").descending());
			Page<ContractEntityMapping> pageResult = repository
					.findAll(pageable);

			List<ContractEntityMapping> mappings = pageResult.getContent();

			Map<Integer, String> siteCache = new HashMap<>();
			Map<Integer, String> customerCache = new HashMap<>();

			List<ContractEntityMappingResponse> response = new ArrayList<>();

			for (ContractEntityMapping mapping : mappings) {

				ContractEntityMappingResponse res = mapper.toResponse(mapping);

				try {
					Integer siteId = mapping.getSiteId();

					if (siteCache.containsKey(siteId)) {
						res.setSiteName(siteCache.get(siteId));
					} else {
						CompSiteResponse site = validationService
								.validateAndGetSite(siteId, "Source");
						siteCache.put(siteId, site.getSiteName());
						res.setSiteName(site.getSiteName());
					}

				} catch (Exception ex) {
					logger.warn("Site fetch failed for siteId {}",
							mapping.getSiteId());
					res.setSiteName("Unknown Site");
				}

				try {
					if (mapping.getCustomer() != null) {

						Integer customerId = mapping.getCustomer()
								.getCustomerId();

						if (customerCache.containsKey(customerId)) {
							res.setCustomerName(customerCache.get(customerId));
						} else {
							CustomerMasterEntity customer = customerRepository
									.findById(customerId).orElse(null);

							if (customer != null) {
								customerCache.put(customerId,
										customer.getCustomerName());
								res.setCustomerName(customer.getCustomerName());
							} else {
								res.setCustomerName("Unknown Customer");
							}
						}

					} else {
						res.setCustomerName("Unknown Customer");
					}

				} catch (Exception ex) {
					logger.error("Customer fetch failed for mappingId {}",
							mapping.getMappingId(), ex);
					res.setCustomerName("Unknown Customer");
				}

				response.add(res);
			}

			logger.info("Total mappings fetched in this page {}: {}",
					pageResult.getNumber(), response.size());

			Map<String, Object> payload = new HashMap<>();
			payload.put("data", response);
			payload.put("currentPage", pageResult.getNumber());
			payload.put("totalItems", pageResult.getTotalElements());
			payload.put("totalPages", pageResult.getTotalPages());

			return new ResponseEntity("Success", 200, payload);

		} catch (DataAccessException dae) {
			logger.error("Database error while fetching mappings", dae);
			return new ResponseEntity("Database error", 500, null);

		} catch (Exception ex) {
			logger.error("Unexpected error while fetching mappings", ex);
			return new ResponseEntity("Failed to fetch mappings", 500, null);
		}
	}

	@Override
	@Transactional
	public ResponseEntity deactivateMapping(Integer id) {

		logger.info("Deactivate Mapping API called {}", id);

		if (id == null) {
			throw new BadRequestException("Mapping id cannot be null");
		}

		try {

			ContractEntityMapping mapping = repository.findById(id)
					.orElseThrow(() -> {
						logger.error("Mapping not found with id {}", id);
						return new ResourceNotFoundException(
								"Mapping not found with id " + id);
					});

			mapping.setIsActive("N");
			mapping.setModifiedOn(LocalDateTime.now());

			repository.save(mapping);

			logger.info("Mapping deactivated successfully {}", id);

			return new ResponseEntity("Mapping deactivated successfully", 200,
					null);

		} catch (ResourceNotFoundException ex) {

			throw ex;

		} catch (Exception ex) {

			logger.error("Error while deactivating mapping", ex);
			throw new DatabaseOperationException(
					"Failed to deactivate mapping");
		}
	}

	@Override
	public ResponseEntity getMappingByContractId(Integer id) {

		logger.info("Get Mapping By ContractId API called {}", id);

		if (id == null) {
			logger.error("Contract id is null");
			throw new BadRequestException("Contract id cannot be null");
		}

		try {

			List<ContractEntityMapping> mappings = repository
					.findByContractContractId(id);

			if (mappings == null || mappings.isEmpty()) {
				logger.error("No mapping found with Contract id {}", id);
				throw new ResourceNotFoundException(
						"Mapping not found with Contract id " + id);
			}

			Map<Integer, String> siteCache = new HashMap<>();
			Map<Integer, String> customerCache = new HashMap<>();

			List<ContractEntityMappingResponse> responseList = new ArrayList<>();

			for (ContractEntityMapping mapping : mappings) {

				ContractEntityMappingResponse res = mapper.toResponse(mapping);

				try {
					Integer siteId = mapping.getSiteId();

					if (siteCache.containsKey(siteId)) {
						res.setSiteName(siteCache.get(siteId));
					} else {
						CompSiteResponse site = validationService
								.validateAndGetSite(siteId, "Source");
						siteCache.put(siteId, site.getSiteName());
						res.setSiteName(site.getSiteName());
					}

				} catch (Exception ex) {
					logger.warn("Site fetch failed for siteId {}",
							mapping.getSiteId());
					res.setSiteName("Unknown Site");
				}

				try {
					if (mapping.getCustomer() != null) {

						Integer customerId = mapping.getCustomer()
								.getCustomerId();

						if (customerCache.containsKey(customerId)) {
							res.setCustomerName(customerCache.get(customerId));
						} else {
							CustomerMasterEntity customer = customerRepository
									.findById(customerId).orElse(null);

							if (customer != null) {
								customerCache.put(customerId,
										customer.getCustomerName());
								res.setCustomerName(customer.getCustomerName());
							} else {
								res.setCustomerName("Unknown Customer");
							}
						}

					} else {
						res.setCustomerName("Unknown Customer");
					}

				} catch (Exception ex) {
					logger.error("Customer fetch failed for mappingId {}",
							mapping.getMappingId(), ex);
					res.setCustomerName("Unknown Customer");
				}

				responseList.add(res);
			}

			logger.info("Mapping fetched successfully for contract id {}", id);

			return new ResponseEntity("Success", 200, responseList);

		} catch (BadRequestException | ResourceNotFoundException ex) {

			logger.error("Validation error while fetching mapping: {}",
					ex.getMessage());
			throw ex;

		} catch (Exception ex) {

			logger.error("Error while fetching mapping for contract id {}", id,
					ex);
			throw new DatabaseOperationException("Failed to fetch mapping");
		}
	}

	@Override
	@Transactional
	public ResponseEntity deactivateBulkContractEntity(List<Integer> ids) {

		logger.info("Bulk deactivate ContractEntityMapping API called with ids {}", ids);

		if (ids == null || ids.isEmpty()) {
			throw new BadRequestException("Mapping ids cannot be null or empty");
		}

		try {

			List<ContractEntityMapping> mappings = repository.findAllById(ids);

			if (mappings.isEmpty()) {
				logger.error("No mappings found for ids {}", ids);
				throw new ResourceNotFoundException("No mappings found for given ids");
			}

			LocalDateTime now = LocalDateTime.now();

			mappings.forEach(mapping -> {
				mapping.setIsActive("N");
				mapping.setModifiedOn(now);
			});

			repository.saveAll(mappings);

			logger.info("Bulk mappings deactivated successfully for ids {}", ids);

			return new ResponseEntity("Mappings deactivated successfully", 200, null);

		} catch (ResourceNotFoundException ex) {
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while bulk deactivating mappings for ids {}", ids, ex);
			throw new DatabaseOperationException("Failed to deactivate mappings");
		}
	}
}