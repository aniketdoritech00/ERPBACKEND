package com.doritech.ItemService.Controller;

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

import com.doritech.ItemService.Entity.ResponseEntity;
import com.doritech.ItemService.Exception.ApiResponse;
import com.doritech.ItemService.Request.GodownRequestDTO;
import com.doritech.ItemService.Response.GodownResponseDTO;
import com.doritech.ItemService.Service.GodownService;
import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item/api/godown")
public class GodownController {

	@Autowired
	private GodownService service;

	@PostMapping("/saveGodown")
	public ResponseEntity saveGodown(@Validated(OnCreate.class) @RequestBody GodownRequestDTO dto,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {
		dto.setCreatedBy(Integer.parseInt(userId));
		GodownResponseDTO result = service.save(dto);
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Godown saved successfully");
		response.setData(result);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Godown saved successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getGodownById/{id}")
	public ResponseEntity getGodownById(@PathVariable Integer id,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		GodownResponseDTO result = service.getById(id);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Godown fetched successfully");
		response.setData(result);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Godown fetched successfully", HttpStatus.OK.value(), response);
	}

	@PutMapping("/updateGodown/{id}")
	public ResponseEntity updateGodown(@PathVariable Integer id,
			@Validated(OnUpdate.class) @RequestBody GodownRequestDTO dto, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setModifiedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();

		response.setSuccess(true);
		response.setMessage("Godown updated successfully");
		response.setData(service.update(id, dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Godown updated successfully", HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteMultiple")
	public ResponseEntity deleteMultipleGodown(@RequestBody List<Integer> ids,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		System.out.println("USER ID is " + Integer.parseInt(userId));

		service.deleteMultiple(ids);

		ApiResponse<Object> response = new ApiResponse<>();

		response.setSuccess(true);
		response.setMessage("Godown deleted successfully");
		response.setData(null);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Godown deleted successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllGodown")
	public ResponseEntity getAllGodown(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		var result = service.getAll(page, size);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Godown list fetched successfully");
		response.setData(result);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Godown list fetched successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllGodowns")
	public ResponseEntity getAllGodowns(@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Godown list fetched successfully");
		response.setData(service.getAllGodowns());
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Godown list fetched successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/filterGodown")
	public ResponseEntity filterGodown(@RequestParam(required = false) String name,
			@RequestParam(required = false) String code, @RequestParam(required = false) String type,
			@RequestParam(required = false) String active, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		var result = service.filter(name, code, type, active, page, size);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Filtered list fetched successfully");
		response.setData(result);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Filtered list fetched successfully", HttpStatus.OK.value(), response);
	}
}