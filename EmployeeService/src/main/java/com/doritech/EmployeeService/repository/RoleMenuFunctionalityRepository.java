package com.doritech.EmployeeService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.RoleMenuAccess;
import com.doritech.EmployeeService.entity.RoleMenuFunctionality;
@Repository
public interface RoleMenuFunctionalityRepository extends JpaRepository<RoleMenuFunctionality, Integer> {

	List<RoleMenuFunctionality> findByRoleMenuAccess(RoleMenuAccess roleMenuAccess);

	void deleteByRoleMenuAccess(RoleMenuAccess savedAccess);
}