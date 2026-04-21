package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.CustomerService.Entity.ContractCustomerItemMapping;
import com.doritech.CustomerService.Entity.ContractEntityMapping;
import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Entity.ContractItemPackage;
import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.ContractItemMappingMapper;
import com.doritech.CustomerService.Repository.ContractCustomerItemMaapingRepository;
import com.doritech.CustomerService.Repository.ContractEntityMappingRepository;
import com.doritech.CustomerService.Repository.ContractItemMappingRepository;
import com.doritech.CustomerService.Repository.ContractItemPackageRepository;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Request.ContractItemMappingRequest;
import com.doritech.CustomerService.Response.ContractItemMappingResponse;
import com.doritech.CustomerService.Response.ContractItemPackageResponse;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Service.ContractItemMappingService;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
public class ContractItemMappingServiceImpl implements ContractItemMappingService {

	private static final Logger logger = LoggerFactory.getLogger(ContractItemMappingServiceImpl.class);

	@Autowired
	private ContractItemMappingRepository repository;

	@Autowired
	private ContractItemMappingMapper mapper;

	@Autowired
	private ContractMasterRepository contractRepository;

	@Autowired
	private ValidationService validationService;

	@Autowired
	private ContractItemPackageRepository contractItemPackageRepository;

	@Autowired
	private ContractCustomerItemMaapingRepository contractCustomerItemMaapingRepository;

	@Autowired
	private ContractEntityMappingRepository contractEntityMappingRepository;

	@Value("${item.service.url}")
	private String itemServiceUrl;

	// @Override
	// @Transactional
	// public ResponseEntity
	// saveOrUpdateItemMapping(List<ContractItemMappingRequest> requests) {

	// logger.info("SaveOrUpdate ContractItemMapping API started for {} requests",
	// requests.size());

	// if (requests == null || requests.isEmpty()) {
	// logger.error("Request list cannot be empty");
	// throw new BadRequestException("Request list cannot be empty");
	// }

	// List<ContractItemMappingResponse> responseList = new ArrayList<>();
	// Set<String> uniqueCheck = new HashSet<>();

	// for (ContractItemMappingRequest request : requests) {

	// logger.info("Processing request — mappingId: {}, contractId: {}, itemId: {}",
	// request.getContractMappingId(), request.getContractId(),
	// request.getItemId());

	// ContractMaster contract =
	// contractRepository.findById(request.getContractId()).orElseThrow(() -> {
	// logger.error("Contract not found with id {}", request.getContractId());
	// return new ResourceNotFoundException("Contract not found with id : " +
	// request.getContractId());
	// });

	// logger.info("Validating item with id {}", request.getItemId());
	// validationService.validateItemExists(request.getItemId());

	// if (request.getContractMappingId() != null) {

	// logger.info("Updating existing mapping with id {}",
	// request.getContractMappingId());

	// ContractItemMapping entity =
	// repository.findById(request.getContractMappingId()).orElseThrow(() -> {
	// logger.error("ContractItemMapping not found with id {}",
	// request.getContractMappingId());
	// return new ResourceNotFoundException(
	// "ContractItemMapping not found with id : " + request.getContractMappingId());
	// });

	// boolean exists =
	// repository.existsByContract_ContractIdAndItemId(request.getContractId(),
	// request.getItemId());

	// if (exists && !entity.getItemId().equals(request.getItemId())) {
	// logger.error("Duplicate item mapping found for contractId {} and itemId {}",
	// request.getContractId(), request.getItemId());
	// throw new BadRequestException("Item already added for this contract
	// (contractId: "
	// + request.getContractId() + ", itemId: " + request.getItemId() + ")");
	// }

	// entity.setContract(contract);
	// entity.setItemId(request.getItemId());
	// entity.setUnitPrice(request.getUnitPrice());
	// if (request.getBuyBackUnitPrice() != null) {
	// entity.setBuyBackItemId(request.getItemId());
	// entity.setBuyBackUnitPrice(request.getBuyBackUnitPrice());
	// }
	// entity.setQuantity(request.getQuantity());
	// entity.setWarrantyPeriod(request.getWarrantyPeriod());
	// entity.setAmcRate(request.getAmcRate());
	// entity.setMandatoryQuotation(request.getMandatoryQuotation());
	// entity.setApprovalRequired(request.getApprovalRequired());
	// entity.setIsActive(request.getIsActive());
	// entity.setModifiedBy(request.getModifiedBy());
	// entity.setModifiedOn(LocalDateTime.now());

	// ContractItemMapping updated = repository.save(entity);

	// logger.info("Mapping updated successfully with id {}",
	// updated.getContractMappingId());
	// responseList.add(mapper.toResponse(updated));

	// } else {

	// logger.info("Creating new mapping for contractId {} and itemId {}",
	// request.getContractId(),
	// request.getItemId());

	// String key = request.getContractId() + "-" + request.getItemId();
	// if (!uniqueCheck.add(key)) {
	// logger.error("Duplicate item in request for contractId {} and itemId {}",
	// request.getContractId(), request.getItemId());
	// throw new BadRequestException("Duplicate item in request for contractId: "
	// + request.getContractId() + ", itemId: " + request.getItemId());
	// }

	// boolean exists =
	// repository.existsByContract_ContractIdAndItemId(request.getContractId(),
	// request.getItemId());

	// if (exists) {
	// logger.error("Item already mapped for contractId {} and itemId {}",
	// request.getContractId(),
	// request.getItemId());
	// throw new BadRequestException("Item already added for this contract
	// (contractId: "
	// + request.getContractId() + ", itemId: " + request.getItemId() + ")");
	// }

	// ContractItemMapping entity = mapper.toEntity(request);
	// entity.setContract(contract);
	// entity.setCreatedBy(request.getCreatedBy());
	// entity.setCreatedOn(LocalDateTime.now());
	// entity.setModifiedBy(null);
	// entity.setModifiedOn(null);

	// ContractItemMapping saved = repository.save(entity);

	// logger.info("Mapping created successfully with id {}",
	// saved.getContractMappingId());
	// responseList.add(mapper.toResponse(saved));
	// }
	// }

	// logger.info("Total mappings processed successfully: {}",
	// responseList.size());
	// return new ResponseEntity("Contract Item Mapping saved/updated successfully",
	// 200, responseList);
	// }

	@Override
	@Transactional
	public ResponseEntity saveOrUpdateItemMapping(List<ContractItemMappingRequest> requests) {

		logger.info("SaveOrUpdate ContractItemMapping API started for {} requests", requests.size());

		if (requests == null || requests.isEmpty()) {
			logger.error("Request list cannot be empty");
			throw new BadRequestException("Request list cannot be empty");
		}

		List<ContractItemMappingResponse> responseList = new ArrayList<>();
		Set<String> uniqueCheck = new HashSet<>();

		for (ContractItemMappingRequest request : requests) {

			logger.info("Processing request — mappingId: {}, contractId: {}, itemId: {}",
					request.getContractMappingId(), request.getContractId(), request.getItemId());

			ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(() -> {
				logger.error("Contract not found with id {}", request.getContractId());
				return new ResourceNotFoundException("Contract not found with id : " + request.getContractId());
			});

			ContractEntityMapping entityMapping = contractEntityMappingRepository.findById(request.getEntityMappingId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"ContractEntityMapping not found with id : " + request.getEntityMappingId()));

			logger.info("Validating item with id {}", request.getItemId());
			validationService.validateItemExists(request.getItemId());

			if (request.getContractMappingId() != null) {

				logger.info("Updating existing mapping with id {}", request.getContractMappingId());

				ContractItemMapping entity = repository.findById(request.getContractMappingId()).orElseThrow(() -> {
					logger.error("ContractItemMapping not found with id {}", request.getContractMappingId());
					return new ResourceNotFoundException(
							"ContractItemMapping not found with id : " + request.getContractMappingId());
				});

				boolean exists = repository.existsByContract_ContractIdAndItemId(request.getContractId(),
						request.getItemId());

				if (exists && !entity.getItemId().equals(request.getItemId())) {
					logger.error("Duplicate item mapping found for contractId {} and itemId {}",
							request.getContractId(), request.getItemId());
					throw new BadRequestException("Item already added for this contract (contractId: "
							+ request.getContractId() + ", itemId: " + request.getItemId() + ")");
				}

				entity.setContract(contract);
				entity.setItemId(request.getItemId());
				entity.setUnitPrice(request.getUnitPrice());
				if (request.getBuyBackUnitPrice() != null) {
					entity.setBuyBackItemId(request.getItemId());
					entity.setBuyBackUnitPrice(request.getBuyBackUnitPrice());
				}
				entity.setQuantity(request.getQuantity());
				entity.setWarrantyPeriod(request.getWarrantyPeriod());
				entity.setAmcRate(request.getAmcRate());
				entity.setMandatoryQuotation(request.getMandatoryQuotation());
				entity.setApprovalRequired(request.getApprovalRequired());
				entity.setIsActive(request.getIsActive());
				entity.setModifiedBy(request.getModifiedBy());
				entity.setModifiedOn(LocalDateTime.now());

				ContractItemMapping updated = repository.save(entity);

				// 👉 SAVE IN CUSTOMER ITEM MAPPING
				saveCustomerItemMapping(entityMapping, updated, request);
				
				logger.info("Mapping updated successfully with id {}", updated.getContractMappingId());
				responseList.add(mapper.toResponse(updated));

			} else {

				logger.info("Creating new mapping for contractId {} and itemId {}", request.getContractId(),
						request.getItemId());

				String key = request.getContractId() + "-" + request.getItemId();
				if (!uniqueCheck.add(key)) {
					logger.error("Duplicate item in request for contractId {} and itemId {}",
							request.getContractId(), request.getItemId());
					throw new BadRequestException("Duplicate item in request for contractId: "
							+ request.getContractId() + ", itemId: " + request.getItemId());
				}

				boolean exists = repository.existsByContract_ContractIdAndItemId(request.getContractId(),
						request.getItemId());

				if (exists) {
					logger.error("Item already mapped for contractId {} and itemId {}", request.getContractId(),
							request.getItemId());
					throw new BadRequestException("Item already added for this contract (contractId: "
							+ request.getContractId() + ", itemId: " + request.getItemId() + ")");
				}

				ContractItemMapping entity = mapper.toEntity(request);
				entity.setContract(contract);
				entity.setCreatedBy(request.getCreatedBy());
				entity.setCreatedOn(LocalDateTime.now());
				entity.setModifiedBy(null);
				entity.setModifiedOn(null);

				ContractItemMapping saved = repository.save(entity);

				// 👉 SAVE IN CUSTOMER ITEM MAPPING
				saveCustomerItemMapping(entityMapping, saved, request);

				logger.info("Mapping created successfully with id {}", saved.getContractMappingId());
				responseList.add(mapper.toResponse(saved));
			}
		}

		logger.info("Total mappings processed successfully: {}", responseList.size());
		return new ResponseEntity("Contract Item Mapping saved/updated successfully", 200, responseList);
	}

	// 👉 HELPER METHOD TO SAVE/UPDATE CUSTOMER ITEM MAPPING
	private void saveCustomerItemMapping(ContractEntityMapping entityMapping,
			ContractItemMapping itemMapping,
			ContractItemMappingRequest request) {

		Optional<ContractCustomerItemMapping> existing = contractCustomerItemMaapingRepository
				.findByContractEntityMapping_MappingIdAndContractItemMapping_ContractMappingId(
						entityMapping.getMappingId(),
						itemMapping.getContractMappingId());

		ContractCustomerItemMapping mapping;

		if (existing.isPresent()) {
			// 🔁 UPDATE
			mapping = existing.get();
			mapping.setQuantity(request.getQuantity());
		} else {
			// 🆕 CREATE
			mapping = new ContractCustomerItemMapping();
			mapping.setContractEntityMapping(entityMapping);
			mapping.setContractItemMapping(itemMapping);
			mapping.setQuantity(request.getQuantity());
		}

		contractCustomerItemMaapingRepository.save(mapping);
	}

	@Override
	@Transactional
	public ResponseEntity updateItemMapping(Integer id, ContractItemMappingRequest request) {

		logger.info("Update ContractItemMapping API started for id {}", id);

		if (id == null) {
			logger.error("Mapping id cannot be null");
			throw new BadRequestException("Mapping id cannot be null");
		}

		if (request == null) {
			logger.error("Request body cannot be null");
			throw new BadRequestException("Request body cannot be null");
		}

		ContractItemMapping entity = repository.findById(id).orElseThrow(() -> {
			logger.error("ContractItemMapping not found for id {}", id);
			return new ResourceNotFoundException("ContractItemMapping not found with id : " + id);
		});

		ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(() -> {
			logger.error("Contract not found with id {}", request.getContractId());
			return new ResourceNotFoundException("Contract not found with id : " + request.getContractId());
		});

		logger.info("Validating item with id {}", request.getItemId());
		validationService.validateItemExists(request.getItemId());

		entity.setContract(contract);
		entity.setItemId(request.getItemId());
		entity.setUnitPrice(request.getUnitPrice());
		entity.setQuantity(request.getQuantity());
		entity.setWarrantyPeriod(request.getWarrantyPeriod());
		entity.setAmcRate(request.getAmcRate());
		entity.setMandatoryQuotation(request.getMandatoryQuotation());
		entity.setApprovalRequired(request.getApprovalRequired());
		entity.setIsActive(request.getIsActive());
		entity.setModifiedBy(request.getModifiedBy());
		entity.setModifiedOn(LocalDateTime.now());

		ContractItemMapping updated = repository.save(entity);

		logger.info("ContractItemMapping updated successfully for id {}", id);

		return new ResponseEntity("Contract Item Mapping updated successfully", 200, mapper.toResponse(updated));
	}

	@Override
	public ResponseEntity getItemMappingById(Integer id) {

		logger.info("Get ContractItemMapping by id {}", id);

		if (id == null) {
			logger.error("Mapping id cannot be null");
			throw new BadRequestException("Mapping id cannot be null");
		}

		ContractItemMapping entity = repository.findById(id).orElseThrow(() -> {
			logger.error("ContractItemMapping not found for id {}", id);
			return new ResourceNotFoundException("ContractItemMapping not found with id: " + id);
		});

		ContractItemMappingResponse response = mapper.toResponse(entity);

		if (entity.getContract() != null) {
			String contractName = entity.getContract().getContractName();
			if (contractName != null && !contractName.trim().isEmpty()) {
				response.setContractId(entity.getContract().getContractId());
				response.setContractName(contractName);
				response.setContractNo(entity.getContract().getContractNo());
			} else {
				logger.warn("Contract name is null/empty for contractId {}", entity.getContract().getContractId());
			}
		} else {
			logger.warn("Contract is null for mapping id {}", id);
		}

		try {
			ItemIDResponse item = validationService.validateAndGetItem(entity.getItemId());
			response.setItemId(item.getItemId());
			response.setItemName(item.getItemName());
		} catch (ResourceNotFoundException ex) {
			logger.warn("Item not found for itemId {}: {}", entity.getItemId(), ex.getMessage());
			response.setItemId(entity.getItemId());
			response.setItemName("Unknown Item");
		} catch (ExternalServiceException ex) {
			logger.error("External service error for itemId {}: {}", entity.getItemId(), ex.getMessage());
			response.setItemId(entity.getItemId());
			response.setItemName("Unknown Item");
		}

		logger.info("ContractItemMapping fetched successfully for id {}", id);

		return new ResponseEntity("Contract Item Mapping fetched successfully", 200, response);
	}

	@Override
	public ResponseEntity getAllItemMappings(int page, int size) {

		logger.info("Get all ContractItemMappings with pagination page {} size {}", page, size);

		if (page < 0 || size <= 0) {
			logger.error("Invalid pagination parameters page {} size {}", page, size);
			throw new BadRequestException("Invalid page or size value");
		}

		Pageable pageable = PageRequest.of(page, size);
		Page<ContractItemMapping> pageResult = repository.findAll(pageable);

		List<ContractItemMappingResponse> responses = new ArrayList<>();

		for (ContractItemMapping mapping : pageResult.getContent()) {
			ContractItemMappingResponse response = mapper.toResponse(mapping);

			if (mapping.getContract() != null) {
				String contractName = mapping.getContract().getContractName();
				if (contractName != null && !contractName.trim().isEmpty()) {
					response.setContractId(mapping.getContract().getContractId());
					response.setContractName(contractName);
					response.setContractNo(mapping.getContract().getContractNo());
				} else {
					logger.warn("Contract name is null/empty for contractId {}", mapping.getContract().getContractId());
				}
			}

			try {
				ItemIDResponse item = validationService.validateAndGetItem(mapping.getItemId());
				response.setItemId(item.getItemId());
				response.setItemName(item.getItemName());
				response.setItemCode(item.getItemCode());
			} catch (ResourceNotFoundException ex) {
				logger.warn("Item not found for itemId {}: {}", mapping.getItemId(), ex.getMessage());
				response.setItemId(mapping.getItemId());
				response.setItemName("Unknown Item");
				response.setItemCode("Unknown Item Code");
			} catch (ExternalServiceException ex) {
				logger.error("External service error for itemId {}: {}", mapping.getItemId(), ex.getMessage());
				response.setItemId(mapping.getItemId());
				response.setItemName("Unknown Item");
				response.setItemCode("Unknown Item Code");
			}

			responses.add(response);
		}

		Map<String, Object> pagination = new HashMap<>();
		pagination.put("content", responses);
		pagination.put("currentPage", pageResult.getNumber());
		pagination.put("pageSize", pageResult.getSize());
		pagination.put("totalElements", pageResult.getTotalElements());
		pagination.put("totalPages", pageResult.getTotalPages());
		pagination.put("isLast", pageResult.isLast());

		logger.info("Total Contract Item Mappings fetched {}", responses.size());

		return new ResponseEntity("Contract Item Mappings fetched successfully", 200, pagination);
	}

	@Override
	@Transactional
	public ResponseEntity deleteItemMapping(Integer id) {

		logger.info("Delete ContractItemMapping {}", id);

		if (id == null) {
			logger.error("Mapping id cannot be null");
			throw new BadRequestException("Mapping id cannot be null");
		}

		ContractItemMapping entity = repository.findById(id).orElseThrow(() -> {
			logger.error("ContractItemMapping not found for id {}", id);
			return new ResourceNotFoundException("ContractItemMapping not found with id : " + id);
		});

		entity.setIsActive("N");
		entity.setModifiedOn(LocalDateTime.now());

		repository.save(entity);

		logger.info("ContractItemMapping deactivated successfully for id {}", id);

		return new ResponseEntity("Contract Item Mapping deleted successfully", 200, null);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getContractItemByContractId(Integer contractId) {

		logger.info("Fetching Contract Items for contractId {}", contractId);

		if (contractId == null) {
			logger.error("ContractId cannot be null");
			throw new BadRequestException("ContractId cannot be null");
		}

		List<ContractItemMapping> mappings = repository.findByContract_ContractId(contractId);

		if (mappings == null || mappings.isEmpty()) {
			logger.warn("No Contract Items found for contractId {}", contractId);
			return new ResponseEntity("No data found", 200, new ArrayList<>());
		}

		Map<Integer, ItemIDResponse> cache = new ConcurrentHashMap<>();

		List<CompletableFuture<ItemIDResponse>> futures = mappings.stream()
				.map(mapping -> CompletableFuture.supplyAsync(() -> {
					Integer itemId = mapping.getItemId();
					try {
						if (cache.containsKey(itemId)) {
							return cache.get(itemId);
						} else {
							ItemIDResponse item = validationService.validateAndGetItem(itemId);
							cache.put(itemId, item);
							return item;
						}
					} catch (Exception ex) {
						logger.error("Error fetching item details for itemId {}: {}", itemId, ex.getMessage(), ex);
						ItemIDResponse fallback = new ItemIDResponse();
						fallback.setItemId(itemId);
						fallback.setItemName("Unknown Item");
						fallback.setItemCode("N/A");
						return fallback;
					}
				})).collect(Collectors.toList());

		List<ItemIDResponse> responses = futures.stream().map(CompletableFuture::join).distinct()
				.collect(Collectors.toList());

		logger.info("Total Contract Items fetched: {}", responses.size());

		return new ResponseEntity("Contract Items fetched successfully", 200, responses);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getItemByContractId(Integer contractId) {

		logger.info("Fetching Contract Item Mappings for contractId {}", contractId);

		if (contractId == null) {
			logger.error("ContractId cannot be null");
			throw new BadRequestException("ContractId cannot be null");
		}

		List<ContractItemMapping> mappings = repository.findByContract_ContractId(contractId);

		if (mappings == null || mappings.isEmpty()) {
			logger.warn("No Contract Item Mappings found for contractId {}", contractId);
			return new ResponseEntity("No data found", 200, new ArrayList<>());
		}

		Map<Integer, ItemIDResponse> cache = new HashMap<>();
		List<ContractItemMappingResponse> responses = new ArrayList<>();

		for (ContractItemMapping mapping : mappings) {

			ContractItemMappingResponse responseObj = mapper.toResponse(mapping);

			try {
				Integer itemId = mapping.getItemId();
				logger.info("Fetching item details for itemId {}", itemId);

				ItemIDResponse item;
				if (cache.containsKey(itemId)) {
					logger.debug("Item fetched from cache for itemId {}", itemId);
					item = cache.get(itemId);
				} else {
					item = validationService.validateAndGetItem(itemId);
					cache.put(itemId, item);
				}

				responseObj.setItemId(item.getItemId());
				responseObj.setItemName(item.getItemName());
				responseObj.setItemCode(item.getItemCode());

			} catch (ResourceNotFoundException ex) {
				logger.warn("Item not found for itemId {}: {}", mapping.getItemId(), ex.getMessage());
				responseObj.setItemId(mapping.getItemId());
				responseObj.setItemName("Unknown Item");
				responseObj.setItemCode("N/A");
			} catch (ExternalServiceException ex) {
				logger.error("External service error for itemId {}: {}", mapping.getItemId(), ex.getMessage());
				responseObj.setItemId(mapping.getItemId());
				responseObj.setItemName("Unknown Item");
				responseObj.setItemCode("N/A");
			}

			responses.add(responseObj);
		}

		logger.info("Total Contract Item Mappings fetched: {}", responses.size());

		return new ResponseEntity("Contract Item Mappings fetched successfully", 200, responses);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getPackageItemsByContractAndItem(Integer contractId, Integer itemId) {

		logger.info("Fetching package items for contractId {} and itemId {}", contractId, itemId);

		if (contractId == null) {
			logger.error("ContractId cannot be null");
			throw new BadRequestException("ContractId cannot be null");
		}

		if (itemId == null) {
			logger.error("ItemId cannot be null");
			throw new BadRequestException("ItemId cannot be null");
		}

		ContractItemMapping mapping = repository.findByContract_ContractIdAndItemId(contractId, itemId)
				.orElseThrow(() -> {
					logger.error("No mapping found for contractId {} and itemId {}", contractId, itemId);
					return new ResourceNotFoundException(
							"No item mapping found for contractId: " + contractId + ", itemId: " + itemId);
				});

		Integer contractMappingId = mapping.getContractMappingId();
		logger.info("Found contractMappingId {} for contractId {} and itemId {}", contractMappingId, contractId,
				itemId);

		List<ContractItemPackage> packages = contractItemPackageRepository
				.findByContractItemMapping_ContractMappingId(contractMappingId);

		if (packages == null || packages.isEmpty()) {
			logger.warn("No package items found for contractMappingId {}", contractMappingId);
			return new ResponseEntity("No package items found", 200, new ArrayList<>());
		}

		List<ContractItemPackageResponse> responses = new ArrayList<>();

		for (ContractItemPackage pkg : packages) {

			ContractItemPackageResponse response = new ContractItemPackageResponse();
			response.setPackageId(pkg.getPackageId());
			response.setContractMappingId(contractMappingId);
			response.setMappedItemId(pkg.getMappedItemId());
			response.setIsActive(pkg.getIsActive());
			response.setCreatedOn(pkg.getCreatedOn());
			response.setModifiedOn(pkg.getModifiedOn());
			response.setCreatedBy(pkg.getCreatedBy());
			response.setModifiedBy(pkg.getModifiedBy());

			try {
				logger.info("Fetching item details for mappedItemId {}", pkg.getMappedItemId());
				ItemIDResponse item = validationService.validateAndGetItem(pkg.getMappedItemId());
				logger.info("Fetched itemName: {}, basePrice: {} for mappedItemId {}", item.getItemName(),
						item.getBasePrice(), pkg.getMappedItemId());
				response.setItemName(item.getItemName());
				response.setBasePrice(item.getBasePrice());
				response.setQty(item.getQty());
			} catch (ResourceNotFoundException ex) {
				logger.warn("Item not found for mappedItemId {}: {}", pkg.getMappedItemId(), ex.getMessage());
				response.setItemName("Unknown Item");
				response.setBasePrice(null);
			} catch (ExternalServiceException ex) {
				logger.error("External service error for mappedItemId {}: {}", pkg.getMappedItemId(), ex.getMessage());
				response.setItemName("Unknown Item");
				response.setBasePrice(null);
			}

			responses.add(response);
		}

		logger.info("Total package items fetched: {} for contractMappingId {}", responses.size(), contractMappingId);
		return new ResponseEntity("Package items fetched successfully", 200, responses);
	}

	@Override
	@Transactional
	public ResponseEntity deactivateContractItemMappings(List<Integer> mappingIds, String userId) {

		logger.info("Deactivating ContractItemMappings for ids {} by user {}", mappingIds, userId);

		if (mappingIds == null || mappingIds.isEmpty()) {
			logger.error("Mapping id list cannot be null or empty");
			throw new BadRequestException("Mapping id list cannot be null or empty");
		}

		List<ContractItemMapping> mappings = repository.findAllById(mappingIds);

		if (mappings == null || mappings.isEmpty()) {
			logger.error("No ContractItemMappings found for ids {}", mappingIds);
			throw new ResourceNotFoundException("No ContractItemMappings found for given ids");
		}

		if (mappings.size() != mappingIds.size()) {
			logger.error("One or more mapping ids not found");
			throw new ResourceNotFoundException("Some mapping ids not found");
		}

		for (ContractItemMapping mapping : mappings) {
			mapping.setIsActive("N");
			mapping.setModifiedOn(LocalDateTime.now());
		}

		repository.saveAll(mappings);

		logger.info("Successfully deactivated {} ContractItemMappings", mappings.size());

		return new ResponseEntity("Contract Item Mappings deactivated successfully", 200, null);
	}
}