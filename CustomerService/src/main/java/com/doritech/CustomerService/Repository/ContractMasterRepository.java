package com.doritech.CustomerService.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.ContractMaster;

@Repository
public interface ContractMasterRepository
		extends
			JpaRepository<ContractMaster, Integer> {

	Optional<ContractMaster> findByContractNo(String contractNo);

	boolean existsByContractNo(String contractNo);

	List<ContractMaster> findAll(Specification<ContractMaster> filterContracts);

	@Query("FROM ContractMaster c WHERE c.isActive = 'Y'")
	List<ContractMaster> getActiveContracts();

	List<ContractMaster> findByContractType(String type);

	@Query("""
			    SELECT c FROM ContractMaster c
			    WHERE c.contractType = :type
			    AND c.contractId NOT IN (
			        SELECT ea.contractEntityMapping.contract.contractId FROM EmployeeAssignmentEntity ea
			        WHERE LOWER(ea.status) = LOWER(:status)
			    )
			""")
	List<ContractMaster> findAvailableContracts(@Param("type") String type,
			@Param("status") String status);

				long countByIsActive(String string);

	long countByContractStartDateBetween(LocalDate start, LocalDate end);

	long countByContractEndDateBetween(LocalDate start, LocalDate end);

	Page<ContractMaster> findByContractTypeIgnoreCase(String string, Pageable pageable);
    List<ContractMaster> findByContractTypeAndIsActive(String type, String isActive);
}