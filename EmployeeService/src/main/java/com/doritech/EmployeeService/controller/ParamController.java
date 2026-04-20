package com.doritech.EmployeeService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.doritech.EmployeeService.exception.ApiResponse;
import com.doritech.EmployeeService.request.ParamRequestDTO;
import com.doritech.EmployeeService.service.ParamService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api/param")
public class ParamController {

	@Autowired
	private ParamService service;

	@PostMapping("/saveParam")
	public ResponseEntity saveParam(@Valid @RequestBody ParamRequestDTO dto, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Param saved successfully");
		response.setData(service.save(dto));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Saved Successfully", HttpStatus.CREATED.value(), response);
	}

	// Save
	@PostMapping("/saveMultipleParam")
	public ResponseEntity saveMultipleParam(@Valid @RequestBody List<ParamRequestDTO> dtos,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("All Param saved successfully");
		response.setData(service.saveAllParamRecords(dtos));
		response.setStatusCode(HttpStatus.CREATED.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Saved Successfully", HttpStatus.CREATED.value(), response);
	}

	@PutMapping("/updateMultipleParam")
	public ResponseEntity updateParam(@Valid @RequestBody List<ParamRequestDTO> paramList,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Param updated successfully");
		response.setData(service.updateMultipleParam(paramList));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Updated Successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getParamById/{id}")
	public ResponseEntity getById(@PathVariable Integer id, HttpServletRequest request) {

		System.out.println("Request URI: " + request.getRequestURI());

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Param fetched successfully");
		response.setData(service.getById(id));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Fetched Successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllParams")
	public ResponseEntity getAllParams(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {

		System.out.println("Request URI: " + request.getRequestURI());

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Params fetched successfully");
		response.setData(service.getAllParams(page, size));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Fetched Successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getParamByCode/{code}")
	public ResponseEntity getByCode(@PathVariable String code, HttpServletRequest request) {

		System.out.println("Request URI: " + request.getRequestURI());

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Param list fetched successfully");
		response.setData(service.getByCode(code));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Fetched Successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getParamByCodeAndSerial")
	public ResponseEntity getByCode(@RequestParam String code, @RequestParam String serial,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Param list fetched successfully");
		response.setData(service.getByCodeAndSerial(code, serial));
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Fetched Successfully", HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteParamById/{id}")
	public ResponseEntity deleteById(@PathVariable Integer id, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		service.deleteById(id);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Param deleted successfully");
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Deleted Successfully", HttpStatus.OK.value(), response);
	}


	@DeleteMapping("/deleteParamByCode/{code}")
	public ResponseEntity deleteByCode(@PathVariable String code, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		System.out.println("User id is: " + userId);
		System.out.println("Request URI: " + request.getRequestURI());

		service.deleteByCode(code);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Param deleted successfully");
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Deleted Successfully", HttpStatus.OK.value(), response);
	}
}