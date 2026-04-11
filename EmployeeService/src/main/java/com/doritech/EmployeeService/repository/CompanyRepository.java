package com.doritech.EmployeeService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.doritech.EmployeeService.entity.CompanyEntity;
import com.doritech.EmployeeService.entity.EmployeeMaster;

public interface CompanyRepository
		extends
			JpaRepository<CompanyEntity, Integer>,
			JpaSpecificationExecutor<CompanyEntity> {

	@Query(value = "SELECT comp_code FROM comp_master ORDER BY comp_id DESC LIMIT 1", nativeQuery = true)
	String findLastCompanyCode();

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, Long id);

	boolean existsByCompanyCode(String companyCode);

	List<CompanyEntity> findByCompanyNameContainingIgnoreCase(
			String companyName);

	Optional<CompanyEntity> findByCompanyCode(String companyCode);

	List<CompanyEntity> findByCityIgnoreCase(String city);

	List<CompanyEntity> findByPostalCode(String postalCode);

	void deleteByIdIn(List<Integer> ids);

	List<CompanyEntity> findByActive(String active);

	Optional<CompanyEntity> findById(Integer companyId);

	boolean existsByCompanyCodeAndIdNot(String companyCode, Integer id);

	@Query("SELECT c.id, c.companyName, c.companyCode FROM CompanyEntity c WHERE c.active = 'Y'")
	List<Object[]> findCompanyIdAndName();

	Optional<CompanyEntity> findByCompanyCodeIgnoreCase(String companyCode);
}