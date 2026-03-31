package com.doritech.ItemService.Mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.doritech.ItemService.Entity.GodownMasterEntity;
import com.doritech.ItemService.Request.GodownRequestDTO;
import com.doritech.ItemService.Response.GodownResponseDTO;

@Mapper(componentModel = "spring")
public interface GodownMapper {

	@Mapping(target = "godownId", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	GodownMasterEntity toEntity(GodownRequestDTO dto);

	GodownResponseDTO toDTO(GodownMasterEntity entity);

	List<GodownResponseDTO> toDTOList(List<GodownMasterEntity> entities);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "godownId", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "godownCode", ignore = true)
	void updateEntityFromDto(GodownRequestDTO dto,
			@MappingTarget GodownMasterEntity entity);

}