package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.ContractEntityMapping;
import com.doritech.CustomerService.Entity.ContractMaster;

@Repository
public interface ContractEntityMappingRepository
		extends
			JpaRepository<ContractEntityMapping, Integer> {

	List<ContractEntityMapping> findByContractContractId(Integer contractId);

	List<ContractEntityMapping> findByContract(ContractMaster contract);

	Page<ContractEntityMapping> findAllByContract_ContractTypeAndIsActive(
        String contractType,
        String isActive,
        Pageable pageable);

}