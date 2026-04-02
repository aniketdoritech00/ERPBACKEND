package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Entity.StockRequestDetailEntity;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.StockRequestDetailsRepository;
import com.doritech.CustomerService.Request.StockRequestDetailRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Response.StockRequestDetailResponse;
import com.doritech.CustomerService.Service.StockRequestDetailsService;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
public class StockRequestDetailsServiceImpl
		implements
			StockRequestDetailsService {

	private static final Logger logger = LoggerFactory
			.getLogger(StockRequestDetailsServiceImpl.class);

	@Autowired
	private ValidationService validationService;

	@Autowired
	private ContractMasterRepository contractRepository;

	@Autowired
	private StockRequestDetailsRepository stockRequestDetailsRepository;

	@Override
	public ResponseEntity saveStockRequestDetails(
			StockRequestDetailRequest request) {

		logger.info("Save StockRequestDetails started");

		ResponseEntity response = new ResponseEntity();

		try {

			if (request == null) {
				logger.error("Request body cannot be null");
				response.setMessage("Request body cannot be null");
				response.setStatusCode(400);
				return response;
			}

			// logger.info("Checking duplicate for itemId {} and contractId {}",
			// request.getItemId(),
			// request.getContractId());
			//
			// if
			// (stockRequestDetailsRepository.existsByItemIdAndContractId(request.getItemId(),
			// request.getContractId())) {
			//
			// logger.warn("Duplicate found for itemId {} and contractId {}",
			// request.getItemId(),
			// request.getContractId());
			//
			// response.setMessage("Item and Contract combination already exists
			// ");
			// response.setStatusCode(400);
			// response.setPayload(null);
			// return response;
			// }

			logger.info("Validating item {}", request.getItemId());
			ItemIDResponse item = validationService
					.validateAndGetItem(request.getItemId());

			if (request.getContractId() != null) {
				logger.info("Validating contract {}", request.getContractId());
				contractRepository.findById(request.getContractId())
						.orElseThrow(() -> new ResourceNotFoundException(
								"Contract not found with id "
										+ request.getContractId()));
			}

			StockRequestDetailEntity entity = toEntity(request);
			entity.setItemId(item.getItemId());
			entity.setContractId(request.getContractId());
			entity.setCreatedOn(LocalDateTime.now());
			entity.setModifiedOn(null);
			entity.setModifiedBy(null);

			logger.info("Saving stock request details");
			StockRequestDetailEntity saved = stockRequestDetailsRepository
					.save(entity);

			response.setMessage("Stock Details Saved Successfully ");
			response.setPayload(toResponse(saved));
			response.setStatusCode(201);

		} catch (IllegalArgumentException e) {
			logger.error("Validation error: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("External service error: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error: {}", e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllStockRequestDetails(int page, int size) {

		logger.info("Fetching all stock request details page {} size {}", page,
				size);

		ResponseEntity response = new ResponseEntity();

		try {

			if (page < 0 || size <= 0) {
				logger.error("Invalid pagination params page={} size={}", page,
						size);
				response.setMessage("Page must be >= 0 and size must be > 0");
				response.setStatusCode(400);
				return response;
			}

			PageRequest pageable = PageRequest.of(page, size);
			Page<StockRequestDetailEntity> pageRequest = stockRequestDetailsRepository
					.findAll(pageable);

			List<StockRequestDetailResponse> list = pageRequest.getContent()
					.stream().map(entity -> (toResponse(entity))).toList();

			PageResponse<StockRequestDetailResponse> pageResponse = new PageResponse<>();
			pageResponse.setContent(list);
			pageResponse.setLastPage(pageRequest.isLast());
			pageResponse.setPageNumber(pageRequest.getNumber());
			pageResponse.setPageSize(pageRequest.getSize());
			pageResponse.setTotalElements(pageRequest.getTotalElements());
			pageResponse.setTotalPages(pageRequest.getTotalPages());

			response.setPayload(pageResponse);
			response.setMessage("Stock requests Details fetched successfully");
			response.setStatusCode(200);

		} catch (ResourceNotFoundException e) {
			logger.error(
					"Resource not found while fetching all stock requests: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while fetching all stock requests: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error(
					"Unexpected error while fetching all stock requests: {}",
					e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getStockRequestDetailsById(Integer id) {

		logger.info("Fetching stock request details by id {}", id);

		ResponseEntity response = new ResponseEntity();

		try {

			if (id == null) {
				logger.error("Id cannot be null");
				response.setMessage("Stock request id cannot be null");
				response.setStatusCode(400);
				return response;
			}

			StockRequestDetailEntity entity = stockRequestDetailsRepository
					.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(
							"Stock requestdetails not found with id " + id));

			response.setMessage("Stock request details fetched successfully");
			response.setStatusCode(200);
			response.setPayload((toResponse(entity)));

		} catch (ResourceNotFoundException e) {
			logger.error(
					"Resource not found while fetching all stock requests: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while fetching all stock requests: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error(
					"Unexpected error while fetching all stock requests: {}",
					e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity updateStockRequestDetails(Integer id,
			StockRequestDetailRequest request) {

		logger.info("Updating stock request details id {}", id);

		ResponseEntity response = new ResponseEntity();

		try {

			if (id == null) {
				logger.error("Id is null");
				response.setStatusCode(400);
				response.setMessage(
						"Please provide at least one stock request id to delete");
				return response;
			}

			if (request == null) {
				logger.error("Request is null");
				response.setMessage("Request body cannot be null");
				response.setStatusCode(400);
				return response;
			}

			StockRequestDetailEntity existing = stockRequestDetailsRepository
					.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(
							"Stock Request Details Not Found with "
									+ request.getStockRequestDetailId()));

			// logger.info("Checking duplicate for update");
			//
			// if
			// (stockRequestDetailsRepository.existsByItemIdAndContractIdAndStockRequestDetailIdNot(request.getItemId(),
			// request.getContractId(), request.getStockRequestDetailId())) {
			//
			// logger.warn("Duplicate found during update");
			//
			// response.setMessage("Item and Contract combination already exists
			// ");
			// response.setStatusCode(400);
			// response.setPayload(null);
			// return response;
			// }

			ItemIDResponse item = validationService
					.validateAndGetItem(request.getItemId());

			if (request.getContractId() != null) {
				contractRepository.findById(request.getContractId())
						.orElseThrow(() -> {
							return new ResourceNotFoundException(
									"Contract not found with id "
											+ request.getContractId());
						});
			}

			existing.setItemId(item.getItemId());
			existing.setRequestedQty(request.getRequestedQty());
			existing.setContractId(request.getContractId());
			existing.setModifiedOn(LocalDateTime.now());

			logger.info("Saving updated entity id {}", id);
			StockRequestDetailEntity save = stockRequestDetailsRepository
					.save(existing);

			response.setMessage("Stock request details updated successfully");
			response.setStatusCode(200);
			response.setPayload(toResponse(save));

		} catch (IllegalArgumentException e) {
			logger.error(
					"Validation error while updating stock request with id {}: {}",
					id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error(
					"Resource not found while updating stock request with id {}: {}",
					id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while updating stock request with id {}: {}",
					id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error(
					"Unexpected error while updating stock request with id {}: {}",
					id, e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}
		return response;
	}

	@Override
	public ResponseEntity deleteMultipleStockDetails(List<Integer> ids) {

		logger.info("Deleting multiple stock request details {}", ids);

		ResponseEntity response = new ResponseEntity();

		try {

			if (ids == null || ids.isEmpty()) {
				logger.error("Ids list is empty");
				response.setStatusCode(400);
				response.setMessage(
						"Please provide at least one stock request id to delete");
				return response;
			}

			List<StockRequestDetailEntity> entities = stockRequestDetailsRepository
					.findAllById(ids);

			if (entities.isEmpty()) {
				logger.warn("No records found for ids {}", ids);
				response.setStatusCode(404);
				response.setMessage("No stock requests found for provided ids");
				return response;
			}

			logger.info("Deleting {} records", entities.size());
			stockRequestDetailsRepository.deleteAll(entities);

			response.setMessage(entities.size()
					+ " stock request(s) Details  deleted successfully");
			response.setStatusCode(200);

		} catch (Exception e) {
			logger.error("Error deleting records: {}", e.getMessage(), e);
			response.setStatusCode(500);
			response.setMessage("Internal Server Error");
		}

		return response;
	}

	public static StockRequestDetailEntity toEntity(
			StockRequestDetailRequest request) {
		StockRequestDetailEntity entity = new StockRequestDetailEntity();
		entity.setContractId(request.getContractId());
		entity.setItemId(request.getItemId());
		entity.setRequestedQty(request.getRequestedQty());
		return entity;
	}

	public StockRequestDetailResponse toResponse(
			StockRequestDetailEntity entity) {

		StockRequestDetailResponse response = new StockRequestDetailResponse();

		response.setStockRequestDetailId(entity.getStockRequestDetailId());
		response.setApprovedQty(entity.getApprovedQty());
		response.setContractId(entity.getContractId());
		response.setItemId(entity.getItemId());
		response.setRequestedQty(entity.getRequestedQty());

		try {
			CompSiteResponse sourceSite = validationService.validateAndGetSite(
					entity.getStockRequest().getSourceSiteId(), "Source");

			response.setSourceSiteName(sourceSite.getSiteName());
			response.setSourceSiteCode(sourceSite.getSiteCode());

		} catch (Exception ex) {
			logger.warn("Error fetching source site");
		}

		try {
			CompSiteResponse requestedSite = validationService
					.validateAndGetSite(
							entity.getStockRequest().getRequestedSiteId(),
							"Requested");

			response.setRequestedSiteName(requestedSite.getSiteName());
			response.setRequestedSiteCode(requestedSite.getSiteCode());

		} catch (Exception ex) {
			logger.warn("Error fetching requested site");
		}

		try {
			ItemIDResponse item = validationService
					.validateAndGetItem(response.getItemId());

			response.setItemName(item.getItemName());
			response.setItemCode(item.getItemCode());

		} catch (Exception ex) {
			logger.warn("Error fetching item details");
		}

		if (response.getContractId() != null) {
			contractRepository.findById(response.getContractId())
					.ifPresent(contract -> {
						response.setContractName(contract.getContractName());
						response.setContractNo(contract.getContractNo());
					});
		}

		return response;
	}
}