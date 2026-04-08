package com.doritech.EmployeeService.serviceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.Mapper.HierarchyMapper;
import com.doritech.EmployeeService.Specification.HierarchyLevelSpecification;
import com.doritech.EmployeeService.entity.HierarchyLevelEntity;
import com.doritech.EmployeeService.entity.HierarchyMasterEntity;
import com.doritech.EmployeeService.exception.BusinessException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.HierarchyLevelRepository;
import com.doritech.EmployeeService.repository.HierarchyMasterRepository;
import com.doritech.EmployeeService.request.HierarchyLevelFilterDTO;
import com.doritech.EmployeeService.request.HierarchyLevelRequestDTO;
import com.doritech.EmployeeService.response.HierarchyLevelResponseDTO;
import com.doritech.EmployeeService.response.HierarchyMasterResponseDTO;
import com.doritech.EmployeeService.response.PageResponseDTO;
import com.doritech.EmployeeService.response.ParamResponseDTO;
import com.doritech.EmployeeService.service.HierarchyLevelService;

import jakarta.transaction.Transactional;

@Service
public class HierarchyLevelServiceImpl implements HierarchyLevelService {

	@Autowired
	private HierarchyLevelRepository levelRepo;

	@Autowired
	private HierarchyMasterRepository masterRepo;

	@Autowired
	private ParamServiceImpl paramServiceImpl;

	@Override
	public HierarchyLevelResponseDTO save(HierarchyLevelRequestDTO dto) {

		HierarchyMasterEntity hierarchy = masterRepo
				.findById(dto.getHierarchyId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Hierarchy not found"));

		boolean exists = levelRepo.existsByHierarchy_IdAndLevelNumber(
				dto.getHierarchyId(), dto.getLevelNumber());

		if (exists)
			throw new BusinessException(
					"Level number already exists for this hierarchy");

		// 2️⃣ Check max hierarchy levels
		Integer existingLevels = levelRepo
				.countByHierarchy_Id(dto.getHierarchyId());

		if (existingLevels >= hierarchy.getHierarchyLevels()) {
			throw new BusinessException(
					"Cannot add more levels. Maximum allowed levels for this hierarchy are "
							+ hierarchy.getHierarchyLevels());
		}

		HierarchyLevelEntity entity = new HierarchyLevelEntity();
		entity.setHierarchy(hierarchy);
		entity.setLevelNumber(dto.getLevelNumber());
		entity.setLevelName(dto.getLevelName());
		entity.setEndNode(dto.getEndNode());

		entity.setCreatedBy(dto.getCreatedBy());

		entity = levelRepo.save(entity);

		return HierarchyMapper.toLevelDTO(entity);
	}

	@Override
	public HierarchyLevelResponseDTO update(Integer id,
			HierarchyLevelRequestDTO dto) {

		HierarchyLevelEntity entity = levelRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Hierarchy Level not present With this ID : " + id));

		HierarchyMasterEntity hierarchy = masterRepo
				.findById(dto.getHierarchyId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Hierarchy not found"));

		entity.setHierarchy(hierarchy);
		entity.setLevelNumber(dto.getLevelNumber());
		entity.setLevelName(dto.getLevelName());
		entity.setEndNode(dto.getEndNode());

		// entity.setEndNode(dto.getEndNode() != null ? dto.getEndNode() :
		// false);

		entity.setModifiedBy(dto.getModifiedBy());

		entity = levelRepo.save(entity);

		return HierarchyMapper.toLevelDTO(entity);
	}

	@Override
	@Transactional
	public List<HierarchyLevelResponseDTO> saveMultipleHierarchyLevels(
			List<HierarchyLevelRequestDTO> dtoList) {

		if (dtoList == null || dtoList.isEmpty()) {
			throw new BusinessException("Hierarchy level list cannot be empty");
		}

		Set<Integer> levelNumbers = new HashSet<>();
		Set<String> levelNames = new HashSet<>();

		for (HierarchyLevelRequestDTO dto : dtoList) {

			if (!levelNumbers.add(dto.getLevelNumber())) {
				throw new BusinessException("Duplicate levelNumber in request: "
						+ dto.getLevelNumber());
			}

			String normalizedName = dto.getLevelName().trim().toLowerCase();

			if (!levelNames.add(normalizedName)) {
				throw new BusinessException("Duplicate levelName in request: "
						+ dto.getLevelName());
			}
		}

		Integer hierarchyId = dtoList.get(0).getHierarchyId();

		HierarchyMasterEntity hierarchy = masterRepo.findById(hierarchyId)
				.orElseThrow(
						() -> new BusinessException("Hierarchy not found"));

		for (HierarchyLevelRequestDTO dto : dtoList) {

			boolean numberExists = levelRepo.existsByHierarchy_IdAndLevelNumber(
					dto.getHierarchyId(), dto.getLevelNumber());

			if (numberExists) {
				throw new BusinessException(
						"Level number already exists in DB: "
								+ dto.getLevelNumber());
			}

			boolean nameExists = levelRepo
					.existsByHierarchy_IdAndLevelNameIgnoreCase(
							dto.getHierarchyId(), dto.getLevelName().trim());

			if (nameExists) {
				throw new BusinessException("Level name already exists in DB: "
						+ dto.getLevelName());
			}
		}

		// ✅ Convert DTO → Entity
		List<HierarchyLevelEntity> entityList = dtoList.stream().map(dto -> {
			HierarchyLevelEntity entity = new HierarchyLevelEntity();
			entity.setHierarchy(hierarchy);
			entity.setLevelNumber(dto.getLevelNumber());
			entity.setLevelName(dto.getLevelName());
			entity.setEndNode(dto.getEndNode());
			entity.setCreatedBy(dto.getCreatedBy());
			return entity;
		}).toList();

		List<HierarchyLevelEntity> savedEntities = levelRepo
				.saveAll(entityList);

		return savedEntities.stream().map(HierarchyMapper::toLevelDTO).toList();
	}

	@Override
	@Transactional
	public List<HierarchyLevelResponseDTO> updateMultipleHierarchyLevels(
			Integer hierarchyId, List<HierarchyLevelRequestDTO> dtoList) {

		if (dtoList == null || dtoList.isEmpty()) {
			throw new BusinessException("Hierarchy level list cannot be empty");
		}

		HierarchyMasterEntity hierarchy = masterRepo.findById(hierarchyId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Hierarchy not found"));

		Set<Integer> levelNumbers = new HashSet<>();
		Set<String> levelNames = new HashSet<>();

		for (HierarchyLevelRequestDTO dto : dtoList) {

			if (!levelNumbers.add(dto.getLevelNumber())) {
				throw new BusinessException(
						"Duplicate levelNumber: " + dto.getLevelNumber());
			}

			String normalizedName = dto.getLevelName().trim().toLowerCase();

			if (!levelNames.add(normalizedName)) {
				throw new BusinessException(
						"Duplicate levelName: " + dto.getLevelName());
			}
		}

		List<HierarchyLevelEntity> existingLevels = levelRepo
				.findByHierarchy_Id(hierarchyId);

		Map<Integer, HierarchyLevelEntity> existingMap = existingLevels.stream()
				.collect(Collectors.toMap(HierarchyLevelEntity::getLevelNumber,
						e -> e));

		List<HierarchyLevelEntity> entitiesToSave = new ArrayList<>();

		for (HierarchyLevelRequestDTO dto : dtoList) {

			HierarchyLevelEntity entity = existingMap.get(dto.getLevelNumber());

			if (entity == null) {
				entity = new HierarchyLevelEntity();
				entity.setHierarchy(hierarchy);
				entity.setCreatedBy(dto.getCreatedBy());
			} else {
				entity.setModifiedBy(dto.getCreatedBy());
				entity.setModifiedOn(LocalDateTime.now());
			}

			entity.setLevelNumber(dto.getLevelNumber());
			entity.setLevelName(dto.getLevelName());
			entity.setEndNode(dto.getEndNode());

			entitiesToSave.add(entity);
		}

		List<HierarchyLevelEntity> saved = levelRepo.saveAll(entitiesToSave);

		return saved.stream().map(HierarchyMapper::toLevelDTO).toList();
	}

	@Override
	public List<HierarchyLevelResponseDTO> getByHierarchy(Integer hierarchyId) {

		List<HierarchyLevelEntity> levels = levelRepo
				.findByHierarchy_Id(hierarchyId);

		if (levels.isEmpty()) {
			throw new ResourceNotFoundException(
					"No hierarchy levels found for hierarchyId: "
							+ hierarchyId);
		}

		return levels.stream().map(HierarchyMapper::toLevelDTO).toList();
	}

	@Override
	public HierarchyMasterResponseDTO getHierarchyByHierarchyLevel(
			Integer hierarchyLevelId) {

		Optional<HierarchyLevelEntity> optional = levelRepo
				.findById(hierarchyLevelId);

		if (optional.isEmpty())
			throw new ResourceNotFoundException(
					"Hierarchy Level Not found with Id  " + hierarchyLevelId);

		HierarchyMasterEntity entity = optional.get().getHierarchy();

		return HierarchyMapper.toMasterDTO(entity);
	}

	@Override
	public PageResponseDTO<HierarchyLevelResponseDTO> getAllHierarchyLevel(
			int page, int size) {
		if (page < 0)
			throw new BusinessException("Invalid page number");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1–100");

		List<ParamResponseDTO> paramList = paramServiceImpl
				.getByCodeAndSerial("HIERARCHY", "ENTITY_TYPE");

		Map<String, String> entityTypeMap = paramList.stream()
				.collect(Collectors.toMap(ParamResponseDTO::getDesp1,
						ParamResponseDTO::getDesp2));

		Page<HierarchyLevelResponseDTO> result = levelRepo
				.findAll(PageRequest.of(page, size)).map(entity -> {
					HierarchyLevelResponseDTO dto = HierarchyMapper
							.toLevelDTO(entity);

					String entityType = entityTypeMap.get(dto.getEntityType());

					dto.setEntityType(entityType);

					return dto;
				});

		return new PageResponseDTO<>(result);
	}

	@Override
	public void delete(Integer id) {

		if (!levelRepo.existsById(id))
			throw new ResourceNotFoundException("Hierarchy level not found");

		levelRepo.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteMultiple(List<Integer> ids) {

		if (ids == null || ids.isEmpty()) {
			throw new BusinessException("ID list cannot be empty");
		}

		List<Integer> existingIds = levelRepo.findAllById(ids).stream()
				.map(HierarchyLevelEntity::getId).toList();

		List<Integer> missingIds = ids.stream()
				.filter(id -> !existingIds.contains(id)).toList();

		if (!missingIds.isEmpty()) {
			throw new ResourceNotFoundException(
					"Hierarchy levels not found for IDs: " + missingIds);
		}

		levelRepo.deleteAllById(ids);
	}

	@Override
	public PageResponseDTO<HierarchyLevelResponseDTO> filterHierarchyLevel(
			HierarchyLevelFilterDTO dto) {

		int page = dto.getPage() != null ? dto.getPage() : 0;
		int size = dto.getSize() != null ? dto.getSize() : 10;

		if (page < 0)
			throw new BusinessException("Invalid page number");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1–100");

		Pageable pageable = PageRequest.of(page, size);

		Page<HierarchyLevelResponseDTO> result = levelRepo
				.findAll(HierarchyLevelSpecification.filter(dto), pageable)
				.map(HierarchyMapper::toLevelDTO);

		return new PageResponseDTO<>(result);
	}

	@Override
	public HierarchyLevelResponseDTO getLevelByHierarchylevelId(
			Integer hierarchylevelId) {
		HierarchyLevelEntity entity = levelRepo.findById(hierarchylevelId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Hierarchy Level not present With this ID : "
								+ hierarchylevelId));

		HierarchyLevelResponseDTO responseDTO = new HierarchyLevelResponseDTO(
				entity.getId(), entity.getHierarchy().getId(),
				entity.getHierarchy().getHierarchyName(), 
				entity.getHierarchy().getEntityType(), entity.getLevelNumber(),
				entity.getLevelName(), entity.getEndNode(),
				entity.getCreatedOn());

		return responseDTO;
	}

}
