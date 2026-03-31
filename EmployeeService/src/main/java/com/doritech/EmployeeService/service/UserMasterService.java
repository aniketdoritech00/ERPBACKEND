package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.UserMasterRequest;

import jakarta.validation.Valid;

public interface UserMasterService {

	ResponseEntity saveUser(@Valid UserMasterRequest request);

	ResponseEntity updateUser(Integer id, @Valid UserMasterRequest request);

	ResponseEntity getUserById(Integer id);

	ResponseEntity getAllUsers(int page, int size);

	ResponseEntity deleteUserById(Integer id);

	

	ResponseEntity deleteMultipleUsers(List<Integer> userIds);

	ResponseEntity filterUsers(String loginId, String userType, Integer sourceId, Integer roleId, String isActive,
			int page, int size);

	ResponseEntity getAllEmployeeNames();
}
