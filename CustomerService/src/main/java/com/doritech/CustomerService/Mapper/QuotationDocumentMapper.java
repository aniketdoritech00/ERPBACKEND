package com.doritech.CustomerService.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doritech.CustomerService.Entity.QuotationDocument;
import com.doritech.CustomerService.Response.QuotationDocumentResponse;

@Mapper(componentModel = "spring")
public interface QuotationDocumentMapper {

	@Mapping(target = "quotationId", source = "quotationMaster.quotationId")
	@Mapping(target = "document", source = "document")
	QuotationDocumentResponse toResponse(QuotationDocument entity);

}