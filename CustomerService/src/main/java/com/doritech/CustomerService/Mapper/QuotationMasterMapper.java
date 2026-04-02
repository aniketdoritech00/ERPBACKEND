package com.doritech.CustomerService.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doritech.CustomerService.Entity.QuotationMaster;
import com.doritech.CustomerService.Request.QuotationMasterRequest;
import com.doritech.CustomerService.Response.QuotationMasterResponse;

@Mapper(componentModel = "spring", uses = {QuotationDetailMapper.class})
public interface QuotationMasterMapper {

    @Mapping(target = "quotationId", ignore = true)
    @Mapping(target = "contract.contractId", source = "contractId")
    @Mapping(target = "customer.customerId", source = "customerId")
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "modifiedOn", ignore = true)
    QuotationMaster toEntity(QuotationMasterRequest request);

    @Mapping(target = "contractId", source = "contract.contractId")
    @Mapping(target = "customerId", source = "customer.customerId")
    QuotationMasterResponse toResponse(QuotationMaster entity);
}