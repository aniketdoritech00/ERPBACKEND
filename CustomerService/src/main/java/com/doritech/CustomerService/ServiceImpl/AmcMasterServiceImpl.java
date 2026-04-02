package com.doritech.CustomerService.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.AmcMasterEntity;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.AmcMasterMapper;
import com.doritech.CustomerService.Repository.AmcMasterRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.AmcMasterRequest;
import com.doritech.CustomerService.Response.AmcMasterResponse;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Service.AmcMasterService;
import com.doritech.CustomerService.Specification.AmcMasterSpecification;
@Service
public class AmcMasterServiceImpl implements AmcMasterService {

	@Autowired
	private AmcMasterRepository repo;

	@Autowired
	private AmcMasterMapper mapper;

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	@Override
	public AmcMasterResponse create(AmcMasterRequest request) {

		validateAmcNumber(request.getAmcNumber());

		CustomerMasterEntity customer = customerMasterRepository
				.findById(request.getCustomerId())
				.orElseThrow(() -> new BadRequestException(
						"Customer not found with ID "
								+ request.getCustomerId()));

		AmcMasterEntity entity = mapper.toEntity(request);

		entity.setCustomer(customer);

		AmcMasterEntity savedEntity = repo.save(entity);

		return mapper.toResponse(savedEntity);
	}

	@Override
	public AmcMasterResponse update(Integer id, AmcMasterRequest request) {

		AmcMasterEntity entity = getAmcOrThrow(id);

		mapper.updateEntityFromRequest(request, entity);

		return mapper.toResponse(repo.save(entity));
	}

	@Override
	public void deleteMultipleByIds(List<Integer> ids) {

		List<AmcMasterEntity> entities = repo.findAllById(ids);

		if (entities.isEmpty()) {
			throw new ResourceNotFoundException("No AMC found for given IDs");
		}

		repo.deleteAllInBatch(entities);
	}

	@Override
	public AmcMasterResponse getByAmcNumber(String amcNumber) {

		return repo.findByAmcNumber(amcNumber).map(mapper::toResponse)
				.orElseThrow(
						() -> new ResourceNotFoundException("AMC not found"));
	}

	@Override
	public PageResponse<AmcMasterResponse> filter(String amcStatus,
			Integer customerId, String amcCategory, String amcName,
			LocalDate startDate, LocalDate endDate, BigDecimal minValue,
			BigDecimal maxValue, Integer createdBy, Integer page,
			Integer size) {

		Specification<AmcMasterEntity> spec = buildSpecification(amcStatus,
				customerId, amcCategory, amcName, startDate, endDate, minValue,
				maxValue, createdBy);

		Pageable pageable = PageRequest.of(page, size);

		Page<AmcMasterEntity> data = repo.findAll(spec, pageable);

		if (data.isEmpty()) {
			throw new ResourceNotFoundException(
					"AMC Data not found with given filters");
		}

		List<AmcMasterResponse> responses = data.stream()
				.map(mapper::toResponse).toList();

		return new PageResponse<>(responses, data.getNumber(), data.getSize(),
				data.getTotalElements(), data.getTotalPages(), data.isLast());
	}

	@Override
	public AmcMasterResponse getById(Integer id) {

		return mapper.toResponse(getAmcOrThrow(id));
	}

	// custom method to reuse them
	private void validateAmcNumber(String amcNumber) {
		repo.findByAmcNumber(amcNumber).ifPresent(a -> {
			throw new BadRequestException("AMC Number already exists");
		});
	}

	private AmcMasterEntity getAmcOrThrow(Integer id) {
		return repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"AMC not found with ID: " + id));
	}

	private Specification<AmcMasterEntity> buildSpecification(String amcStatus,
			Integer customerId, String amcCategory, String amcName,
			LocalDate startDate, LocalDate endDate, BigDecimal minValue,
			BigDecimal maxValue, Integer createdBy) {

		return Specification.where(AmcMasterSpecification.hasStatus(amcStatus))
				.and(AmcMasterSpecification.hasCustomerId(customerId))
				.and(AmcMasterSpecification.hasCategory(amcCategory))
				.and(AmcMasterSpecification.hasNameLike(amcName))
				.and(AmcMasterSpecification.startDateAfter(startDate))
				.and(AmcMasterSpecification.endDateBefore(endDate))
				.and(AmcMasterSpecification.valueGreaterThan(minValue))
				.and(AmcMasterSpecification.valueLessThan(maxValue))
				.and(AmcMasterSpecification.createdBy(createdBy));
	}

}
