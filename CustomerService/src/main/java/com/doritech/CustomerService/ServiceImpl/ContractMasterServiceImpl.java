package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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

import com.doritech.CustomerService.Entity.ContractDocuments;
import com.doritech.CustomerService.Entity.ContractEntityMapping;
import com.doritech.CustomerService.Entity.ContractInstallationDetails;
import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.QuotationDetail;
import com.doritech.CustomerService.Entity.QuotationDocument;
import com.doritech.CustomerService.Entity.QuotationMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;

import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.DatabaseOperationException;
import com.doritech.CustomerService.Exception.DuplicateResourceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.ContractMapper;
import com.doritech.CustomerService.Projection.ContractItemPackageProjection;
import com.doritech.CustomerService.Repository.ContractDocumentRepository;
import com.doritech.CustomerService.Repository.ContractEntityMappingRepository;
import com.doritech.CustomerService.Repository.ContractInstallationDetailsRepository;
import com.doritech.CustomerService.Repository.ContractItemMappingRepository;
import com.doritech.CustomerService.Repository.ContractItemPackageRepository;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Repository.QuotationDetailRepository;
import com.doritech.CustomerService.Repository.QuotationDocumentRepository;
import com.doritech.CustomerService.Repository.QuotationMasterRepository;
import com.doritech.CustomerService.Repository.StockRequestDetailsRepository;

import com.doritech.CustomerService.Request.ContractInstallationRequest;
import com.doritech.CustomerService.Request.ContractMasterRequest;
import com.doritech.CustomerService.Request.StockDeliveryChallanRequest;
import com.doritech.CustomerService.Request.StockRequestDetailRequest;
import com.doritech.CustomerService.Request.StockRequestRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.CompanySiteMappingResponse;
import com.doritech.CustomerService.Response.ContractDocumentResponse;
import com.doritech.CustomerService.Response.ContractEntityMappingResponse;
import com.doritech.CustomerService.Response.ContractFullResponseDTO;
import com.doritech.CustomerService.Response.ContractItemMappingResponse;
import com.doritech.CustomerService.Response.ContractItemPackageResponse;
import com.doritech.CustomerService.Response.ContractMasterResponse;
import com.doritech.CustomerService.Response.HierarchyLevelResponseDTO;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Response.ParamResponseDTO;
import com.doritech.CustomerService.Response.QuotationDetailResponse;
import com.doritech.CustomerService.Response.QuotationDocumentResponse;
import com.doritech.CustomerService.Response.QuotationMasterResponse;
import com.doritech.CustomerService.Service.ContractMasterService;
import com.doritech.CustomerService.Specification.ContractSpecification;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
public class ContractMasterServiceImpl implements ContractMasterService {

	private static final Logger logger = LoggerFactory.getLogger(ContractMasterServiceImpl.class);

	@Autowired
	private ContractMasterRepository contractRepository;
	@Autowired
	private CustomerMasterRepository customerRepository;

	@Autowired
	private ContractEntityMappingRepository contractEntityMappingRepository;

	@Autowired
	private ContractItemMappingRepository contractItemMappingRepository;

	@Autowired
	private ContractDocumentRepository contractDocumentRepository;

	@Autowired
	private QuotationMasterRepository quotationMasterRepository;

	@Autowired
	private ContractItemPackageRepository contractItemPackageRepository;

	@Autowired
	private QuotationDetailRepository quotationDetailRepository;

	@Autowired
	private QuotationDocumentRepository quotationDocumentRepository;

	@Autowired
	private ValidationService validationService;
	@Autowired
	ContractInstallationDetailsRepository installationRepository;

	@Autowired
	private StockRequestServiceImpl stockRequestService;

	@Autowired
	private StockRequestDetailsRepository stockRequestDetailsRepository;

	@Autowired
	private StockDeliveryChallanServiceImpl stockDeliveryChallanService;

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

		if (!customerRepository.existsById(request.getCustomerId())) {
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
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getContractById(Integer id) {

		logger.info("Get Contract By ID API called {}", id);

		if (id == null) {
			logger.error("Contract ID is null");
			throw new BadRequestException("Contract ID cannot be null");
		}

		ContractMaster contract = contractRepository.findById(id).orElseThrow(() -> {
			logger.error("Contract not found for id {}", id);
			return new ResourceNotFoundException("Contract not found");
		});

		logger.info("Contract fetched successfully {}", id);

		return new ResponseEntity("Success", 200, ContractMapper.toResponse(contract));
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

		Pageable pageable = PageRequest.of(page, size, Sort.by("contractId").descending());

		Page<ContractMaster> contractPage = contractRepository.findAll(pageable);

		List<ContractMasterResponse> response = contractPage.getContent().stream().map(ContractMapper::toResponse)
				.toList();

		Map<String, Object> result = new HashMap<>();
		result.put("contracts", response);
		result.put("currentPage", contractPage.getNumber());
		result.put("totalItems", contractPage.getTotalElements());
		result.put("totalPages", contractPage.getTotalPages());

		logger.info("Fetched {} contracts", response.size());

		return new ResponseEntity("Success", 200, result);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllInstallationContracts(int page, int size) {

		logger.info("Get All Contracts API called page {} size {}", page, size);

		if (page < 0) {
			page = 0;
		}

		if (size <= 0) {
			size = 10;
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("contractId").descending());

		Page<ContractMaster> contractPage =
		        contractRepository.findByContractTypeIgnoreCaseAndIsActive(
		                "IN",
		                "Y",
		                pageable
		        );
		if (contractPage.isEmpty()) {
			logger.warn("No IN type contracts found");
			throw new ResourceNotFoundException("No Installation type contracts found");
		}

		List<ContractMasterResponse> response = contractPage.getContent().stream().map(ContractMapper::toResponse)
				.toList();

		Map<String, Object> result = new HashMap<>();
		result.put("contracts", response);
		result.put("currentPage", contractPage.getNumber());
		result.put("totalItems", contractPage.getTotalElements());
		result.put("totalPages", contractPage.getTotalPages());

		logger.info("Fetched {} IN type contracts", response.size());

		return new ResponseEntity("Success", 200, result);
	}

	@Override
	@Transactional
	public ResponseEntity deactivateContracts(List<Integer> ids) {

		logger.info("Deactivate Contract API called for ids: {}", ids);

		if (ids == null || ids.isEmpty()) {
			logger.error("Contract IDs list is null or empty");
			throw new BadRequestException("Contract IDs list cannot be null or empty");
		}

		List<Integer> notFoundIds = new ArrayList<>();
		List<Integer> deactivatedIds = new ArrayList<>();

		for (Integer id : ids) {
			Optional<ContractMaster> contractOpt = contractRepository.findById(id);

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

		logger.info("Contracts deactivated: {}, Not found: {}", deactivatedIds, notFoundIds);

		if (!notFoundIds.isEmpty() && !deactivatedIds.isEmpty()) {
			String message = "Partially deactivated. Success: " + deactivatedIds + ", Not found: " + notFoundIds;
			return new ResponseEntity(message, 207, null);
		}

		if (deactivatedIds.isEmpty()) {
			throw new ResourceNotFoundException("No contracts found for given IDs: " + notFoundIds);
		}

		return new ResponseEntity("Contract(s) deactivated successfully", 200, null);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity filterContracts(String contractNo, Integer customerId, String contractType, String isActive) {

		logger.info("Filter Contracts API called");

		if (contractNo == null && customerId == null && contractType == null && isActive == null) {
			throw new BadRequestException("At least one filter parameter must be provided");
		}

		List<ContractMaster> contracts = contractRepository
				.findAll(ContractSpecification.filterContracts(contractNo, customerId, contractType, isActive));

		if (contracts.isEmpty()) {
			logger.warn("No contracts found for given filters");
			throw new ResourceNotFoundException("No contracts found");
		}

		List<ContractMasterResponse> response = contracts.stream().map(ContractMapper::toResponse).toList();

		logger.info("Filtered contracts fetched {}", response.size());

		return new ResponseEntity("Contracts fetched successfully", 200, response);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getContractNamesAndIds() {

		logger.info("Fetching active contract IDs and names");

		List<ContractMaster> contracts = contractRepository.getActiveContracts();

		if (contracts == null || contracts.isEmpty()) {
			logger.warn("No active contracts found");
			throw new ResourceNotFoundException("No active contracts found");
		}

		List<ContractMasterResponse> simplified = contracts.stream().filter(c -> "Y".equalsIgnoreCase(c.getIsActive()))
				.map(c -> {
					ContractMasterResponse temp = new ContractMasterResponse();
					temp.setContractId(c.getContractId());
					temp.setContractName(c.getContractName());
					temp.setContractNo(c.getContractNo());
					return temp;
				}).toList();

		logger.info("Fetched {} active contracts", simplified.size());

		return new ResponseEntity("Contracts fetched successfully", 200, simplified);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getContractDetailsByType(String type) {

		logger.info("Fetching contracts by type: {}", type);
		if (type == null || type.trim().isEmpty()) {
			logger.error("Contract type is null or empty");
			throw new BadRequestException("Contract type cannot be null or empty");
		}

		try {

			List<ContractMaster> contractList = contractRepository.findAvailableContracts(type, "PENDING");

			if (contractList == null || contractList.isEmpty()) {
				logger.warn("No contracts found for type: {}", type);
				throw new ResourceNotFoundException("No contracts found for given type");
			}

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

			List<ContractMasterResponse> responseList = new ArrayList<>();

			for (ContractMaster contract : contractList) {

				ContractMasterResponse response = new ContractMasterResponse();

				response.setContractId(contract.getContractId());
				response.setContractNo(contract.getContractNo());
				response.setContractName(contract.getContractName());
				response.setCustomerId(contract.getCustomer().getCustomerId());
				response.setCustomerName(contract.getCustomer().getCustomerName());

				response.setZoneId(contract.getCustomer().getHierarchyLevelId());

				HierarchyLevelResponseDTO responseDTO = validationService
						.validateAndGetHierarchyLevel(contract.getCustomer().getHierarchyLevelId());
				response.setZoneName(responseDTO.getLevelName());

				List<CompanySiteMappingResponse> companySiteMappingResponses = validationService
						.getAllCompSiteMappingByCompId(contract.getCustomer().getCompId());

				response.setSiteId(companySiteMappingResponses.get(0).getSiteId());

				CompSiteResponse siteResponse = validationService
						.validateAndGetSite(companySiteMappingResponses.get(0).getSiteId(), "AB");

				response.setSiteName(siteResponse.getSiteName());

				response.setIfsc(contract.getCustomer().getIfsc());

				response.setDistrict(siteResponse.getDistrict());

				List<ContractItemMapping> contractItemMappings = contractItemMappingRepository
						.findByContract_ContractId(contract.getContractId());

				List<String> productTypes = contractItemMappings.stream().map(itemMapping -> {
					Integer itemId = itemMapping.getItemId();

					String category = itemIdToCategoryMap.get(itemId);

					if (category == null) {
						return null;
					}

					return categoryToParamMap.get(category);
				}).filter(Objects::nonNull).distinct().toList();

				response.setProductTypes(productTypes);

				response.setContractStartDate(contract.getContractStartDate());
				response.setContractEndDate(contract.getContractEndDate());
				response.setContractStatus(contract.getContractStatus());
				String paramType = typeToParamMap.get(contract.getContractType());
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

			logger.info("Successfully fetched {} contracts for type: {}", responseList.size(), type);

			return new ResponseEntity("Contracts fetched successfully", 200, responseList);

		} catch (ResourceNotFoundException ex) {
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while fetching contracts by type: {}", type, ex);
			throw new DatabaseOperationException("Failed to fetch contracts by type");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllActiveContractsByType(String type) {

		logger.info("Fetching contracts by type: {}", type);
		if (type == null || type.trim().isEmpty()) {
			logger.error("Contract type is null or empty");
			throw new BadRequestException("Contract type cannot be null or empty");
		}

		try {

			List<ContractMaster> contractList = contractRepository.findByContractTypeAndIsActive(type, "Y");

			if (contractList == null || contractList.isEmpty()) {
				logger.warn("No contracts found for type: {}", type);
				throw new ResourceNotFoundException("No contracts found for given type");
			}

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

			List<ContractMasterResponse> responseList = new ArrayList<>();

			for (ContractMaster contract : contractList) {

				ContractMasterResponse response = new ContractMasterResponse();

				response.setContractId(contract.getContractId());
				response.setContractNo(contract.getContractNo());
				response.setContractName(contract.getContractName());
				response.setCustomerId(contract.getCustomer().getCustomerId());
				response.setCustomerName(contract.getCustomer().getCustomerName());

				response.setZoneId(contract.getCustomer().getHierarchyLevelId());

				HierarchyLevelResponseDTO responseDTO = validationService
						.validateAndGetHierarchyLevel(contract.getCustomer().getHierarchyLevelId());
				response.setZoneName(responseDTO.getLevelName());

				List<CompanySiteMappingResponse> companySiteMappingResponses = validationService
						.getAllCompSiteMappingByCompId(contract.getCustomer().getCompId());

				response.setSiteId(companySiteMappingResponses.get(0).getSiteId());

				CompSiteResponse siteResponse = validationService
						.validateAndGetSite(companySiteMappingResponses.get(0).getSiteId(), "AB");

				response.setSiteName(siteResponse.getSiteName());

				response.setIfsc(contract.getCustomer().getIfsc());

				response.setDistrict(siteResponse.getDistrict());

				List<ContractItemMapping> contractItemMappings = contractItemMappingRepository
						.findByContract_ContractId(contract.getContractId());

				List<String> productTypes = contractItemMappings.stream().map(itemMapping -> {
					Integer itemId = itemMapping.getItemId();

					String category = itemIdToCategoryMap.get(itemId);

					if (category == null) {
						return null;
					}

					return categoryToParamMap.get(category);
				}).filter(Objects::nonNull).distinct().toList();

				response.setProductTypes(productTypes);

				response.setContractStartDate(contract.getContractStartDate());
				response.setContractEndDate(contract.getContractEndDate());
				response.setContractStatus(contract.getContractStatus());
				String paramType = typeToParamMap.get(contract.getContractType());
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

			logger.info("Successfully fetched {} contracts for type: {}", responseList.size(), type);

			return new ResponseEntity("Contracts fetched successfully", 200, responseList);

		} catch (ResourceNotFoundException ex) {
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while fetching contracts by type: {}", type, ex);
			throw new DatabaseOperationException("Failed to fetch contracts by type");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getContractNamesAndIdsForFillter() {

		logger.info("Fetching active contract IDs and names");

		List<ContractMaster> contracts = contractRepository.getActiveContracts();

		if (contracts == null || contracts.isEmpty()) {
			logger.warn("No active contracts found");
			throw new ResourceNotFoundException("No active contracts found");
		}

		List<ContractMasterResponse> simplified = contracts.stream().map(c -> {
			ContractMasterResponse temp = new ContractMasterResponse();
			temp.setContractId(c.getContractId());
			temp.setContractName(c.getContractName());
			temp.setContractNo(c.getContractNo());
			return temp;
		}).toList();

		logger.info("Fetched {} active contracts", simplified.size());

		return new ResponseEntity("Contracts fetched successfully", 200, simplified);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getFullContractDetails(Integer contractId) {

		if (contractId == null) {
			throw new BadRequestException("Contract ID cannot be null");
		}

		// ================= CONTRACT =================
		ContractMaster contract = contractRepository.findById(contractId)
				.orElseThrow(() -> new ResourceNotFoundException("Contract not found"));

		ContractFullResponseDTO response = new ContractFullResponseDTO();

		response.setContractId(contract.getContractId());
		response.setContractNo(contract.getContractNo());
		response.setContractName(contract.getContractName());
		response.setCustomerId(contract.getCustomer().getCustomerId());
		response.setCustomerName(contract.getCustomer().getCustomerName());

		response.setContractStartDate(contract.getContractStartDate());
		response.setContractEndDate(contract.getContractEndDate());
		response.setContractStatus(contract.getContractStatus());
		response.setContractType(contract.getContractType());
		response.setBillingFrequency(contract.getBillingFrequency());
		response.setAmcType(contract.getAmcType());
		response.setTermCondition(contract.getTermCondition());
		response.setPaymentTerms(contract.getPaymentTerms());
		response.setIsActive(contract.getIsActive());

		// ================= FETCH ALL DATA IN BULK =================
		List<ContractEntityMapping> entityMappings = contractEntityMappingRepository
				.findByContractContractId(contractId);

		List<ContractItemMapping> itemMappings = contractItemMappingRepository.findByContract_ContractId(contractId);

		List<ContractItemPackageProjection> allPackages = contractItemPackageRepository.findAllByContractId(contractId); // 🔥
																															// custom
																															// query
																															// needed

		List<ContractDocuments> documents = contractDocumentRepository.findByContractId(contractId);

		List<QuotationMaster> quotations = quotationMasterRepository.findByContract_ContractId(contractId);

		List<Integer> quotationIds = quotations.stream().map(QuotationMaster::getQuotationId).toList();

		List<QuotationDetail> allQuotationDetails = quotationDetailRepository.findByQuotationIds(quotationIds);

		List<QuotationDocument> allQuotationDocs = quotationDocumentRepository.findByQuotationIds(quotationIds);

		// ================= COLLECT IDS FOR BULK API =================
		Set<Integer> itemIds = new HashSet<>();
		Set<Integer> siteIds = new HashSet<>();

		itemMappings.forEach(i -> itemIds.add(i.getItemId()));
		allPackages.forEach(p -> itemIds.add(p.getMappedItemId()));
		allQuotationDetails.forEach(q -> {
			itemIds.add(q.getItemId());
			if (q.getParentItemId() != null)
				itemIds.add(q.getParentItemId());
			siteIds.add(q.getSiteId());
		});
		entityMappings.forEach(e -> siteIds.add(e.getSiteId()));

		// ================= BULK FETCH =================
		Map<Integer, ItemIDResponse> itemMap = validationService.getAllItems().stream()
				.collect(Collectors.toMap(ItemIDResponse::getItemId, i -> i));

		Map<Integer, CompSiteResponse> siteMap = validationService.getAllSites().stream()
				.collect(Collectors.toMap(CompSiteResponse::getSiteId, s -> s));

		// ================= ENTITY MAPPING =================
		response.setEntityMappings(entityMappings.stream().map(mapping -> {

			ContractEntityMappingResponse dto = new ContractEntityMappingResponse();

			dto.setMappingId(mapping.getMappingId());
			dto.setCustomerId(mapping.getCustomer().getCustomerId());
			dto.setCustomerName(mapping.getCustomer().getCustomerName());
			dto.setCustomerCode(mapping.getCustomer().getCustomerCode());

			dto.setSiteId(mapping.getSiteId());

			CompSiteResponse site = siteMap.get(mapping.getSiteId());
			dto.setSiteName(site != null ? site.getSiteName() : null);
			dto.setSiteCode(site != null ? site.getSiteCode() : null);
			dto.setSiteDistrictName(site != null ? site.getDistrict() : null);

			dto.setMinNoVisits(mapping.getMinNoVisits());
			dto.setVisitsFrequency(mapping.getVisitsFrequency());
			dto.setVisitsPaid(mapping.getVisitsPaid());
			dto.setIsActive(mapping.getIsActive());

			return dto;

		}).toList());

		// ================= GROUP PACKAGES =================
		Map<Integer, List<ContractItemPackageProjection>> packageMap = allPackages.stream()
				.collect(Collectors.groupingBy(p -> p.getContractMappingId()));

		// ================= ITEM MAPPING =================
		response.setItemMappings(itemMappings.stream().map(mapping -> {

			ContractItemMappingResponse dto = new ContractItemMappingResponse();

			dto.setContractMappingId(mapping.getContractMappingId());
			dto.setItemId(mapping.getItemId());

			ItemIDResponse item = itemMap.get(mapping.getItemId());
			dto.setItemName(item != null ? item.getItemName() : null);

			dto.setQuantity(mapping.getQuantity());
			dto.setUnitPrice(mapping.getUnitPrice());
			dto.setMandatoryQuotation(mapping.getMandatoryQuotation());
			dto.setWarrantyPeriod(mapping.getWarrantyPeriod());
			dto.setAmcRate(mapping.getAmcRate());
			dto.setBuyBackItemId(mapping.getBuyBackItemId());
			dto.setBuyBackUnitPrice(mapping.getBuyBackUnitPrice());
			dto.setApprovalRequired(mapping.getApprovalRequired());
			dto.setIsActive(mapping.getIsActive());

			// PACKAGES
			List<ContractItemPackageProjection> packages = packageMap.getOrDefault(mapping.getContractMappingId(),
					List.of());

			dto.setPackages(packages.stream().map(p -> {

				ContractItemPackageResponse pr = new ContractItemPackageResponse();

				pr.setPackageId(p.getPackageId());
				pr.setMappedItemId(p.getMappedItemId());
				pr.setMappedItemName(p.getMappedItemName());
				pr.setMappingItemCode(p.getMappingItemCode());
				pr.setMappingItemName(p.getMappingItemName());

				pr.setIsActive(p.getIsActive());

				return pr;

			}).toList());

			return dto;

		}).toList());

		// ================= DOCUMENTS =================
		response.setDocuments(documents.stream().map(doc -> {
			ContractDocumentResponse dto = new ContractDocumentResponse();
			dto.setDocumentId(doc.getDocumentId());
			dto.setDocumentName(doc.getDocumentName());
			dto.setDocumentType(doc.getDocumentType());
			dto.setUploadDate(doc.getUploadDate());
			dto.setIsActive(doc.getIsActive());
			return dto;
		}).toList());

		// ================= GROUP QUOTATION DATA =================
		Map<Integer, List<QuotationDetail>> detailMap = allQuotationDetails.stream()
				.collect(Collectors.groupingBy(d -> d.getQuotationMaster().getQuotationId()));

		Map<Integer, List<QuotationDocument>> docMap = allQuotationDocs.stream()
				.collect(Collectors.groupingBy(d -> d.getQuotationMaster().getQuotationId()));

		// ================= QUOTATIONS =================
		response.setQuotations(quotations.stream().map(q -> {

			QuotationMasterResponse qdto = new QuotationMasterResponse();

			qdto.setQuotationId(q.getQuotationId());
			qdto.setQuotationCode(q.getQuotationCode());
			qdto.setQuotationDate(q.getQuotationDate());
			qdto.setStatus(q.getStatus());

			// DETAILS
			List<QuotationDetail> details = detailMap.getOrDefault(q.getQuotationId(), List.of());

			qdto.setItems(details.stream().map(d -> {

				QuotationDetailResponse dto = new QuotationDetailResponse();

				dto.setQuotationDetailId(d.getQuotationDetailId());
				dto.setItemId(d.getItemId());

				ItemIDResponse item = itemMap.get(d.getItemId());
				dto.setItemName(item != null ? item.getItemName() : null);

				dto.setParentItemId(d.getParentItemId() != null ? d.getParentItemId() : null);

				if (d.getParentItemId() != null) {
					ItemIDResponse parent = itemMap.get(d.getParentItemId());
					dto.setParentItemName(parent != null ? parent.getItemName() : null);
				}

				CompSiteResponse site = siteMap.get(d.getSiteId());
				dto.setSiteName(site != null ? site.getSiteName() : null);

				dto.setQty(d.getQty());
				dto.setIsActive(d.getIsActive());

				return dto;

			}).toList());

			// DOCUMENTS
			List<QuotationDocument> docsList = docMap.getOrDefault(q.getQuotationId(), List.of());

			qdto.setDocuments(docsList.stream().map(d -> {
				QuotationDocumentResponse dto = new QuotationDocumentResponse();
				dto.setQuotationDocumentId(d.getQuotationDocumentId());
				dto.setDocumentType(d.getDocumentType());
				dto.setDocumentDate(d.getDocumentDate());
				dto.setIsActive(d.getIsActive());
				return dto;
			}).toList());

			return qdto;

		}).toList());

		return new ResponseEntity("Success", 200, response);
	}

	@Override
	@Transactional
	public ResponseEntity saveContractInstallationDetails(List<ContractInstallationRequest> requestList,
			Integer userId) {

		if (requestList == null || requestList.isEmpty()) {
			throw new BadRequestException("Request list cannot be empty");
		}

		List<Map<String, Object>> payloadList = new ArrayList<>();
		Set<String> messages = new LinkedHashSet<>();
		
		for (ContractInstallationRequest req : requestList) {

			if (req.getContractId() == null) {
				throw new BadRequestException("ContractId is required");
			}

			ContractMaster contract = contractRepository.findById(req.getContractId()).orElseThrow(
					() -> new ResourceNotFoundException("Contract not found with id: " + req.getContractId()));

			ContractInstallationDetails entity = installationRepository.findByContractContractId(req.getContractId())
					.orElseGet(() -> {
						ContractInstallationDetails newEntity = new ContractInstallationDetails();
						newEntity.setContract(contract);
						newEntity.setIsActive("Y");
						return newEntity;
					});

			Map<String, Object> payload = new HashMap<>();
			payload.put("contractId", req.getContractId());

			boolean isUpdated = false;

			if (req.getSalesOrderNumber() != null || req.getSalesOrderDate() != null) {

				if (req.getSalesOrderNumber() != null) {
					entity.setSalesOrderNumber(req.getSalesOrderNumber());
					payload.put("salesOrderNumber", req.getSalesOrderNumber());
				}

				if (req.getSalesOrderDate() != null) {
					entity.setSalesOrderDate(req.getSalesOrderDate());
					payload.put("salesOrderDate", req.getSalesOrderDate());
				}

				entity.setSalesOrderCreatedAt(LocalDateTime.now());
				entity.setSalesOrderCreatedBy(String.valueOf(userId));

				messages.add("Sales order updated");
				isUpdated = true;
			}

			if (req.getIsMaterialRequired() != null) {
				entity.setIsMaterialRequired(req.getIsMaterialRequired());
				entity.setMaterialRequestedAt(LocalDateTime.now());
				entity.setMaterialRequestedBy(String.valueOf(userId));

				payload.put("isMaterialRequired", req.getIsMaterialRequired());

				messages.add("Material requirement updated");
				isUpdated = true;

				if (Boolean.TRUE.equals(req.getIsMaterialRequired())) {
				StockRequestRequest stockReq = new StockRequestRequest();
				stockReq.setRequestedSiteId(contractEntityMappingRepository.findByContractContractId(req.getContractId()).get(0).getSiteId());
				stockReq.setSourceSiteId(5);
				stockReq.setCreatedBy(userId);

				List<ContractItemMapping> itemMappings = contractItemMappingRepository
						.findByContract_ContractId(req.getContractId());

				List<StockRequestDetailRequest> items = new ArrayList<>();
				for (ContractItemMapping item : itemMappings) {
					StockRequestDetailRequest detail = new StockRequestDetailRequest();
					detail.setItemId(item.getItemId());
					detail.setRequestedQty(item.getQuantity());
					items.add(detail);
				}

				stockReq.setItems(items);

				stockRequestService.saveStockRequest(stockReq);

			}
			}

			if (req.getMovementStatus() != null) {
				entity.setMovementStatus(req.getMovementStatus());
				entity.setStoreProcessedAt(LocalDateTime.now());
				entity.setStoreProcessedBy(String.valueOf(userId));
				payload.put("movementStatus", req.getMovementStatus());

				messages.add("Movement status updated");
				isUpdated = true;
			}

			if (req.getDocketNumber() != null || req.getLogisticsRemarks() != null) {

			    if (req.getDocketNumber() != null) {
			        entity.setDocketNumber(req.getDocketNumber());
			        payload.put("docketNumber", req.getDocketNumber());
			    }

			    if (req.getLogisticsRemarks() != null) {
			        entity.setLogisticsRemarks(req.getLogisticsRemarks());
			        payload.put("logisticsRemarks", req.getLogisticsRemarks());
			    }

			    entity.setLogisticsProcessedAt(LocalDateTime.now());
			    entity.setLogisticsProcessedBy(String.valueOf(userId));

			    messages.add("Logistics details updated");
			    isUpdated = true;
			}
			
			if (req.getBrfNumber() != null) {

			    entity.setBrfNumber(req.getBrfNumber());
			    entity.setBrfCreatedAt(LocalDateTime.now());
			    entity.setBrfCreatedBy(String.valueOf(userId));

			    payload.put("brfNumber", req.getBrfNumber());

			    messages.add("BRF number updated");
			    isUpdated = true;

				StockDeliveryChallanRequest challanReq = new StockDeliveryChallanRequest();
				challanReq.setDeliveryChallanNo(req.getDocketNumber());

				List<Integer> stockRequestIds = stockRequestDetailsRepository.findByContractId(req.getContractId())
						.stream().map(d -> d.getStockRequest().getStockRequestId()).distinct().toList();

				challanReq.setStockRequestIds(stockRequestIds);
				challanReq.setCreatedBy(userId);

				stockDeliveryChallanService.saveStockDelivery(challanReq);

			}

			if (req.getBillNumber() != null || req.getBillDate() != null || req.getBillAmount() != null
					|| req.getIsBillSubmitted() != null) {

				if (req.getBillAmount() != null && req.getBillAmount().doubleValue() < 0) {
					throw new BadRequestException("Bill amount cannot be negative");
				}

				if (req.getBillNumber() != null) {
					entity.setBillNumber(req.getBillNumber());
					payload.put("billNumber", req.getBillNumber());
				}

				if (req.getBillDate() != null) {
					entity.setBillDate(req.getBillDate());
					payload.put("billDate", req.getBillDate());
				}

				if (req.getBillAmount() != null) {
					entity.setBillAmount(req.getBillAmount());
					payload.put("billAmount", req.getBillAmount());
				}

				if (req.getIsBillSubmitted() != null) {
					entity.setIsBillSubmitted(req.getIsBillSubmitted());
					payload.put("isBillSubmitted", req.getIsBillSubmitted());
				}

				entity.setBillProcessedAt(LocalDateTime.now());
				entity.setBillProcessedBy(String.valueOf(userId));

				messages.add("Billing details updated");
				isUpdated = true;
			}

			if (!isUpdated) {
				throw new BadRequestException("At least one valid field must be provided");
			}

			installationRepository.save(entity);
			payloadList.add(payload);
		}

		String finalMessage = messages.isEmpty() ? "Installation details updated successfully"
				: String.join(", ", messages);

		return new ResponseEntity(finalMessage, 200, payloadList);
	}

	// @Override
	// @Transactional(readOnly = true)
	// public ResponseEntity getFullContractDetails(Integer contractId) {

	// logger.info("Get Contract By ID API called {}", contractId);

	// if (contractId == null) {
	// logger.error("Contract ID is null");
	// throw new BadRequestException("Contract ID cannot be null");
	// }

	// ContractMaster contract =
	// contractRepository.findById(contractId).orElseThrow(() -> {
	// logger.error("Contract not found for id {}", contractId);
	// return new ResourceNotFoundException("Contract not found");
	// });

	// ContractFullResponseDTO response = new ContractFullResponseDTO();
	// response.setContractId(contract.getContractId());
	// response.setContractNo(contract.getContractNo());
	// response.setContractName(contract.getContractName());
	// response.setCustomerId(contract.getCustomer().getCustomerId());
	// response.setCustomerName(contract.getCustomer().getCustomerName());

	// // ================= CONTRACT =================
	// response.setContractStartDate(contract.getContractStartDate());
	// response.setContractEndDate(contract.getContractEndDate());
	// response.setContractStatus(contract.getContractStatus());
	// response.setContractType(contract.getContractType());
	// response.setBillingFrequency(contract.getBillingFrequency());
	// response.setAmcType(contract.getAmcType());
	// response.setTermCondition(contract.getTermCondition());
	// response.setPaymentTerms(contract.getPaymentTerms());
	// response.setIsActive(contract.getIsActive());

	// List<ContractEntityMapping> entityMappings = contractEntityMappingRepository
	// .findByContractContractId(contract.getContractId());

	// List<ContractEntityMappingResponse> entityMappingsResponse =
	// entityMappings.stream().map(mapping -> {

	// ContractEntityMappingResponse dto = new ContractEntityMappingResponse();

	// dto.setMappingId(mapping.getMappingId());
	// dto.setCustomerId(mapping.getCustomer().getCustomerId());
	// dto.setCustomerName(mapping.getCustomer().getCustomerName());
	// dto.setCustomerCode(mapping.getCustomer().getCustomerCode());

	// dto.setSiteId(mapping.getSiteId());

	// // 👉 fetch site name (IMPORTANT)
	// CompSiteResponse site =
	// validationService.validateAndGetSite(mapping.getSiteId(), "AB");
	// dto.setSiteName(site != null ? site.getSiteName() : null);
	// dto.setSiteCode(site != null ? site.getSiteCode() : null);

	// dto.setMinNoVisits(mapping.getMinNoVisits());
	// dto.setVisitsFrequency(mapping.getVisitsFrequency());
	// dto.setVisitsPaid(mapping.getVisitsPaid());
	// dto.setIsActive(mapping.getIsActive());

	// return dto;

	// }).toList();

	// response.setEntityMappings(entityMappingsResponse);

	// List<ContractItemMapping> itemMappings = contractItemMappingRepository
	// .findByContract_ContractId(contract.getContractId());

	// List<ContractItemMappingResponse> itemMappingsResponse =
	// itemMappings.stream().map(mapping -> {

	// ContractItemMappingResponse dto = new ContractItemMappingResponse();

	// dto.setContractMappingId(mapping.getContractMappingId());
	// dto.setItemId(mapping.getItemId());

	// // 👉 fetch item name
	// ItemIDResponse itemResponse =
	// validationService.validateAndGetItem(mapping.getItemId());
	// dto.setItemName(itemResponse != null ? itemResponse.getItemName() : null);

	// dto.setQuantity(mapping.getQuantity());
	// dto.setUnitPrice(mapping.getUnitPrice());
	// dto.setMandatoryQuotation(mapping.getMandatoryQuotation());
	// dto.setWarrantyPeriod(mapping.getWarrantyPeriod());
	// dto.setAmcRate(mapping.getAmcRate());
	// dto.setBuyBackItemId(mapping.getBuyBackItemId());
	// dto.setBuyBackUnitPrice(mapping.getBuyBackUnitPrice());
	// dto.setApprovalRequired(mapping.getApprovalRequired());
	// dto.setIsActive(mapping.getIsActive());

	// // // ================= PACKAGES =================
	// List<ContractItemPackage> packages = contractItemPackageRepository
	// .findByContractItemMapping_ContractMappingId(
	// mapping.getContractMappingId());

	// List<ContractItemPackageResponse> packageResponses = packages.stream().map(p
	// -> {

	// ContractItemPackageResponse pr = new ContractItemPackageResponse();
	// pr.setPackageId(p.getPackageId());
	// pr.setMappedItemId(p.getMappedItemId());

	// ItemIDResponse itemMappedResponse2 =
	// validationService.validateAndGetItem(p.getMappedItemId());
	// pr.setMappedItemName(itemMappedResponse2 != null ?
	// itemMappedResponse2.getItemName() : null);

	// //pr.setMappingItemId(p.getContractItemMapping().getItemId());

	// ItemIDResponse itemMappedResponse =
	// validationService.validateAndGetItem(p.getContractItemMapping().getItemId());
	// pr.setMappingItemName(itemMappedResponse != null ?
	// itemMappedResponse.getItemName() : null);

	// pr.setIsActive(p.getIsActive());

	// return pr;

	// }).toList();

	// dto.setPackages(packageResponses);

	// return dto;

	// }).toList();

	// response.setItemMappings(itemMappingsResponse);

	// List<ContractDocuments> documents =
	// contractDocumentRepository.findByContractId(contract.getContractId());

	// List<ContractDocumentResponse> documentsResponse = documents.stream().map(doc
	// -> {

	// ContractDocumentResponse dto = new ContractDocumentResponse();

	// dto.setDocumentId(doc.getDocumentId());
	// dto.setDocumentName(doc.getDocumentName());
	// dto.setDocumentType(doc.getDocumentType());
	// // dto.setDocument(doc.getDocument());
	// dto.setUploadDate(doc.getUploadDate());
	// dto.setIsActive(doc.getIsActive());

	// return dto;

	// }).toList();

	// response.setDocuments(documentsResponse);

	// List<QuotationMaster> quotations = quotationMasterRepository
	// .findByContract_ContractId(contract.getContractId());

	// List<QuotationMasterResponse> quotationsResponse = quotations.stream().map(q
	// -> {

	// QuotationMasterResponse qdto = new QuotationMasterResponse();

	// qdto.setQuotationId(q.getQuotationId());
	// qdto.setQuotationCode(q.getQuotationCode());
	// qdto.setQuotationDate(q.getQuotationDate());
	// qdto.setStatus(q.getStatus());
	// qdto.setPoNo(q.getPoNo());
	// qdto.setSalesOrderNo(q.getSalesOrderNo());
	// qdto.setCommunicationMode(q.getCommunicationMode());
	// qdto.setRemarks(q.getRemarks());
	// qdto.setIsActive(q.getIsActive());
	// qdto.setCreatedBy(q.getCreatedBy());

	// // // // ========= DETAILS =========
	// List<QuotationDetail> details = quotationDetailRepository
	// .findByQuotationMaster_QuotationId(q.getQuotationId());

	// List<QuotationDetailResponse> detailResponses = details.stream().map(d -> {

	// QuotationDetailResponse dto = new QuotationDetailResponse();

	// dto.setQuotationDetailId(d.getQuotationDetailId());
	// dto.setItemId(d.getItemId());

	// ItemIDResponse itemMappedResponse =
	// validationService.validateAndGetItem(d.getItemId());
	// dto.setItemName(itemMappedResponse != null ? itemMappedResponse.getItemName()
	// : null);

	// dto.setParentItemId(d.getParentItemId());

	// if (d.getParentItemId() != null) {
	// ItemIDResponse parentItemResponse =
	// validationService.validateAndGetItem(d.getParentItemId());
	// dto.setParentItemName(parentItemResponse != null ?
	// parentItemResponse.getItemName() : null);
	// }

	// dto.setItemPrice(Double.parseDouble(d.getItemPrice().toString()));
	// dto.setGstRate(Double.parseDouble(d.getGstRate().toString()));
	// dto.setQty(d.getQty());
	// dto.setSiteId(d.getSiteId());

	// CompSiteResponse siteResponse =
	// validationService.validateAndGetSite(d.getSiteId(), "AB");
	// dto.setSiteName(siteResponse != null ? siteResponse.getSiteName() : null);

	// dto.setIsActive(d.getIsActive());

	// return dto;

	// }).toList();

	// qdto.setItems(detailResponses);

	// // // ========= DOCUMENTS =========
	// List<QuotationDocument> docs = quotationDocumentRepository
	// .findByQuotationMasterQuotationId(q.getQuotationId());

	// List<QuotationDocumentResponse> docResponses = docs.stream().map(d -> {

	// QuotationDocumentResponse dto = new QuotationDocumentResponse();

	// dto.setQuotationDocumentId(d.getQuotationDocumentId());
	// dto.setDocumentType(d.getDocumentType());
	// dto.setDocumentSource(d.getDocumentSource());
	// dto.setDocumentDate(d.getDocumentDate());
	// dto.setDocument(d.getDocument());
	// dto.setIsActive(d.getIsActive());

	// return dto;

	// }).toList();

	// qdto.setDocuments(docResponses);

	// return qdto;

	// }).toList();

	// response.setQuotations(quotationsResponse);
	// logger.info("Contract fetched successfully {}", contractId);

	// return new ResponseEntity("Success", 200, response);
	// }

}