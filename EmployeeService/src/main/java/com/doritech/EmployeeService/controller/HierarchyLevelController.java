package com.doritech.EmployeeService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.exception.ApiResponse;
import com.doritech.EmployeeService.request.HierarchyLevelFilterDTO;
import com.doritech.EmployeeService.request.HierarchyLevelRequestDTO;
import com.doritech.EmployeeService.service.HierarchyLevelService;
import com.doritech.EmployeeService.service.OnCreate;
import com.doritech.EmployeeService.service.OnUpdate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api/hierarchy-level")
public class HierarchyLevelController {

	@Autowired
	private HierarchyLevelService service;

	@PostMapping("/saveHierarchyLevel")
	public ResponseEntity saveHierarchyLevel(
			@Validated(OnCreate.class) @RequestBody HierarchyLevelRequestDTO dto,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setCreatedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy level created successfully");
		response.setData(service.save(dto));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Hierarchy level created",
				HttpStatus.CREATED.value(), response);
	}

	@PutMapping("/updateHierarchyLevel")
	public ResponseEntity updateHierarchyLevel(@RequestParam Integer id,
			@Validated(OnUpdate.class) @RequestBody HierarchyLevelRequestDTO dto,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setModifiedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy level updated successfully");
		response.setData(service.update(id, dto));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Hierarchy level updated successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getLevelByHierarchylevelId/{hierarchylevelId}")
	public ResponseEntity getLevelByHierarchylevelId(
			@PathVariable Integer hierarchylevelId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy level updated successfully");
		response.setData(service.getLevelByHierarchylevelId(hierarchylevelId));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Hierarchy level fetch successfully",
				HttpStatus.OK.value(), response);
	}

	@PostMapping("/saveMultipleHierarchyLevels")
	public ResponseEntity saveMultipleHierarchyLevels(
			@Valid @RequestBody List<HierarchyLevelRequestDTO> dtoList,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dtoList.forEach(dto -> dto.setCreatedBy(Integer.parseInt(userId)));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy levels created successfully");
		response.setData(service.saveMultipleHierarchyLevels(dtoList));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Hierarchy levels created",
				HttpStatus.CREATED.value(), response);
	}

	@PutMapping("/updateMultipleHierarchyLevels/{hierarchyId}")
	public ResponseEntity updateMultiple(@PathVariable Integer hierarchyId,
			@RequestBody List<HierarchyLevelRequestDTO> dtoList,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		dtoList.forEach(dto -> dto.setCreatedBy(Integer.parseInt(userId)));
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy levels Updated Successfully");
		response.setData(
				service.updateMultipleHierarchyLevels(hierarchyId, dtoList));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());
		return new ResponseEntity("Hierarchy levels Updated Successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getHierarchyLevels/{hierarchyId}")
	public ResponseEntity getHierarchyLevels(@PathVariable Integer hierarchyId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy levels fetched");
		response.setData(service.getByHierarchy(hierarchyId));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Hierarchy levels fetched",
				HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteHierarchyLevel/{id}")
	public ResponseEntity deleteHierarchyLevel(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		service.delete(id);
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy level deleted");
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Hierarchy level deleted",
				HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteMultipleHierarchyLevel")
	public ResponseEntity deleteMultipleHierarchyLevel(
			@RequestParam List<Integer> ids,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		System.out.println("User id is" + userId);
		service.deleteMultiple(ids);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy levels deleted");
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Hierarchy levels deleted",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllHierarchy")
	public ResponseEntity getAllHierarchyLevel(@RequestParam int page,
			@RequestParam int size) {
		return new ResponseEntity("All Data", 200,
				service.getAllHierarchyLevel(page, size));
	}

	@GetMapping("/filterHierarchyLevels")
	public ResponseEntity filterHierarchyLevels(
			@ModelAttribute HierarchyLevelFilterDTO dto) {

		return new ResponseEntity("Filtered Hierarchy Levels", 200,
				service.filterHierarchyLevel(dto));
	}

	@GetMapping("/getHierarchyByHierarchyLevel/{hierarchyLevelId}")
	public ResponseEntity getHierarchyByHierarchyLevel(
			@PathVariable Integer hierarchyLevelId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Hierarchy fetched");
		response.setData(
				service.getHierarchyByHierarchyLevel(hierarchyLevelId));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Hierarchy fetched", HttpStatus.OK.value(),
				response);
	}
}
