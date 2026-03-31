package com.doritech.EmployeeService.service;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.RoleMenuAccessRequest;

public interface RoleMenuAccessService {

	ResponseEntity assignMenuAccess(RoleMenuAccessRequest request);

	ResponseEntity unassignMenuAccess(Integer roleId, Integer menuId);

	ResponseEntity getAllRoleMenuAccess();

	ResponseEntity getByRoleId(Integer roleId);
}