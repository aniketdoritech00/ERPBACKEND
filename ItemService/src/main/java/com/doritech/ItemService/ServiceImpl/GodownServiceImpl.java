package com.doritech.ItemService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.ItemService.Entity.GodownMasterEntity;
import com.doritech.ItemService.Exception.BusinessException;
import com.doritech.ItemService.Exception.ResourceNotFoundException;
import com.doritech.ItemService.Mapper.GodownMapper;
import com.doritech.ItemService.Repository.GodownMasterRepository;
import com.doritech.ItemService.Request.GodownRequestDTO;
import com.doritech.ItemService.Response.GodownResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;
import com.doritech.ItemService.Service.GodownService;
import com.doritech.ItemService.Specification.GodownSpecification;

@Service
public class GodownServiceImpl implements GodownService {

	@Autowired
	private GodownMasterRepository repository;

	@Autowired
	private GodownMapper mapper;

	public static final List<String> EXCLUDED_GODOWNS = List
			.of("InTransit Godown", "Temporary Godown");

	@Override
	@Transactional
	public GodownResponseDTO save(GodownRequestDTO dto) {

		if (repository.existsByGodownCode(dto.getGodownCode())) {
			throw new BusinessException(
					"Godown code already exists : " + dto.getGodownCode());
		}

		GodownMasterEntity entity = mapper.toEntity(dto);

		entity.setCreatedOn(LocalDateTime.now());

		repository.save(entity);

		return mapper.toDTO(entity);
	}

	@Override
	@Transactional
	public GodownResponseDTO update(Integer id, GodownRequestDTO dto) {

		GodownMasterEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Godown not found with id : " + id));

		// 🚫 Restrict update for excluded godowns
		if (EXCLUDED_GODOWNS.stream().anyMatch(
				name -> name.equalsIgnoreCase(entity.getGodownName()))) {

			throw new BusinessException(
					"Update not allowed for system reserved godown : "
							+ entity.getGodownName());
		}

		mapper.updateEntityFromDto(dto, entity);

		entity.setModifiedOn(LocalDateTime.now());

		repository.save(entity);

		return mapper.toDTO(entity);
	}

	@Override
	public GodownResponseDTO getById(Integer id) {

		GodownMasterEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Godown not found with id : " + id));

		return mapper.toDTO(entity);
	}

	@Override
	public PageResponseDTO<GodownResponseDTO> getAll(int page, int size) {

		if (page < 0)
			throw new BusinessException("Page cannot be negative");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1 and 100");

		Page<GodownResponseDTO> resultPage = repository
				.findAll(PageRequest.of(page, size)).map(mapper::toDTO);

		return new PageResponseDTO<>(resultPage);
	}

	@Override
	public List<GodownResponseDTO> getAllGodowns() {

		return mapper.toDTOList(repository.findByGodownNameNotInAndIsActive(
				EXCLUDED_GODOWNS, "Y", Sort.by("godownName").ascending()));
	}

	@Override
	public PageResponseDTO<GodownResponseDTO> filter(String name, String code,
			String type, String active, int page, int size) {

		Page<GodownResponseDTO> resultPage = repository
				.findAll(GodownSpecification.filter(name, code, type, active),
						PageRequest.of(page, size))
				.map(mapper::toDTO);

		return new PageResponseDTO<>(resultPage);
	}

	@Override
	@Transactional
	public void deleteMultiple(List<Integer> ids) {

		if (ids == null || ids.isEmpty()) {
			throw new BusinessException("Godown id list cannot be empty");
		}

		List<GodownMasterEntity> list = repository.findAllById(ids);

		if (list.size() != ids.size()) {
			throw new ResourceNotFoundException(
					"Some godown ids not found in database");
		}

		// 🚫 Check for restricted godowns
		List<String> restricted = list.stream()
				.map(GodownMasterEntity::getGodownName)
				.filter(name -> EXCLUDED_GODOWNS.stream()
						.anyMatch(ex -> ex.equalsIgnoreCase(name)))
				.toList();

		if (!restricted.isEmpty()) {
			throw new BusinessException(
					"Delete not allowed for system reserved godowns: "
							+ restricted);
		}

		repository.deleteAll(list);
	}
}