package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
import com.doritech.CustomerService.Response.CustomerResponse;
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
			throw new BadRequestException("Request list cannot be empty");
		}

		Set<String> uniqueCheck = new HashSet<>();
		for (ContractEntityMappingRequest request : requests) {

			if (request.getMappingId() != null && !repository.existsById(request.getMappingId())) {
				throw new ResourceNotFoundException("Mapping not found with id: " + request.getMappingId());
			}

			String key = request.getCustomerId() + "-" + request.getSiteId() + "-" + request.getEmployeeId() + "-"
					+ request.getSiteFromDate();

			if (!uniqueCheck.add(key)) {
				throw new BadRequestException("Duplicate entry in request for customerId=" + request.getCustomerId()
						+ ", siteId=" + request.getSiteId() + ", employeeId=" + request.getEmployeeId());
			}
		}

		List<ContractEntityMappingResponse> responseList = new ArrayList<>();

		for (ContractEntityMappingRequest request : requests) {
			responseList.add(processSingleMapping(request));
		}

		return new ResponseEntity("Mappings saved/updated successfully", 200, responseList);
	}

	private ContractEntityMappingResponse processSingleMapping(ContractEntityMappingRequest request) {

		ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(
				() -> new ResourceNotFoundException("Contract not found with id: " + request.getContractId()));

		CustomerMasterEntity customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

		validateNoExistingActiveMapping(request, contract, customer);

		validationService.validateSiteExists(request.getSiteId());
		handleBranchAllocation(request, customer);

		validationService.validateEmployeeExists(request.getEmployeeId());
		handleEmployeeAllocation(request, customer);

		ContractEntityMapping mapping = buildMapping(request, contract, customer);
		ContractEntityMapping saved = repository.save(mapping);

		return mapper.toResponse(saved);
	}

	private void validateNoExistingActiveMapping(ContractEntityMappingRequest request, ContractMaster contract,
			CustomerMasterEntity customer) {

		List<ContractEntityMapping> existingMappings = repository
				.findByContract_ContractIdAndCustomer_CustomerIdAndSiteId(request.getContractId(),
						request.getCustomerId(), request.getSiteId());

		for (ContractEntityMapping m : existingMappings) {
			boolean isSameRecord = request.getMappingId() != null && m.getMappingId().equals(request.getMappingId());

			if (!isSameRecord && "Y".equalsIgnoreCase(m.getIsActive())) {
				throw new BadRequestException("Mapping already exists for" + " Contract: '" + contract.getContractName()
						+ "' (id=" + request.getContractId() + ")" + ", Customer: '" + customer.getCustomerName()
						+ "' (id=" + request.getCustomerId() + ")" + ", SiteId: " + request.getSiteId());
			}
		}
	}

	private void handleBranchAllocation(ContractEntityMappingRequest request, CustomerMasterEntity customer) {

		CustomerBranchAllocation existing = customerBranchAllocationRepository
				.findByCustomer_CustomerIdAndSiteIdAndIsActive(request.getCustomerId(), request.getSiteId(), "Y");

		if (existing == null) {
			saveBranch(customer, request.getSiteId(), request.getSiteFromDate(), request.getCreatedBy());
			return;
		}

		LocalDate existingDate = existing.getFromDate();
		LocalDate newDate = request.getSiteFromDate();

		if (existingDate.equals(newDate)) {
			return;
		}

		List<CustomerBranchAllocation> allBranches = customerBranchAllocationRepository
				.findByCustomer_CustomerIdAndSiteId(request.getCustomerId(), request.getSiteId());

		boolean conflictExists = allBranches.stream()
				.anyMatch(b -> !b.getAllocationId().equals(existing.getAllocationId())
						&& b.getFromDate().equals(newDate) && "Y".equals(b.getIsActive()));

		if (conflictExists) {
			throw new BadRequestException("Site conflict: siteId=" + request.getSiteId()
					+ " is already actively assigned on date=" + newDate);
		}

		if (!newDate.isAfter(existingDate)) {
			throw new BadRequestException("Site siteId=" + request.getSiteId() + " is already assigned from "
					+ existingDate + ". New date must be after existing date.");
		}

		existing.setIsActive("N");
		customerBranchAllocationRepository.save(existing);
		saveBranch(customer, request.getSiteId(), newDate, request.getCreatedBy());
	}

	private void saveBranch(CustomerMasterEntity customer, Integer siteId, LocalDate fromDate, Integer createdBy) {
		CustomerBranchAllocation branch = new CustomerBranchAllocation();
		branch.setCustomer(customer);
		branch.setSiteId(siteId);
		branch.setFromDate(fromDate);
		branch.setIsActive("Y");
		branch.setCreatedBy(createdBy);
		customerBranchAllocationRepository.save(branch);
	}

	private void handleEmployeeAllocation(ContractEntityMappingRequest request, CustomerMasterEntity customer) {

		List<CustomerEmployeeAllocation> activeAllocations = customerEmployeeAllocationRepository
				.findByEmployeeIdAndIsActive(request.getEmployeeId(), "Y");

		for (CustomerEmployeeAllocation emp : activeAllocations) {
			boolean isSameCustomer = emp.getCustomer().getCustomerId().equals(request.getCustomerId());

			if (!isSameCustomer && emp.getFromDate().equals(request.getEmployeeFromDate())) {
				throw new BadRequestException("Employee conflict: employeeId=" + request.getEmployeeId()
						+ " is already assigned to another customer (customerId=" + emp.getCustomer().getCustomerId()
						+ ")" + " on date=" + emp.getFromDate());
			}
		}

		CustomerEmployeeAllocation existing = customerEmployeeAllocationRepository
				.findByCustomer_CustomerIdAndEmployeeIdAndIsActive(request.getCustomerId(), request.getEmployeeId(),
						"Y");

		if (existing == null) {
			saveEmployee(customer, request.getEmployeeId(), request.getEmployeeFromDate(), request.getCreatedBy());
			return;
		}

		LocalDate existingDate = existing.getFromDate();
		LocalDate newDate = request.getEmployeeFromDate();

		if (existingDate.equals(newDate)) {
			return;
		}

		if (!newDate.isAfter(existingDate)) {
			throw new BadRequestException("Employee employeeId=" + request.getEmployeeId()
					+ " is already assigned from " + existingDate + ". New date must be after existing date.");
		}

		existing.setIsActive("N");
		customerEmployeeAllocationRepository.save(existing);
		saveEmployee(customer, request.getEmployeeId(), newDate, request.getCreatedBy());
	}

	private void saveEmployee(CustomerMasterEntity customer, Integer employeeId, LocalDate fromDate,
			Integer createdBy) {
		CustomerEmployeeAllocation emp = new CustomerEmployeeAllocation();
		emp.setCustomer(customer);
		emp.setEmployeeId(employeeId);
		emp.setFromDate(fromDate);
		emp.setIsActive("Y");
		emp.setCreatedBy(createdBy);
		customerEmployeeAllocationRepository.save(emp);
	}

	private ContractEntityMapping buildMapping(ContractEntityMappingRequest request, ContractMaster contract,
			CustomerMasterEntity customer) {

		ContractEntityMapping mapping;

		if (request.getMappingId() != null) {
			mapping = repository.findById(request.getMappingId()).orElseThrow(
					() -> new ResourceNotFoundException("Mapping not found with id: " + request.getMappingId()));
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

		return mapping;
	}

	@Override
	@Transactional
	public ResponseEntity updateMapping(Integer id, ContractEntityMappingRequest request) {

		logger.info("Update Mapping API called for id {}", id);

		if (id == null) {
			throw new BadRequestException("Mapping id cannot be null");
		}

		if (request == null) {
			throw new BadRequestException("Request body cannot be null");
		}

		ContractEntityMapping mapping = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id " + id));

		ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(
				() -> new ResourceNotFoundException("Contract not found with id " + request.getContractId()));

		CustomerMasterEntity customer = customerRepository.findById(request.getCustomerId()).orElseThrow(
				() -> new ResourceNotFoundException("Customer not found with id " + request.getCustomerId()));

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
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getMappingById(Integer id) {

		logger.info("Get Mapping By ID API called {}", id);

		if (id == null) {
			throw new BadRequestException("Mapping id cannot be null");
		}

		ContractEntityMapping mapping = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id " + id));

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

		if (mapping.getCustomer() != null) {
			Integer customerId = mapping.getCustomer().getCustomerId();
			logger.info("Fetching customer details for customerId {}", customerId);
			CustomerMasterEntity customer = customerRepository.findById(customerId).orElse(null);
			if (customer != null) {
				response.setCustomerId(customer.getCustomerId());
				response.setCustomerName(customer.getCustomerName());
			} else {
				logger.warn("Customer not found for id {}", customerId);
				response.setCustomerName("Unknown Customer");
			}
		} else {
			logger.warn("Customer is null in mapping id {}", id);
			response.setCustomerName("Unknown Customer");
		}

		logger.info("Mapping fetched successfully {}", id);

		return new ResponseEntity("Success", 200, response);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllMappings(int page, int size) {

		logger.info("getAllMappings API hit with page {} and size {}", page, size);

		if (page < 0 || size <= 0) {
			throw new BadRequestException("Invalid page or size value");
		}

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
					logger.warn("Contract name is null/empty for contractId {}", mapping.getContract().getContractId());
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
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllContractEntityMappings(String contractType, int page, int size) {

		logger.info("getAllContractEntityMappings API hit with page {} and size {}", page, size);

		if (contractType == null) {
			logger.error("Contract type cannot be null");
			throw new BadRequestException("Contract type cannot be null");
		}

		try {

			Pageable pageable = PageRequest.of(page, size, Sort.by("mappingId").descending());

			Page<ContractEntityMapping> pageResult = repository.findAvailableContracts(contractType, "Pending",
					pageable);

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

				Optional<CustomerEmployeeAllocation> employeeAllocation = customerEmployeeAllocationRepository
						.findByCustomerCustomerIdAndIsActive(mapping.getCustomer().getCustomerId(), "Y");

				if (employeeAllocation.isPresent()) {
					res.setFaId(employeeAllocation.get().getEmployeeId());
					EmployeeDTO employeeDTO = validationService
							.validateEmployeeExists(employeeAllocation.get().getEmployeeId());
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
						} else {
							CompSiteResponse site = validationService.validateAndGetSite(siteId, "Source");
							siteCache.put(siteId, site);
							res.setSiteName(site.getSiteName());
							res.setSiteCode(site.getSiteCode());
							res.setSiteDistrictName(site.getDistrict());
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

				res.setIfsc(mapping.getContract().getCustomer().getIfsc());
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

		ContractEntityMapping mapping = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Mapping not found with id " + id));

		mapping.setIsActive("N");
		mapping.setModifiedOn(LocalDateTime.now());

		repository.save(mapping);

		logger.info("Mapping deactivated successfully {}", id);

		return new ResponseEntity("Mapping deactivated successfully", 200, null);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getMappingByContractId(Integer id) {

		logger.info("Get Mapping By ContractId API called {}", id);

		if (id == null) {
			throw new BadRequestException("Contract id cannot be null");
		}

		List<ContractEntityMapping> mappings = repository.findByContractContractId(id);

		if (mappings == null || mappings.isEmpty()) {
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
	}

	@Override
	@Transactional
	public ResponseEntity deactivateBulkContractEntity(List<Integer> ids) {

		logger.info("Bulk deactivate ContractEntityMapping API called with ids {}", ids);

		if (ids == null || ids.isEmpty()) {
			throw new BadRequestException("Mapping ids cannot be null or empty");
		}

		List<ContractEntityMapping> mappings = repository.findAllById(ids);

		if (mappings.isEmpty()) {
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
	}
@Override
	public ResponseEntity getCustomerNameAndCodeByContractID(Integer contractId) {

		logger.info("Fetching customer details for contractId: {}", contractId);

		if (contractId == null) {
			logger.error("contractId is null");
			throw new BadRequestException("contractId cannot be null");
		}

		List<ContractEntityMapping> contractEntityMappings = repository.findByContractContractId(contractId);

		if (contractEntityMappings == null || contractEntityMappings.isEmpty()) {
			logger.error("No ContractEntityMapping found for contractId: {}", contractId);
			throw new ResourceNotFoundException("No mapping found for given contractId");
		}

		List<ContractEntityMappingResponse> customerMapping =
		        contractEntityMappings.stream()
		                .map(m -> {
		                    ContractEntityMappingResponse result = new ContractEntityMappingResponse();

		                    result.setMappingId(m.getMappingId());
		                    result.setCustomerId(m.getCustomer().getCustomerId());

		                    return result;
		                })
		                .collect(Collectors.toList());
		

		if (customerMapping.isEmpty()) {
			logger.error("CustomerIds list is empty for contractId: {}", contractId);
			throw new ResourceNotFoundException("No customerIds found");
		}

		List<CustomerResponse> payload = contractEntityMappings.stream()
		        .filter(m -> m != null && m.getCustomer() != null)
		        .map(m -> {
		            CustomerMasterEntity c = m.getCustomer();

		            if (!"Y".equalsIgnoreCase(c.getIsActive())) {
		                return null;
		            }

		            CustomerResponse r = new CustomerResponse();
		            r.setCustomerId(c.getCustomerId());
		            r.setCustomerName(c.getCustomerName());
		            r.setCustomerCode(c.getCustomerCode());

		            r.setMappingId(m.getMappingId());

		            return r;
		        })
		        .filter(Objects::nonNull)
		        .collect(Collectors.toList());
		
		ResponseEntity response = new ResponseEntity();
		response.setMessage("Success");
		response.setStatusCode(200);
		response.setPayload(payload);

		logger.info("Successfully fetched {} customers for contractId: {}", payload.size(), contractId);

		return response;
	}
}