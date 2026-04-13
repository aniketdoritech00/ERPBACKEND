package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.CustomerService.Entity.ContractEntityMapping;
import com.doritech.CustomerService.Entity.ContractItemMapping;
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
import com.doritech.CustomerService.Repository.ContractItemMappingRepository;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.CustomerBranchAllocationRepository;
import com.doritech.CustomerService.Repository.CustomerEmployeeAllocationRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.ContractEntityMappingRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.ContractEntityMappingResponse;
import com.doritech.CustomerService.Response.EmployeeDTO;
import com.doritech.CustomerService.Response.HierarchyLevelResponseDTO;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Response.ParamResponseDTO;
import com.doritech.CustomerService.Service.ContractEntityMappingService;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
public class ContractEntityMappingServiceImpl implements ContractEntityMappingService {

	private static final Logger logger = LoggerFactory.getLogger(ContractEntityMappingServiceImpl.class);

	@Autowired
	private ContractEntityMappingRepository repository;

	@Autowired
	private ContractMasterRepository contractRepository;

	@Autowired
	private CustomerMasterRepository customerRepository;

	@Autowired
	private ContractItemMappingRepository contractItemMappingRepository;

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
	public ResponseEntity saveOrUpdateMappings(List<ContractEntityMappingRequest> requests) {

		logger.info("SaveOrUpdate ContractEntityMapping API called for {} requests",
				requests != null ? requests.size() : 0);

		if (requests == null || requests.isEmpty()) {
			logger.error("Request list is empty");
			throw new BadRequestException("Request list cannot be empty");
		}

		List<ContractEntityMappingResponse> responseList = new ArrayList<>();

		try {

			for (ContractEntityMappingRequest request : requests) {
				if (request.getMappingId() != null && !repository.existsById(request.getMappingId())) {
					logger.error("Mapping not found with id {}", request.getMappingId());
					throw new ResourceNotFoundException("Mapping not found with id " + request.getMappingId());
				}
			}

			for (ContractEntityMappingRequest request : requests) {

				logger.info("Processing mappingId: {}, contractId: {}, customerId: {}", request.getMappingId(),
						request.getContractId(), request.getCustomerId());

				ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(() -> {
					logger.error("Contract not found {}", request.getContractId());
					return new ResourceNotFoundException("Contract not found with id " + request.getContractId());
				});

				CustomerMasterEntity customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> {
					logger.error("Customer not found {}", request.getCustomerId());
					return new ResourceNotFoundException("Customer not found with id " + request.getCustomerId());
				});

				validationService.validateSiteExists(request.getSiteId());

				CustomerBranchAllocation existingBranch = customerBranchAllocationRepository
						.findByCustomer_CustomerIdAndSiteIdAndIsActive(request.getCustomerId(), request.getSiteId(),
								"Y");

				if (existingBranch == null) {

					CustomerBranchAllocation branch = new CustomerBranchAllocation();
					branch.setCustomer(customer);
					branch.setSiteId(request.getSiteId());
					branch.setFromDate(request.getSiteFromDate());
					branch.setIsActive("Y");
					branch.setCreatedBy(request.getCreatedBy());

					customerBranchAllocationRepository.save(branch);

				} else {

					LocalDate existingDate = existingBranch.getFromDate();
					LocalDate newDate = request.getSiteFromDate();

					if (!existingDate.equals(newDate)) {

						if (!newDate.isAfter(existingDate)) {
							logger.error("Branch already assigned till {}", existingDate);
							throw new BadRequestException(
									"Site already assigned till " + existingDate + ". Please assign after this date");
						}

						existingBranch.setIsActive("N");
						customerBranchAllocationRepository.save(existingBranch);

						CustomerBranchAllocation newBranch = new CustomerBranchAllocation();
						newBranch.setCustomer(customer);
						newBranch.setSiteId(request.getSiteId());
						newBranch.setFromDate(newDate);
						newBranch.setIsActive("Y");
						newBranch.setCreatedBy(request.getCreatedBy());

						customerBranchAllocationRepository.save(newBranch);
					}
				}

				validationService.validateEmployeeExists(request.getEmployeeId());

				CustomerEmployeeAllocation existingEmployee = customerEmployeeAllocationRepository
						.findByCustomer_CustomerIdAndEmployeeIdAndIsActive(request.getCustomerId(),
								request.getEmployeeId(), "Y");

				if (existingEmployee == null) {

					CustomerEmployeeAllocation emp = new CustomerEmployeeAllocation();
					emp.setCustomer(customer);
					emp.setEmployeeId(request.getEmployeeId());
					emp.setFromDate(request.getEmployeeFromDate());
					emp.setIsActive("Y");
					emp.setCreatedBy(request.getCreatedBy());

					customerEmployeeAllocationRepository.save(emp);

				} else {

					LocalDate existingDate = existingEmployee.getFromDate();
					LocalDate newDate = request.getEmployeeFromDate();

					if (!existingDate.equals(newDate)) {

						if (!newDate.isAfter(existingDate)) {
							logger.error("Employee already assigned till {}", existingDate);
							throw new BadRequestException(
									"Employee already assigned till " + existingDate + ". Assign after this date");
						}

						existingEmployee.setIsActive("N");
						customerEmployeeAllocationRepository.save(existingEmployee);

						CustomerEmployeeAllocation newEmp = new CustomerEmployeeAllocation();
						newEmp.setCustomer(customer);
						newEmp.setEmployeeId(request.getEmployeeId());
						newEmp.setFromDate(newDate);
						newEmp.setIsActive("Y");
						newEmp.setCreatedBy(request.getCreatedBy());

						customerEmployeeAllocationRepository.save(newEmp);
					}
				}

				ContractEntityMapping mapping;

				if (request.getMappingId() != null) {

					mapping = repository.findById(request.getMappingId()).orElseThrow(
							() -> new ResourceNotFoundException("Mapping not found with id " + request.getMappingId()));

					mapping.setModifiedBy(request.getModifiedBy());
					mapping.setModifiedOn(LocalDateTime.now());

				} else {

					mapping = new ContractEntityMapping();
					mapping.setCreatedBy(request.getCreatedBy());
					mapping.setCreatedOn(LocalDateTime.now());
				}

				mapping.setContract(contract);
				mapping.setCustomer(customer);
				mapping.setSiteId(request.getSiteId());
				mapping.setMinNoVisits(request.getMinNoVisits());
				mapping.setVisitsFrequency(request.getVisitsFrequency());
				mapping.setVisitsPaid(request.getVisitsPaid());
				mapping.setIsActive(request.getIsActive());

				ContractEntityMapping saved;

				try {
					saved = repository.save(mapping);
				} catch (Exception ex) {
					logger.error("DB error while saving mapping: {}", ex.getMessage(), ex);
					throw new DatabaseOperationException("Failed to save mapping");
				}

				responseList.add(mapper.toResponse(saved));
			}

			return new ResponseEntity("Mappings saved/updated successfully", 200, responseList);

		} catch (BadRequestException | ResourceNotFoundException ex) {
			throw ex;

		} catch (ExternalServiceException | DatabaseOperationException ex) {
			throw ex;

		} catch (Exception ex) {
			throw new DatabaseOperationException("Failed to save or update mappings");
		}
	}

	@Override
	@Transactional
	public ResponseEntity updateMapping(Integer id, ContractEntityMappingRequest request) {

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

			ContractEntityMapping mapping = repository.findById(id).orElseThrow(() -> {
				logger.error("Mapping not found with id {}", id);
				return new ResourceNotFoundException("Mapping not found with id " + id);
			});

			ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(() -> {
				logger.error("Contract not found with id {}", request.getContractId());
				return new ResourceNotFoundException("Contract not found with id " + request.getContractId());
			});

			CustomerMasterEntity customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> {
				logger.error("Customer not found with id {}", request.getCustomerId());
				return new ResourceNotFoundException("Customer not found with id " + request.getCustomerId());
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

			return new ResponseEntity("Mapping updated successfully", 200, mapper.toResponse(mapping));

		} catch (BadRequestException ex) {
			logger.warn("BadRequestException while updating mapping with id {}: {}", id, ex.getMessage());
			throw ex;
		} catch (ResourceNotFoundException ex) {
			logger.warn("ResourceNotFoundException while updating mapping with id {}: {}", id, ex.getMessage());
			throw ex;
		} catch (ExternalServiceException ex) {
			logger.error("ExternalServiceException while updating mapping with id {}: {}", id, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Unexpected error while updating mapping with id {}: {}", id, ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to update mapping");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getMappingById(Integer id) {

		logger.info("Get Mapping By ID API called {}", id);

		if (id == null) {
			logger.error("Mapping id cannot be null");
			throw new BadRequestException("Mapping id cannot be null");
		}

		try {

			ContractEntityMapping mapping = repository.findById(id).orElseThrow(() -> {
				logger.error("Mapping not found with id {}", id);
				return new ResourceNotFoundException("Mapping not found with id " + id);
			});

			ContractEntityMappingResponse response = mapper.toResponse(mapping);

			try {
				logger.info("Fetching site details for siteId {}", mapping.getSiteId());

				CompSiteResponse site = validationService.validateAndGetSite(mapping.getSiteId(), "Source");

				response.setSiteId(site.getSiteId());
				response.setSiteName(site.getSiteName());

			} catch (Exception ex) {
				logger.warn("Unable to fetch site details for siteId {}: {}", mapping.getSiteId(), ex.getMessage());
				response.setSiteName("Unknown Site");
			}

			try {

				if (mapping.getCustomer() != null) {

					Integer customerId = mapping.getCustomer().getCustomerId();

					logger.info("Fetching customer details for customerId {}", customerId);

					CustomerMasterEntity customer = customerRepository.findById(customerId).orElseThrow(
							() -> new ResourceNotFoundException("Customer not found with id " + customerId));

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
			logger.error("Unexpected error in getMappingById for id {}", id, ex);
			throw new DatabaseOperationException("Failed to fetch mapping");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllMappings(int page, int size) {

		logger.info("getAllMappings API hit with page {} and size {}", page, size);

		if (page < 0 || size <= 0) {
			logger.error("Invalid pagination parameters page {} size {}", page, size);
			throw new BadRequestException("Invalid page or size value");
		}

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("mappingId").descending());
			Page<ContractEntityMapping> pageResult = repository.findAll(pageable);

			List<ContractEntityMapping> mappings = pageResult.getContent();

			Map<Integer, CompSiteResponse> siteCache = new HashMap<>();
			Map<Integer, CustomerMasterEntity> customerCache = new HashMap<>();

			List<ContractEntityMappingResponse> response = new ArrayList<>();

			for (ContractEntityMapping mapping : mappings) {

				ContractEntityMappingResponse res = mapper.toResponse(mapping);

				if (mapping.getContract() != null) {
					String contractName = mapping.getContract().getContractName();
					if (contractName != null && !contractName.trim().isEmpty()) {
						res.setContractId(mapping.getContract().getContractId());
						res.setContractName(contractName);
						res.setContractNo(mapping.getContract().getContractNo());
					} else {
						logger.warn("Contract name is null/empty for contractId {}",
								mapping.getContract().getContractId());
					}
				} else {
					logger.warn("Contract is null for mappingId {}", mapping.getMappingId());
				}

				try {
					Integer siteId = mapping.getSiteId();
					if (siteId != null) {
						if (siteCache.containsKey(siteId)) {
							CompSiteResponse site = siteCache.get(siteId);
							res.setSiteName(site.getSiteName());
							res.setSiteCode(site.getSiteCode());
						} else {
							CompSiteResponse site = validationService.validateAndGetSite(siteId, "Source");
							siteCache.put(siteId, site);
							res.setSiteName(site.getSiteName());
							res.setSiteCode(site.getSiteCode());
						}
					} else {
						res.setSiteName("Unknown Site");
					}
				} catch (Exception ex) {
					logger.warn("Site fetch failed for siteId {}", mapping.getSiteId());
					res.setSiteName("Unknown Site");
				}

				try {
					if (mapping.getCustomer() != null) {
						Integer customerId = mapping.getCustomer().getCustomerId();
						if (customerId != null) {
							if (customerCache.containsKey(customerId)) {
								CustomerMasterEntity customer = customerCache.get(customerId);
								res.setCustomerName(customer.getCustomerName());
								res.setCustomerCode(customer.getCustomerCode());
							} else {
								CustomerMasterEntity customer = customerRepository.findById(customerId).orElse(null);
								if (customer != null) {
									customerCache.put(customerId, customer);
									res.setCustomerName(customer.getCustomerName());
									res.setCustomerCode(customer.getCustomerCode());
								} else {
									res.setCustomerName("Unknown Customer");
								}
							}
						} else {
							res.setCustomerName("Unknown Customer");
						}
					} else {
						res.setCustomerName("Unknown Customer");
					}
				} catch (Exception ex) {
					logger.error("Customer fetch failed for mappingId {}", mapping.getMappingId(), ex);
					res.setCustomerName("Unknown Customer");
				}

				response.add(res);
			}

			logger.info("Total mappings fetched in this page {}: {}", pageResult.getNumber(), response.size());

			Map<String, Object> payload = new HashMap<>();
			payload.put("data", response);
			payload.put("currentPage", pageResult.getNumber());
			payload.put("pageSize", pageResult.getSize());
			payload.put("totalItems", pageResult.getTotalElements());
			payload.put("totalPages", pageResult.getTotalPages());
			payload.put("isLast", pageResult.isLast());

			return new ResponseEntity("Success", 200, payload);

		} catch (BadRequestException ex) {
			logger.warn("Validation error while fetching mappings {}", ex.getMessage());
			throw ex;
		} catch (DatabaseOperationException ex) {
			logger.error("DatabaseOperationException while fetching mappings {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Unexpected error while fetching mappings", ex);
			throw new DatabaseOperationException("Failed to fetch mappings");
		}
	}

		@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllContractEntityMappings(String contractType,int page,int size) {

		logger.info("getAllContractEntityMappings API hit with page {} and size {}", page, size);

		if (contractType==null) {
			logger.error("Contract type cannot be null");
			throw new BadRequestException("Contract type cannot be null");
		}

		try {

			Pageable pageable = PageRequest.of(page, size, Sort.by("mappingId").descending());
			
			Page<ContractEntityMapping> pageResult = repository.findAvailableContracts(contractType, "Pending", pageable);

			List<ContractEntityMapping> mappings = pageResult.getContent();

			Map<Integer, CompSiteResponse> siteCache = new HashMap<>();
			Map<Integer, CustomerMasterEntity> customerCache = new HashMap<>();

			List<ContractEntityMappingResponse> response = new ArrayList<>();

			List<ItemIDResponse> itemIDResponses = validationService.getAllItems();

			List<ParamResponseDTO> categoryParamResponseDTOs = validationService.getParamByCodeAndSerial("Item",
					"Category");

			List<ParamResponseDTO> typeParamResponseDTOs = validationService.getParamByCodeAndSerial("CONTRACT",
					"CONTRACT_TYPE");

			Map<Integer, String> itemIdToCategoryMap = itemIDResponses.stream()
					.collect(Collectors.toMap(ItemIDResponse::getItemId, ItemIDResponse::getCategory, (a, b) -> a

					));

			Map<String, String> categoryToParamMap = categoryParamResponseDTOs.stream()
					.collect(Collectors.toMap(ParamResponseDTO::getDesp1, ParamResponseDTO::getDesp2, (a, b) -> a));

			Map<String, String> typeToParamMap = typeParamResponseDTOs.stream()
					.collect(Collectors.toMap(ParamResponseDTO::getDesp1, ParamResponseDTO::getDesp2, (a, b) -> a));

			for (ContractEntityMapping mapping : mappings) {

				ContractEntityMappingResponse res = mapper.toResponse(mapping);

				String paramType = typeToParamMap.get(mapping.getContract().getContractType());
				res.setContractType(paramType);
				res.setMinNoVisits(mapping.getMinNoVisits());

				HierarchyLevelResponseDTO responseDTO = validationService
						.validateAndGetHierarchyLevel(mapping.getCustomer().getHierarchyLevelId());
				res.setZoneName(responseDTO.getLevelName());

				List<ContractItemMapping> contractItemMappings = contractItemMappingRepository
						.findByContract_ContractId(mapping.getContract().getContractId());

				List<String> productTypes = contractItemMappings.stream().map(itemMapping -> {
					Integer itemId = itemMapping.getItemId();

					String category = itemIdToCategoryMap.get(itemId);

					if (category == null) {
						return null;
					}

					return categoryToParamMap.get(category);
				}).filter(Objects::nonNull).distinct().toList();

				res.setProductList(productTypes);

				Optional<CustomerEmployeeAllocation> employeeAllocation = customerEmployeeAllocationRepository.findByCustomerCustomerIdAndIsActive(mapping.getCustomer().getCustomerId(), "Y");

				if(employeeAllocation.isPresent()) {
					res.setFaId(employeeAllocation.get().getEmployeeId());
					EmployeeDTO employeeDTO = validationService.validateEmployeeExists(employeeAllocation.get().getEmployeeId());
					res.setFaName(employeeDTO.getEmployeeName());
				}

				if (mapping.getContract() != null) {
					String contractName = mapping.getContract().getContractName();
					if (contractName != null && !contractName.trim().isEmpty()) {
						res.setContractId(mapping.getContract().getContractId());
						res.setContractName(contractName);
						res.setContractNo(mapping.getContract().getContractNo());
						res.setContractStartDate(mapping.getContract().getContractStartDate());	
						res.setContractEndDate(mapping.getContract().getContractEndDate());
						
					} else {
						logger.warn("Contract name is null/empty for contractId {}",
								mapping.getContract().getContractId());
					}
				} else {
					logger.warn("Contract is null for mappingId {}", mapping.getMappingId());
				}

				try {
					Integer siteId = mapping.getSiteId();
					if (siteId != null) {
						if (siteCache.containsKey(siteId)) {
							CompSiteResponse site = siteCache.get(siteId);
							res.setSiteName(site.getSiteName());
							res.setSiteCode(site.getSiteCode());
							res.setSiteDistrictName(site.getDistrict());
							res.setIfsc(site.getIfsc());
						} else {
							CompSiteResponse site = validationService.validateAndGetSite(siteId, "Source");
							siteCache.put(siteId, site);
							res.setSiteName(site.getSiteName());
							res.setSiteCode(site.getSiteCode());
							res.setSiteDistrictName(site.getDistrict());
							res.setIfsc(site.getIfsc());
						}
					} else {
						res.setSiteName("Unknown Site");
					}
				} catch (Exception ex) {
					logger.warn("Site fetch failed for siteId {}", mapping.getSiteId());
					res.setSiteName("Unknown Site");
				}

				try {
					if (mapping.getCustomer() != null) {
						Integer customerId = mapping.getCustomer().getCustomerId();
						if (customerId != null) {
							if (customerCache.containsKey(customerId)) {
								CustomerMasterEntity customer = customerCache.get(customerId);
								res.setCustomerName(customer.getCustomerName());
								res.setCustomerCode(customer.getCustomerCode());
							} else {
								CustomerMasterEntity customer = customerRepository.findById(customerId).orElse(null);
								if (customer != null) {
									customerCache.put(customerId, customer);
									res.setCustomerName(customer.getCustomerName());
									res.setCustomerCode(customer.getCustomerCode());
								} else {
									res.setCustomerName("Unknown Customer");
								}
							}
						} else {
							res.setCustomerName("Unknown Customer");
						}
					} else {
						res.setCustomerName("Unknown Customer");
					}
				} catch (Exception ex) {
					logger.error("Customer fetch failed for mappingId {}", mapping.getMappingId(), ex);
					res.setCustomerName("Unknown Customer");
				}

				response.add(res);
			}

			logger.info("Total mappings fetched in this page {}: {}", pageResult.getNumber(), response.size());

			Map<String, Object> payload = new HashMap<>();
			payload.put("data", response);
			payload.put("currentPage", pageResult.getNumber());
			payload.put("pageSize", pageResult.getSize());
			payload.put("totalItems", pageResult.getTotalElements());
			payload.put("totalPages", pageResult.getTotalPages());
			payload.put("isLast", pageResult.isLast());

			return new ResponseEntity("Success", 200, payload);

		} catch (BadRequestException ex) {
			logger.warn("Validation error while fetching mappings {}", ex.getMessage());
			throw ex;
		} catch (DatabaseOperationException ex) {
			logger.error("DatabaseOperationException while fetching mappings {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Unexpected error while fetching mappings", ex);
			throw new DatabaseOperationException("Failed to fetch mappings");
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

			ContractEntityMapping mapping = repository.findById(id).orElseThrow(() -> {
				logger.error("Mapping not found with id {}", id);
				return new ResourceNotFoundException("Mapping not found with id " + id);
			});

			mapping.setIsActive("N");
			mapping.setModifiedOn(LocalDateTime.now());

			repository.save(mapping);

			logger.info("Mapping deactivated successfully {}", id);

			return new ResponseEntity("Mapping deactivated successfully", 200, null);

		} catch (ResourceNotFoundException ex) {

			throw ex;

		} catch (Exception ex) {

			logger.error("Error while deactivating mapping", ex);
			throw new DatabaseOperationException("Failed to deactivate mapping");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getMappingByContractId(Integer id) {

		logger.info("Get Mapping By ContractId API called {}", id);

		if (id == null) {
			logger.error("Contract id is null");
			throw new BadRequestException("Contract id cannot be null");
		}

		try {

			List<ContractEntityMapping> mappings = repository.findByContractContractId(id);

			if (mappings == null || mappings.isEmpty()) {
				logger.error("No mapping found with Contract id {}", id);
				throw new ResourceNotFoundException("Mapping not found with Contract id " + id);
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
						CompSiteResponse site = validationService.validateAndGetSite(siteId, "Source");
						siteCache.put(siteId, site.getSiteName());
						res.setSiteName(site.getSiteName());
					}

				} catch (Exception ex) {
					logger.warn("Site fetch failed for siteId {}", mapping.getSiteId());
					res.setSiteName("Unknown Site");
				}

				try {
					if (mapping.getCustomer() != null) {

						Integer customerId = mapping.getCustomer().getCustomerId();

						if (customerCache.containsKey(customerId)) {
							res.setCustomerName(customerCache.get(customerId));
						} else {
							CustomerMasterEntity customer = customerRepository.findById(customerId).orElse(null);

							if (customer != null) {
								customerCache.put(customerId, customer.getCustomerName());
								res.setCustomerName(customer.getCustomerName());
							} else {
								res.setCustomerName("Unknown Customer");
							}
						}

					} else {
						res.setCustomerName("Unknown Customer");
					}

				} catch (Exception ex) {
					logger.error("Customer fetch failed for mappingId {}", mapping.getMappingId(), ex);
					res.setCustomerName("Unknown Customer");
				}

				responseList.add(res);
			}

			logger.info("Mapping fetched successfully for contract id {}", id);

			return new ResponseEntity("Success", 200, responseList);

		} catch (BadRequestException | ResourceNotFoundException ex) {

			logger.error("Validation error while fetching mapping: {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {

			logger.error("Error while fetching mapping for contract id {}", id, ex);
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