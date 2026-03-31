package com.doritech.EmployeeService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doritech.EmployeeService.entity.MenuMaster;

public interface MenuMasterRepository extends JpaRepository<MenuMaster, Integer> {

	@Query("SELECT DISTINCT m FROM MenuMaster m LEFT JOIN FETCH m.functionalities ORDER BY m.sequence ASC")
	List<MenuMaster> findAllWithFunctionalities();

	@Query("SELECT m FROM MenuMaster m LEFT JOIN FETCH m.functionalities WHERE m.menuId = :id")
	Optional<MenuMaster> findByIdWithFunctionalities(@Param("id") Integer id);

	@Query("SELECT DISTINCT m FROM MenuMaster m LEFT JOIN FETCH m.functionalities WHERE m.parentMenuId = 0 ORDER BY m.sequence")
	List<MenuMaster> findParentMenus();

	@Query("SELECT DISTINCT m FROM MenuMaster m LEFT JOIN FETCH m.functionalities WHERE m.parentMenuId = :parentId ORDER BY m.sequence")
	List<MenuMaster> findChildMenus(@Param("parentId") Integer parentId);

}