package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.ContractItemMappingRequest;

public interface ContractItemMappingService {

	// ResponseEntity createItemMapping(List<ContractItemMappingRequest> requests);

	ResponseEntity getItemMappingById(Integer id);

	ResponseEntity getAllItemMappings(int page, int size);

	ResponseEntity deleteItemMapping(Integer id);

	ResponseEntity updateItemMapping(Integer id, ContractItemMappingRequest request);

	ResponseEntity getContractItemByContractId(Integer id);

	ResponseEntity getItemByContractId(Integer contractId);

	ResponseEntity saveOrUpdateItemMapping(List<ContractItemMappingRequest> requests);

	ResponseEntity getPackageItemsByContractAndItem(Integer contractId, Integer itemId);
		ResponseEntity deactivateContractItemMappings(List<Integer> mappingIds, String userId);

}