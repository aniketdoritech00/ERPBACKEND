package com.doritech.CustomerService.ServiceImpl;

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

import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.DatabaseOperationException;
import com.doritech.CustomerService.Exception.DuplicateResourceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.ContractMapper;
import com.doritech.CustomerService.Repository.ContractItemMappingRepository;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Repository.EmployeeAssignmentRepository;
import com.doritech.CustomerService.Request.ContractMasterRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.CompanySiteMappingResponse;
import com.doritech.CustomerService.Response.ContractMasterResponse;
import com.doritech.CustomerService.Response.HierarchyLevelResponseDTO;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Response.ParamResponseDTO;
import com.doritech.CustomerService.Service.ContractMasterService;
import com.doritech.CustomerService.Specification.ContractSpecification;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
public class ContractMasterServiceImpl implements ContractMasterService {

	private static final Logger logger = LoggerFactory
			.getLogger(ContractMasterServiceImpl.class);

	@Autowired
	private ContractMasterRepository contractRepository;
	@Autowired
	private CustomerMasterRepository customerRepository;

	@Autowired
	private EmployeeAssignmentRepository assignmentRepository;

	@Autowired
	private ContractItemMappingRepository contractItemMappingRepository;

	@Autowired
	private ValidationService validationService;

	@Override
	@Transactional
	public ResponseEntity saveOrUpdateContract(Integer id, ContractMasterRequest request) {

		logger.info("SaveOrUpdate Contract API called with id {}", id);

		if (request == null) {
			logger.error("Request is null");
			throw new BadRequestException("Request cannot be null");
		}

		if (request.getCustomerId() == null) {
			logger.error("Customer ID is null");
			throw new BadRequestException("Customer ID cannot be null");
		}

		try {

			boolean customerExists = customerRepository.existsById(request.getCustomerId());

			if (!customerExists) {
				logger.error("Customer not found with id {}", request.getCustomerId());
				throw new ResourceNotFoundException("Customer not found with id : " + request.getCustomerId());
			}

			if (request.getContractStartDate() != null && request.getContractEndDate() != null) {
				if (request.getContractEndDate().isBefore(request.getContractStartDate())) {
					logger.error("Contract end date {} cannot be before start date {}", request.getContractEndDate(),
							request.getContractStartDate());
					throw new BadRequestException("Contract end date cannot be before start date");
				}
			}

			ContractMaster contract;

			if (id != null && contractRepository.existsById(id)) {

				logger.info("Updating contract with id {}", id);

				ContractMaster existingContract = contractRepository.findById(id).orElseThrow(() -> {
					logger.error("Contract not found for id {}", id);
					return new ResourceNotFoundException("Contract not found");
				});

				if (!existingContract.getContractNo().equals(request.getContractNo())
						&& contractRepository.existsByContractNo(request.getContractNo())) {
					logger.error("Duplicate contract number {}", request.getContractNo());
					throw new DuplicateResourceException("Contract number already exists");
				}

				contract = ContractMapper.toEntity(request);

				contract.setContractId(existingContract.getContractId());
				contract.setCreatedBy(existingContract.getCreatedBy());
				contract.setCreatedOn(existingContract.getCreatedOn());
				contract.setModifiedBy(request.getModifiedBy());
				contract.setModifiedOn(LocalDateTime.now());

				contractRepository.save(contract);

				logger.info("Contract updated successfully with id {}", id);

				return new ResponseEntity("Contract updated successfully", 200, ContractMapper.toResponse(contract));
			}

			logger.info("Creating new contract");

			if (contractRepository.existsByContractNo(request.getContractNo())) {
				logger.error("Duplicate contract number {}", request.getContractNo());
				throw new DuplicateResourceException("Contract number already exists");
			}

			contract = ContractMapper.toEntity(request);

			contract.setCreatedBy(request.getCreatedBy());
			contract.setCreatedOn(LocalDateTime.now());
			contract.setModifiedBy(null);
			contract.setModifiedOn(null);

			contractRepository.save(contract);

			logger.info("Contract created successfully with id {}", contract.getContractId());

			return new ResponseEntity("Contract created successfully", 200, ContractMapper.toResponse(contract));

		} catch (BadRequestException | DuplicateResourceException | ResourceNotFoundException ex) {

			logger.error("Validation error in saveOrUpdate contract: {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {

			logger.error("Unexpected error in saveOrUpdate contract", ex);
			throw new DatabaseOperationException("Failed to save or update contract");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getContractById(Integer id) {

		logger.info("Get Contract By ID API called {}", id);

		if (id == null) {
			logger.error("Contract ID is null");
			throw new BadRequestException("Contract ID cannot be null");
		}

		ContractMaster contract = contractRepository.findById(id)
				.orElseThrow(() -> {
					logger.error("Contract not found for id {}", id);
					return new ResourceNotFoundException("Contract not found");
				});

		logger.info("Contract fetched successfully {}", id);

		return new ResponseEntity("Success", 200,
				ContractMapper.toResponse(contract));
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllContracts(int page, int size) {

		logger.info("Get All Contracts API called page {} size {}", page, size);

		if (page < 0) {
			page = 0;
		}

		if (size <= 0) {
			size = 10;
		}

		try {

			Pageable pageable = PageRequest.of(page, size,
					Sort.by("contractId").descending());

			Page<ContractMaster> contractPage = contractRepository
					.findAll(pageable);

			List<ContractMasterResponse> response = contractPage.getContent()
					.stream().map(ContractMapper::toResponse).toList();

			Map<String, Object> result = new HashMap<>();
			result.put("contracts", response);
			result.put("currentPage", contractPage.getNumber());
			result.put("totalItems", contractPage.getTotalElements());
			result.put("totalPages", contractPage.getTotalPages());

			logger.info("Fetched {} contracts", response.size());

			return new ResponseEntity("Success", 200, result);

		} catch (Exception ex) {

			logger.error("Error while fetching contracts", ex);
			throw new DatabaseOperationException("Failed to fetch contracts");
		}
	}

	@Override
	@Transactional
	public ResponseEntity deactivateContracts(List<Integer> ids) {

		logger.info("Deactivate Contract API called for ids: {}", ids);

		if (ids == null || ids.isEmpty()) {
			logger.error("Contract IDs list is null or empty");
			throw new BadRequestException(
					"Contract IDs list cannot be null or empty");
		}

		try {
			List<Integer> notFoundIds = new ArrayList<>();
			List<Integer> deactivatedIds = new ArrayList<>();

			for (Integer id : ids) {
				Optional<ContractMaster> contractOpt = contractRepository
						.findById(id);

				if (contractOpt.isEmpty()) {
					logger.warn("Contract not found for id: {}", id);
					notFoundIds.add(id);
					continue;
				}

				ContractMaster contract = contractOpt.get();
				contract.setIsActive("N");
				contract.setModifiedOn(LocalDateTime.now());
				contractRepository.save(contract);
				deactivatedIds.add(id);
			}

			logger.info("Contracts deactivated: {}, Not found: {}",
					deactivatedIds, notFoundIds);

			if (!notFoundIds.isEmpty() && !deactivatedIds.isEmpty()) {
				String message = "Partially deactivated. Success: "
						+ deactivatedIds + ", Not found: " + notFoundIds;
				return new ResponseEntity(message, 207, null);
			}

			if (deactivatedIds.isEmpty()) {
				throw new ResourceNotFoundException(
						"No contracts found for given IDs: " + notFoundIds);
			}

			return new ResponseEntity("Contract(s) deactivated successfully",
					200, null);

		} catch (ResourceNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			logger.error("Error while deactivating contracts", ex);
			throw new DatabaseOperationException(
					"Failed to deactivate contracts");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity filterContracts(String contractNo, Integer customerId,
			String contractType, String isActive) {

		logger.info("Filter Contracts API called");

		if (contractNo == null && customerId == null && contractType == null
				&& isActive == null) {
			throw new BadRequestException(
					"At least one filter parameter must be provided");
		}

		try {

			List<ContractMaster> contracts = contractRepository
					.findAll(ContractSpecification.filterContracts(contractNo,
							customerId, contractType, isActive));

			if (contracts.isEmpty()) {
				logger.warn("No contracts found for given filters");
				throw new ResourceNotFoundException("No contracts found");
			}

			List<ContractMasterResponse> response = contracts.stream()
					.map(ContractMapper::toResponse).toList();

			logger.info("Filtered contracts fetched {}", response.size());

			return new ResponseEntity("Contracts fetched successfully", 200,
					response);

		} catch (ResourceNotFoundException | BadRequestException ex) {

			throw ex;

		} catch (Exception ex) {

			logger.error("Error while filtering contracts", ex);
			throw new DatabaseOperationException("Failed to filter contracts");
		}
	}

	@Override
	public ResponseEntity getContractNamesAndIds() {
		logger.info("Fetching active contract IDs and names");

		ResponseEntity response = new ResponseEntity();

		try {
			List<ContractMaster> contracts = contractRepository.getActiveContracts();

			if (contracts == null || contracts.isEmpty()) {
				logger.warn("No active contracts found");
				response.setStatusCode(404);
				response.setMessage("No active contracts found");
				response.setPayload(null);
				return response;
			}

			List<ContractMasterResponse> simplified = contracts.stream()
					.filter(c -> "Y".equalsIgnoreCase(c.getIsActive())).map(c -> {
						ContractMasterResponse temp = new ContractMasterResponse();
						temp.setContractId(c.getContractId());
						temp.setContractName(c.getContractName());
						temp.setContractNo(c.getContractNo());
						return temp;
					}).toList();

			logger.info("Fetched {} active contracts", simplified.size());
			response.setStatusCode(200);
			response.setMessage("Contracts fetched successfully");
			response.setPayload(simplified);

		} catch (Exception e) {
			logger.error("Error fetching contracts", e);
			response.setStatusCode(500);
			response.setMessage("Internal server error");
			response.setPayload(null);
		}

		return response;
	}
	// @Override
	// @Transactional(readOnly = true)
	// public ResponseEntity getContractDetailsByType(String type) {
	//
	// logger.info("Fetching contracts by type: {}", type);
	//
	// if (type == null || type.trim().isEmpty()) {
	// logger.error("Contract type is null or empty");
	// throw new BadRequestException(
	// "Contract type cannot be null or empty");
	// }
	//
	// try {
	//
	// // 🔥 Step 1: Get all pending contract IDs
	// List<Integer> pendingContractIds = assignmentRepository
	// .findContractIdsByStatus("PENDING");
	//
	// // 🔥 Step 2: Fetch contracts by type
	// List<ContractMaster> contractList = contractRepository
	// .findByContractType(type);
	//
	// if (contractList == null || contractList.isEmpty()) {
	// logger.warn("No contracts found for type: {}", type);
	// throw new ResourceNotFoundException(
	// "No contracts found for given type");
	// }
	//
	// // 🔥 Step 3: Filter contracts (IMPORTANT)
	// List<ContractMaster> filteredContracts = contractList.stream()
	// .filter(contract -> !pendingContractIds
	// .contains(contract.getContractId()))
	// .toList();
	//
	// if (filteredContracts.isEmpty()) {
	// throw new ResourceNotFoundException(
	// "All contracts are already assigned (Pending)");
	// }
	//
	// List<ContractMasterResponse> responseList = new ArrayList<>();
	//
	// for (ContractMaster contract : filteredContracts) {
	//
	// ContractMasterResponse response = new ContractMasterResponse();
	//
	// response.setContractId(contract.getContractId());
	// response.setContractNo(contract.getContractNo());
	// response.setContractName(contract.getContractName());
	// response.setCustomerId(contract.getCustomer().getCustomerId());
	// response.setCustomerName(
	// contract.getCustomer().getCustomerName());
	//
	// response.setZoneId(
	// contract.getCustomer().getHierarchyLevelId());
	//
	// HierarchyLevelResponseDTO responseDTO = validationService
	// .validateAndGetHierarchyLevel(
	// contract.getCustomer().getHierarchyLevelId());
	// response.setZoneName(responseDTO.getLevelName());
	//
	// List<CompanySiteMappingResponse> companySiteMappingResponses =
	// validationService
	// .getAllCompSiteMappingByCompId(
	// contract.getCustomer().getCompId());
	//
	// response.setSiteId(
	// companySiteMappingResponses.get(0).getSiteId());
	//
	// CompSiteResponse siteResponse = validationService
	// .validateAndGetSite(
	// companySiteMappingResponses.get(0).getSiteId(),
	// "AB");
	//
	// response.setSiteName(siteResponse.getSiteName());
	// response.setIfsc(siteResponse.getIfsc());
	// response.setDistrict(siteResponse.getDistrict());
	//
	// response.setContractStartDate(contract.getContractStartDate());
	// response.setContractEndDate(contract.getContractEndDate());
	// response.setContractStatus(contract.getContractStatus());
	// response.setContractType(contract.getContractType());
	// response.setBillingFrequency(contract.getBillingFrequency());
	// response.setAmcType(contract.getAmcType());
	// response.setTermCondition(contract.getTermCondition());
	// response.setPaymentTerms(contract.getPaymentTerms());
	// response.setIsActive(contract.getIsActive());
	// response.setCreatedOn(contract.getCreatedOn());
	// response.setModifiedOn(contract.getModifiedOn());
	// response.setCreatedBy(contract.getCreatedBy());
	// response.setModifiedBy(contract.getModifiedBy());
	//
	// responseList.add(response);
	// }
	//
	// logger.info("Successfully fetched {} contracts for type: {}",
	// responseList.size(), type);
	//
	// return new ResponseEntity("Contracts fetched successfully", 200,
	// responseList);
	//
	// } catch (ResourceNotFoundException ex) {
	// throw ex;
	//
	// } catch (Exception ex) {
	// logger.error("Error while fetching contracts by type: {}", type,
	// ex);
	// throw new BadRequestException(
	// "Something went wrong while fetching contracts");
	// }
	// }

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getContractDetailsByType(String type) {

		logger.info("Fetching contracts by type: {}", type);

		if (type == null || type.trim().isEmpty()) {
			logger.error("Contract type is null or empty");
			throw new BadRequestException(
					"Contract type cannot be null or empty");
		}

		try {

			List<ContractMaster> contractList = contractRepository
					.findAvailableContracts(type, "PENDING");

			if (contractList == null || contractList.isEmpty()) {
				logger.warn("No contracts found for type: {}", type);
				throw new ResourceNotFoundException(
						"No contracts found for given type");
			}

			List<ItemIDResponse> itemIDResponses = validationService
					.getAllItems();

			List<ParamResponseDTO> categoryParamResponseDTOs = validationService
					.getParamByCodeAndSerial("Item", "Category");

			List<ParamResponseDTO> typeParamResponseDTOs = validationService
					.getParamByCodeAndSerial("CONTRACT", "CONTRACT_TYPE");

			Map<Integer, String> itemIdToCategoryMap = itemIDResponses.stream()
					.collect(Collectors.toMap(ItemIDResponse::getItemId,
							ItemIDResponse::getCategory, (a, b) -> a

					));

			Map<String, String> categoryToParamMap = categoryParamResponseDTOs
					.stream()
					.collect(Collectors.toMap(ParamResponseDTO::getDesp1,
							ParamResponseDTO::getDesp2, (a, b) -> a));

			Map<String, String> typeToParamMap = typeParamResponseDTOs.stream()
					.collect(Collectors.toMap(ParamResponseDTO::getDesp1,
							ParamResponseDTO::getDesp2, (a, b) -> a));

			List<ContractMasterResponse> responseList = new ArrayList<>();

			for (ContractMaster contract : contractList) {

				ContractMasterResponse response = new ContractMasterResponse();

				response.setContractId(contract.getContractId());
				response.setContractNo(contract.getContractNo());
				response.setContractName(contract.getContractName());
				response.setCustomerId(contract.getCustomer().getCustomerId());
				response.setCustomerName(
						contract.getCustomer().getCustomerName());

				response.setZoneId(
						contract.getCustomer().getHierarchyLevelId());

				HierarchyLevelResponseDTO responseDTO = validationService
						.validateAndGetHierarchyLevel(
								contract.getCustomer().getHierarchyLevelId());
				response.setZoneName(responseDTO.getLevelName());

				List<CompanySiteMappingResponse> companySiteMappingResponses = validationService
						.getAllCompSiteMappingByCompId(
								contract.getCustomer().getCompId());

				response.setSiteId(
						companySiteMappingResponses.get(0).getSiteId());

				CompSiteResponse siteResponse = validationService
						.validateAndGetSite(
								companySiteMappingResponses.get(0).getSiteId(),
								"AB");

				response.setSiteName(siteResponse.getSiteName());

				response.setIfsc(siteResponse.getIfsc());

				response.setDistrict(siteResponse.getDistrict());

				List<ContractItemMapping> contractItemMappings = contractItemMappingRepository
						.findByContract_ContractId(contract.getContractId());

				List<String> productTypes = contractItemMappings.stream()
						.map(itemMapping -> {
							Integer itemId = itemMapping.getItemId();

							String category = itemIdToCategoryMap.get(itemId);

							if (category == null) {
								return null;
							}

							return categoryToParamMap.get(category);
						}).filter(Objects::nonNull).toList();

				response.setProductTypes(productTypes);

				response.setContractStartDate(contract.getContractStartDate());
				response.setContractEndDate(contract.getContractEndDate());
				response.setContractStatus(contract.getContractStatus());
				String paramType = typeToParamMap
						.get(contract.getContractType());
				response.setContractType(paramType);
				response.setBillingFrequency(contract.getBillingFrequency());
				response.setAmcType(contract.getAmcType());
				response.setTermCondition(contract.getTermCondition());
				response.setPaymentTerms(contract.getPaymentTerms());
				response.setIsActive(contract.getIsActive());
				response.setCreatedOn(contract.getCreatedOn());
				response.setModifiedOn(contract.getModifiedOn());
				response.setCreatedBy(contract.getCreatedBy());
				response.setModifiedBy(contract.getModifiedBy());

				responseList.add(response);
			}

			logger.info("Successfully fetched {} contracts for type: {}",
					responseList.size(), type);

			return new ResponseEntity("Contracts fetched successfully", 200,
					responseList);

		} catch (ResourceNotFoundException ex) {
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while fetching contracts by type: {}", type,
					ex);
			throw new BadRequestException(
					"Something went wrong while fetching contracts");
		}
	}
}