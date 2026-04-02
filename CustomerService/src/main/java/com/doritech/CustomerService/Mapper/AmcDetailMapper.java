package com.doritech.CustomerService.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.doritech.CustomerService.Entity.AmcDetailEntity;
import com.doritech.CustomerService.Request.AmcDetailRequest;
import com.doritech.CustomerService.Response.AmcDetailResponse;

@Mapper(componentModel = "spring")
public interface AmcDetailMapper {

	@Mapping(target = "amc", ignore = true)
	@Mapping(target = "modifiedBy", ignore = true)
	AmcDetailEntity toEntity(AmcDetailRequest request);

	@Mapping(source = "amc.amcId", target = "amcId")
	@Mapping(target = "amcName", expression = "java(entity.getAmc() != null ? entity.getAmc().getAmcName() : null)")
	AmcDetailResponse toResponse(AmcDetailEntity entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "amc", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	void updateEntityFromRequest(AmcDetailRequest request,
			@MappingTarget AmcDetailEntity entity);
}