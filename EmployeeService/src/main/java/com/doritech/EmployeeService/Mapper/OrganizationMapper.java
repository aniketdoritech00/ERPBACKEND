package com.doritech.EmployeeService.Mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.doritech.EmployeeService.entity.CustomerOrganizationEntity;
import com.doritech.EmployeeService.request.OrganizationRequestDTO;
import com.doritech.EmployeeService.response.OrganizationResponseDTO;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

	// Save mapping
	CustomerOrganizationEntity toEntity(OrganizationRequestDTO dto);

	// Response mapping
	OrganizationResponseDTO toDTO(CustomerOrganizationEntity entity);

	// Update mapping (ignore null fields)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateEntityFromDto(OrganizationRequestDTO dto, @MappingTarget CustomerOrganizationEntity entity);
}
