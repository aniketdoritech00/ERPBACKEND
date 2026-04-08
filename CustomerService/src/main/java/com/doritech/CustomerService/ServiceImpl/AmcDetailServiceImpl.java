package com.doritech.CustomerService.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.AmcDetailEntity;
import com.doritech.CustomerService.Entity.AmcMasterEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Mapper.AmcDetailMapper;
import com.doritech.CustomerService.Repository.AmcDetailRepository;
import com.doritech.CustomerService.Repository.AmcMasterRepository;
import com.doritech.CustomerService.Request.AmcDetailFilterRequest;
import com.doritech.CustomerService.Request.AmcDetailRequest;
import com.doritech.CustomerService.Response.AmcDetailResponse;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Service.AmcDetailService;
import com.doritech.CustomerService.Specification.AmcDetailSpecification;

@Service
public class AmcDetailServiceImpl implements AmcDetailService {

	@Autowired
	private AmcDetailRepository repo;

	@Autowired
	private AmcMasterRepository amcMasterRepository;

	@Autowired
	private AmcDetailMapper mapper;

	@Override
	public AmcDetailResponse create(AmcDetailRequest request) {

		AmcMasterEntity amc = amcMasterRepository.findById(request.getAmcId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"AMC not found with ID: " + request.getAmcId()));

		AmcDetailEntity entity = mapper.toEntity(request);
		entity.setAmc(amc);

		return mapper.toResponse(repo.save(entity));
	}

	@Override
	public List<AmcDetailResponse> createBulk(List<AmcDetailRequest> requests) {

		if (requests == null || requests.isEmpty()) {
			throw new BadRequestException("Request list cannot be empty");
		}

		List<AmcDetailEntity> entities = requests.stream().map(req -> {

			AmcMasterEntity amc = amcMasterRepository.findById(req.getAmcId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"AMC not found with ID: " + req.getAmcId()));

			AmcDetailEntity entity = mapper.toEntity(req);
			entity.setAmc(amc);

			return entity;

		}).toList();

		List<AmcDetailEntity> saved = repo.saveAll(entities);

		return saved.stream().map(mapper::toResponse).toList();
	}

	// @Override
	// public List<AmcDetailResponse> updateBulk(Integer
	// amcId,List<AmcDetailRequest> requests) {
	//
	// if (requests == null || requests.isEmpty()) {
	// throw new BadRequestException("Request list cannot be empty");
	// }
	//
	// List<AmcDetailEntity> entities = requests.stream().map(req -> {
	//
	// AmcDetailEntity entity = repo.findById(req.getAmcDetailId())
	// .orElseThrow(() -> new ResourceNotFoundException(
	// "AMC Detail not found with ID: "
	// + req.getAmcDetailId()));
	//
	// mapper.updateEntityFromRequest(req, entity);
	//
	// if (req.getAmcId() != null) {
	// AmcMasterEntity amc = amcMasterRepository
	// .findById(req.getAmcId())
	// .orElseThrow(() -> new ResourceNotFoundException(
	// "AMC not found with ID: " + req.getAmcId()));
	//
	// entity.setAmc(amc);
	// }
	//
	// return entity;
	//
	// }).toList();
	//
	// List<AmcDetailEntity> updated = repo.saveAll(entities);
	//
	// return updated.stream().map(mapper::toResponse).toList();
	// }

	@Override
	public AmcDetailResponse update(Integer id, AmcDetailRequest request) {

		AmcDetailEntity entity = getOrThrow(id);

		mapper.updateEntityFromRequest(request, entity);

		if (request.getAmcId() != null) {
			AmcMasterEntity amc = amcMasterRepository
					.findById(request.getAmcId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"AMC not found with ID: " + request.getAmcId()));

			entity.setAmc(amc);
		}

		return mapper.toResponse(repo.save(entity));
	}

	@Override
	public void deleteById(Integer id) {

		AmcDetailEntity entity = getOrThrow(id);
		repo.delete(entity);
	}

	@Override
	public List<AmcDetailResponse> getByAmcId(Integer amcId) {

		List<AmcDetailEntity> list = repo.findByAmc_AmcId(amcId);

		if (list.isEmpty()) {
			throw new ResourceNotFoundException("No AMC Details found");
		}

		return list.stream().map(mapper::toResponse).toList();
	}

	@Override
	public AmcDetailResponse getById(Integer id) {
		return mapper.toResponse(getOrThrow(id));
	}

	private AmcDetailEntity getOrThrow(Integer id) {
		return repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"AMC Detail not found with ID: " + id));
	}

	@Override
	public PageResponse<AmcDetailResponse> filter(
			AmcDetailFilterRequest request) {

		Specification<AmcDetailEntity> spec = Specification
				.where(AmcDetailSpecification.hasAmcId(request.getAmcId()))
				.and(AmcDetailSpecification.hasItemId(request.getItemId()))
				.and(AmcDetailSpecification
						.hasDescription(request.getDescription()))
				.and(AmcDetailSpecification
						.hasCreatedBy(request.getCreatedBy()))
				.and(AmcDetailSpecification.createdAfter(request.getFromDate()))
				.and(AmcDetailSpecification.createdBefore(request.getToDate()));

		Pageable pageable = PageRequest.of(request.getPage(),
				request.getSize());

		Page<AmcDetailEntity> data = repo.findAll(spec, pageable);

		List<AmcDetailResponse> responses = data.stream()
				.map(mapper::toResponse).toList();

		return new PageResponse<>(responses, data.getNumber(), data.getSize(),
				data.getTotalElements(), data.getTotalPages(), data.isLast());

	}
}