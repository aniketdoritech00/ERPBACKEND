package com.doritech.EmployeeService.serviceImp;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.Mapper.HierarchyMapper;
import com.doritech.EmployeeService.Specification.HierarchyMasterSpecification;
import com.doritech.EmployeeService.entity.HierarchyMasterEntity;
import com.doritech.EmployeeService.exception.BusinessException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.CompanyRepository;
import com.doritech.EmployeeService.repository.HierarchyMasterRepository;
import com.doritech.EmployeeService.repository.OrganizationRepository;
import com.doritech.EmployeeService.request.HierarchyMasterFilterDTO;
import com.doritech.EmployeeService.request.HierarchyMasterRequestDTO;
import com.doritech.EmployeeService.response.HierarchyMasterResponseDTO;
import com.doritech.EmployeeService.response.PageResponseDTO;
import com.doritech.EmployeeService.response.ParamResponseDTO;
import com.doritech.EmployeeService.service.HierarchyMasterService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class HierarchyMasterServiceImpl implements HierarchyMasterService {

	@Autowired
	private HierarchyMasterRepository repo;

	@Autowired
	private CompanyRepository companyRepo;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private ParamServiceImpl serviceImpl;

	@Override
	public HierarchyMasterResponseDTO saveHierarchy(
			HierarchyMasterRequestDTO dto) {

		// Duplicate Check
		boolean exists = repo.existsByHierarchyNameIgnoreCaseAndCompany_Id(
				dto.getHierarchyName(), dto.getCompanyId());

		if (exists) {
			throw new BusinessException("Hierarchy name already exists");
		}

		HierarchyMasterEntity entity = new HierarchyMasterEntity();

		entity.setHierarchyName(dto.getHierarchyName());
		entity.setEntityType(dto.getEntityType());
		entity.setHierarchyLevels(dto.getHierarchyLevels());
		entity.setDescription(dto.getDescription());
		entity.setActive(dto.getActive() != null ? dto.getActive() : "Y");
		entity.setCreatedBy(dto.getCreatedBy());

		// Company Mapping
		entity.setCompany(companyRepo.findById(dto.getCompanyId()).orElseThrow(
				() -> new ResourceNotFoundException("Company not found")));

		// Organization Mapping (Optional)
		if (dto.getOrganizationId() != null) {
			entity.setOrganization(
					organizationRepository.findById(dto.getOrganizationId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Organization not found")));
		}

		entity = repo.save(entity);

		return HierarchyMapper.toMasterDTO(entity);
	}

	@Override
	public HierarchyMasterResponseDTO updateHierarchy(Integer id,
			HierarchyMasterRequestDTO dto) {

		HierarchyMasterEntity entity = repo.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Hierarchy not found"));

		entity.setHierarchyName(dto.getHierarchyName());
		entity.setDescription(dto.getDescription());
		// Organization Mapping (Optional)
		if (dto.getOrganizationId() != null) {
			entity.setOrganization(
					organizationRepository.findById(dto.getOrganizationId())
							.orElseThrow(() -> new ResourceNotFoundException(
									"Organization not found")));
		}
		entity.setActive(dto.getActive());
		entity.setHierarchyLevels(dto.getHierarchyLevels());
		entity.setModifiedBy(dto.getModifiedBy());
		entity = repo.save(entity);

		return HierarchyMapper.toMasterDTO(entity);
	}

	@Override
	public HierarchyMasterResponseDTO getByHierarchyId(Integer id) {

		HierarchyMasterEntity entity = repo.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Hierarchy not found"));

		return HierarchyMapper.toMasterDTO(entity);
	}

	// @Override
	// public PageResponseDTO<HierarchyMasterResponseDTO> getAllHierarchy(int
	// page,
	// int size) {
	//
	// if (page < 0)
	// throw new BusinessException("Invalid page number");
	//
	// if (size <= 0 || size > 100)
	// throw new BusinessException("Page size must be between 1–100");
	//
	// // List<ParamResponseDTO> paramList = serviceImpl
	// // .getByCodeAndSerial("HIERARCHY", "ENTITY_TYPE");
	//
	// Page<HierarchyMasterResponseDTO> result = repo
	// .findAll(PageRequest.of(page, size))
	// .map(HierarchyMapper::toMasterDTO);
	//
	// return new PageResponseDTO<>(result);
	// }

	@Override
	public PageResponseDTO<HierarchyMasterResponseDTO> getAllHierarchy(int page,
			int size) {

		if (page < 0)
			throw new BusinessException("Invalid page number");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1–100");

		List<ParamResponseDTO> paramList = serviceImpl
				.getByCodeAndSerial("HIERARCHY", "ENTITY_TYPE");

		Map<String, String> entityTypeMap = paramList.stream()
				.collect(Collectors.toMap(ParamResponseDTO::getDesp1,
						ParamResponseDTO::getDesp2));

		Page<HierarchyMasterResponseDTO> result = repo
				.findAll(PageRequest.of(page, size)).map(entity -> {
					HierarchyMasterResponseDTO dto = HierarchyMapper
							.toMasterDTO(entity);

					String entityType = entityTypeMap.get(dto.getEntityType());

					dto.setEntityType(entityType);

					return dto;
				});

		return new PageResponseDTO<>(result);
	}

	@Override
	public List<HierarchyMasterResponseDTO> getAllHierarchy() {

		List<HierarchyMasterResponseDTO> list = repo
				.findByActive("Y", Sort.by("hierarchyName").ascending())
				.stream().map(HierarchyMapper::toMasterDTO).toList();

		if (list.isEmpty()) {
			throw new ResourceNotFoundException(
					"Active hierarchy data not found");
		}

		return list;
	}

	@Override
	public List<HierarchyMasterResponseDTO> getAllHierarchyNames() {

		List<HierarchyMasterResponseDTO> list = repo
				.findAll( Sort.by("hierarchyName").ascending())
				.stream().map(HierarchyMapper::toHierarchyDTO).toList();

		if (list.isEmpty()) {
			throw new ResourceNotFoundException(
					"Hierarchy data not found");
		}

		return list;
	}

	@Override
	public void deleteHierarchy(Integer id) {

		if (!repo.existsById(id))
			throw new ResourceNotFoundException("Hierarchy not found");

		repo.deleteById(id);
	}

	@Override
	public void deleteMultipleHierarchy(List<Integer> ids) {

		if (ids == null || ids.isEmpty()) {
			throw new BusinessException("ID list cannot be empty");
		}

		List<Integer> existingIds = repo.findAllById(ids).stream()
				.map(HierarchyMasterEntity::getId).toList();

		List<Integer> missingIds = ids.stream()
				.filter(id -> !existingIds.contains(id)).toList();

		if (!missingIds.isEmpty()) {
			throw new ResourceNotFoundException(
					"Hierarchy not found for IDs: " + missingIds);
		}

		repo.deleteAllById(ids);

	}

	@Override
	public List<HierarchyMasterResponseDTO> getAllActiveHierarchies() {

		return repo.findByActive("Y").stream().map(entity -> {
			HierarchyMasterResponseDTO dto = new HierarchyMasterResponseDTO();

			dto.setId(entity.getId());
			dto.setHierarchyName(entity.getHierarchyName());
			dto.setEntityType(entity.getEntityType());
			dto.setHierarchyLevels(entity.getHierarchyLevels());
			dto.setDescription(entity.getDescription());
			dto.setActive(entity.getActive());
			dto.setCreatedOn(entity.getCreatedOn());
			dto.setModifiedOn(entity.getModifiedOn());

			if (entity.getCompany() != null)
				dto.setCompanyId(entity.getCompany().getId());

			if (entity.getOrganization() != null)
				dto.setOrganizationId(entity.getOrganization().getId());

			return dto;
		}).toList();
	}

	@Override
	public PageResponseDTO<HierarchyMasterResponseDTO> filterHierarchy(
			HierarchyMasterFilterDTO dto) {

		int page = dto.getPage() != null ? dto.getPage() : 0;
		int size = dto.getSize() != null ? dto.getSize() : 10;

		if (page < 0) {
			throw new BusinessException("Page number cannot be negative");
		}

		if (size <= 0) {
			throw new BusinessException("Page size must be greater than 0");
		}

		if (size > 100) {
			throw new BusinessException("Page size cannot be greater than 100");
		}

		Pageable pageable = PageRequest.of(page, size);

		Page<HierarchyMasterResponseDTO> result = repo
				.findAll(HierarchyMasterSpecification.filter(dto), pageable)
				.map(HierarchyMapper::toMasterDTO);

		return new PageResponseDTO<>(result);
	}

}
