package com.doritech.CustomerService.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Entity.StockRequestDetailEntity;
import com.doritech.CustomerService.Entity.StockRequestEntity;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.StockDeliveryChallanRepository;
import com.doritech.CustomerService.Repository.StockRequestDetailsRepository;
import com.doritech.CustomerService.Repository.StockRequestRepository;
import com.doritech.CustomerService.Request.StockRequestDetailRequest;
import com.doritech.CustomerService.Request.StockRequestRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Response.StockRequestDetailResponse;
import com.doritech.CustomerService.Response.StockRequestResponse;
import com.doritech.CustomerService.Service.StockRequestService;
import com.doritech.CustomerService.Specification.StockRequestSpecification;
import com.doritech.CustomerService.ValidationService.ValidationService;

import jakarta.transaction.Transactional;

@Service
public class StockRequestServiceImpl implements StockRequestService {

	private static final Logger logger = LoggerFactory
			.getLogger(StockRequestServiceImpl.class);

	@Autowired
	private StockRequestRepository stockRequestRepository;

	@Autowired
	private ValidationService validationService;

	@Autowired
	private ContractMasterRepository contractMasterRepository;

	@Autowired
	private StockRequestDetailsRepository stockRequestDetailsRepository;

	@Autowired
	private StockDeliveryChallanRepository stockDeliveryChallanRepository;

	@Override
	public ResponseEntity saveStockRequest(StockRequestRequest request) {

		logger.info("Save stock request service started");

		ResponseEntity response = new ResponseEntity();

		try {

			if (request == null) {
				logger.error("Request body cannot be null");
				response.setMessage("Request body cannot be null");
				response.setStatusCode(400);
				return response;
			}

			if (request.getSourceSiteId()
					.equals(request.getRequestedSiteId())) {

				logger.warn(
						"Source site and requested site cannot be same. sourceSiteId: {}, requestedSiteId: {}",
						request.getSourceSiteId(),
						request.getRequestedSiteId());

				response.setStatusCode(400);
				response.setMessage(
						"Source Site and Requested Site cannot be the same");
				response.setPayload(null);

				return response;
			}

			if (stockRequestRepository.existsBySourceSiteIdAndRequestedSiteId(
					request.getSourceSiteId(), request.getRequestedSiteId())) {

				logger.warn(
						"Duplicate stock request found for sourceSiteId: {} and requestedSiteId: {}",
						request.getSourceSiteId(),
						request.getRequestedSiteId());

				response.setMessage(
						"Source Site and Requested Site already exists");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}
			logger.info("Validating source site with id {}",
					request.getSourceSiteId());
			CompSiteResponse sourceSite = validationService
					.validateAndGetSite(request.getSourceSiteId(), "Source");

			logger.info("Validating requested site with id {}",
					request.getRequestedSiteId());
			CompSiteResponse requestedSite = validationService
					.validateAndGetSite(request.getRequestedSiteId(),
							"Requested");

			StockRequestEntity entity = toEntity(request);
			entity.setSourceSiteId(sourceSite.getSiteId());
			entity.setRequestedSiteId(requestedSite.getSiteId());
			entity.setCreatedOn(LocalDateTime.now());
			entity.setStatus("RE");
			entity.setCreatedBy(request.getCreatedBy());
			List<StockRequestDetailEntity> detailItems = new ArrayList<>();

			for (StockRequestDetailRequest detailRequest : request.getItems()) {

				logger.info("Validating item with id {}",
						detailRequest.getItemId());
				ItemIDResponse item = validationService
						.validateAndGetItem(detailRequest.getItemId());

				if (detailRequest.getContractId() != null) {
					logger.info("Validating contract with id {}",
							detailRequest.getContractId());
					contractMasterRepository
							.findById(detailRequest.getContractId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Contract not found with id "
											+ detailRequest.getContractId()));
				}

				StockRequestDetailEntity detail = new StockRequestDetailEntity();
				detail.setItemId(item.getItemId());
				detail.setContractId(detailRequest.getContractId());
				detail.setCreatedOn(LocalDateTime.now());
				detail.setCreatedBy(request.getCreatedBy());
				detail.setRequestedQty(detailRequest.getRequestedQty());
				detailItems.add(detail);

				detail.setStockRequest(entity);
			}

			entity.setItems(detailItems);

			StockRequestEntity saved = stockRequestRepository.save(entity);

			logger.info("Stock request saved successfully with id {}",
					saved.getStockRequestId());

			response.setPayload(toResponse(saved));
			response.setMessage("Data Saved Successfully");
			response.setStatusCode(201);

		} catch (IllegalArgumentException e) {
			logger.error("Validation error while saving stock request: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while saving stock request: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while saving stock request: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Unexpected error while saving stock request: {}",
					e.getMessage(), e);
			response.setMessage("Internal Server Error" + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getStockRequestById(Integer id) {

		logger.info("Get stock request by id service started for id {}", id);

		ResponseEntity response = new ResponseEntity();

		try {

			if (id == null) {
				logger.error("Stock request id cannot be null");
				response.setMessage("Stock request id cannot be null");
				response.setStatusCode(400);
				return response;
			}

			StockRequestEntity stockRequest = stockRequestRepository
					.findById(id).orElseThrow(() -> {
						logger.error("Stock request not found with id {}", id);
						return new ResourceNotFoundException(
								"Stock request not found with id " + id);
					});

			logger.info("Stock request fetched successfully with id {}", id);

			response.setMessage("Stock request fetched successfully");
			response.setStatusCode(200);
			response.setPayload((toResponse(stockRequest)));

		} catch (ResourceNotFoundException e) {
			logger.error(
					"Resource not found while fetching stock request with id {}: {}",
					id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while fetching stock request with id {}: {}",
					id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error(
					"Unexpected error while fetching stock request with id {}: {}",
					id, e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity updateStockRequest(Integer stockRequestId,
			StockRequestRequest request) {

		logger.info("Update stock request service started for id {}",
				stockRequestId);

		ResponseEntity response = new ResponseEntity();

		try {

			if (stockRequestId == null) {
				logger.error("Stock request id cannot be null");
				response.setMessage("Stock request id cannot be null");
				response.setStatusCode(400);
				return response;
			}

			if (request == null) {
				logger.error("Request body cannot be null");
				response.setMessage("Request body cannot be null");
				response.setStatusCode(400);
				return response;
			}

			StockRequestEntity existing = stockRequestRepository
					.findById(stockRequestId).orElseThrow(() -> {
						logger.error("Stock request not found with id {}",
								stockRequestId);
						return new ResourceNotFoundException(
								"Stock request not found with id ");
					});

			if ("AP".equalsIgnoreCase(existing.getStatus())) {
				logger.warn("Update blocked. Status is APPROVED for id {}",
						stockRequestId);

				response.setMessage(
						"Approved stock requests cannot be updated");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (!"RE".equalsIgnoreCase(existing.getStatus())) {
				logger.warn("Update blocked. Invalid status {} for id {}",
						existing.getStatus(), stockRequestId);

				response.setMessage(
						"Only Requested stock requests can be updated");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (stockRequestRepository
					.existsBySourceSiteIdAndRequestedSiteIdAndStockRequestIdNot(
							request.getSourceSiteId(),
							request.getRequestedSiteId(), stockRequestId)) {

				logger.warn(
						"Duplicate found during update for sourceSiteId: {} and requestedSiteId: {}",
						request.getSourceSiteId(),
						request.getRequestedSiteId());

				response.setMessage(
						"Source Site and Requested Site combination already exists");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}
			logger.info("Validating source site with id {}",
					request.getSourceSiteId());
			CompSiteResponse sourceSite = validationService
					.validateAndGetSite(request.getSourceSiteId(), "Source");

			logger.info("Validating requested site with id {}",
					request.getRequestedSiteId());
			CompSiteResponse requestedSite = validationService
					.validateAndGetSite(request.getRequestedSiteId(),
							"Requested");

			existing.setSourceSiteId(sourceSite.getSiteId());
			existing.setRequestedSiteId(requestedSite.getSiteId());
			existing.setModifiedBy(request.getModifiedBy());
			existing.setModifiedOn(LocalDateTime.now());

			List<StockRequestDetailEntity> updatedItems = new ArrayList<>();

			for (StockRequestDetailRequest detailRequest : request.getItems()) {

				logger.info("Validating item with id {}",
						detailRequest.getItemId());
				ItemIDResponse item = validationService
						.validateAndGetItem(detailRequest.getItemId());

				if (detailRequest.getContractId() != null) {
					logger.info("Validating contract with id {}",
							detailRequest.getContractId());
					contractMasterRepository
							.findById(detailRequest.getContractId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Contract not found with id "
											+ detailRequest.getContractId()));
				}

				StockRequestDetailEntity detail;

				if (detailRequest.getStockRequestDetailId() != null) {
					detail = stockRequestDetailsRepository
							.findById(detailRequest.getStockRequestDetailId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Stock request detail not found with id "
											+ detailRequest
													.getStockRequestDetailId()));
					detail.setModifiedOn(LocalDateTime.now());
					detail.setModifiedBy(request.getModifiedBy());
				} else {

					detail = new StockRequestDetailEntity();
					detail.setCreatedBy(request.getCreatedBy());
					detail.setCreatedOn(LocalDateTime.now());
					detail.setStockRequest(existing);
				}

				detail.setItemId(item.getItemId());
				detail.setContractId(detailRequest.getContractId());
				detail.setRequestedQty(detailRequest.getRequestedQty());

				updatedItems.add(detail);
			}

			existing.getItems().clear();
			existing.getItems().addAll(updatedItems);

			StockRequestEntity updated = stockRequestRepository.save(existing);

			logger.info("Stock request updated successfully with id {}",
					stockRequestId);

			response.setPayload(toResponse(updated));
			response.setMessage("Stock request updated successfully");
			response.setStatusCode(200);

		} catch (IllegalArgumentException e) {
			logger.error(
					"Validation error while updating stock request with id {}: {}",
					stockRequestId, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error(
					"Resource not found while updating stock request with id {}: {}",
					stockRequestId, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while updating stock request with id {}: {}",
					stockRequestId, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error(
					"Unexpected error while updating stock request with id {}: {}",
					stockRequestId, e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	@Transactional
	public ResponseEntity deleteMultipleStockRequest(
			List<Integer> stockRequestIds) {

		logger.info("Delete multiple stock requests service started");

		ResponseEntity response = new ResponseEntity();

		try {

			if (stockRequestIds == null || stockRequestIds.isEmpty()) {
				logger.error("Stock request id list is empty");
				response.setStatusCode(400);
				response.setMessage(
						"Please provide at least one stock request id to delete");
				return response;
			}

			List<StockRequestEntity> entities = stockRequestRepository
					.findAllById(stockRequestIds);

			if (entities.isEmpty()) {
				logger.error("No stock requests found for provided ids: {}",
						stockRequestIds);
				response.setStatusCode(404);
				response.setMessage("No stock requests found for provided ids");
				return response;
			}

			List<StockRequestEntity> deletableEntities = new ArrayList<>();

			boolean hasApproved = false;
			boolean hasDispatched = false;

			for (StockRequestEntity entity : entities) {

				String status = entity.getStatus();

				if ("RE".equalsIgnoreCase(status)) {
					deletableEntities.add(entity);
				}

				else if ("AP".equalsIgnoreCase(status)) {
					hasApproved = true;
					logger.warn("StockRequest {} is APPROVED, cannot delete",
							entity.getStockRequestId());
				}

				else if ("DI".equalsIgnoreCase(status)) {
					hasDispatched = true;
					logger.warn("StockRequest {} is DISPATCHED, cannot delete",
							entity.getStockRequestId());
				}
			}

			if (deletableEntities.isEmpty()) {

				if (hasApproved) {
					response.setMessage(
							"Approved stock requests cannot be deleted");
				} else if (hasDispatched) {
					response.setMessage(
							"Dispatched stock requests cannot be deleted");
				}

				response.setStatusCode(400);
				return response;
			}

			List<Integer> deletableIds = deletableEntities.stream()
					.map(StockRequestEntity::getStockRequestId).toList();

			logger.info("Deleting child records for stockRequestIds: {}",
					deletableIds);
			stockRequestDetailsRepository
					.deleteByStockRequest_StockRequestIdIn(deletableIds);

			logger.info("Deleting parent stock requests: {}", deletableIds);
			stockRequestRepository.deleteAll(deletableEntities);

			logger.info("{} stock request(s) deleted successfully",
					deletableEntities.size());

			response.setStatusCode(200);
			response.setMessage("Stock request(s) deleted successfully.");

		} catch (Exception e) {
			logger.error("Unexpected error while deleting stock requests: {}",
					e.getMessage(), e);
			response.setStatusCode(500);
			response.setMessage("Internal Server Error");
		}

		return response;
	}

	@Override
	@Transactional
	public ResponseEntity approveStockRequest(Integer id, StockRequestRequest request) {

		ResponseEntity response = new ResponseEntity();

		try {
			logger.info("Approve API called for stockRequestId: {}", id);

			if (id == null) {
				logger.warn("StockRequestId is null");
				response.setStatusCode(400);
				response.setMessage("Please provide stock request id");
				return response;
			}

			if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
				logger.warn("Invalid request body for stockRequestId: {}", id);
				response.setStatusCode(400);
				response.setMessage("Request body cannot be null");
				return response;
			}

			StockRequestEntity existing = stockRequestRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Stock Request Not Found With this id " + id));

			logger.info("StockRequest found. Current status: {}", existing.getStatus());

			if ("AP".equalsIgnoreCase(existing.getStatus())) {
				logger.warn("Approval blocked. Already APPROVED for id {}", id);

				response.setStatusCode(400);
				response.setMessage("Stock request is already approved");
				return response;
			}

			if ("DI".equalsIgnoreCase(existing.getStatus())) {
				logger.warn("Approval blocked. Already DISPATCHED for id {}", id);

				response.setStatusCode(400);
				response.setMessage("Dispatched stock requests cannot be approved");
				return response;
			}

			logger.info("Processing {} items for approval", request.getItems().size());

			for (StockRequestDetailRequest detailRequest : request.getItems()) {

				logger.debug("Processing detailId: {}", detailRequest.getStockRequestDetailId());

				if (detailRequest.getStockRequestDetailId() == null) {
					logger.error("stockRequestDetailId is null in request");

					response.setStatusCode(400);
					response.setMessage("stockRequestDetailId cannot be null");
					return response;
				}

				StockRequestDetailEntity detail = stockRequestDetailsRepository
						.findById(detailRequest.getStockRequestDetailId())
						.orElseThrow(() -> new ResourceNotFoundException("Stock Request Details Not Found "));

				if (detailRequest.getApprovedQty() == null) {
					logger.warn("ApprovedQty is null for itemId: {}", detail.getItemId());
					response.setStatusCode(400);
					response.setMessage("Approved quantity cannot be null  ");
					return response;
				}

				if (detailRequest.getApprovedQty().compareTo(BigDecimal.ZERO) == 0) {
					logger.warn("ApprovedQty is zero for itemId: {}", detail.getItemId());

					response.setStatusCode(400);
					response.setMessage("Approved quantity cannot be zero");
					return response;
				}

				if (detailRequest.getApprovedQty().compareTo(detail.getRequestedQty()) > 0) {
					logger.warn("ApprovedQty > RequestedQty for itemId: {}", detail.getItemId());
					response.setStatusCode(400);
					response.setMessage("Approved quantity cannot be greater than requested quantity");
					return response;
				}

				if (detailRequest.getApprovedQty().compareTo(BigDecimal.ZERO) < 0) {
					logger.warn("Negative ApprovedQty for itemId: {}", detail.getItemId());
					response.setStatusCode(400);
					response.setMessage("Approved quantity cannot be negative ");
					return response;
				}

				detail.setApprovedQty(detailRequest.getApprovedQty());
				detail.setModifiedOn(LocalDateTime.now());

				stockRequestDetailsRepository.save(detail);

				logger.debug("Updated detailId: {} successfully", detail.getStockRequestDetailId());
			}

			existing.setStatus("AP");
			existing.setApprovedBy(request.getApprovedBy());
			existing.setApprovalDate(LocalDate.now());

			StockRequestEntity saved = stockRequestRepository.save(existing);

			logger.info("StockRequest approved successfully for id: {}", id);

			StockRequestResponse enrichResponse = toResponse(saved);

			response.setStatusCode(200);
			response.setMessage("Stock Request Approved Successfully");
			response.setPayload(enrichResponse);

		} catch (IllegalArgumentException e) {
			logger.error("Validation error while updating stock request with id {}: {}", id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while updating stock request with id {}: {}", id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("External service error while updating stock request with id {}: {}", id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error while updating stock request with id {}: {}", id, e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}


	@Override
	public ResponseEntity filterStockRequests(Integer sourceSiteId,
			Integer requestedSiteId, String status, int page, int size) {

		logger.info("Filter stock requests service started");

		ResponseEntity response = new ResponseEntity();

		try {

			Pageable pageable = PageRequest.of(page, size);

			Page<StockRequestEntity> pageData = stockRequestRepository
					.findAll(StockRequestSpecification.filter(sourceSiteId,
							requestedSiteId, status), pageable);

			List<StockRequestResponse> responseList = pageData.getContent()
					.stream().map(entity -> (toResponse(entity))).toList();

			if (responseList.isEmpty()) {
				logger.warn(
						"No stock requests found for given filter criteria");
				response.setMessage("No stock requests found");
				response.setStatusCode(404);
				response.setPayload(null);
			} else {

				PageResponse<StockRequestResponse> pageResponse = new PageResponse<>();
				pageResponse.setContent(responseList);
				pageResponse.setPageNumber(pageData.getNumber());
				pageResponse.setPageSize(pageData.getSize());
				pageResponse.setTotalElements(pageData.getTotalElements());
				pageResponse.setTotalPages(pageData.getTotalPages());
				pageResponse.setLastPage(pageData.isLast());

				logger.info(
						"Filtered stock requests fetched successfully. Total: {}",
						pageData.getTotalElements());

				response.setMessage("Stock requests fetched successfully");
				response.setStatusCode(200);
				response.setPayload(pageResponse);
			}

		} catch (ResourceNotFoundException e) {
			logger.error(
					"Resource not found while filtering stock requests: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while filtering stock requests: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error while filtering stock requests: {}",
					e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllStockRequest(int page, int size) {

		logger.info(
				"Get all stock request service started with page: {}, size: {}",
				page, size);

		ResponseEntity response = new ResponseEntity();

		try {

			if (page < 0 || size <= 0) {
				logger.error(
						"Invalid pagination parameters - page: {}, size: {}",
						page, size);
				response.setMessage("Page must be >= 0 and size must be > 0");
				response.setStatusCode(400);
				return response;
			}

			logger.info("Fetching all stock requests from repository");
			Pageable pageable = PageRequest.of(page, size);
			Page<StockRequestEntity> pageData = stockRequestRepository
					.findAll(pageable);

			if (pageData.isEmpty()) {
				logger.warn("No stock requests found in database");
				response.setMessage("Data Not Found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			logger.info("Mapping {} stock request entities to response",
					pageData.getNumberOfElements());
			List<StockRequestResponse> responseList = pageData.getContent()
					.stream().map(this::toResponse)
					.collect(Collectors.toList());

			PageResponse<StockRequestResponse> pageResponse = new PageResponse<>();
			pageResponse.setContent(responseList);
			pageResponse.setPageNumber(pageData.getNumber());
			pageResponse.setPageSize(pageData.getSize());
			pageResponse.setTotalElements(pageData.getTotalElements());
			pageResponse.setTotalPages(pageData.getTotalPages());
			pageResponse.setLastPage(pageData.isLast());

			logger.info(
					"Get all stock request completed successfully - found {} records out of {} total",
					pageData.getNumberOfElements(),
					pageData.getTotalElements());

			response.setPayload(pageResponse);
			response.setMessage("Stock requests fetched successfully");
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
	public ResponseEntity getAllApprovedStockRequests(int page, int size) {

		ResponseEntity response = new ResponseEntity();

		try {

			if (page < 0 || size <= 0) {
				logger.error(
						"Invalid pagination parameters - page: {}, size: {}",
						page, size);
				response.setMessage("Page must be >= 0 and size must be > 0");
				response.setStatusCode(400);
				return response;
			}

			logger.info("Fetching all approved stock requests");

			PageRequest pageable = PageRequest.of(page, size);

			Page<StockRequestEntity> pageRequest = stockRequestRepository
					.findByStatus("AP", pageable);

			List<StockRequestResponse> list = pageRequest.getContent().stream()
					.map(this::toResponse).toList();

			if (list == null || list.isEmpty()) {
				logger.warn("No approved stock requests found");

				response.setStatusCode(404);
				response.setMessage("No approved stock requests found");
				return response;
			}

			PageResponse<StockRequestResponse> pageResponse = new PageResponse<>();
			pageResponse.setContent(list);
			pageResponse.setPageNumber(pageRequest.getNumber());
			pageResponse.setPageSize(pageRequest.getSize());
			pageResponse.setTotalElements(pageRequest.getTotalElements());
			pageResponse.setTotalPages(pageRequest.getTotalPages());
			pageResponse.setLastPage(pageRequest.isLast());

			logger.info(
					"Approved stock requests fetched successfully. Count: {}",
					list.size());

			response.setStatusCode(200);
			response.setMessage("Approved stock requests fetched successfully");
			response.setPayload(pageResponse);
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
	public ResponseEntity getApprovedStockWithoutChallan(int page, int size) {

		ResponseEntity response = new ResponseEntity();

		try {

			if (page < 0 || size <= 0) {
				logger.error(
						"Invalid pagination parameters - page: {}, size: {}",
						page, size);
				response.setMessage(
						"Invalid pagination parameters: page must be >= 0 and size must be > 0");
				response.setStatusCode(400);
				return response;
			}

			logger.info("Fetching approved stock requests without challan");

			PageRequest pageable = PageRequest.of(page, size);

			Page<StockRequestEntity> pageRequest = stockRequestRepository
					.findByStatus("AP", pageable);

			List<StockRequestResponse> list = pageRequest.getContent().stream()
					.filter(sr -> !stockDeliveryChallanRepository
							.existsByStockRequest(sr))
					.map(this::toResponse).toList();

			if (list.isEmpty()) {
				logger.warn("No approved stock requests found without challan");

				response.setStatusCode(404);
				response.setMessage(
						"No approved stock requests available without challan");
				return response;
			}

			PageResponse<StockRequestResponse> pageResponse = new PageResponse<>();
			pageResponse.setContent(list);
			pageResponse.setPageNumber(pageRequest.getNumber());
			pageResponse.setPageSize(pageRequest.getSize());
			pageResponse.setTotalElements(pageRequest.getTotalElements());
			pageResponse.setTotalPages(pageRequest.getTotalPages());
			pageResponse.setLastPage(pageRequest.isLast());

			logger.info(
					"Approved stock requests without challan fetched successfully. Count: {}",
					list.size());

			response.setStatusCode(200);
			response.setMessage(
					"Approved stock requests without challan fetched successfully");
			response.setPayload(pageResponse);

		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while fetching stock requests: {}",
					e.getMessage());
			response.setMessage("Requested resource not found");
			response.setStatusCode(404);
			response.setPayload(null);

		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while fetching stock requests: {}",
					e.getMessage());
			response.setMessage(
					"External service error occurred while fetching stock requests");
			response.setStatusCode(502);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Unexpected error while fetching stock requests: {}",
					e.getMessage(), e);
			response.setMessage(
					"Unexpected error occurred while processing request");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	private StockRequestEntity toEntity(StockRequestRequest dto) {

		StockRequestEntity entity = new StockRequestEntity();

		entity.setSourceSiteId(dto.getSourceSiteId());
		entity.setRequestedSiteId(dto.getRequestedSiteId());

		return entity;
	}

	private StockRequestResponse toResponse(StockRequestEntity entity) {

		StockRequestResponse dto = new StockRequestResponse();

		dto.setStockRequestId(entity.getStockRequestId());
		dto.setSourceSiteId(entity.getSourceSiteId());
		dto.setRequestedSiteId(entity.getRequestedSiteId());
		dto.setStatus(entity.getStatus());
		dto.setApprovedBy(entity.getApprovedBy());
		dto.setApprovalDate(entity.getApprovalDate());

		try {
			CompSiteResponse sourceSite = validationService
					.validateAndGetSite(dto.getSourceSiteId(), "Source");

			dto.setSourceSiteName(sourceSite.getSiteName());
			dto.setSourceSiteCode(sourceSite.getSiteCode());

		} catch (Exception ex) {
			logger.warn("Could not fetch source site for id {}",
					dto.getSourceSiteId(), ex);
		}

		try {
			CompSiteResponse requestedSite = validationService
					.validateAndGetSite(dto.getRequestedSiteId(), "Requested");

			dto.setRequestedSiteName(requestedSite.getSiteName());
			dto.setRequestedSiteCode(requestedSite.getSiteCode());

		} catch (Exception ex) {
			logger.warn("Could not fetch requested site for id {}",
					dto.getRequestedSiteId(), ex);
		}

		if (entity.getItems() != null) {

			List<StockRequestDetailResponse> itemDTOs = entity.getItems()
					.stream().map(item -> {

						StockRequestDetailResponse res = new StockRequestDetailResponse();

						res.setStockRequestDetailId(
								item.getStockRequestDetailId());
						res.setContractId(item.getContractId());
						res.setItemId(item.getItemId());
						res.setRequestedQty(item.getRequestedQty());
						res.setApprovedQty(item.getApprovedQty());

						try {
							ItemIDResponse itemRes = validationService
									.validateAndGetItem(item.getItemId());
							res.setItemName(itemRes.getItemName());
							res.setItemCode(itemRes.getItemCode());
						} catch (Exception ex) {
							logger.warn("Item not found for id {}",
									item.getItemId(), ex);
						}

						if (item.getContractId() != null) {
							contractMasterRepository
									.findById(item.getContractId())
									.ifPresent(contract -> {
										res.setContractName(
												contract.getContractName());
										res.setContractNo(
												contract.getContractNo());
									});
						}

						return res;

					}).toList();

			dto.setItems(itemDTOs);
		}

		return dto;
	}

}
