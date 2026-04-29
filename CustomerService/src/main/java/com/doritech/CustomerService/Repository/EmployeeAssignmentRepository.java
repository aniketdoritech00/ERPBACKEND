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

//	@Query(value = """
//			SELECT DISTINCT
//			    ea.assignment_id,
//			    im.category,
//			    im.item_name,
//			    im.item_code
//			FROM employee_assignment ea
//			JOIN contract_entity_mapping cem
//			    ON ea.mapping_id = cem.mapping_id
//			JOIN contract_item_mapping cim
//			    ON cem.contract_id = cim.contract_id
//			JOIN item_master im
//			    ON im.item_id = cim.item_id
//			WHERE ea.visit_type = 'AM'
//			  AND ea.status = 'Completed'
//			""", nativeQuery = true)
//	List<Object[]> findAllAssignmentItems();

	@Query(value = """
			   SELECT DISTINCT
			       ea.assignment_id,
			       ea.employee_id,
			       im.category
			   FROM employee_assignment ea
			   JOIN contract_entity_mapping cem
			       ON ea.mapping_id = cem.mapping_id
			   JOIN contract_item_mapping cim
			       ON cem.contract_id = cim.contract_id
			   JOIN item_master im
			       ON im.item_id = cim.item_id
			   WHERE ea.visit_type = 'AM'
			     AND ea.status = 'Completed'
			     AND (:employeeId IS NULL OR ea.employee_id = :employeeId)
			     AND (:startDate IS NULL OR ea.visit_date >= :startDate)
			     AND (:endDate IS NULL OR ea.visit_date <= :endDate)
			""", nativeQuery = true)
	List<Object[]> findAllAssignmentItemsByFilter(@Param("employeeId") Integer employeeId,
			@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	@Query(value = """
			    SELECT csm.district
			    FROM employee_assignment ea
			    JOIN comp_site_master csm
			        ON ea.site_id = csm.site_id
			    WHERE ea.assignment_id = :assignmentId
			""", nativeQuery = true)
	String findDistrictByAssignmentId(@Param("assignmentId") Integer assignmentId);

	@Query(value = """
		    SELECT DISTINCT
		        ea.assignment_id,
		        ea.employee_id,
		        ea.site_id,
		        csm.site_name,
		        ea.visit_date,
		        csm.district,
		        im.category,
		        em.employee_name
		    FROM employee_assignment ea
		    JOIN employee_master em
		        ON ea.employee_id = em.employee_id
		    JOIN comp_site_master csm
		        ON ea.site_id = csm.site_id   -- ✅ FIXED JOIN
		    JOIN contract_entity_mapping cem
		        ON ea.mapping_id = cem.mapping_id
		    JOIN contract_item_mapping cim
		        ON cem.contract_id = cim.contract_id
		    JOIN item_master im
		        ON im.item_id = cim.item_id
		    WHERE ea.visit_type = 'AM'
		      AND ea.status = 'Completed'
		      AND (:employeeId IS NULL OR ea.employee_id = :employeeId)
		      AND (:siteId IS NULL OR ea.site_id = :siteId)
		      AND (:startDate IS NULL OR ea.visit_date >= :startDate)
		      AND (:endDate IS NULL OR ea.visit_date <= :endDate)
		""", nativeQuery = true)
		List<Object[]> findAllAssignmentItemsWithDistrict(
		        @Param("employeeId") Integer employeeId,
		        @Param("siteId") Integer siteId,
		        @Param("startDate") LocalDateTime startDate,
		        @Param("endDate") LocalDateTime endDate);
	@Query(value = """
			SELECT
			    ea.assignment_id,
			    ea.employee_id,
			    emp.employee_name, 
			    emp.site_id,
			    site.site_name, 
			    site.district,
			    cem.customer_id,
			    cust.district,
			    ea.visit_type,
			    iem.local_fixed_rate,
			    iem.intercity_per_km,
			    iem.pvc_per_meter,
			    iem.band_per_no,
			    iem.external_helper,
			    iem.stay_amount,
			    inst.pvc_pipe,
			    inst.pvc_bend,
			    ea.helper_id,
			    ea.visit_date,
			    ea.modified_on
					    FROM employee_assignment ea

					    JOIN employee_master emp
					        ON ea.employee_id = emp.employee_id

					    JOIN comp_site_master site
					        ON emp.site_id = site.site_id

					    JOIN contract_entity_mapping cem
					        ON ea.mapping_id = cem.mapping_id

					    JOIN customer_master cust
					        ON cem.customer_id = cust.customer_id

					    LEFT JOIN installation_expense_master iem
					        ON iem.dis_branch_distt = site.district
					        AND iem.bank_branch_distt = cust.district

					    LEFT JOIN installation inst
					        ON inst.assignment_id = ea.assignment_id

					    WHERE ea.visit_type IN ('LO','IC')
					    AND ea.status = 'Completed'
					""", nativeQuery = true)
	List<Object[]> getCompletedAssignmentsWithExpense();
}