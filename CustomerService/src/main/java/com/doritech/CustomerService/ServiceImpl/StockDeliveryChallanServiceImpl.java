package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Entity.StockDeliveryChallanEntity;
import com.doritech.CustomerService.Entity.StockRequestEntity;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.StockDeliveryChallanRepository;
import com.doritech.CustomerService.Repository.StockRequestRepository;
import com.doritech.CustomerService.Request.StockDeliveryChallanRequest;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Response.StockDeliveryChallanResponse;
import com.doritech.CustomerService.Service.StockDeliveryChallanService;

@Service
public class StockDeliveryChallanServiceImpl implements StockDeliveryChallanService {

	private static final Logger logger = LoggerFactory.getLogger(StockDeliveryChallanServiceImpl.class);

	@Autowired
	private StockDeliveryChallanRepository stockDeliveryChallanRepository;

	@Autowired
	private StockRequestRepository stockRequestRepository;
	

	@Override
	public ResponseEntity saveStockDelivery(StockDeliveryChallanRequest request) {
		ResponseEntity response = new ResponseEntity();
		logger.info("saveStockDelivery called with request: {}", request);

		try {
			if (request == null || request.getStockRequestIds() == null || request.getStockRequestIds().isEmpty()) {
				logger.warn("Request body or stockRequestIds cannot be null");
				response.setMessage("Request body cannot be null");
				response.setStatusCode(400);
				return response;
			}

			if (stockDeliveryChallanRepository.existsByDeliveryChallanNo(request.getDeliveryChallanNo())) {
				logger.warn("Delivery Challan No '{}' already exists", request.getDeliveryChallanNo());
				response.setMessage("Delivery Challan No already exists");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			List<StockDeliveryChallanResponse> savedResponses = new ArrayList<>();

			for (Integer stockRequestId : request.getStockRequestIds()) {
				logger.info("Processing stockRequestId: {}", stockRequestId);

				StockRequestEntity stockRequest = stockRequestRepository.findById(stockRequestId).orElseThrow(
						() -> new ResourceNotFoundException("Stock Request Not Found With ID: " + stockRequestId));

				if (stockDeliveryChallanRepository.existsByStockRequest_StockRequestId(stockRequestId)) {
					logger.warn("Stock Request ID '{}' is already used in another delivery challan", stockRequestId);
					response.setMessage("Stock Request ID is already assigned to a delivery challan");
					response.setStatusCode(400);
					response.setPayload(null);
					return response;
				}

				StockDeliveryChallanEntity entity = toEntity(request);
				entity.setStockRequest(stockRequest);
				entity.setCreatedBy(request.getCreatedBy());
				entity.setCreatedOn(LocalDateTime.now());

				StockDeliveryChallanEntity savedEntity = stockDeliveryChallanRepository.save(entity);
				logger.info("Saved StockDeliveryChallanEntity with id: {}", savedEntity.getStockDcId());

				savedResponses.add(toResponse(savedEntity));
			}

			response.setMessage("Stock Delivery Challan Saved Successfully");
			response.setPayload(savedResponses);
			response.setStatusCode(201);
			logger.info("saveStockDelivery completed successfully");

		} catch (ResourceNotFoundException e) {
			logger.error("ResourceNotFoundException: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("ExternalServiceException: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Exception in getAllStockDelivery: ", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllStockDelivery(int page, int size) {
		ResponseEntity response = new ResponseEntity();
		logger.info("getAllStockDelivery called with page: {}, size: {}", page, size);

		try {
			if (page < 0 || size <= 0) {
				logger.warn("Invalid page or size: page={}, size={}", page, size);
				response.setMessage("Page must be >= 0 and size must be > 0");
				response.setStatusCode(400);
				return response;
			}

			PageRequest pageable = PageRequest.of(page, size);
			Page<StockDeliveryChallanEntity> pageRequest = stockDeliveryChallanRepository.findAll(pageable);
			List<StockDeliveryChallanResponse> list = pageRequest.getContent().stream().map(this::toResponse).toList();

			if (list.isEmpty()) {
				logger.info("No stock delivery challans found");
				response.setMessage("Stock Delivery Challen Not Found");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			PageResponse<StockDeliveryChallanResponse> pageResponse = new PageResponse<>();
			pageResponse.setContent(list);
			pageResponse.setLastPage(pageRequest.isLast());
			pageResponse.setTotalElements(pageRequest.getTotalElements());
			pageResponse.setPageNumber(pageRequest.getNumber());
			pageResponse.setTotalPages(pageRequest.getTotalPages());
			pageResponse.setPageSize(pageRequest.getSize());

			response.setMessage("Stock Delivery Challan fetched successfully");
			response.setPayload(pageResponse);
			response.setStatusCode(200);

			logger.info("getAllStockDelivery completed successfully, returned {} records", list.size());

		} catch (ResourceNotFoundException e) {
			logger.error("ResourceNotFoundException: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("ExternalServiceException: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Exception in getAllStockDelivery: ", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getStockDeliveryById(Integer id) {
		ResponseEntity response = new ResponseEntity();
		try {
			if (id == null) {
				response.setMessage("Stock delivery challan id cannot be null");
				response.setStatusCode(400);
				return response;
			}
			StockDeliveryChallanEntity entity = stockDeliveryChallanRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Stock Delivery Challan Not Found With" + id));

			StockDeliveryChallanResponse Toresponse = toResponse(entity);
			response.setMessage("Stock Delivery Challan Fetched Succesfully ");
			response.setStatusCode(200);
			response.setPayload(Toresponse);
		} catch (ResourceNotFoundException e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity deleteMultipleStockDelivery(List<Integer> ids) {
		ResponseEntity response = new ResponseEntity();

		try {

			if (ids == null || ids.isEmpty()) {
				response.setStatusCode(400);
				response.setMessage("Please provide at least one stock Delivery Challan id to delete");
				return response;
			}

			List<StockDeliveryChallanEntity> entities = stockDeliveryChallanRepository.findAllById(ids);

			if (entities.isEmpty()) {
				response.setStatusCode(404);
				response.setMessage("No Stock Delivery Challan Found For Provided Ids");
				return response;
			}

			List<Integer> foundIds = entities.stream().map(StockDeliveryChallanEntity::getStockDcId).toList();
			List<Integer> missingIds = ids.stream().filter(id -> !foundIds.contains(id)).toList();

			stockDeliveryChallanRepository.deleteAll(entities);

			if (missingIds.isEmpty()) {
				response.setMessage(" Stock Delivery challan deleted successfully");
			} else {
				response.setMessage(" Stock Delivery challan deleted successfully. Some ids not found: ");
			}

			response.setStatusCode(200);

		} catch (ResourceNotFoundException e) {
			logger.error("ResourceNotFoundException: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Exception in getAllStockDelivery: ", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}



	private StockDeliveryChallanResponse toResponse(StockDeliveryChallanEntity entity) {

		StockDeliveryChallanResponse dto = new StockDeliveryChallanResponse();

		dto.setStockDcId(entity.getStockDcId());
		dto.setDeliveryChallanNo(entity.getDeliveryChallanNo());

		if (entity.getStockRequest() != null) {
			dto.setStockRequestIds(List.of(entity.getStockRequest().getStockRequestId()));
		}
		return dto;
	}

	public static StockDeliveryChallanEntity toEntity(StockDeliveryChallanRequest request) {

		StockDeliveryChallanEntity entity = new StockDeliveryChallanEntity();
		entity.setDeliveryChallanNo(request.getDeliveryChallanNo());
		return entity;
	}

}
