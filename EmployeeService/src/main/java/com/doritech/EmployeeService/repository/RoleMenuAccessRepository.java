package com.doritech.EmployeeService.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.MenuMaster;
import com.doritech.EmployeeService.entity.RoleMaster;
import com.doritech.EmployeeService.entity.RoleMenuAccess;

@Repository
public interface RoleMenuAccessRepository extends JpaRepository<RoleMenuAccess, Integer> {

	Optional<RoleMenuAccess> findByRoleMasterRoleIdAndMenuMasterMenuId(Integer roleId, Integer menuId);

	Optional<RoleMenuAccess> findByRoleMasterAndMenuMaster(RoleMaster role, MenuMaster menu);

	List<RoleMenuAccess> findByRoleMaster_RoleId(Integer roleId);

	@Query("""
			    SELECT DISTINCT rma
			    FROM RoleMenuAccess rma
			    LEFT JOIN FETCH rma.roleMaster
			    LEFT JOIN FETCH rma.menuMaster
			    LEFT JOIN FETCH rma.roleMenuFunctionalities
			""")
	List<RoleMenuAccess> findAllWithDetails();

	boolean existsByRoleMaster(RoleMaster existingRole);

	List<RoleMenuAccess> findByRoleMasterRoleIdIn(ArrayList arrayList);
}