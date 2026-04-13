package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.CompanySiteMappingRequest;

import jakarta.validation.Valid;

public interface CompanySiteMappingService {

	ResponseEntity saveCompSiteMapping(
			@Valid CompanySiteMappingRequest request);

	ResponseEntity getCompSiteMappingBYId(Integer Id);

	ResponseEntity getAllCompSiteMapping(int page, int size);

	ResponseEntity updateCompSiteMapping(Integer Id,
			@Valid CompanySiteMappingRequest request);

	ResponseEntity deleteMultipleCompSiteMapping(List<Integer> ids);

	ResponseEntity filterCompSiteMapping(CompanySiteMappingRequest request,
			int page, int size);

	ResponseEntity getActiveCompSiteNameAndCodeAndId();

	ResponseEntity getAllCompSiteMappingByCompId(Integer compId);

	ResponseEntity getAllCompSiteNameAndCodeAndId();
}
