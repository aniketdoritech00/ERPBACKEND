package com.doritech.EmployeeService.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.EmployeeMaster;

@Repository
public interface EmployeeMasterRepository extends JpaRepository<EmployeeMaster, Integer> {

	boolean existsByEmployeeCode(String employeeCode);

	Page<EmployeeMaster> findAll(Specification<EmployeeMaster> filter, Pageable pageable);

	List<EmployeeMaster> findByIsActive(String string);

	boolean existsByEmployeeId(Integer sourceId);

	List<EmployeeMaster> findBySite_SiteIdAndDesignation(Integer siteId, String string);

	List<EmployeeMaster> findBySite_SiteIdAndDesignationAndIsActive(Integer siteId, String string, String string2);

	Optional<EmployeeMaster> findByEmployeeCodeIgnoreCase(String employeeCode);

	@Query("SELECT e.employeeCode FROM EmployeeMaster e WHERE e.employeeCode IN :codes")
	Set<String> findExistingEmployeeCodes(@Param("codes") List<String> codes);
}