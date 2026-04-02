package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.ContractItemPackageRequest;

import jakarta.validation.Valid;

public interface ContractItemPackageService {

	//ResponseEntity createPackage(ContractItemPackageRequest request);

	ResponseEntity updatePackage(Integer id, ContractItemPackageRequest request);

	ResponseEntity deletePackage(Integer id);

	ResponseEntity getAllPackages(int page, int size);

	ResponseEntity getPackageById(Integer id);


	ResponseEntity saveOrUpdatePackageList(@Valid List<ContractItemPackageRequest> requests);

	ResponseEntity getPackageByContractId(Integer contractId);

}