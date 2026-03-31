package com.doritech.EmployeeService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.HierarchyLevelEntity;

@Repository
public interface HierarchyLevelRepository
		extends JpaRepository<HierarchyLevelEntity, Integer>, JpaSpecificationExecutor<HierarchyLevelEntity> {

	List<HierarchyLevelEntity> findByHierarchy_Id(Integer hierarchyId);

	boolean existsByHierarchy_IdAndLevelNumber(Integer hierarchyId, Integer levelNumber);

	boolean existsByHierarchy_IdAndLevelNameIgnoreCase(Integer hierarchyId, String levelName);

	boolean existsById(Integer id);

	@Query(value = "SELECT level_name FROM hierarchy_level WHERE hierarchy_level_id = :id", nativeQuery = true)
	Optional<String> findLevelNameById(Integer id);

	Integer countByHierarchy_Id(Integer hierarchyId);
}
