package com.doritech.CustomerService.Mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.doritech.CustomerService.Entity.AmcMasterEntity;
import com.doritech.CustomerService.Request.AmcMasterRequest;
import com.doritech.CustomerService.Response.AmcMasterResponse;

@Mapper(componentModel = "spring")
public interface AmcMasterMapper {

	@Mapping(target = "modifiedBy", ignore = true)
	AmcMasterEntity toEntity(AmcMasterRequest request);

	AmcMasterResponse toResponse(AmcMasterEntity entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "amcNumber", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	void updateEntityFromRequest(AmcMasterRequest request,
			@MappingTarget AmcMasterEntity entity);
}