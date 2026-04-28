package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.DispatchEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.DispatchRespository;
import com.doritech.CustomerService.Request.DispatchRequest;
import com.doritech.CustomerService.Response.DispatchResponse;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Service.DispatchService;
import com.doritech.CustomerService.Specification.DispatchDetailsSpecification;

@Service
public class DispatchServiceImpl implements DispatchService {

	private static final Logger logger = LoggerFactory.getLogger(DispatchServiceImpl.class);

	@Autowired
	private DispatchRespository dispatchRespository;

	@Override
	public ResponseEntity saveDispatchDeatils(DispatchRequest request) {
		ResponseEntity response = new ResponseEntity();

		try {
			logger.info("Entering saveDispatchDeatils method");

			if (request == null) {
				logger.warn("Request body is null");
				response.setMessage("Request body cannot be null");
				response.setStatusCode(400);
				return response;
			}

			if (dispatchRespository.existsByDeliveryChallanNo(request.getDeliveryChallanNo())) {
				logger.warn("Duplicate DeliveryChallanNo: {}", request.getDeliveryChallanNo());
				response.setMessage("Delivery Challan no Already exists");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (dispatchRespository.existsByConsignmentNo(request.getConsignmentNo())) {
				logger.warn("Duplicate ConsignmentNo: {}", request.getConsignmentNo());
				response.setMessage("Consignment No is Already exists ");
				response.setPayload(null);
				response.setStatusCode(400);
				return response;
			}

			DispatchEntity entity = toEntity(request);
			entity.setCreatedOn(LocalDateTime.now());
			entity.setCreatedBy(request.getCreatedBy());
			entity.setDispatchDate(request.getDispatchDate());

			DispatchEntity saved = dispatchRespository.save(entity);
			logger.info("Dispatch saved successfully with ID: {}", saved.getDispatchId());

			DispatchResponse response2 = toResponse(saved);
			response.setPayload(response2);
			response.setMessage("Dispatch Details Saved Successfully");
			response.setStatusCode(201);

		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException in saveDispatchDeatils", e);
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error("ResourceNotFoundException in saveDispatchDeatils", e);
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("ExternalServiceException in saveDispatchDeatils", e);
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error in saveDispatchDeatils", e);
			response.setMessage("Internal Server Error" + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllDispatchDetails(int page, int size) {
		ResponseEntity response = new ResponseEntity();
		try {
			logger.info("Fetching all dispatch details - page: {}, size: {}", page, size);

			if (page < 0 || size <= 0) {
				logger.warn("Invalid pagination parameters");
				response.setMessage("Page must be >= 0 and size must be > 0");
				response.setStatusCode(400);
				return response;
			}

			PageRequest pageable = PageRequest.of(page, size);
			Page<DispatchEntity> pageRequest = dispatchRespository.findAll(pageable);

			List<DispatchResponse> list = pageRequest.getContent().stream().map(this::toResponse).toList();

			if (list == null || list.isEmpty()) {
				logger.warn("No dispatch records found");
				response.setStatusCode(404);
				response.setMessage("No Dispatch details found");
				return response;
			}

			PageResponse<DispatchResponse> pageResponse = new PageResponse<>();
			pageResponse.setContent(list);
			pageResponse.setLastPage(pageRequest.isLast());
			pageResponse.setPageNumber(pageRequest.getNumber());
			pageResponse.setPageSize(pageRequest.getSize());
			pageResponse.setTotalElements(pageRequest.getTotalElements());
			pageResponse.setTotalPages(pageRequest.getTotalPages());

			logger.info("Dispatch details fetched successfully");
			response.setMessage("Dispatch Details fetched successfully");
			response.setStatusCode(200);
			response.setPayload(pageResponse);

		} catch (ResourceNotFoundException e) {
			logger.error("ResourceNotFoundException in getAllDispatchDetails", e);
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("ExternalServiceException in getAllDispatchDetails", e);
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error in getAllDispatchDetails", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity getDispathchDetailsById(Integer id) {
		ResponseEntity response = new ResponseEntity();
		try {
			logger.info("Fetching dispatch by ID: {}", id);

			if (id == null) {
				logger.warn("Dispatch ID is null");
				response.setMessage("Dispatch id cannot be null");
				response.setStatusCode(400);
				return response;
			}

			DispatchEntity entity = dispatchRespository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Dispatch Details Not Found with id " + id));

			logger.info("Dispatch found for ID: {}", id);

			DispatchResponse response2 = toResponse(entity);
			response.setMessage("Dispatched Details Fatched Successfully ");
			response.setPayload(response2);
			response.setStatusCode(200);

		} catch (ResourceNotFoundException e) {
			logger.error("Dispatch not found for ID: {}", id, e);
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("ExternalServiceException in getDispathchDetailsById", e);
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error in getDispathchDetailsById", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity deleteMultipleDispatchDetails(List<Integer> ids) {
		ResponseEntity response = new ResponseEntity();
		try {
			logger.info("Deleting dispatch records for IDs: {}", ids);

			if (ids.isEmpty() || ids == null) {
				logger.warn("Invalid ID list provided");
				response.setMessage("Please Provide atleast One Id to delete ");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			List<DispatchEntity> entities = dispatchRespository.findAllById(ids);

			if (entities.isEmpty() || entities == null) {
				logger.warn("No dispatch records found for given IDs");
				response.setMessage("No Dispatched Details Found For Provided Ids");
				response.setStatusCode(404);
				return response;
			}

			List<Integer> foundList = entities.stream().map(DispatchEntity::getDispatchId).toList();

			List<Integer> missingIds = ids.stream().filter(id -> !foundList.contains(id)).toList();

			dispatchRespository.deleteAll(entities);

			if (missingIds.isEmpty()) {
				logger.info("All dispatch records deleted successfully");
				response.setMessage("Dispatch Details deleted successfully ");
			} else {
				logger.warn("Some IDs not found: {}", missingIds);
				response.setMessage("Dispatch Details deleted successfully Some Ids Not Found");
			}

			response.setStatusCode(200);

		} catch (Exception e) {
			logger.error("Unexpected error in deleteMultipleDispatchDetails", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity updateDispatchDeatils(Integer id, DispatchRequest request) {
		ResponseEntity response = new ResponseEntity();
		try {
			logger.info("Updating dispatch with ID: {}", id);

			DispatchEntity entity = dispatchRespository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Dispatch deatils Not Found "));

			if (dispatchRespository.existsByDeliveryChallanNoAndDispatchIdNot(request.getDeliveryChallanNo(), id)) {
				logger.warn("Duplicate DeliveryChallanNo on update: {}", request.getDeliveryChallanNo());
				response.setMessage("Delivery Challan no Already exists");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (dispatchRespository.existsByConsignmentNoAndDispatchIdNot(request.getConsignmentNo(), id)) {
				logger.warn("Duplicate ConsignmentNo on update: {}", request.getConsignmentNo());
				response.setMessage("Consignment No is Already exists ");
				response.setPayload(null);
				response.setStatusCode(400);
				return response;
			}

			entity.setConsignmentNo(request.getConsignmentNo());
			entity.setDeliveryChallanNo(request.getDeliveryChallanNo());
			entity.setDispatchMode(request.getDispatchMode());
			entity.setDispatchVendor(request.getDispatchVendor());
			entity.setRemarks(request.getRemarks());
			entity.setStatus(request.getStatus());
			entity.setDispatchDate(request.getDispatchDate());
			entity.setMrnNo(request.getMrnNo());
			entity.setModifiedBy(request.getModifiedBy());
			entity.setModifiedOn(LocalDateTime.now());

			DispatchEntity updated = dispatchRespository.save(entity);
			logger.info("Dispatch updated successfully for ID: {}", id);

			response.setMessage("Dispatched Details Updated Successfully ");
			response.setStatusCode(200);
			response.setPayload(toResponse(updated));

		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException in updateDispatchDeatils", e);
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
		} catch (ResourceNotFoundException e) {
			logger.error("Dispatch not found for update ID: {}", id, e);
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
		} catch (ExternalServiceException e) {
			logger.error("ExternalServiceException in updateDispatchDeatils", e);
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
		} catch (Exception e) {
			logger.error("Unexpected error in updateDispatchDeatils", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	@Override
	public ResponseEntity filterDispatchDetails(String deliveryChallanNo, String consignmentNo, String dispatchMode,
			String dispatchVendor, String status, int page, int size) {

		logger.info("Filter dispatch service started");

		ResponseEntity response = new ResponseEntity();

		try {

			Pageable pageable = PageRequest.of(page, size);

			Page<DispatchEntity> pageData = dispatchRespository.findAll(DispatchDetailsSpecification
					.filter(deliveryChallanNo, consignmentNo, dispatchMode, dispatchVendor, status),
					pageable);

			List<DispatchResponse> responseList = pageData.getContent().stream().map(entity -> toResponse(entity))
					.toList();

			if (responseList.isEmpty()) {
				logger.warn("No dispatch records found for given filter criteria");
				response.setMessage("No dispatch records found");
				response.setStatusCode(404);
				response.setPayload(null);
			} else {

				PageResponse<DispatchResponse> pageResponse = new PageResponse<>();
				pageResponse.setContent(responseList);
				pageResponse.setPageNumber(pageData.getNumber());
				pageResponse.setPageSize(pageData.getSize());
				pageResponse.setTotalElements(pageData.getTotalElements());
				pageResponse.setTotalPages(pageData.getTotalPages());
				pageResponse.setLastPage(pageData.isLast());

				logger.info("Filtered dispatch records fetched successfully. Total: {}", pageData.getTotalElements());

				response.setMessage("Dispatch records fetched successfully");
				response.setStatusCode(200);
				response.setPayload(pageResponse);
			}

		} catch (ResourceNotFoundException e) {
			logger.error("Resource not found while filtering dispatch: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);

		} catch (ExternalServiceException e) {
			logger.error("External service error while filtering dispatch: {}", e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Unexpected error while filtering dispatch: {}", e.getMessage(), e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}

	public DispatchEntity toEntity(DispatchRequest request) {
		DispatchEntity entity = new DispatchEntity();
		entity.setDeliveryChallanNo(request.getDeliveryChallanNo());
		entity.setConsignmentNo(request.getConsignmentNo());
		entity.setDispatchMode(request.getDispatchMode());
		entity.setStatus(request.getStatus());
		entity.setDispatchVendor(request.getDispatchVendor());
		entity.setRemarks(request.getRemarks());
		entity.setMrnNo(request.getMrnNo());
		return entity;
	}

	public DispatchResponse toResponse(DispatchEntity entity) {
		DispatchResponse response = new DispatchResponse();
		response.setDispatchId(entity.getDispatchId());
		response.setDeliveryChallanNo(entity.getDeliveryChallanNo());
		response.setConsignmentNo(entity.getConsignmentNo());
		response.setDispatchMode(entity.getDispatchMode());
		response.setDispatchVendor(entity.getDispatchVendor());
		response.setDispatchDate(entity.getDispatchDate());
		response.setRemarks(entity.getRemarks());
		response.setStatus(entity.getStatus());
		response.setMrnNo(entity.getMrnNo());
		response.setReceivedBy(entity.getReceivedBy());
		response.setReceivedDate(entity.getReceivedDate());
		return response;
	}
}