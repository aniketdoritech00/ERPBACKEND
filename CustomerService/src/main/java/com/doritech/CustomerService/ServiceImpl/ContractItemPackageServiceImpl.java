package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.boot.MappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Entity.ContractItemPackage;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.DatabaseOperationException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.ContractItemPackageMapper;
import com.doritech.CustomerService.Projection.ContractItemPackageProjection;
import com.doritech.CustomerService.Repository.ContractItemMappingRepository;
import com.doritech.CustomerService.Repository.ContractItemPackageRepository;
import com.doritech.CustomerService.Request.ContractItemPackageRequest;
import com.doritech.CustomerService.Response.ContractItemPackageResponse;
import com.doritech.CustomerService.Service.ContractItemPackageService;

import jakarta.transaction.Transactional;

@Service
public class ContractItemPackageServiceImpl implements ContractItemPackageService {

	private static final Logger logger = LoggerFactory.getLogger(ContractItemPackageServiceImpl.class);

	@Autowired
	private ContractItemPackageRepository repository;

	@Autowired
	private ContractItemPackageMapper mapper;

	@Autowired
	ContractItemMappingRepository mappingRepository;

	@Override
	@Transactional
	public ResponseEntity saveOrUpdatePackageList(List<ContractItemPackageRequest> requests) {

		logger.info("SaveOrUpdate ContractItemPackage List API started");

		if (requests == null || requests.isEmpty()) {
			logger.error("Request list cannot be null or empty");
			throw new BadRequestException("Request list cannot be null or empty");
		}

		try {

			List<Object> responseList = new ArrayList<>();

			for (ContractItemPackageRequest request : requests) {

				if (request == null) {
					logger.error("Individual request body cannot be null");
					throw new BadRequestException("Individual request body cannot be null");
				}

				logger.info("Checking ContractItemMapping existence for id {}", request.getContractMappingId());

				ContractItemMapping mapping = mappingRepository.findById(request.getContractMappingId())
						.orElseThrow(() -> {
							logger.error("Contract mapping not found with id {}", request.getContractMappingId());
							return new ResourceNotFoundException(
									"Contract mapping not found with id: " + request.getContractMappingId());
						});

				logger.info("Checking duplicate record for contractMappingId {} and mappedItemId {}",
						request.getContractMappingId(), request.getMappedItemId());

				boolean exists;

				if (request.getPackageId() != null) {
					exists = repository.existsByContractItemMappingAndMappedItemIdAndPackageIdNot(mapping,
							request.getMappedItemId(), request.getPackageId());
				} else {
					exists = repository.existsByContractItemMappingAndMappedItemId(mapping, request.getMappedItemId());
				}

				if (exists) {
					logger.error("Record already exists for contractMappingId {} and mappedItemId {}",
							request.getContractMappingId(), request.getMappedItemId());
					throw new BadRequestException("Record already exists with same contractMappingId and mappedItemId");
				}

				if (request.getPackageId() != null) {

					logger.info("Updating ContractItemPackage with id {}", request.getPackageId());

					ContractItemPackage entity = repository.findById(request.getPackageId()).orElseThrow(() -> {
						logger.error("Package not found with id {}", request.getPackageId());
						return new ResourceNotFoundException("Package not found with id: " + request.getPackageId());
					});

					entity.setContractItemMapping(mapping);
					entity.setMappedItemId(request.getMappedItemId());
					entity.setIsActive(request.getIsActive());
					entity.setModifiedBy(request.getModifiedBy());
					entity.setModifiedOn(LocalDateTime.now());

					ContractItemPackage updated = repository.save(entity);

					logger.info("ContractItemPackage updated successfully with id {}", updated.getPackageId());

					responseList.add(mapper.toResponse(updated));

				} else {

					logger.info("Creating new ContractItemPackage");

					ContractItemPackage entity = mapper.toEntity(request);
					entity.setContractItemMapping(mapping);
					entity.setCreatedOn(LocalDateTime.now());
					entity.setModifiedBy(null);
					entity.setModifiedOn(null);

					ContractItemPackage saved = repository.save(entity);

					logger.info("ContractItemPackage created successfully with id {}", saved.getPackageId());

					responseList.add(mapper.toResponse(saved));
				}
			}

			logger.info("All ContractItemPackage records processed successfully, total: {}", responseList.size());

			return new ResponseEntity("ContractItemPackage list processed successfully", 200, responseList);

		} catch (BadRequestException ex) {
			logger.warn("BadRequestException in saveOrUpdatePackageList: {}", ex.getMessage());
			throw ex;
		} catch (ResourceNotFoundException ex) {
			logger.warn("ResourceNotFoundException in saveOrUpdatePackageList: {}", ex.getMessage());
			throw ex;
		} catch (MappingException ex) {
			logger.error("MappingException in saveOrUpdatePackageList: {}", ex.getMessage());
			throw ex;
		} catch (DatabaseOperationException ex) {
			logger.error("DatabaseOperationException in saveOrUpdatePackageList: {}", ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Unexpected error in saveOrUpdatePackageList: {}", ex.getMessage(), ex);
			throw new DatabaseOperationException("Failed to process ContractItemPackage list");
		}
	}

	@Override
	public ResponseEntity updatePackage(Integer id, ContractItemPackageRequest request) {

		logger.info("Updating ContractItemPackage with id {}", id);

		try {

			if (id == null) {
				logger.error("Update ContractItemPackage failed: id is null");
				return new ResponseEntity("ID cannot be null", HttpStatus.BAD_REQUEST.value(), null);
			}

			if (request == null) {
				logger.error("Update ContractItemPackage failed: request is null for id {}", id);
				return new ResponseEntity("Request body cannot be null", HttpStatus.BAD_REQUEST.value(), null);
			}

			ContractItemPackage entity = repository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

			logger.info("Checking ContractItemMapping existence for id {}", request.getContractMappingId());

			ContractItemMapping mapping = mappingRepository.findById(request.getContractMappingId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Contract mapping not found with id: " + request.getContractMappingId()));

			entity.setContractItemMapping(mapping);
			entity.setMappedItemId(request.getMappedItemId());
			entity.setIsActive(request.getIsActive());
			entity.setModifiedBy(request.getModifiedBy());
			entity.setModifiedOn(LocalDateTime.now());

			ContractItemPackage updated = repository.save(entity);

			logger.info("ContractItemPackage updated successfully with id {}", id);

			logger.info("Mapping updated entity to response for id {}", id);

			return new ResponseEntity("ContractItemPackage updated successfully", HttpStatus.OK.value(),
					mapper.toResponse(updated));

		} catch (ResourceNotFoundException e) {

			logger.error("Resource not found while updating ContractItemPackage id {}: {}", id, e.getMessage());

			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);

		} catch (MappingException e) {

			logger.error("Mapping error during updatePackage for id {}: {}", id, e.getMessage());

			return new ResponseEntity("Mapping error: " + e.getMessage(), HttpStatus.BAD_REQUEST.value(), null);

		} catch (Exception e) {

			logger.error("Unexpected error while updating ContractItemPackage with id {}: {}", id, e.getMessage(), e);

			return new ResponseEntity("Internal server error: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getPackageById(Integer id) {
		logger.info("Fetching ContractItemPackage with id {}", id);
		try {
			if (id == null) {
				return new ResponseEntity("ID cannot be null", HttpStatus.BAD_REQUEST.value(), null);
			}

			ContractItemPackageProjection projection = repository.findDetailsById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

			return new ResponseEntity("ContractItemPackage fetched successfully", HttpStatus.OK.value(), projection);

		} catch (ResourceNotFoundException e) {
			logger.error("ContractItemPackage not found with id {}: {}", id, e.getMessage());
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
		} catch (Exception e) {
			logger.error("Unexpected error while fetching ContractItemPackage with id {}: {}", id, e.getMessage(), e);
			return new ResponseEntity("Internal server error: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getAllPackages(int page, int size) {
		logger.info("Fetching all ContractItemPackages with page {} and size {}", page, size);
		try {
			if (page < 0 || size <= 0) {
				logger.error("Invalid pagination parameters page {} size {}", page, size);
				throw new BadRequestException("Invalid page or size value");
			}

			Pageable pageable = PageRequest.of(page, size);
			Page<ContractItemPackageProjection> pageResult = repository.findAllWithDetails(pageable);

			List<ContractItemPackageResponse> responses = pageResult.getContent().stream().map(mapper::toResponse)
					.collect(Collectors.toList());

			Map<String, Object> payload = new HashMap<>();
			payload.put("data", responses);
			payload.put("currentPage", pageResult.getNumber());
			payload.put("totalItems", pageResult.getTotalElements());
			payload.put("totalPages", pageResult.getTotalPages());
			payload.put("pageSize", pageResult.getSize());
			payload.put("isLast", pageResult.isLast());

			logger.info("Total ContractItemPackages fetched: {}", responses.size());

			return new ResponseEntity("Packages fetched successfully", HttpStatus.OK.value(), payload);

		} catch (BadRequestException e) {
			logger.warn("Pagination validation failed: {}", e.getMessage());
			throw e;

		} catch (MappingException e) {
			logger.error("Mapping error during getAllPackages: {}", e.getMessage());
			return new ResponseEntity("Mapping error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
					null);

		} catch (Exception e) {
			logger.error("Unexpected error while fetching all ContractItemPackages: {}", e.getMessage(), e);
			throw new DatabaseOperationException("Failed to fetch ContractItemPackages");
		}
	}

	@Override
	public ResponseEntity deletePackage(Integer id) {
		logger.info("Deleting ContractItemPackage with id {}", id);
		try {
			if (id == null) {
				logger.error("Delete ContractItemPackage failed: id is null");
				return new ResponseEntity("ID cannot be null", HttpStatus.BAD_REQUEST.value(), null);
			}

			ContractItemPackage entity = repository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Package not found with id: " + id));

			repository.delete(entity);

			logger.info("ContractItemPackage deleted successfully with id {}", id);
			return new ResponseEntity("ContractItemPackage deleted successfully", HttpStatus.OK.value(), null);

		} catch (ResourceNotFoundException e) {
			logger.error("ContractItemPackage not found with id {}: {}", id, e.getMessage());
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND.value(), null);
		} catch (Exception e) {
			logger.error("Unexpected error while deleting ContractItemPackage with id {}: {}", id, e.getMessage(), e);
			return new ResponseEntity("Internal server error: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity getPackageByContractId(Integer contractId) {
		logger.info("Fetching packages for contractId {}", contractId);
		try {
			if (contractId == null) {
				return new ResponseEntity("Contract ID cannot be null", HttpStatus.BAD_REQUEST.value(), null);
			}

			List<ContractItemPackageProjection> data = repository.findAllByContractId(contractId);

			if (data.isEmpty()) {
				return new ResponseEntity("No packages found for contract id: " + contractId, HttpStatus.OK.value(),
						Collections.emptyList());
			}

			return new ResponseEntity("Packages fetched successfully", HttpStatus.OK.value(), data);

		} catch (Exception e) {
			logger.error("Unexpected error while fetching packages for contractId {}: {}", contractId, e.getMessage(),
					e);
			return new ResponseEntity("Internal server error: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
		}
	}

	@Override
	public ResponseEntity deleteBulkPackage(List<Integer> ids) {

		logger.info("Bulk delete ContractItemPackage API called with ids {}", ids);

		try {

			if (ids == null || ids.isEmpty()) {
				logger.error("Delete failed: ids are null or empty");
				return new ResponseEntity("IDs cannot be null or empty", 400, null);
			}

			List<ContractItemPackage> entities = repository.findAllById(ids);

			if (entities.isEmpty()) {
				logger.error("No ContractItemPackage found for ids {}", ids);
				throw new ResourceNotFoundException("No ContractItemPackage found for given ids");
			}

			repository.deleteAll(entities);

			logger.info("Bulk ContractItemPackage deleted successfully for ids {}", ids);

			return new ResponseEntity("ContractItemPackage deleted successfully", 200, null);

		} catch (ResourceNotFoundException e) {

			logger.error("Delete failed for ids {}: {}", ids, e.getMessage());
			return new ResponseEntity(e.getMessage(), 404, null);

		} catch (Exception e) {

			logger.error("Unexpected error while deleting for ids {}: {}", ids, e.getMessage(), e);
			return new ResponseEntity("Internal server error: " + e.getMessage(), 500, null);
		}
	}
}