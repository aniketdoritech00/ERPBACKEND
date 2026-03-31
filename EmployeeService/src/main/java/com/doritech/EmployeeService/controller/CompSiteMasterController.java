package com.doritech.EmployeeService.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.doritech.EmployeeService.repository.CompSiteMasterRepository;
import com.doritech.EmployeeService.request.CompSiteMasterRequest;
import com.doritech.EmployeeService.service.CompSiteMasterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api/companies-site")
public class CompSiteMasterController {

	@Autowired
	public CompSiteMasterService compSiteMasterService;

	private static final Logger logger = LoggerFactory
			.getLogger(CompSiteMasterController.class);

	@Autowired
	private CompSiteMasterRepository siteRepository;

	@PostMapping("/createCompanySite")
	public ResponseEntity createCompanySite(
			@RequestBody @Valid CompSiteMasterRequest requestBody,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		requestBody.setCreatedBy(Integer.parseInt(userId));
		return compSiteMasterService.saveCompSite(requestBody);
	}

	@GetMapping("/getAllCompanySite")
	public ResponseEntity getAllCompanySite(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {
		return compSiteMasterService.getAllCompSite(page, size);
	}

	@GetMapping("/getCompanySiteById")
	public ResponseEntity getCompanySiteById(@RequestParam Integer id,
			HttpServletRequest request) {
		return compSiteMasterService.getCompSiteById(id);
	}

	@GetMapping("/getCompanySiteBysiteCode")
	public ResponseEntity getCompanySiteBysiteCode(@RequestParam String siteCode,
			HttpServletRequest request) {
		return compSiteMasterService.getCompanySiteBysiteCode(siteCode);
	}

	@PutMapping("/updateCompanySiteById")
	public ResponseEntity updateCompanySiteById(@RequestParam Integer id,
			@RequestBody @Valid CompSiteMasterRequest requestBody,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		requestBody.setModifiedBy(Integer.parseInt(userId));
		return compSiteMasterService.UpdateCompSiteById(id, requestBody);
	}

	@DeleteMapping("/deleteMultipleCompanySite")
	public ResponseEntity deleteMultipleCompanySite(
			@RequestParam List<Integer> siteIds,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {
		return compSiteMasterService.deleteMultipleCompSiteMasters(siteIds);
	}

	@GetMapping("/filterSites")
	public ResponseEntity filterSites(
			@RequestParam(required = false) String siteName,
			@RequestParam(required = false) String siteCode,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String state,
			@RequestParam(required = false) String country,
			@RequestParam(required = false) String contactPerson,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String postalCode,
			@RequestParam(required = false) Integer createdBy,
			@RequestParam(required = false) Integer hierarchyLevelId,
			@RequestParam(required = false) String isActive,

			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return compSiteMasterService.filterSites(siteName, siteCode, city,
				state, country, contactPerson, email, postalCode, createdBy,
				hierarchyLevelId, isActive, page, size);
	}
	@GetMapping("/getDistrictBySite/{siteId}")
	public ResponseEntity getDistrictBySite(@PathVariable Integer siteId) {
		return compSiteMasterService.getSiteDistrict(siteId);
	}

	@GetMapping("/exists/{siteId}")
	public Boolean checkSiteExists(@PathVariable Integer siteId) {

		logger.info("Check Site Exists API called for siteId {}", siteId);

		try {

			boolean exists = siteRepository.existsById(siteId);

			if (exists) {
				logger.info("Site exists with id {}", siteId);
			} else {
				logger.warn("Site not found with id {}", siteId);
			}

			return exists;

		} catch (Exception ex) {

			logger.error("Error while checking site existence for siteId {}",
					siteId, ex);
			return false;
		}

	}
	@GetMapping("/feign-all-sites")
	public ResponseEntity fetchAllSitesForFeign() {
		return compSiteMasterService.getAllSitesForFeign();
	}
}