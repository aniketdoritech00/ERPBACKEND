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
import com.doritech.ItemService.Repository.ItemMasterRepository;
import com.doritech.ItemService.Request.ItemMasterRequestDTO;
import com.doritech.ItemService.Service.ItemMasterService;
import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item/api/item")
public class ItemMasterController {

	@Autowired
	private ItemMasterService service;

	@Autowired
	private ItemMasterRepository repository;

	@PostMapping("/saveItem")
	public ResponseEntity saveItem(@Validated(OnCreate.class) @RequestBody ItemMasterRequestDTO dto,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		dto.setCreatedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Item saved successfully");
		response.setData(service.saveItem(dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Item saved successfully", HttpStatus.OK.value(), response);
	}

	@PutMapping("/updateItem/{id}")
	public ResponseEntity updateItem(@PathVariable Integer id,
			@Validated(OnUpdate.class) @RequestBody ItemMasterRequestDTO dto, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setModifiedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Item updated successfully");
		response.setData(service.updateItem(id, dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Item updated successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getItemById/{id}")
	public ResponseEntity getItemById(@PathVariable Integer id,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Item fetched successfully");
		response.setData(service.getItemById(id));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Item fetched successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/exists/{itemId}")
	public Boolean checkItemExists(@PathVariable Integer itemId) {

		if (itemId == null) {
			throw new IllegalArgumentException("Item id cannot be null");
		}

		try {
			return repository.existsById(itemId);
		} catch (Exception ex) {
			throw new RuntimeException("Error while checking item existence");
		}
	}

	@GetMapping("/getAllItem")
	public ResponseEntity getAllItem(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Items fetched successfully");
		response.setData(service.getAllItems(page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Items fetched successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllItems")
	public ResponseEntity getAllItem(HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Items fetched successfully");
		response.setData(service.getAllItems());
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Items fetched successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/itemfilter")
	public ResponseEntity itemfilter(@RequestParam(required = false) String itemName,
			@RequestParam(required = false) String itemCode, @RequestParam(required = false) String active,
			@RequestParam(required = false) String itemType, @RequestParam(required = false) String category,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Filtered items fetched successfully");
		response.setData(service.itemFilter(itemName, itemCode, active, itemType, category, page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Filtered items fetched successfully", HttpStatus.OK.value(), response);
	}

	@GetMapping("/code/{code}")
	public ResponseEntity getByItemCode(@PathVariable String code,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Item fetched by code");
		response.setData(service.getByItemCode(code));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Item fetched by code", HttpStatus.OK.value(), response);
	}

	@GetMapping("/type/{type}")
	public ResponseEntity getByItemType(@PathVariable String type,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Item fetched by type");
		response.setData(service.getByItemType(type));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Item fetched by type", HttpStatus.OK.value(), response);
	}

	@GetMapping("/parentType")
	public ResponseEntity getParentItem(@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Parent items fetched");
		response.setData(service.getParentItem());
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Parent items fetched", HttpStatus.OK.value(), response);
	}

	@GetMapping("/componentType")
	public ResponseEntity getComponentItem(@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Component items fetched");
		response.setData(service.getComponemtItem());
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Component items fetched", HttpStatus.OK.value(), response);
	}

	@GetMapping("/existItemCode")
	public ResponseEntity existItemCode(@RequestParam String itemCode,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		boolean exists = service.existItemCode(itemCode);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setData(exists);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		if (exists) {
			response.setMessage("Item code already exists");
			return new ResponseEntity("Item code already exists", HttpStatus.OK.value(), response);
		} else {
			response.setMessage("Item code is available");
			return new ResponseEntity("Item code is available", HttpStatus.OK.value(), response);
		}
	}

	@DeleteMapping("/deleteMultipleItems")
	public ResponseEntity deleteMultipleItems(@RequestBody List<Integer> ids, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		service.deleteMultipleItems(ids);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Items deleted successfully");
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Items deleted successfully", HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteMultipleItemCode")
	public ResponseEntity deleteMultipleItemCode(@RequestBody List<String> codes,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest request) {

		service.deleteMultipleItemCode(codes);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Items deleted successfully");
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Items deleted successfully", HttpStatus.OK.value(), response);
	}
}