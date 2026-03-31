package com.doritech.ItemService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.doritech.ItemService.Entity.CommTemplateMasterEntity;
import com.doritech.ItemService.Exception.BusinessException;
import com.doritech.ItemService.Exception.ResourceNotFoundException;
import com.doritech.ItemService.Mapper.CommTemplateMapper;
import com.doritech.ItemService.Repository.CommTemplateRepository;
import com.doritech.ItemService.Request.CommTemplateRequestDTO;
import com.doritech.ItemService.Response.CommTemplateResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;
import com.doritech.ItemService.Service.CommTemplateService;

import jakarta.persistence.criteria.Predicate;

@Service
public class CommTemplateServiceImpl implements CommTemplateService {

	@Autowired
	private CommTemplateRepository repo;

	@Autowired
	private CommTemplateMapper mapper;

	@Override
	public CommTemplateResponseDTO saveTemplate(CommTemplateRequestDTO dto) {

		if (repo.existsByTemplateNameAndCustomerId(dto.getTemplateName(),
				dto.getCustomerId())) {

			throw new BusinessException(
					"Template already exists for this customer");
		}

		CommTemplateMasterEntity entity = mapper.toEntity(dto);

		entity.setCreatedOn(LocalDateTime.now());

		// if (entity.getIsActive() == null)
		// entity.setIsActive("Y");

		repo.save(entity);

		return mapper.toDTO(entity);
	}

	@Override
	public CommTemplateResponseDTO updateTemplate(Integer id,
			CommTemplateRequestDTO dto) {

		CommTemplateMasterEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Template not found with id : " + id));

		mapper.updateEntityFromDTO(dto, entity);

		entity.setModifiedOn(LocalDateTime.now());

		repo.save(entity);

		return mapper.toDTO(entity);
	}

	@Override
	public void deleteTemplate(Integer id) {

		CommTemplateMasterEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Template not found with id : " + id));

		repo.delete(entity);
	}

	@Override
	public CommTemplateResponseDTO getTemplateById(Integer id) {

		CommTemplateMasterEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Template not found with id : " + id));

		return mapper.toDTO(entity);
	}

	@Override
	public List<CommTemplateResponseDTO> getAllTemplate() {

		return repo.findAll().stream().map(mapper::toDTO).toList();
	}

	@Override
	public PageResponseDTO<CommTemplateResponseDTO> getAllTemplateWithPagination(
			int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		Page<CommTemplateMasterEntity> result = repo.findAll(pageable);

		Page<CommTemplateResponseDTO> dataPage = result.map(mapper::toDTO);

		return new PageResponseDTO<>(dataPage);
	}

	@Override
	public List<CommTemplateResponseDTO> filterTemplate(Integer customerId,
			String commType, String templateType, String isActive) {

		Specification<CommTemplateMasterEntity> spec = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (customerId != null)
				predicates.add(cb.equal(root.get("customerId"), customerId));

			if (commType != null)
				predicates.add(cb.equal(root.get("commType"), commType));

			if (templateType != null)
				predicates
						.add(cb.equal(root.get("templateType"), templateType));

			if (isActive != null)
				predicates.add(cb.equal(root.get("isActive"), isActive));

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		return repo.findAll(spec).stream().map(mapper::toDTO).toList();
	}
}