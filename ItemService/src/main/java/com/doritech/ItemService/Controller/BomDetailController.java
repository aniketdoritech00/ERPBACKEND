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
import com.doritech.ItemService.Request.BomDetailRequestDTO;
import com.doritech.ItemService.Service.BomDetailService;
import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item/api/bom")
public class BomDetailController {

	@Autowired
	private BomDetailService service;

	@PostMapping("/saveBomDetail")
	public ResponseEntity saveBomDetail(
			@Validated(OnCreate.class) @RequestBody BomDetailRequestDTO dto,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setCreatedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("BOM saved successfully");
		response.setData(service.saveBomDetail(dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("BOM saved successfully",
				HttpStatus.OK.value(), response);
	}

	@PutMapping("/updateBomDetail/{id}")
	public ResponseEntity updateBomDetail(@PathVariable Integer id,
			@Validated(OnUpdate.class) @RequestBody BomDetailRequestDTO dto,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setModifiedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("BOM updated successfully");
		response.setData(service.updateBomDetail(id, dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("BOM updated successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getBomDetailById/{id}")
	public ResponseEntity getBomDetailById(@PathVariable Integer id,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("BOM fetched successfully");
		response.setData(service.getBomDetailById(id));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("BOM fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getBomDetailByBomId/{id}")
	public ResponseEntity getBomDetailByBomId(@PathVariable Integer id,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("BOM fetched successfully");
		response.setData(service.getBomDetailByBomId(id));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("BOM fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteBomDetailById/{id}")
	public ResponseEntity deleteBomDetailById(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		service.deleteById(id);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("BOM Deleted successfully");
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("BOM Deleted successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllBomDetail")
	public ResponseEntity getAllBomDetail(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("BOM list fetched");
		response.setData(service.getAllBomDetails(page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("BOM list fetched", HttpStatus.OK.value(),
				response);
	}

	@GetMapping("/bomFilter")
	public ResponseEntity bomFilter(
			@RequestParam(required = false) Integer bomItemId,
			@RequestParam(required = false) Integer rawItemId,
			@RequestParam(required = false) String active,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Filtered BOM fetched");
		response.setData(service.filterBomDetails(bomItemId, rawItemId, active,
				page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Filtered BOM fetched", HttpStatus.OK.value(),
				response);
	}

	@DeleteMapping("/deleteMultipleBom")
	public ResponseEntity deleteMultipleBom(@RequestBody List<Integer> ids,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		service.deleteMultiple(ids);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("BOM deleted successfully");
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("BOM deleted successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/existBom")
	public ResponseEntity existBom(@RequestParam Integer bomItemId,
			@RequestParam Integer rawItemId,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Exist check done");
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());
		boolean exists = service.existBomCombination(bomItemId, rawItemId);

		response.setData(exists);

		if (exists) {
			response.setMessage("BOM combination already exists");
			return new ResponseEntity("BOM already exists",
					HttpStatus.OK.value(), response);
		} else {
			response.setMessage("BOM combination available");
			return new ResponseEntity("BOM available", HttpStatus.OK.value(),
					response);
		}
	}
}