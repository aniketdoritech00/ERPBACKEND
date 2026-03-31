package com.doritech.ItemService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.ItemService.Entity.ResponseEntity;
import com.doritech.ItemService.Request.CommTemplateRequestDTO;
import com.doritech.ItemService.Response.CommTemplateResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;
import com.doritech.ItemService.Service.CommTemplateService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/item/api/template")
public class CommTemplateController {

	@Autowired
	private CommTemplateService service;

	@PostMapping("/saveTemplate")
	public ResponseEntity saveTemplate(
			@Valid @RequestBody CommTemplateRequestDTO dto) {

		CommTemplateResponseDTO response = service.saveTemplate(dto);

		return new ResponseEntity("Template saved successfully", 200, response);
	}

	@PutMapping("/updateTemplate/{id}")
	public ResponseEntity updateTemplate(@PathVariable Integer id,
			@Valid @RequestBody CommTemplateRequestDTO dto) {

		CommTemplateResponseDTO response = service.updateTemplate(id, dto);

		return new ResponseEntity("Template updated successfully", 200,
				response);
	}

	@DeleteMapping("/deleteTemplate/{id}")
	public ResponseEntity deleteTemplate(@PathVariable Integer id) {

		service.deleteTemplate(id);

		return new ResponseEntity("Template deleted successfully", 200, null);
	}

	@GetMapping("/getTemplateById/{id}")
	public ResponseEntity getTemplateById(@PathVariable Integer id) {

		CommTemplateResponseDTO response = service.getTemplateById(id);

		return new ResponseEntity("Success", 200, response);
	}

	@GetMapping("/getAllTemplate")
	public ResponseEntity getAllTemplate() {

		List<CommTemplateResponseDTO> response = service.getAllTemplate();

		return new ResponseEntity("Success", 200, response);
	}

	@GetMapping("/getAllTemplateWithPagination")
	public ResponseEntity getAllTemplateWithPagination(@RequestParam int page,
			@RequestParam int size) {

		PageResponseDTO<CommTemplateResponseDTO> response = service
				.getAllTemplateWithPagination(page, size);

		return new ResponseEntity("Success", 200, response);
	}

	@GetMapping("/filterTemplate")
	public ResponseEntity filterTemplate(
			@RequestParam(required = false) Integer customerId,
			@RequestParam(required = false) String commType,
			@RequestParam(required = false) String templateType,
			@RequestParam(required = false) String isActive) {

		List<CommTemplateResponseDTO> response = service
				.filterTemplate(customerId, commType, templateType, isActive);

		return new ResponseEntity("Success", 200, response);
	}
}