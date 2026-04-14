package com.doritech.EmployeeService.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.CompSiteMappingEntity;

@Repository
public interface CompanySiteMappingRepository
		extends
			JpaRepository<CompSiteMappingEntity, Integer> {

	Page<CompSiteMappingEntity> findAll(
			Specification<CompSiteMappingEntity> specification,
			Pageable pageable);

	boolean existsByCompanyEntity_IdAndCompSiteMaster_SiteId(Integer compId,
			Integer siteId);

	boolean existsByCompanyEntity_IdAndCompSiteMaster_SiteIdAndCompSiteIdNot(
			Integer compId, Integer siteId, Integer id);

	List<CompSiteMappingEntity> findByCompanyEntity_Id(Integer compId);

	List<CompSiteMappingEntity> findByCompanyEntity_IdAndIsActive(Integer comp, String string);

}
