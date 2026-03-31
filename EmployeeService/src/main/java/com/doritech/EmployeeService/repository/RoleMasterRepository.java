package com.doritech.EmployeeService.repository;

import com.doritech.EmployeeService.entity.RoleMaster;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMasterRepository extends JpaRepository<RoleMaster, Integer>, JpaSpecificationExecutor<RoleMaster> {
	
    boolean existsByRoleName(String roleName);

	List<RoleMaster> findByIsActive(String string);

	boolean existsByRoleNameAndRoleIdNot(String roleName, Integer id);
}