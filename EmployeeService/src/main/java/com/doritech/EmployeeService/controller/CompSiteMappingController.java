package com.doritech.EmployeeService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.doritech.EmployeeService.request.CompanySiteMappingRequest;
import com.doritech.EmployeeService.service.CompanySiteMappingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api/comp-site-mapping")
@Validated
public class CompSiteMappingController {

	@Autowired
	private CompanySiteMappingService companySiteMappingService;

	@PostMapping("/saveCompSiteMapping")
	public ResponseEntity saveCompSiteMapping(
			@RequestBody @Valid CompanySiteMappingRequest request,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest httpRequest) {
		request.setCreatedBy(Integer.parseInt(userId));
		return companySiteMappingService.saveCompSiteMapping(request);
	}

	@GetMapping("/getCompSiteMappingById")
	public ResponseEntity getCompSiteMappingById(@RequestParam Integer id,
			HttpServletRequest httpRequest) {
		return companySiteMappingService.getCompSiteMappingBYId(id);
	}

	@GetMapping("/getAllCompSiteMappingByCompId/{compId}")
	public ResponseEntity getAllCompSiteMappingByCompId(
			@PathVariable Integer compId, HttpServletRequest httpRequest) {

		return companySiteMappingService.getAllCompSiteMappingByCompId(compId);
	}

	@GetMapping("/getAllCompSiteMapping")
	public ResponseEntity getAllCompSiteMapping(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest httpRequest) {
		return companySiteMappingService.getAllCompSiteMapping(page, size);
	}

	@PutMapping("/updateCompSiteMapping")
	public ResponseEntity updateCompSiteMapping(@RequestParam Integer id,
			@RequestBody @Valid CompanySiteMappingRequest request,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest httpRequest) {
		request.setModifiedBy(Integer.parseInt(userId));
		return companySiteMappingService.updateCompSiteMapping(id, request);
	}

	@DeleteMapping("/deleteMultipleCompSiteMapping")
	public ResponseEntity deleteMultipleCompSiteMapping(
			@RequestParam List<Integer> ids,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest httpRequest) {
		return companySiteMappingService.deleteMultipleCompSiteMapping(ids);
	}

	@GetMapping("/compSiteMappingFilter")
	public ResponseEntity compSiteMappingFilter(
			CompanySiteMappingRequest request,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return companySiteMappingService.filterCompSiteMapping(request, page,
				size);
	}

	@GetMapping("/getActiveCompSiteNameAndCodeAndId")
	public ResponseEntity getActiveCompSiteNameAndCodeAndId() {
		return companySiteMappingService.getActiveCompSiteNameAndCodeAndId();
	}
	
	@GetMapping("/getAllCompSiteNameAndCodeAndId")
	public ResponseEntity getAllCompSiteNameAndCodeAndId() {
		return companySiteMappingService.getAllCompSiteNameAndCodeAndId();
	}
}