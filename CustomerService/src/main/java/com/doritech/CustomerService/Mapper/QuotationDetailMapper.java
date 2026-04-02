package com.doritech.CustomerService.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doritech.CustomerService.Entity.QuotationDetail;
import com.doritech.CustomerService.Request.QuotationDetailRequest;
import com.doritech.CustomerService.Response.QuotationDetailResponse;

@Mapper(componentModel = "spring")
public interface QuotationDetailMapper {

	@Mapping(target = "quotationDetailId", ignore = true)
	@Mapping(target = "quotationMaster", ignore = true)
	QuotationDetail toEntity(QuotationDetailRequest request);

	@Mapping(target = "quotationId", source = "quotationMaster.quotationId")
	@Mapping(target = "itemId", source = "itemId")
	@Mapping(target = "siteId", source = "siteId")
	@Mapping(target = "itemName", ignore = true)
	@Mapping(target = "siteName", ignore = true)
	@Mapping(target = "parentItemName", ignore = true)
	@Mapping(target = "children", ignore = true)
	QuotationDetailResponse toResponse(QuotationDetail entity);
}