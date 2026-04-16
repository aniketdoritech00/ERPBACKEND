package com.doritech.CustomerService.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.doritech.CustomerService.Entity.QuotationDetail;
import com.doritech.CustomerService.Entity.QuotationMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.QuotationDetailMapper;
import com.doritech.CustomerService.Repository.QuotationDetailRepository;
import com.doritech.CustomerService.Repository.QuotationMasterRepository;
import com.doritech.CustomerService.Request.QuotationDetailRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Response.QuotationDetailResponse;
import com.doritech.CustomerService.Service.QuotationDetailService;
import com.doritech.CustomerService.ValidationService.ValidationService;

import jakarta.validation.ConstraintViolationException;

@Service
public class QuotationDetailServiceImpl implements QuotationDetailService {

	private static final Logger logger = LoggerFactory.getLogger(QuotationDetailServiceImpl.class);

	private final QuotationDetailRepository repository;
	private final QuotationMasterRepository quotationRepository;
	private final QuotationDetailMapper mapper;

	@Autowired
	private ValidationService validationService;

	public QuotationDetailServiceImpl(QuotationDetailRepository repository,
			QuotationMasterRepository quotationRepository, QuotationDetailMapper mapper) {
		this.repository = repository;
		this.quotationRepository = quotationRepository;
		this.mapper = mapper;
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public ResponseEntity saveAndUpdateQuotationDetails(Integer quotationId, List<QuotationDetailRequest> requests) {

		logger.info("Save/Update quotation details service started for quotationId {}", quotationId);

		ResponseEntity response = new ResponseEntity();

		try {

			if (quotationId == null) {
				logger.error("Quotation id cannot be null");
				response.setMessage("Quotation id cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (requests == null || requests.isEmpty()) {
				logger.error("Request list cannot be null or empty");
				response.setMessage("Request list cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			logger.info("Validating quotation with id {}", quotationId);
			QuotationMaster quotation = quotationRepository.findById(quotationId).orElseThrow(() -> {
				logger.error("Quotation not found with id {}", quotationId);
				return new ResourceNotFoundException("Quotation not found with id " + quotationId);
			});

			requests.sort(Comparator.comparing(r -> r == null || r.getParentItemId() == null ? 0 : 1));

			Map<Integer, Integer> itemIdToDetailId = new HashMap<>();
			int createCount = 0;
			int updateCount = 0;
			List<QuotationDetailResponse> resultList = new ArrayList<>();

			for (int i = 0; i < requests.size(); i++) {

				QuotationDetailRequest request = requests.get(i);

				if (request == null) {
					logger.warn("Request at index {} is null, skipping", i);
					continue;
				}

				logger.info(
						"Processing record [{}] - quotationDetailId: {}, itemId: {}, siteId: {}, parentItemId: {}",
						i, request.getQuotationDetailId(), request.getItemId(),
						request.getSiteId(), request.getParentItemId());

				if (request.getItemId() == null) {
					logger.error("itemId is null at record [{}]", i);
					response.setMessage("itemId cannot be null at index " + i);
					response.setStatusCode(400);
					response.setPayload(null);
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return response;
				}

				if (request.getSiteId() == null) {
					logger.error("siteId is null at record [{}]", i);
					response.setMessage("siteId cannot be null at index " + i);
					response.setStatusCode(400);
					response.setPayload(null);
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return response;
				}

				logger.info("Validating site with id {} for record [{}]", request.getSiteId(), i);
				validationService.validateSiteExists(request.getSiteId());

				logger.info("Validating item with id {} for record [{}]", request.getItemId(), i);
				validationService.validateItemExists(request.getItemId());

				if (request.getParentItemId() != null) {
					logger.info("parentItemId {} found for record [{}], validating existence",
							request.getParentItemId(), i);

					boolean parentExistsInBatch = itemIdToDetailId.containsKey(request.getParentItemId());
					boolean parentExistsInDB = !parentExistsInBatch
							&& repository.existsByItemId(request.getParentItemId());

					if (!parentExistsInBatch && !parentExistsInDB) {
						logger.error("Parent item not found with id {} for record [{}]",
								request.getParentItemId(), i);
						response.setMessage("Parent item not found with id "
								+ request.getParentItemId() + " at index " + i);
						response.setStatusCode(404);
						response.setPayload(null);
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return response;
					}

					logger.info("Parent item id {} validated successfully for record [{}]",
							request.getParentItemId(), i);
				}

				QuotationDetail entity;

				if (request.getQuotationDetailId() == null) {

					logger.info("No quotationDetailId found for record [{}], performing CREATE", i);

					Integer resolvedParentDetailId = resolveParentDetailId(
							request.getParentItemId(), itemIdToDetailId);

					logger.info(
							"Checking duplicate for quotationId {}, itemId {}, siteId {},"
									+ " resolvedParentDetailId {} at record [{}]",
							quotationId, request.getItemId(), request.getSiteId(),
							resolvedParentDetailId, i);

					boolean isDuplicate = repository
							.existsByQuotationMasterQuotationIdAndItemIdAndSiteIdAndParentItemId(
									quotationId, request.getItemId(),
									request.getSiteId(), resolvedParentDetailId);

					if (isDuplicate) {
						logger.error(
								"Duplicate record found for quotationId {}, itemId {}, siteId {},"
										+ " parentDetailId {} at record [{}]",
								quotationId, request.getItemId(), request.getSiteId(),
								resolvedParentDetailId, i);
						response.setMessage("Record already exists for quotationId " + quotationId
								+ ", itemId " + request.getItemId()
								+ ", siteId " + request.getSiteId()
								+ ", parentDetailId " + resolvedParentDetailId
								+ " at index " + i);
						response.setStatusCode(409);
						response.setPayload(null);
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return response;
					}

					entity = mapper.toEntity(request);
					entity.setQuotationMaster(quotation);
					entity.setSiteId(request.getSiteId());
					entity.setQty(request.getQty());
					entity.setItemId(request.getItemId());
					entity.setParentItemId(resolvedParentDetailId);
					entity.setItemPrice(request.getItemPrice());
					entity.setGstRate(request.getGstRate());
					entity.setIsActive(request.getIsActive());
					entity.setCreatedOn(new Date());
					entity.setCreatedBy(request.getCreatedBy());
					entity.setModifiedOn(null);
					entity.setModifiedBy(null);

					createCount++;

				} else {

					logger.info("quotationDetailId {} found for record [{}], performing UPDATE",
							request.getQuotationDetailId(), i);

					entity = repository.findById(request.getQuotationDetailId()).orElseThrow(() -> {
						logger.error("Quotation detail not found with id {}",
								request.getQuotationDetailId());
						return new ResourceNotFoundException(
								"Quotation detail not found with id "
										+ request.getQuotationDetailId());
					});

					Integer resolvedParentDetailId = resolveParentDetailId(
							request.getParentItemId(), itemIdToDetailId);

					logger.info(
							"Checking duplicate for quotationId {}, itemId {}, siteId {},"
									+ " resolvedParentDetailId {} excluding quotationDetailId {}"
									+ " at record [{}]",
							quotationId, request.getItemId(), request.getSiteId(),
							resolvedParentDetailId, request.getQuotationDetailId(), i);

					boolean isDuplicate = repository
							.existsByQuotationMasterQuotationIdAndItemIdAndSiteIdAndParentItemIdAndQuotationDetailIdNot(
									quotationId, request.getItemId(), request.getSiteId(),
									resolvedParentDetailId, request.getQuotationDetailId());

					if (isDuplicate) {
						logger.error(
								"Duplicate record found for quotationId {}, itemId {}, siteId {},"
										+ " parentDetailId {} excluding id {} at record [{}]",
								quotationId, request.getItemId(), request.getSiteId(),
								resolvedParentDetailId, request.getQuotationDetailId(), i);
						response.setMessage("Record already exists for quotationId " + quotationId
								+ ", itemId " + request.getItemId()
								+ ", siteId " + request.getSiteId()
								+ ", parentDetailId " + resolvedParentDetailId
								+ " at index " + i);
						response.setStatusCode(409);
						response.setPayload(null);
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return response;
					}

					entity.setSiteId(request.getSiteId());
					entity.setItemId(request.getItemId());
					entity.setQty(request.getQty());
					entity.setItemPrice(request.getItemPrice());
					entity.setGstRate(request.getGstRate());
					entity.setParentItemId(resolvedParentDetailId);
					entity.setIsActive(request.getIsActive());
					entity.setModifiedOn(new Date());
					entity.setModifiedBy(request.getModifiedBy());

					updateCount++;
				}

				QuotationDetail saved = repository.save(entity);
				itemIdToDetailId.put(saved.getItemId(), saved.getQuotationDetailId());
				resultList.add(mapper.toResponse(saved));

				logger.info("Record [{}] saved successfully with quotationDetailId {}", i,
						saved.getQuotationDetailId());
			}

			logger.info(
					"Save/Update quotation details completed for quotationId {}"
							+ " | Created: {}, Updated: {}",
					quotationId, createCount, updateCount);

			response.setMessage("Quotation details saved successfully | Created: "
					+ createCount + ", Updated: " + updateCount);
			response.setStatusCode(200);
			response.setPayload(resultList);

		} catch (BadRequestException e) {
			logger.error("Bad request while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage());
			response.setMessage("Invalid input: " + e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		} catch (ConstraintViolationException e) {
			logger.error(
					"Bean validation failed while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage());
			response.setMessage("Validation failed: " + e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		} catch (ResourceNotFoundException e) {
			logger.error(
					"Resource not found while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		} catch (DataIntegrityViolationException e) {
			logger.error(
					"Data integrity violation while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage());
			response.setMessage(
					"Invalid reference data: the provided item or site does not exist"
							+ " in the local database");
			response.setStatusCode(409);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		} catch (DataAccessException e) {
			logger.error(
					"Database error while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage(), e);
			response.setMessage("Database error: " + e.getMostSpecificCause().getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		} catch (NullPointerException e) {
			logger.error(
					"Null pointer exception while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage(), e);
			response.setMessage(
					"A required field is null. Please check the request payload.");
			response.setStatusCode(400);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

		} catch (Exception e) {
			logger.error(
					"Unexpected error while saving quotation details for quotationId {}: {}",
					quotationId, e.getMessage(), e);
			response.setMessage("Unable to save quotation details: " + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}

		return response;
	}


	private Integer resolveParentDetailId(Integer parentItemId,
			Map<Integer, Integer> itemIdToDetailId) {
		if (parentItemId == null)
			return null;
		Integer resolvedId = itemIdToDetailId.get(parentItemId);
		if (resolvedId != null)
			return resolvedId;
		QuotationDetail parentEntity = repository.findByItemId(parentItemId);
		if (parentEntity != null)
			return parentEntity.getQuotationDetailId();
		return null;
	}


	@Override
	public ResponseEntity deleteQuotationDetails(List<Integer> ids) {

		logger.info("Bulk delete quotation detail service started for ids {}", ids);

		ResponseEntity response = new ResponseEntity();

		try {

			if (ids == null || ids.isEmpty()) {
				logger.error("Quotation detail ids list cannot be null or empty");
				response.setMessage("Quotation detail ids list cannot be null or empty");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			boolean hasNullId = ids.stream().anyMatch(id -> id == null);
			if (hasNullId) {
				logger.error("ids list contains null values");
				response.setMessage("ids list must not contain null values");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			List<QuotationDetail> entities = repository.findAllById(ids);

			if (entities.isEmpty()) {
				logger.error("No quotation details found for given ids {}", ids);
				response.setMessage("No quotation details found for given ids");
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			if (entities.size() != ids.size()) {
				List<Integer> foundIds = entities.stream()
						.map(QuotationDetail::getQuotationDetailId).toList();
				List<Integer> notFoundIds = ids.stream()
						.filter(id -> !foundIds.contains(id)).toList();
				logger.warn("Some IDs not found and will be skipped: {}", notFoundIds);
			}

			repository.deleteAll(entities);

			logger.info("Quotation details deleted successfully for ids {}", ids);

			response.setMessage("Quotation details deleted successfully");
			response.setStatusCode(200);
			response.setPayload(ids);

		} catch (DataIntegrityViolationException e) {
			logger.error(
					"Data integrity violation while deleting quotation details for ids {}: {}",
					ids, e.getMessage());
			response.setMessage(
					"Cannot delete: one or more records are referenced by other data");
			response.setStatusCode(409);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error("Database error while deleting quotation details for ids {}: {}",
					ids, e.getMessage(), e);
			response.setMessage("Database error: " + e.getMostSpecificCause().getMessage());
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument while deleting quotation details for ids {}: {}",
					ids, e.getMessage());
			response.setMessage("Invalid input: " + e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error(
					"Unexpected error while bulk deleting quotation details for ids {}: {}",
					ids, e.getMessage(), e);
			response.setMessage("Unable to delete quotation details: " + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}


	@Override
	public ResponseEntity getQuotationDetailById(Integer id) {

		logger.info("Fetch quotation detail service started for id {}", id);

		ResponseEntity response = new ResponseEntity();

		try {

			if (id == null) {
				logger.error("Quotation detail id cannot be null");
				response.setMessage("Quotation detail id cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (id <= 0) {
				logger.error("Quotation detail id must be a positive integer, received: {}", id);
				response.setMessage("Quotation detail id must be a positive integer");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			QuotationDetail entity = repository.findById(id).orElseThrow(() -> {
				logger.error("Quotation detail not found with id {}", id);
				return new ResourceNotFoundException(
						"Quotation detail not found with id " + id);
			});

			QuotationDetailResponse detailResponse = mapper.toResponse(entity);

			CompSiteResponse site = validationService.validateAndGetSite(
					entity.getSiteId(), "Quotation");
			ItemIDResponse item = validationService.validateAndGetItem(entity.getItemId());

			logger.info("Fetched siteName: {} and itemName: {} for quotation detail id {}",
					site.getSiteName(), item.getItemName(), id);

			detailResponse.setSiteName(site.getSiteName());
			detailResponse.setItemName(item.getItemName());

			if (entity.getParentItemId() != null) {
				logger.info(
						"Fetching parent entity for parentItemId (quotationDetailId) {}"
								+ " for quotation detail id {}",
						entity.getParentItemId(), id);

				QuotationDetail parentEntity = repository
						.findById(entity.getParentItemId()).orElse(null);

				if (parentEntity != null) {
					ItemIDResponse parentItem = validationService
							.validateAndGetItem(parentEntity.getItemId());
					logger.info("Fetched parentItemName: {} for quotation detail id {}",
							parentItem.getItemName(), id);
					detailResponse.setParentItemName(parentItem.getItemName());
				} else {
					logger.warn(
							"Parent entity not found for parentItemId {} for quotation detail id {}",
							entity.getParentItemId(), id);
					detailResponse.setParentItemName(null);
				}
			} else {
				logger.info("parentItemId is null for quotation detail id {}", id);
				detailResponse.setParentItemName(null);
			}

			logger.info("Fetching children for quotationDetailId {}",
					entity.getQuotationDetailId());

			List<QuotationDetail> childEntities = repository
					.findByQuotationMasterQuotationIdAndParentItemId(
							entity.getQuotationMaster().getQuotationId(),
							entity.getQuotationDetailId());

			if (childEntities != null && !childEntities.isEmpty()) {

				logger.info("Found {} children for quotationDetailId {}",
						childEntities.size(), entity.getQuotationDetailId());

				List<QuotationDetailResponse> children = childEntities.stream().map(child -> {

					QuotationDetailResponse childResponse = mapper.toResponse(child);

					try {
						CompSiteResponse childSite = validationService
								.validateAndGetSite(child.getSiteId(), "Quotation");
						ItemIDResponse childItem = validationService
								.validateAndGetItem(child.getItemId());

						logger.info(
								"Fetched siteName: {} and itemName: {}"
										+ " for child quotationDetailId {}",
								childSite.getSiteName(), childItem.getItemName(),
								child.getQuotationDetailId());

						childResponse.setSiteName(childSite.getSiteName());
						childResponse.setItemName(childItem.getItemName());
						childResponse.setParentItemName(item.getItemName());
						childResponse.setChildren(null);

					} catch (ExternalServiceException e) {
						logger.warn(
								"External service error for child quotationDetailId {}: {}",
								child.getQuotationDetailId(), e.getMessage());
						childResponse.setSiteName(null);
						childResponse.setItemName(null);
						childResponse.setParentItemName(null);
					} catch (ResourceNotFoundException e) {
						logger.warn(
								"Site or item not found for child quotationDetailId {}: {}",
								child.getQuotationDetailId(), e.getMessage());
						childResponse.setSiteName(null);
						childResponse.setItemName(null);
						childResponse.setParentItemName(null);
					}

					return childResponse;

				}).toList();

				detailResponse.setChildren(children);

			} else {
				logger.info("No children found for quotationDetailId {}",
						entity.getQuotationDetailId());
				detailResponse.setChildren(Collections.emptyList());
			}

			logger.info("Quotation detail fetched successfully with id {}", id);

			response.setMessage("Quotation detail fetched successfully");
			response.setStatusCode(200);
			response.setPayload(detailResponse);

		} catch (BadRequestException e) {
			logger.error("Bad request while fetching quotation detail with id {}: {}",
					id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);

		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument while fetching quotation detail with id {}: {}",
					id, e.getMessage());
			response.setMessage("Invalid input: " + e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);

		} catch (ResourceNotFoundException e) {
			logger.error(
					"Resource not found while fetching quotation detail with id {}: {}",
					id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(404);
			response.setPayload(null);

		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while fetching quotation detail with id {}: {}",
					id, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error("Database error while fetching quotation detail with id {}: {}",
					id, e.getMessage(), e);
			response.setMessage("Database error: " + e.getMostSpecificCause().getMessage());
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (NullPointerException e) {
			logger.error("Null pointer while fetching quotation detail with id {}: {}",
					id, e.getMessage(), e);
			response.setMessage(
					"A required value is missing in the stored data. Contact support.");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error(
					"Unexpected error while fetching quotation detail with id {}: {}",
					id, e.getMessage(), e);
			response.setMessage("Unable to fetch quotation detail: " + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}


	@Override
	public ResponseEntity getAllQuotationDetails(int page, int size) {

		logger.info("Fetch all quotation details service started - page: {}, size: {}",
				page, size);

		ResponseEntity response = new ResponseEntity();

		try {

			if (page < 0) {
				logger.error("Invalid page number: {}", page);
				response.setMessage("page must be >= 0, received: " + page);
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (size <= 0) {
				logger.error("Invalid page size: {}", size);
				response.setMessage("size must be > 0, received: " + size);
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			Pageable pageable = PageRequest.of(page, size);
			Page<QuotationDetail> parentPage = repository.findByParentItemIdIsNull(pageable);

			if (parentPage == null || parentPage.isEmpty()) {
				logger.info("No quotation details found");
				response.setMessage("No quotation details available");
				response.setStatusCode(200);
				response.setPayload(Collections.emptyList());
				return response;
			}

			logger.info("Found {} parent records", parentPage.getTotalElements());

			List<QuotationDetailResponse> list = parentPage.getContent().stream().map(entity -> {

				QuotationDetailResponse detailResponse = mapper.toResponse(entity);

				try {
					CompSiteResponse site = validationService.validateAndGetSite(
							entity.getSiteId(), "Quotation");
					ItemIDResponse item = validationService.validateAndGetItem(
							entity.getItemId());

					logger.info(
							"Fetched siteName: {} and itemName: {} for quotationDetailId {}",
							site.getSiteName(), item.getItemName(),
							entity.getQuotationDetailId());

					detailResponse.setSiteName(site.getSiteName());
					detailResponse.setItemName(item.getItemName());
					detailResponse.setParentItemName(null);

					logger.info("Fetching children for quotationDetailId {}",
							entity.getQuotationDetailId());

					List<QuotationDetail> childEntities = repository
							.findByQuotationMasterQuotationIdAndParentItemId(
									entity.getQuotationMaster().getQuotationId(),
									entity.getQuotationDetailId());

					if (childEntities != null && !childEntities.isEmpty()) {

						logger.info("Found {} children for quotationDetailId {}",
								childEntities.size(), entity.getQuotationDetailId());

						List<QuotationDetailResponse> children = childEntities.stream()
								.map(child -> {

									QuotationDetailResponse childResponse = mapper
											.toResponse(child);

									try {
										CompSiteResponse childSite = validationService
												.validateAndGetSite(child.getSiteId(), "Quotation");
										ItemIDResponse childItem = validationService
												.validateAndGetItem(child.getItemId());

										logger.info(
												"Fetched siteName: {} and itemName: {}"
														+ " for child quotationDetailId {}",
												childSite.getSiteName(),
												childItem.getItemName(),
												child.getQuotationDetailId());

										childResponse.setSiteName(childSite.getSiteName());
										childResponse.setItemName(childItem.getItemName());
										childResponse.setParentItemName(item.getItemName());
										childResponse.setChildren(null);

									} catch (ExternalServiceException e) {
										logger.warn(
												"External service error for child"
														+ " quotationDetailId {}: {}",
												child.getQuotationDetailId(), e.getMessage());
										childResponse.setSiteName(null);
										childResponse.setItemName(null);
										childResponse.setParentItemName(null);
									} catch (ResourceNotFoundException e) {
										logger.warn(
												"Site or item not found for child"
														+ " quotationDetailId {}: {}",
												child.getQuotationDetailId(), e.getMessage());
										childResponse.setSiteName(null);
										childResponse.setItemName(null);
										childResponse.setParentItemName(null);
									}

									return childResponse;

								}).toList();

						detailResponse.setChildren(children);

					} else {
						logger.info("No children found for quotationDetailId {}",
								entity.getQuotationDetailId());
						detailResponse.setChildren(Collections.emptyList());
					}

				} catch (ExternalServiceException e) {
					logger.warn("External service error for quotationDetailId {}: {}",
							entity.getQuotationDetailId(), e.getMessage());
					detailResponse.setSiteName(null);
					detailResponse.setItemName(null);
					detailResponse.setParentItemName(null);
					detailResponse.setChildren(Collections.emptyList());
				} catch (ResourceNotFoundException e) {
					logger.warn("Site or item not found for quotationDetailId {}: {}",
							entity.getQuotationDetailId(), e.getMessage());
					detailResponse.setSiteName(null);
					detailResponse.setItemName(null);
					detailResponse.setParentItemName(null);
					detailResponse.setChildren(Collections.emptyList());
				}

				return detailResponse;

			}).toList();

			Map<String, Object> payload = new HashMap<>();
			payload.put("data", list);
			payload.put("currentPage", parentPage.getNumber());
			payload.put("totalItems", parentPage.getTotalElements());
			payload.put("totalPages", parentPage.getTotalPages());
			payload.put("pageSize", parentPage.getSize());

			logger.info("Fetched total {} quotation details - page {}/{}",
					parentPage.getTotalElements(), page + 1, parentPage.getTotalPages());

			response.setMessage("Quotation details fetched successfully");
			response.setStatusCode(200);
			response.setPayload(payload);

		} catch (IllegalArgumentException e) {
			logger.error("Illegal argument while fetching all quotation details: {}",
					e.getMessage());
			response.setMessage("Invalid pagination parameters: " + e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);

		} catch (ExternalServiceException e) {
			logger.error("External service error while fetching all quotation details: {}",
					e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error("Database error while fetching all quotation details: {}",
					e.getMessage(), e);
			response.setMessage("Database error: " + e.getMostSpecificCause().getMessage());
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (NullPointerException e) {
			logger.error("Null pointer while fetching all quotation details: {}",
					e.getMessage(), e);
			response.setMessage(
					"A required value is missing in the stored data. Contact support.");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error("Unexpected error while fetching all quotation details: {}",
					e.getMessage(), e);
			response.setMessage("Unable to fetch quotation details: " + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}


	@Override
	public ResponseEntity getByQuotationId(Integer quotationId) {

		logger.info("Fetch quotation details by quotationId service started for quotationId {}",
				quotationId);

		ResponseEntity response = new ResponseEntity();

		try {

			if (quotationId == null) {
				logger.error("Quotation id cannot be null");
				response.setMessage("Quotation id cannot be null");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			if (quotationId <= 0) {
				logger.error("Quotation id must be a positive integer, received: {}", quotationId);
				response.setMessage("Quotation id must be a positive integer");
				response.setStatusCode(400);
				response.setPayload(null);
				return response;
			}

			boolean quotationExists = quotationRepository.existsById(quotationId);
			if (!quotationExists) {
				logger.error("Quotation not found with id {}", quotationId);
				response.setMessage("Quotation not found with id " + quotationId);
				response.setStatusCode(404);
				response.setPayload(null);
				return response;
			}

			List<QuotationDetail> parentEntities = repository
					.findByQuotationMasterQuotationIdAndParentItemIdIsNull(quotationId);

			if (parentEntities == null || parentEntities.isEmpty()) {
				logger.info("No quotation details found for quotationId {}", quotationId);
				response.setMessage(
						"No quotation details available for quotationId " + quotationId);
				response.setStatusCode(200);
				response.setPayload(Collections.emptyList());
				return response;
			}

			logger.info("Found {} parent records for quotationId {}", parentEntities.size(),
					quotationId);

			List<QuotationDetailResponse> list = parentEntities.stream().map(entity -> {

				QuotationDetailResponse detailResponse = mapper.toResponse(entity);

				try {
					CompSiteResponse site = validationService.validateAndGetSite(
							entity.getSiteId(), "Quotation");
					ItemIDResponse item = validationService.validateAndGetItem(
							entity.getItemId());

					logger.info(
							"Fetched siteName: {} and itemName: {} for quotationDetailId {}",
							site.getSiteName(), item.getItemName(),
							entity.getQuotationDetailId());

					detailResponse.setSiteName(site.getSiteName());
					detailResponse.setItemName(item.getItemName());
					detailResponse.setParentItemName(null);

					logger.info("Fetching children for quotationDetailId {}",
							entity.getQuotationDetailId());

					List<QuotationDetail> childEntities = repository
							.findByQuotationMasterQuotationIdAndParentItemId(
									quotationId, entity.getQuotationDetailId());

					if (childEntities != null && !childEntities.isEmpty()) {

						logger.info("Found {} children for quotationDetailId {}",
								childEntities.size(), entity.getQuotationDetailId());

						List<QuotationDetailResponse> children = childEntities.stream()
								.map(child -> {

									QuotationDetailResponse childResponse = mapper
											.toResponse(child);

									try {
										CompSiteResponse childSite = validationService
												.validateAndGetSite(child.getSiteId(), "Quotation");
										ItemIDResponse childItem = validationService
												.validateAndGetItem(child.getItemId());

										logger.info(
												"Fetched siteName: {} and itemName: {}"
														+ " for child quotationDetailId {}",
												childSite.getSiteName(),
												childItem.getItemName(),
												child.getQuotationDetailId());

										childResponse.setSiteName(childSite.getSiteName());
										childResponse.setItemName(childItem.getItemName());
										childResponse.setParentItemName(item.getItemName());
										childResponse.setChildren(null);

									} catch (ExternalServiceException e) {
										logger.warn(
												"External service error for child"
														+ " quotationDetailId {}: {}",
												child.getQuotationDetailId(), e.getMessage());
										childResponse.setSiteName(null);
										childResponse.setItemName(null);
										childResponse.setParentItemName(null);
									} catch (ResourceNotFoundException e) {
										logger.warn(
												"Site or item not found for child"
														+ " quotationDetailId {}: {}",
												child.getQuotationDetailId(), e.getMessage());
										childResponse.setSiteName(null);
										childResponse.setItemName(null);
										childResponse.setParentItemName(null);
									}

									return childResponse;

								}).toList();

						detailResponse.setChildren(children);

					} else {
						logger.info("No children found for quotationDetailId {}",
								entity.getQuotationDetailId());
						detailResponse.setChildren(Collections.emptyList());
					}

				} catch (ExternalServiceException e) {
					logger.warn("External service error for quotationDetailId {}: {}",
							entity.getQuotationDetailId(), e.getMessage());
					detailResponse.setSiteName(null);
					detailResponse.setItemName(null);
					detailResponse.setParentItemName(null);
					detailResponse.setChildren(Collections.emptyList());
				} catch (ResourceNotFoundException e) {
					logger.warn("Site or item not found for quotationDetailId {}: {}",
							entity.getQuotationDetailId(), e.getMessage());
					detailResponse.setSiteName(null);
					detailResponse.setItemName(null);
					detailResponse.setParentItemName(null);
					detailResponse.setChildren(Collections.emptyList());
				}

				return detailResponse;

			}).toList();

			logger.info("Fetched {} parent records with children for quotationId {}",
					list.size(), quotationId);

			response.setMessage("Quotation details fetched successfully");
			response.setStatusCode(200);
			response.setPayload(list);

		} catch (IllegalArgumentException e) {
			logger.error(
					"Illegal argument while fetching quotation details for quotationId {}: {}",
					quotationId, e.getMessage());
			response.setMessage("Invalid input: " + e.getMessage());
			response.setStatusCode(400);
			response.setPayload(null);

		} catch (ExternalServiceException e) {
			logger.error(
					"External service error while fetching quotation details for quotationId {}: {}",
					quotationId, e.getMessage());
			response.setMessage(e.getMessage());
			response.setStatusCode(502);
			response.setPayload(null);

		} catch (DataAccessException e) {
			logger.error(
					"Database error while fetching quotation details for quotationId {}: {}",
					quotationId, e.getMessage(), e);
			response.setMessage("Database error: " + e.getMostSpecificCause().getMessage());
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (NullPointerException e) {
			logger.error(
					"Null pointer while fetching quotation details for quotationId {}: {}",
					quotationId, e.getMessage(), e);
			response.setMessage(
					"A required value is missing in the stored data. Contact support.");
			response.setStatusCode(500);
			response.setPayload(null);

		} catch (Exception e) {
			logger.error(
					"Unexpected error while fetching quotation details for quotationId {}: {}",
					quotationId, e.getMessage(), e);
			response.setMessage("Unable to fetch quotation details: " + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}
}