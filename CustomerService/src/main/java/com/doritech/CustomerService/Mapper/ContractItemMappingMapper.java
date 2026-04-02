package com.doritech.CustomerService.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Request.ContractItemMappingRequest;
import com.doritech.CustomerService.Response.ContractItemMappingResponse;

@Mapper(componentModel = "spring")
public interface ContractItemMappingMapper {

	@Mapping(target = "contract", ignore = true)
	ContractItemMapping toEntity(ContractItemMappingRequest request);

	@Mapping(source = "contract.contractId", target = "contractId")
	ContractItemMappingResponse toResponse(ContractItemMapping entity);

	List<ContractItemMappingResponse> toResponseList(List<ContractItemMapping> entities);

}