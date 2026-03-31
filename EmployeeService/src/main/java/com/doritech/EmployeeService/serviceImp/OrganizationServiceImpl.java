package com.doritech.EmployeeService.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.Mapper.OrganizationMapper;
import com.doritech.EmployeeService.entity.CustomerOrganizationEntity;
import com.doritech.EmployeeService.exception.BusinessException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.OrganizationRepository;
import com.doritech.EmployeeService.request.OrganizationRequestDTO;
import com.doritech.EmployeeService.response.OrganizationResponseDTO;
import com.doritech.EmployeeService.response.PageResponseDTO;
import com.doritech.EmployeeService.service.OrganizationService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationRepository repo;

	@Autowired
	private OrganizationMapper mapper;

	// ===== SAVE =====
	@Override
	public OrganizationResponseDTO saveOrganization(
			OrganizationRequestDTO dto) {

		if (repo.existsByOrgNameIgnoreCase(dto.getOrgName()))
			throw new BusinessException("Organization already exists");

		CustomerOrganizationEntity entity = mapper.toEntity(dto);

		return mapper.toDTO(repo.save(entity));
	}

	// ===== UPDATE =====
	@Override
	public OrganizationResponseDTO updateOrganization(Integer id,
			OrganizationRequestDTO dto) {

		CustomerOrganizationEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Organization not found with ID: " + id));
		
		if (repo.existsByOrgNameIgnoreCaseAndIdNot(dto.getOrgName(), id)) {
			throw new BusinessException("Organization Name already exists");
		}

		mapper.updateEntityFromDto(dto, entity);

		return mapper.toDTO(repo.save(entity));
	}

	// ===== GET BY ID =====
	@Override
	public OrganizationResponseDTO getByOrganizationId(Integer id) {

		CustomerOrganizationEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Organization not found with ID: " + id));

		return mapper.toDTO(entity);
	}

	// ===== GET ALL WITH PAGINATION =====
	@Override
	public PageResponseDTO<OrganizationResponseDTO> getAllOrganization(int page,
			int size) {

		if (page < 0)
			throw new BusinessException("Invalid page number");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1–100");

		Page<OrganizationResponseDTO> result = repo
				.findAll(PageRequest.of(page, size)).map(mapper::toDTO);

		return new PageResponseDTO<>(result);
	}

	// ===== DELETE =====
	@Override
	public void deleteOrganization(Integer id) {

		if (!repo.existsById(id))
			throw new ResourceNotFoundException(
					"Organization not found with ID: " + id);

		repo.deleteById(id);
	}

	// ===== FILTER =====
	@Override
	public PageResponseDTO<OrganizationResponseDTO> filterOrganization(
			String orgName, String active, int page, int size) {

		if (page < 0)
			throw new BusinessException("Invalid page number");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1–100");

		Page<OrganizationResponseDTO> result = repo
				.filterOrganization(orgName, active, PageRequest.of(page, size))
				.map(mapper::toDTO);

		return new PageResponseDTO<>(result);
	}

	// ===== GET ACTIVE =====
	@Override
	public List<OrganizationResponseDTO> getAllActiveOrganizations() {

		return repo.findByActive("Y").stream().map(entity -> {
			OrganizationResponseDTO dto = new OrganizationResponseDTO();
			dto.setId(entity.getId());
			dto.setOrgName(entity.getOrgName());
			return dto;
		}).toList();
	}

	// ===== DELETE MULTIPLE =====
	@Override
	public void deleteMultiple(List<Integer> ids) {

		List<CustomerOrganizationEntity> entities = repo.findAllById(ids);

		if (entities.isEmpty()) {
			throw new ResourceNotFoundException(
					"No organizations found for given IDs");
		}

		repo.deleteAll(entities);
	}

}