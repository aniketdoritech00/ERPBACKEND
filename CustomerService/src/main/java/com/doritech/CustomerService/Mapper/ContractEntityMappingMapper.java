package com.doritech.CustomerService.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.doritech.CustomerService.Entity.ContractEntityMapping;
import com.doritech.CustomerService.Response.ContractEntityMappingResponse;

@Component
public class ContractEntityMappingMapper {

	public ContractEntityMappingResponse toResponse(ContractEntityMapping mapping) {

		ContractEntityMappingResponse response = new ContractEntityMappingResponse();

		response.setMappingId(mapping.getMappingId());
		response.setContractId(mapping.getContract().getContractId());
		response.setCustomerId(mapping.getCustomer().getCustomerId());
		response.setSiteId(mapping.getSiteId());
		response.setMinNoVisits(mapping.getMinNoVisits());
		response.setVisitsFrequency(mapping.getVisitsFrequency());
		response.setVisitsPaid(mapping.getVisitsPaid());
		response.setIsActive(mapping.getIsActive());
		response.setCreatedOn(mapping.getCreatedOn());
		response.setModifiedOn(mapping.getModifiedOn());
		response.setCreatedBy(mapping.getCreatedBy());
		response.setModifiedBy(mapping.getModifiedBy());

		return response;
	}
	
	public List<ContractEntityMappingResponse> toResponseList(List<ContractEntityMapping> mappings) {
		return mappings.stream().map(this::toResponse).collect(Collectors.toList());
	}
}