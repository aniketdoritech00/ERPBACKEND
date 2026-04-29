package com.doritech.CustomerService.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.EmployeeAssignmentEntity;

@Repository
public interface EmployeeAssignmentRepository extends JpaRepository<EmployeeAssignmentEntity, Integer> {

	Page<EmployeeAssignmentEntity> findAll(Pageable pageable);

	Page<EmployeeAssignmentEntity> findByEmployeeId(Integer employeeId, Pageable pageable);

	@Query("SELECT ea.contractEntityMapping.contract.contractId " + "FROM EmployeeAssignmentEntity ea "
			+ "WHERE LOWER(ea.status) = LOWER(:status)")
	List<Integer> findContractIdsByStatus(@Param("status") String status);

	@Query("SELECT c.customerId, c.customerName, c.district, cem.minNoVisits " + "FROM EmployeeAssignmentEntity ea "
			+ "JOIN ea.contractEntityMapping cem " + "JOIN cem.contract cm " + "JOIN cm.customer c "
			+ "WHERE ea.assignmentId = :assignmentId")
	Object[] getCustomerDetailsByAssignmentId(@Param("assignmentId") Integer assignmentId);

	boolean existsByContractEntityMapping_MappingIdAndEmployeeIdAndSiteIdAndStatusNotAndAssignmentStartDateLessThanEqualAndAssignmentEndDateGreaterThanEqual(
			Integer mappingId, Integer employeeId, Integer siteId, String status, LocalDateTime endDate,
			LocalDateTime startDate);
}