package com.doritech.EmployeeService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.HierarchyMasterFilterDTO;
import com.doritech.EmployeeService.request.HierarchyMasterRequestDTO;
import com.doritech.EmployeeService.response.HierarchyMasterResponseDTO;
import com.doritech.EmployeeService.service.HierarchyMasterService;
import com.doritech.EmployeeService.service.OnCreate;
import com.doritech.EmployeeService.service.OnUpdate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee/api/hierarchy")
public class HierarchyMasterController {

	@Autowired
	private HierarchyMasterService service;

	@PostMapping("/saveHierarchy")
	public ResponseEntity saveHierarchy(@Validated(OnCreate.class) @RequestBody HierarchyMasterRequestDTO dto,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {
		dto.setCreatedBy(Integer.parseInt(userId));
		return new ResponseEntity("Saved Successfully", 200, service.saveHierarchy(dto));
	}

	@PutMapping("/updateHierarchy/{id}")
	public ResponseEntity updateHierarchy(@PathVariable Integer id,
			@Validated(OnUpdate.class) @RequestBody HierarchyMasterRequestDTO dto,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {
		dto.setModifiedBy(Integer.parseInt(userId));
		return new ResponseEntity("Updated Successfully", 200, service.updateHierarchy(id, dto));
	}

	@GetMapping("/getByHierarchyId/{id}")
	public ResponseEntity getByHierarchyId(@PathVariable Integer id) {
		return new ResponseEntity("Data Found", 200, service.getByHierarchyId(id));
	}

	@GetMapping("/getAllHierarchy")
	public ResponseEntity getAllHierarchy() {
		return new ResponseEntity("All Data", 200, service.getAllHierarchy());
	}

	@GetMapping("/getAllHierarchyWithPagination")
	public ResponseEntity getAllHierarchy(@RequestParam int page, @RequestParam int size) {
		return new ResponseEntity("All Data", 200, service.getAllHierarchy(page, size));
	}

	@DeleteMapping("/deleteHierarchy/{id}")
	public ResponseEntity deleteHierarchy(@PathVariable Integer id, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());
		service.deleteHierarchy(id);

		return new ResponseEntity("Deleted Successfully", 200, null);
	}

	@DeleteMapping("/deleteMultipleHierarchy")
	public ResponseEntity deleteMultipleHierarchy(@RequestParam List<Integer> ids,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {
		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());
		service.deleteMultipleHierarchy(ids);
		return new ResponseEntity("Deleted Successfully", 200, null);
	}

	@GetMapping("/activeHierarchies")
	public ResponseEntity activeHierarchy() {

		List<HierarchyMasterResponseDTO> response = service.getAllActiveHierarchies();

		return new ResponseEntity("fetch all active Hierarchy successsfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/filterHierarchy")
	public ResponseEntity filterHierarchy(@RequestParam(required = false) String hierarchyName,
			@RequestParam(required = false) String entityType, @RequestParam(required = false) Integer companyId,
			@RequestParam(required = false) Integer organizationId, @RequestParam(required = false) String active,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {

		HierarchyMasterFilterDTO dto = new HierarchyMasterFilterDTO();
		dto.setHierarchyName(hierarchyName);
		dto.setEntityType(entityType);
		dto.setCompanyId(companyId);
		dto.setOrganizationId(organizationId);
		dto.setActive(active);
		dto.setPage(page);
		dto.setSize(size);

		return new ResponseEntity("Filtered Data", 200, service.filterHierarchy(dto));
	}
}