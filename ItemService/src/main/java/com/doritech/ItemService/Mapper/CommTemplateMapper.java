package com.doritech.ItemService.Mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.doritech.ItemService.Entity.CommTemplateMasterEntity;
import com.doritech.ItemService.Request.CommTemplateRequestDTO;
import com.doritech.ItemService.Response.CommTemplateResponseDTO;

@Mapper(componentModel = "spring")
public interface CommTemplateMapper {

	CommTemplateResponseDTO toDTO(CommTemplateMasterEntity entity);

	@BeanMapping(ignoreByDefault = false)
	@Mapping(target = "templateId", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	CommTemplateMasterEntity toEntity(CommTemplateRequestDTO dto);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "templateId", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	void updateEntityFromDTO(CommTemplateRequestDTO dto,
			@MappingTarget CommTemplateMasterEntity entity);
}