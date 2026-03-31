package com.doritech.EmployeeService.Mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.doritech.EmployeeService.entity.CompanyEntity;
import com.doritech.EmployeeService.request.CompanyRequestDTO;
import com.doritech.EmployeeService.response.CompanyResponseDTO;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	CompanyEntity toEntity(CompanyRequestDTO dto);

	CompanyResponseDTO toDTO(CompanyEntity entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void updateEntityFromDto(CompanyRequestDTO dto, @MappingTarget CompanyEntity entity);
}
