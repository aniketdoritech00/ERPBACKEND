package com.doritech.CustomerService.Service;


import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.ContractMasterRequest;

public interface ContractMasterService {



    ResponseEntity getContractById(Integer id);

	ResponseEntity getAllContracts(int page, int size);

	ResponseEntity filterContracts(String contractNo, Integer customerId, String contractType, String isActive);

	ResponseEntity deactivateContracts(List<Integer> ids);

	ResponseEntity getContractNamesAndIds();

	ResponseEntity saveOrUpdateContract(Integer id, ContractMasterRequest request);

	ResponseEntity getContractDetailsByType(String type);

	ResponseEntity getContractNamesAndIdsForFillter();

}