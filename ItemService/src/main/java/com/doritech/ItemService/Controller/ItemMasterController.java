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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item/api/item")
@Tag(name = "Item Master API", description = "APIs for managing items")
public class ItemMasterController {

	@Autowired
	private ItemMasterService service;

	@Autowired
	private ItemMasterRepository repository;

	@Operation(summary = "Save Item", description = "Create a new item")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Item saved successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
	})
	@PostMapping("/saveItem")
	public ResponseEntity saveItem(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Item request payload",
            required = true,
            content = @Content(schema = @Schema(implementation = ItemMasterRequestDTO.class))
        )@Validated(OnCreate.class) @RequestBody ItemMasterRequestDTO dto,
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

	@Operation(summary = "Update Item", description = "Update item by ID")
	@PutMapping("/updateItem/{id}")
	public ResponseEntity updateItem(@Parameter(description = "Item ID", example = "1") @PathVariable Integer id,
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

	@Operation(summary = "Get Item By ID")
	@GetMapping("/getItemById/{id}")
	public ResponseEntity getItemById(@Parameter(description = "Item ID", example = "1") @PathVariable Integer id,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Item fetched successfully");
		response.setData(service.getItemById(id));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Item fetched successfully", HttpStatus.OK.value(), response);
	}

	@Operation(summary = "Check Item Exists")
	@GetMapping("/exists/{itemId}")
	public Boolean checkItemExists(@Parameter(description = "Item ID", example = "1") @PathVariable Integer itemId) {

		if (itemId == null) {
			throw new IllegalArgumentException("Item id cannot be null");
		}

		try {
			return repository.existsById(itemId);
		} catch (Exception ex) {
			throw new RuntimeException("Error while checking item existence");
		}
	}

	@Operation(summary = "Get All Items (Paginated)")
	@GetMapping("/getAllItem")
	public ResponseEntity getAllItem(
			@Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Items fetched successfully");
		response.setData(service.getAllItems(page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Items fetched successfully", HttpStatus.OK.value(), response);
	}

	@Operation(summary = "Get All Items (List)")
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

	@Operation(summary = "Filter Items")
	@GetMapping("/itemfilter")
	public ResponseEntity itemfilter(
			@Parameter(description = "Item Name") @RequestParam(required = false) String itemName,
			@Parameter(description = "Item Code") @RequestParam(required = false) String itemCode,
			@Parameter(description = "Active Status") @RequestParam(required = false) String active,
			@Parameter(description = "Item Type") @RequestParam(required = false) String itemType,
			@Parameter(description = "Category") @RequestParam(required = false) String category,
			@Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "Page size", example = "10") @RequestParam(defaultValue = "10") int size,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Filtered items fetched successfully");
		response.setData(service.itemFilter(itemName, itemCode, active, itemType, category, page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Filtered items fetched successfully", HttpStatus.OK.value(), response);
	}

	@Operation(summary = "Get Item By Code")
	@GetMapping("/code/{code}")
	public ResponseEntity getByItemCode(
			@Parameter(description = "Item Code", example = "ITEM001") @PathVariable String code,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Item fetched by code");
		response.setData(service.getByItemCode(code));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Item fetched by code", HttpStatus.OK.value(), response);
	}

	@Operation(summary = "Get Item By Type")
	@GetMapping("/type/{type}")
	public ResponseEntity getByItemType(
			@Parameter(description = "Item Type", example = "B,I") @PathVariable String type,
			@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Item fetched by type");
		response.setData(service.getByItemType(type));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Item fetched by type", HttpStatus.OK.value(), response);
	}

	@Operation(summary = "Get Parent Items")
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

	@Operation(summary = "Get Component Items")
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

	@Operation(summary = "Check Item Code Exists")
	@GetMapping("/existItemCode")
	public ResponseEntity existItemCode(@Parameter(description = "Item Code", example = "ITEM001") @RequestParam String itemCode,
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

	@Operation(summary = "Delete Multiple Items by ID")
	@DeleteMapping("/deleteMultipleItems")
	public ResponseEntity deleteMultipleItems(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "List of item IDs",
            required = true
        )@RequestBody List<Integer> ids, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		service.deleteMultipleItems(ids);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Items deleted successfully");
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Items deleted successfully", HttpStatus.OK.value(), response);
	}

	@Operation(summary = "Delete Multiple Items by Code")
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