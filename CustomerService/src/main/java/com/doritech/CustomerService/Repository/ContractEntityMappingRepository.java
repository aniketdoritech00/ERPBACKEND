package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	@Query("""
			    SELECT c FROM ContractEntityMapping c
			    WHERE c.contract.contractType = :type
			    AND c.isActive = 'Y'
			    AND NOT EXISTS (
			        SELECT 1 FROM EmployeeAssignmentEntity ea
			        WHERE ea.contractEntityMapping.mappingId = c.mappingId
			        AND LOWER(TRIM(ea.status)) = LOWER(TRIM(:status))
			    )
			""")
	Page<ContractEntityMapping> findAvailableContracts(
			@Param("type") String type,
			@Param("status") String status,
			Pageable pageable);

}