package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.RoleMasterRequest;

import jakarta.validation.Valid;

public interface RoleMasterService {

	ResponseEntity saveRole(@Valid RoleMasterRequest request);

	ResponseEntity updateRole(Integer id, @Valid RoleMasterRequest request);

	ResponseEntity getRoleById(Integer id);

	ResponseEntity getAllRoles(int page, int size);

	ResponseEntity deleteRole(Integer id);

	

	ResponseEntity deleteMultipleRoles(List<Integer> roleIds);

	ResponseEntity filterRoles(String roleName, String isActive, Integer createdBy, Integer modifiedBy, int page,
			int size);

	ResponseEntity getAllRoles();

    ResponseEntity fetchAllRoles();

}
