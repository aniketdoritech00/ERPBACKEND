package com.doritech.EmployeeService.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.EmployeeRequest;
import com.doritech.EmployeeService.response.EmployeeResponse;

public interface EmployeeService {

	EmployeeResponse saveEmployee(EmployeeRequest request);

	EmployeeResponse updateEmployee(Integer employeeId,
			EmployeeRequest request);

	EmployeeResponse getEmployeeById(Integer employeeId);

	List<EmployeeResponse> getAllEmployees();

	Page<EmployeeResponse> getEmployeesWithPagination(int page, int size);

	Page<EmployeeResponse> filterEmployees(String employeeName,
			String employeeCode, String isActive, String email, String phone,
			String department, String designation, String role,
			Integer companyId, Integer siteId, int page, int size);

	ResponseEntity getCompanyIdAndName();

	ResponseEntity getSiteIdAndName();

	List<Map<String, Object>> getEmployeeAsParentList();

	ResponseEntity getAllActiveEmployees();

	ResponseEntity getAllAssociateFa(Integer siteId);

	ResponseEntity getEmployeeDistrict(Integer employeeId);

	ResponseEntity uploadEmployeeExcel(MultipartFile file, Integer userIdInt);

	byte[] generateEmployeeTemplate();

	List<Map<String, Object>> getAllEmployeeByCompanyId(Integer compId);

}