package com.doritech.EmployeeService.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.RoleMaster;
import com.doritech.EmployeeService.entity.UserMaster;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Integer>, JpaSpecificationExecutor<UserMaster> {

	Optional<UserMaster> findByLoginId(String loginId);

	boolean existsByLoginId(String loginId);

	boolean existsByRole(RoleMaster role);

	boolean existsBySourceId(Integer sourceId);

	Optional<RoleMaster> findByUserType(String string);

	List<UserMaster> findAllByUserType(String string);

	boolean existsBySourceIdAndUserIdNot(Integer sourceId, Integer id);

	List<UserMaster> findByRoleRoleIdIn(ArrayList arrayList);

}