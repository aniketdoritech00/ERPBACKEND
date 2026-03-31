package com.doritech.EmployeeService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.CompSiteMasterEntity;

@Repository
public interface CompSiteMasterRepository
		extends
			JpaRepository<CompSiteMasterEntity, Integer> {
	boolean existsBySiteCodeIgnoreCase(String siteCode);

	@Query("SELECT c.siteId, c.siteName,c.siteCode FROM CompSiteMasterEntity c WHERE c.isActive = 'Y'")
	List<Object[]> findSiteIdAndName();

	Page<CompSiteMasterEntity> findAll(
			Specification<CompSiteMasterEntity> filter, Pageable pageable);
	List<CompSiteMasterEntity> findByIsActive(String string);

	boolean existsBySiteCodeIgnoreCaseAndSiteIdNot(String siteCode, Integer id);

	Optional<CompSiteMasterEntity> findBySiteCode(String siteCode);

}
