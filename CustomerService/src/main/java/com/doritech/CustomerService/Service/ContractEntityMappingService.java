package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.ContractEntityMappingRequest;

import jakarta.validation.Valid;

public interface ContractEntityMappingService {


	ResponseEntity updateMapping(Integer id, ContractEntityMappingRequest request);

	ResponseEntity getMappingById(Integer id);

	ResponseEntity getAllMappings(int page, int size);

	ResponseEntity getAllContractEntityMappings(String contractType,int page,int size);

	ResponseEntity deactivateMapping(Integer id);

	ResponseEntity getMappingByContractId(Integer id);

	ResponseEntity saveOrUpdateMappings(@Valid List<ContractEntityMappingRequest> requests);

    ResponseEntity deactivateBulkContractEntity(List<Integer> ids);

}