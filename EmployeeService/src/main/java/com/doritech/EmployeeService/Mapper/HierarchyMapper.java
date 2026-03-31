package com.doritech.EmployeeService.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.doritech.EmployeeService.entity.HierarchyLevelEntity;
import com.doritech.EmployeeService.entity.HierarchyMasterEntity;
import com.doritech.EmployeeService.response.HierarchyLevelResponseDTO;
import com.doritech.EmployeeService.response.HierarchyMasterResponseDTO;

public class HierarchyMapper {

	public static HierarchyLevelResponseDTO toLevelDTO(
			HierarchyLevelEntity entity) {

		return new HierarchyLevelResponseDTO(entity.getId(),
				entity.getHierarchy().getId(),
				entity.getHierarchy().getEntityType(), // 👈 ADD THIS
				entity.getLevelNumber(), entity.getLevelName(),
				entity.getEndNode(), entity.getCreatedOn());
	}

	public static HierarchyMasterResponseDTO toMasterDTO(HierarchyMasterEntity entity) {

		HierarchyMasterResponseDTO dto = new HierarchyMasterResponseDTO();

		dto.setId(entity.getId());
		dto.setHierarchyName(entity.getHierarchyName());
		dto.setEntityType(entity.getEntityType());
		dto.setHierarchyLevels(entity.getHierarchyLevels());
		dto.setDescription(entity.getDescription());
		dto.setActive(entity.getActive());
		dto.setCreatedOn(entity.getCreatedOn());
		dto.setModifiedOn(entity.getModifiedOn());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setModifiedBy(entity.getModifiedBy());

		if (entity.getCompany() != null)
			dto.setCompanyId(entity.getCompany().getId());

		if (entity.getOrganization() != null)
			dto.setOrganizationId(entity.getOrganization().getId());

		if (entity.getLevels() != null) {
			List<HierarchyLevelResponseDTO> levels = entity.getLevels().stream().map(HierarchyMapper::toLevelDTO)
					.collect(Collectors.toList());

			dto.setLevels(levels);
		}

		return dto;
	}
}
