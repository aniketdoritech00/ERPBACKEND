package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.CompSiteMasterRequest;

import jakarta.validation.Valid;

public interface CompSiteMasterService {

	ResponseEntity getAllCompSite(int page, int size);

	ResponseEntity getCompSiteById(Integer Id);
	ResponseEntity getCompanySiteBysiteCode(String siteCode);

	ResponseEntity UpdateCompSiteById(Integer Id,
			@Valid CompSiteMasterRequest request);

	ResponseEntity saveCompSite(@Valid CompSiteMasterRequest request);

	ResponseEntity deleteMultipleCompSiteMasters(List<Integer> siteIds);

	ResponseEntity filterSites(String siteName, String siteCode, String city,
			String state, String country, String contactPerson, String email,
			String postalCode, Integer createdBy, Integer hierarchyLevelId,
			String isActive, int page, int size);

	ResponseEntity getSiteDistrict(Integer siteId);

	ResponseEntity getSiteByEmployeeId(Integer employeeId);

	ResponseEntity getSiteByUserId(Integer userId);

	public ResponseEntity getAllSitesForFeign();
}
