package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Entity.ContractItemPackage;
import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.DatabaseOperationException;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.ContractItemMappingMapper;
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

	@Value("${item.service.url}")
	private String itemServiceUrl;

	@Override
	@Transactional
	public com.doritech.CustomerService.Entity.ResponseEntity saveOrUpdateItemMapping(
			List<ContractItemMappingRequest> requests) {

		logger.info("SaveOrUpdate ContractItemMapping API started for {} requests", requests.size());

		if (requests == null || requests.isEmpty()) {
			logger.error("Request list cannot be empty");
			throw new BadRequestException("Request list cannot be empty");
		}

		try {

			List<ContractItemMappingResponse> responseList = new ArrayList<>();
			Set<String> uniqueCheck = new HashSet<>();

			for (ContractItemMappingRequest request : requests) {

				logger.info("Processing request — mappingId: {}, contractId: {}, itemId: {}",
						request.getContractMappingId(), request.getContractId(), request.getItemId());

				ContractMaster contract = contractRepository.findById(request.getContractId()).orElseThrow(() -> {
					logger.error("Contract not found with id {}", request.getContractId());
					return new ResourceNotFoundException("Contract not found with id : " + request.getContractId());
				});

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
					entity.setQuantity(request.getQuantity());
					entity.setWarrantyPeriod(request.getWarrantyPeriod());
					entity.setAmcRate(request.getAmcRate());
					entity.setMandatoryQuotation(request.getMandatoryQuotation());
					entity.setApprovalRequired(request.getApprovalRequired());
					entity.setIsActive(request.getIsActive());
					entity.setModifiedBy(request.getModifiedBy());
					entity.setModifiedOn(LocalDateTime.now());

					ContractItemMapping updated = repository.save(entity);

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

					logger.info("Mapping created successfully with id {}", saved.getContractMappingId());
					responseList.add(mapper.toResponse(saved));
				}
			}

			logger.info("Total mappings processed successfully: {}", responseList.size());
			return new com.doritech.CustomerService.Entity.ResponseEntity(
					"Contract Item Mapping saved/updated successfully", 200, responseList);

		} catch (BadRequestException ex) {
			logger.warn("BadRequestException in saveOrUpdateItemMapping: {}", ex.getMessage());
			throw ex;
		} catch (ResourceNotFoundException ex) {
			logger.warn("ResourceNotFoundException in saveOrUpdateItemMapping: {}", ex.getMessage());
			throw ex;
		} catch (ExternalServiceException ex) {
			logger.error("ExternalServiceException in saveOrUpdateItemMapping: {}", ex.getMessage());
			throw ex;
		} catch (DatabaseOperationException ex) {
			logger.error("DatabaseOperationException in saveOrUpdateItemMapping: {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Unexpected error in saveOrUpdateItemMapping: {}", ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to process ContractItemMapping");
		}
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

		try {

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

		} catch (BadRequestException ex) {
			logger.warn("BadRequestException while updating ContractItemMapping with id {}: {}", id, ex.getMessage());
			throw ex;
		} catch (ResourceNotFoundException ex) {
			logger.warn("ResourceNotFoundException while updating ContractItemMapping with id {}: {}", id,
					ex.getMessage());
			throw ex;
		} catch (ExternalServiceException ex) {
			logger.error("ExternalServiceException while updating ContractItemMapping with id {}: {}", id,
					ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Unexpected error while updating ContractItemMapping for id {}: {}", id, ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to update ContractItemMapping");
		}
	}

	@Override
	public ResponseEntity getItemMappingById(Integer id) {

		logger.info("Get ContractItemMapping by id {}", id);

		if (id == null) {
			logger.error("Mapping id cannot be null");
			throw new BadRequestException("Mapping id cannot be null");
		}

		try {
			ContractItemMapping entity = repository.findById(id).orElseThrow(() -> {
				logger.error("ContractItemMapping not found for id {}", id);
				return new ResourceNotFoundException("ContractItemMapping not found with id: " + id);
			});

			ContractItemMappingResponse response = mapper.toResponse(entity);

			try {
				ItemIDResponse item = validationService.validateAndGetItem(entity.getItemId());
				response.setItemId(item.getItemId());
				response.setItemName(item.getItemName());
			} catch (ResourceNotFoundException ex) {
				logger.warn("Item not found for itemId {}: {}", entity.getItemId(), ex.getMessage());
				response.setItemId(entity.getItemId());
				response.setItemName("Unknown Item");
			} catch (Exception ex) {
				logger.error("Error fetching item details for itemId {}: {}", entity.getItemId(), ex.getMessage());
				response.setItemId(entity.getItemId());
				response.setItemName("Unknown Item");
			}

			logger.info("ContractItemMapping fetched successfully for id {}", id);
			return new ResponseEntity("Contract Item Mapping fetched successfully", 200, response);

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn("Validation error while fetching ContractItemMapping {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {
			logger.error("Unexpected error while fetching ContractItemMapping for id {}", id, ex);
			throw new DatabaseOperationException("Failed to fetch ContractItemMapping");
		}
	}

	@Override
	public ResponseEntity getAllItemMappings(int page, int size) {

		logger.info("Get all ContractItemMappings with pagination page {} size {}", page, size);

		if (page < 0 || size <= 0) {
			logger.error("Invalid pagination parameters page {} size {}", page, size);
			throw new BadRequestException("Invalid page or size value");
		}

		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<ContractItemMapping> pageResult = repository.findAll(pageable);

			List<ContractItemMappingResponse> responses = new ArrayList<>();

			for (ContractItemMapping mapping : pageResult.getContent()) {
				ContractItemMappingResponse response = mapper.toResponse(mapping);

				try {
					ItemIDResponse item = validationService.validateAndGetItem(mapping.getItemId());
					response.setItemId(item.getItemId());
					response.setItemName(item.getItemName());
				} catch (ResourceNotFoundException ex) {
					logger.warn("Item not found for itemId {}: {}", mapping.getItemId(), ex.getMessage());
					response.setItemId(mapping.getItemId());
					response.setItemName("Unknown Item");
				} catch (Exception ex) {
					logger.error("Error fetching item details for itemId {}: {}", mapping.getItemId(), ex.getMessage());
					response.setItemId(mapping.getItemId());
					response.setItemName("Unknown Item");
				}

				responses.add(response);
			}

			logger.info("Total Contract Item Mappings fetched {}", responses.size());

			return new ResponseEntity("Contract Item Mappings fetched successfully", 200, responses);

		} catch (BadRequestException ex) {
			logger.warn("Validation error while fetching ContractItemMappings {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while fetching ContractItemMappings", ex);
			throw new DatabaseOperationException("Failed to fetch ContractItemMappings");
		}
	}

	@Override
	@Transactional
	public ResponseEntity deleteItemMapping(Integer id) {

		logger.info("Delete ContractItemMapping {}", id);

		if (id == null) {
			logger.error("Mapping id cannot be null");
			throw new BadRequestException("Mapping id cannot be null");
		}

		try {

			ContractItemMapping entity = repository.findById(id).orElseThrow(() -> {
				logger.error("ContractItemMapping not found for id {}", id);
				return new ResourceNotFoundException("ContractItemMapping not found with id : " + id);
			});

			entity.setIsActive("N");
			entity.setModifiedOn(LocalDateTime.now());

			repository.save(entity);

			logger.info("ContractItemMapping deactivated successfully for id {}", id);

			return new ResponseEntity("Contract Item Mapping deleted successfully", 200, null);

		} catch (BadRequestException | ResourceNotFoundException ex) {

			logger.warn("Validation error while deleting ContractItemMapping {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {

			logger.error("Unexpected error while deleting ContractItemMapping for id {}", id, ex);
			throw new DatabaseOperationException("Failed to delete ContractItemMapping");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getContractItemByContractId(Integer contractId) {

		logger.info("Fetching Contract Items for contractId {}", contractId);

		if (contractId == null) {
			logger.error("ContractId cannot be null");
			throw new BadRequestException("ContractId cannot be null");
		}

		try {

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

		} catch (BadRequestException ex) {
			logger.warn("Validation error while fetching Contract Items: {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while fetching Contract Items for contractId {}", contractId, ex);
			throw new DatabaseOperationException("Failed to fetch Contract Items");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getItemByContractId(Integer contractId) {

		logger.info("Fetching Contract Item Mappings for contractId {}", contractId);

		if (contractId == null) {
			logger.error("ContractId cannot be null");
			throw new BadRequestException("ContractId cannot be null");
		}

		try {

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

				} catch (Exception ex) {
					logger.error("Error fetching item details for itemId {}", mapping.getItemId(), ex);
					responseObj.setItemId(mapping.getItemId());
					responseObj.setItemName("Unknown Item");
					responseObj.setItemCode("N/A");
				}

				responses.add(responseObj);
			}

			logger.info("Total Contract Item Mappings fetched: {}", responses.size());

			return new ResponseEntity("Contract Item Mappings fetched successfully", 200, responses);

		} catch (BadRequestException ex) {
			logger.warn("Validation error while fetching ContractItemMappings: {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while fetching ContractItemMappings for contractId {}", contractId, ex);
			throw new DatabaseOperationException("Failed to fetch ContractItemMappings");
		}
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

	    try {

	        ContractItemMapping mapping = repository
	                .findByContract_ContractIdAndItemId(contractId, itemId)
	                .orElseThrow(() -> {
	                    logger.error("No mapping found for contractId {} and itemId {}", contractId, itemId);
	                    return new ResourceNotFoundException(
	                            "No item mapping found for contractId: " + contractId + ", itemId: " + itemId);
	                });

	        Integer contractMappingId = mapping.getContractMappingId();
	        logger.info("Found contractMappingId {} for contractId {} and itemId {}", contractMappingId, contractId, itemId);

	        List<ContractItemPackage> packages =
	                contractItemPackageRepository.findByContractItemMapping_ContractMappingId(contractMappingId);

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
	          //  response.setQty(pkg.getQty());

	            try {
	                logger.info("Fetching item details for mappedItemId {}", pkg.getMappedItemId());

	                ItemIDResponse item = validationService.validateAndGetItem(pkg.getMappedItemId());

	                logger.info("Fetched itemName: {}, basePrice: {} for mappedItemId {}",
	                        item.getItemName(), item.getBasePrice(), pkg.getMappedItemId());

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
	            } catch (Exception ex) {
	                logger.error("Unexpected error fetching item details for mappedItemId {}: {}", pkg.getMappedItemId(), ex.getMessage());
	                response.setItemName("Unknown Item");
	                response.setBasePrice(null);
	            }

	            responses.add(response);
	        }

	        logger.info("Total package items fetched: {} for contractMappingId {}", responses.size(), contractMappingId);
	        return new ResponseEntity("Package items fetched successfully", 200, responses);

	    } catch (BadRequestException | ResourceNotFoundException ex) {
	        logger.warn("Validation error: {}", ex.getMessage());
	        throw ex;
	    } catch (Exception ex) {
	        logger.error("Unexpected error while fetching package items for contractId {} itemId {}", contractId, itemId, ex);
	        throw new DatabaseOperationException("Failed to fetch package items");
	    }
	}


@Override
	@Transactional
	public ResponseEntity deactivateContractItemMappings(List<Integer> mappingIds, String userId) {

		logger.info("Deactivating ContractItemMappings for ids {} by user {}", mappingIds, userId);

		if (mappingIds == null || mappingIds.isEmpty()) {
			logger.error("Mapping id list cannot be null or empty");
			throw new BadRequestException("Mapping id list cannot be null or empty");
		}

		try {

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

		} catch (BadRequestException | ResourceNotFoundException ex) {

			logger.warn("Validation failure during bulk deactivation: {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {

			logger.error("Error occurred while deactivating ContractItemMappings for ids {}", mappingIds, ex);
			throw new DatabaseOperationException("Failed to deactivate ContractItemMappings");
		}
	}}