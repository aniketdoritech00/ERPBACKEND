package com.doritech.ItemService.Controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.doritech.ItemService.Request.GodownInventoryFilterDTO;
import com.doritech.ItemService.Request.GodownInventoryRequestDTO;
import com.doritech.ItemService.Service.GodownInventoryService;
import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item/api/godown-inventory")
public class GodownInventoryController {

	@Autowired
	private GodownInventoryService service;

	@PostMapping("/saveGodownInventory")
	public ResponseEntity saveGodownInventory(@RequestBody @Validated(OnCreate.class) GodownInventoryRequestDTO dto,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {
		dto.setCreatedBy(Integer.parseInt(userId));
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Saved Successfully");
		response.setData(service.save(dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());
		return new ResponseEntity("Saved Successfully", 200, response);
	}

	@PutMapping("/updateGodownInventory/{id}")
	public ResponseEntity updateGodownInventory(@PathVariable Integer id,
			@Validated(OnUpdate.class) @RequestBody GodownInventoryRequestDTO dto,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		// Set modifiedBy from X-User-Id header
		dto.setModifiedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Updated Successfully");
		response.setData(service.update(id, dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());
		return new ResponseEntity("Updated Successfully", 200, response);
	}

	@DeleteMapping("/deleteGodownInventory/{id}")
	public ResponseEntity deleteGodownInventory(@PathVariable Integer id, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		service.delete(id);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Deleted Successfully");
		response.setData(null);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());
		return new ResponseEntity("Deleted Successfully", 200, response);
	}

	@GetMapping("/getAllGodownInventory")
	public ResponseEntity getAllGodownInventory(@RequestParam int page, @RequestParam int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Godown Inventory fetch Successfully");
		response.setData(service.getAllGodownInventory(page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Godown Inventory Successfully", HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/multipleGodownInventory")
	public ResponseEntity deleteMultipleGodownInventory(@RequestBody List<Integer> ids,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		service.deleteMultiple(ids);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Multiple Records Deleted");
		response.setData(null);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());
		return new ResponseEntity("Multiple Records Deleted", 200, response);
	}

	@GetMapping("/filterGodownInventory")
	public ResponseEntity filterGodownInventory(@RequestParam(required = false) Integer godownId,
			@RequestParam(required = false) Integer itemId, @RequestParam(required = false) BigDecimal minReorderLevel,
			@RequestParam(required = false) BigDecimal maxReorderLevel, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		GodownInventoryFilterDTO dto = new GodownInventoryFilterDTO();
		dto.setGodownId(godownId);
		dto.setItemId(itemId);
		dto.setMinReorderLevel(minReorderLevel);
		dto.setMaxReorderLevel(maxReorderLevel);

		Pageable pageable = PageRequest.of(page, size, Sort.by("inventoryId").descending());

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Filtered Data");
		response.setData(service.filter(dto, pageable));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Filtered Data", 200, response);
	}
}