package com.doritech.EmployeeService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.HierarchyMasterEntity;

@Repository
public interface HierarchyMasterRepository
		extends
			JpaRepository<HierarchyMasterEntity, Integer>,
			JpaSpecificationExecutor<HierarchyMasterEntity> {

	List<HierarchyMasterEntity> findByActive(String string);

	List<HierarchyMasterEntity> findByActiveTrue();

	Optional<HierarchyMasterEntity> findById(Integer hierarchyId);

	boolean existsByHierarchyNameIgnoreCaseAndCompany_Id(String hierarchyName,
			Integer companyId);

	List<HierarchyMasterEntity> findByActive(String string, Sort ascending);
}
