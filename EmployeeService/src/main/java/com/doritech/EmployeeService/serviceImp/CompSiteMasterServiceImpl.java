package com.doritech.EmployeeService.serviceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.Mapper.CompSiteMasterMapper;
import com.doritech.EmployeeService.Specification.SiteSpecification;
import com.doritech.EmployeeService.entity.CompSiteMasterEntity;
import com.doritech.EmployeeService.entity.EmployeeMaster;
import com.doritech.EmployeeService.entity.HierarchyLevelEntity;
import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.entity.UserMaster;
import com.doritech.EmployeeService.exception.BusinessException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.CompSiteMasterRepository;
import com.doritech.EmployeeService.repository.EmployeeMasterRepository;
import com.doritech.EmployeeService.repository.HierarchyLevelRepository;
import com.doritech.EmployeeService.repository.UserMasterRepository;
import com.doritech.EmployeeService.request.CompSiteMasterRequest;
import com.doritech.EmployeeService.response.CompSiteMasterResponse;
import com.doritech.EmployeeService.response.PageResponse;
import com.doritech.EmployeeService.service.CompSiteMasterService;

import jakarta.validation.Valid;

@Service
public class CompSiteMasterServiceImpl implements CompSiteMasterService {

	private static final Logger logger = LoggerFactory
			.getLogger(CompSiteMasterServiceImpl.class);

	@Autowired
	private CompSiteMasterRepository repository;

	@Autowired
	private HierarchyLevelRepository hierarchyLevelRepository;

	@Autowired
	private EmployeeMasterRepository employeeRepository;

	@Autowired
	private UserMasterRepository userRepository;

	@Autowired
	private AuditService auditService;

	@Override
	public ResponseEntity getAllCompSite(int page, int size) {
		ResponseEntity response = new ResponseEntity();
		try {
			logger.info("Fetching all sites. Page: {}, Size: {}", page, size);
			Pageable pageable = PageRequest.of(page, size);
			Page<CompSiteMasterEntity> pageData = repository.findAll(pageable);
			List<CompSiteMasterResponse> list = pageData.getContent().stream()
					.map(entity -> {
						CompSiteMasterResponse res = CompSiteMasterMapper
								.mapToResponse(entity);
						if (entity.getHierarchyLevelEntity().getId() != null) {
							Optional<String> levelName = hierarchyLevelRepository
									.findLevelNameById(entity
											.getHierarchyLevelEntity().getId());
							res.setLevelName(levelName.orElse(null));
						}
						return res;
					}).toList();

			if (list.isEmpty()) {
				logger.warn("No site data found");
				response.setMessage("No Data found");
				response.setStatusCode(404);
				response.setPayload(null);

			} else {
				logger.info("Site data fetched successfully. Total records: {}",
						pageData.getTotalElements());
				PageResponse<CompSiteMasterResponse> response2 = new PageResponse<>();
				response2.setContent(list);
				response2.setPageNumber(pageData.getNumber());
				response2.setPageSize(pageData.getSize());
				response2.setTotalElements(pageData.getTotalElements());
				response2.setTotalPages(pageData.getTotalPages());
				response2.setLastPage(pageData.isLast());

				response.setMessage("Data fetched successfully");
				response.setStatusCode(200);
				response.setPayload(response2);
			}

		} catch (Exception e) {
			logger.error("Error while fetching sites: {}", e.getMessage(), e);
			response.setMessage("Internal Server Error: " + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
		}
		return response;
	}

	@Override
	public ResponseEntity getCompSiteById(Integer Id) {
		ResponseEntity response = new ResponseEntity();
		try {
			logger.info("Fetching site by id: {}", Id);

			Optional<CompSiteMasterEntity> optional = repository.findById(Id);

			if (optional.isPresent()) {
				logger.info("Site found with id: {}", Id);
				CompSiteMasterEntity site = optional.get();
				CompSiteMasterResponse mapToResponse = CompSiteMasterMapper
						.mapToResponse(site);
				if (site.getHierarchyLevelEntity().getId() != null) {
					Optional<String> levelName = hierarchyLevelRepository
							.findLevelNameById(
									site.getHierarchyLevelEntity().getId());

					mapToResponse.setLevelName(levelName.orElse(null));
				}
				response.setMessage("Data get Successfully ");
				response.setPayload(mapToResponse);
				response.setStatusCode(200);
			} else {
				logger.warn("Site not found with id: {}", Id);

				response.setMessage("Data Not Found With Id " + Id);
				response.setStatusCode(404);
			}
		} catch (Exception e) {
			logger.error("Error while fetching site by id {}: {}", Id,
					e.getMessage(), e);
			response.setMessage("Internal Server error " + e.getMessage());
			response.setPayload(null);
			response.setStatusCode(500);
		}
		return response;
	}
	
	@Override
	public ResponseEntity getCompanySiteBysiteCode(String siteCode) {

	    ResponseEntity response = new ResponseEntity();

	    try {
	        logger.info("Fetching site by siteCode: {}", siteCode);

	        Optional<CompSiteMasterEntity> optional = repository.findBySiteCode(siteCode);

	        if (optional.isEmpty()) {
	            logger.warn("Site not found with siteCode: {}", siteCode);

	            response.setMessage("No site found with siteCode: " + siteCode);
	            response.setPayload(null);
	            response.setStatusCode(404);
	            return response;
	        }

	        CompSiteMasterEntity site = optional.get();
	        logger.info("Site found with siteCode: {}", siteCode);

	        CompSiteMasterResponse mapToResponse = CompSiteMasterMapper.mapToResponse(site);

	        // ✅ Safe null check
	        if (site.getHierarchyLevelEntity() != null &&
	            site.getHierarchyLevelEntity().getId() != null) {

	            Optional<String> levelName = hierarchyLevelRepository
	                    .findLevelNameById(site.getHierarchyLevelEntity().getId());

	            mapToResponse.setLevelName(levelName.orElse(null));
	        }

	        response.setMessage("Site fetched successfully");
	        response.setPayload(mapToResponse);
	        response.setStatusCode(200);

	    } catch (Exception e) {

	        logger.error("Error while fetching site by siteCode {}: {}", siteCode, e.getMessage(), e);

	        response.setMessage("Something went wrong while fetching site data");
	        response.setPayload(null);
	        response.setStatusCode(500);
	    }

	    return response;
	}

	@Override
	public ResponseEntity UpdateCompSiteById(Integer id,
			@Valid CompSiteMasterRequest request) {

		logger.info("Updating Company Site with id: {}", id);
		ResponseEntity response = new ResponseEntity();
		CompSiteMasterEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Company Site not found with id: " + id));
		
		// if ("BANK".equalsIgnoreCase(request.getSiteType())
	    //         && (request.getIfsc() == null || request.getIfsc().trim().isEmpty())) {

	    //     response.setMessage("IFSC is mandatory when Site Type is BANK");
	    //     response.setStatusCode(400);
	    //     return response;
	    // }

		if (repository.existsBySiteCodeIgnoreCaseAndSiteIdNot(
				request.getSiteCode(), id)) {
			response.setMessage("Site code already exists");
			response.setStatusCode(400);
			return response;
		}

		HierarchyLevelEntity hierarchyLevel = hierarchyLevelRepository
				.findById(request.getHierarchyLevelId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Hierarchy Level not found"));

		CompSiteMasterEntity oldEntity = new CompSiteMasterEntity();
		BeanUtils.copyProperties(entity, oldEntity);

		entity.setSiteName(request.getSiteName());
		entity.setSiteCode(request.getSiteCode());
		entity.setHierarchyLevelEntity(hierarchyLevel);
		entity.setSiteLongitude(request.getSiteLongitude());
		entity.setSiteLatitude(request.getSiteLatitude());
		entity.setContactPerson(request.getContactPerson());
		entity.setEmail(request.getEmail());
		entity.setPhone(request.getPhone());
		entity.setGstNo(request.getGstNo());
		entity.setAddress(request.getAddress());
		entity.setCity(request.getCity());
		entity.setDistrict(request.getDistrict());
		entity.setState(request.getState());
		entity.setCountry(request.getCountry());
		entity.setPostalCode(request.getPostalCode());
		entity.setIsActive(request.getIsActive());
		entity.setModifiedBy(request.getModifiedBy());
		entity.setModifiedOn(LocalDateTime.now());

		CompSiteMasterEntity updated = repository.save(entity);

		auditService.logAudit(request.getModifiedBy(), 1, "comp_site_master",
				id, "UPDATE", oldEntity, updated, "UPDATED_FIELDS", "SYSTEM",
				"SYSTEM");

		CompSiteMasterResponse responseData = CompSiteMasterMapper
				.mapToResponse(updated);

		response.setMessage("Data Updated Successfully");
		response.setPayload(responseData);
		response.setStatusCode(200);

		return response;
	}

	@Override
	public ResponseEntity saveCompSite(@Valid CompSiteMasterRequest request) {

		logger.info("Saving Company Site with SiteCode: {}",
				request.getSiteCode());
		
		// if ("BANK".equalsIgnoreCase(request.getSiteType())
		//         && (request.getIfsc() == null || request.getIfsc().trim().isEmpty())) {

		//     ResponseEntity response = new ResponseEntity();
		//     response.setMessage("IFSC is mandatory when Site Type is BANK");
		//     response.setStatusCode(400);
		//     return response;
		// }

		if (repository.existsBySiteCodeIgnoreCase(request.getSiteCode())) {
			ResponseEntity response = new ResponseEntity();
			response.setMessage("Site code already exists");
			response.setStatusCode(400);
			return response;
		}

		HierarchyLevelEntity hierarchyLevel = hierarchyLevelRepository
				.findById(request.getHierarchyLevelId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Hierarchy Level not found with id: "
								+ request.getHierarchyLevelId()));

		CompSiteMasterEntity entity = CompSiteMasterMapper.mapToEntity(request,
				hierarchyLevel);
		entity.setHierarchyLevelEntity(hierarchyLevel);
		CompSiteMasterEntity saved = repository.save(entity);
		CompSiteMasterResponse responseData = CompSiteMasterMapper
				.mapToResponse(saved);

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Data Saved Successfully");
		response.setPayload(responseData);
		response.setStatusCode(201);

		logger.info("Company Site saved successfully with id: {}",
				saved.getSiteId());

		return response;
	}

	@Override
	public ResponseEntity deleteMultipleCompSiteMasters(List<Integer> siteIds) {

		ResponseEntity response = new ResponseEntity();

		if (siteIds == null || siteIds.isEmpty()) {
			response.setMessage("No site IDs provided for deletion.");
			response.setStatusCode(400);
			return response;
		}

		List<Integer> deletedIds = new ArrayList<>();
		List<Integer> notFoundIds = new ArrayList<>();

		for (Integer siteId : siteIds) {

			CompSiteMasterEntity site = repository.findById(siteId)
					.orElse(null);

			if (site == null) {
				notFoundIds.add(siteId);
				continue;
			}

			repository.delete(site);

			auditService.logAudit(site.getModifiedBy(), 5, "comp_site_master",
					siteId, "DELETE", site, null, "ALL", "SYSTEM", "SYSTEM");

			deletedIds.add(siteId);
		}

		if (deletedIds.isEmpty()) {

			response.setMessage(
					"No sites were deleted. Sites not found: " + notFoundIds);
			response.setStatusCode(404);

		} else if (notFoundIds.isEmpty()) {

			response.setMessage(
					"All selected sites were deleted successfully.");
			response.setStatusCode(200);
		} else {

			response.setMessage("Some sites deleted successfully: " + deletedIds
					+ ". Some IDs not found: " + notFoundIds);
			response.setStatusCode(200);
		}

		return response;
	}

	@Override
	public ResponseEntity filterSites(String siteName, String siteCode,
			String city, String state, String country, String contactPerson,
			String email, String postalCode, Integer createdBy,
			Integer hierarchyLevelId, String isActive, int page, int size) {

		ResponseEntity response = new ResponseEntity();

		try {
			Pageable pageable = PageRequest.of(page, size);

			Page<CompSiteMasterEntity> pageData = repository
					.findAll(SiteSpecification.filter(siteName, siteCode, city,
							state, country, contactPerson, email, postalCode,
							createdBy, hierarchyLevelId, isActive), pageable);

			List<CompSiteMasterResponse> list = pageData.getContent().stream()
					.map(entity -> {
						CompSiteMasterResponse res = CompSiteMasterMapper
								.mapToResponse(entity);
						if (entity.getHierarchyLevelEntity() != null && entity
								.getHierarchyLevelEntity().getId() != null) {
							Optional<String> levelName = hierarchyLevelRepository
									.findLevelNameById(entity
											.getHierarchyLevelEntity().getId());
							res.setLevelName(levelName.orElse(null));
						}
						return res;
					}).toList();

			if (list.isEmpty()) {
				response.setMessage("No Data found");
				response.setStatusCode(404);
				response.setPayload(null);
			} else {
				PageResponse<CompSiteMasterResponse> pageResponse = new PageResponse<>();
				pageResponse.setContent(list);
				pageResponse.setPageNumber(pageData.getNumber());
				pageResponse.setPageSize(pageData.getSize());
				pageResponse.setTotalElements(pageData.getTotalElements());
				pageResponse.setTotalPages(pageData.getTotalPages());
				pageResponse.setLastPage(pageData.isLast());

				response.setMessage("Filtered Data fetched successfully");
				response.setStatusCode(200);
				response.setPayload(pageResponse);
			}

		} catch (Exception e) {
			response.setMessage("Internal Server Error: " + e.getMessage());
			response.setStatusCode(500);
			response.setPayload(null);
		}

		return response;
	}
	public ResponseEntity getSiteDistrict(Integer siteId) {
		if (siteId == null) {
			return new ResponseEntity("Site ID must not be null", 400, null);
		}

		CompSiteMasterEntity site = repository.findById(siteId).orElse(null);
		if (site == null) {
			return new ResponseEntity("Site not found", 404, null);
		}

		String district = site.getDistrict();
		if (district == null || district.isEmpty()) {
			return new ResponseEntity("District not found for this site", 404,
					null);
		}
		return new ResponseEntity("District fetched successfully", 200,
				district);
	}

	@Override
	public ResponseEntity getAllSitesForFeign() {

		ResponseEntity response = new ResponseEntity();

		try {

			List<CompSiteMasterEntity> entities = repository.findAll();

			List<Map<String, Object>> list = new ArrayList<>();

			for (CompSiteMasterEntity entity : entities) {
				Map<String, Object> map = new HashMap<>();
				map.put("siteId", entity.getSiteId());
				map.put("siteName", entity.getSiteName());
				map.put("siteCode", entity.getSiteCode());
				list.add(map);
			}

			response.setMessage(
					"All site details fetched successfully for Feign");
			response.setPayload(list);
			response.setStatusCode(200);

		} catch (Exception e) {
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
		}

		return response;
	}

	@Override
	 public ResponseEntity getSiteByEmployeeId(Integer employeeId) {
        EmployeeMaster employee = employeeRepository.findByIdWithSite(employeeId)
                .orElseThrow(() -> new BusinessException("Employee not found"));

        CompSiteMasterEntity site = employee.getSite();

        return new ResponseEntity(
                "Site fetched successfully",
                200,
                site
        );
    }

	@Override
	 public ResponseEntity getSiteByUserId(Integer userId) {
        UserMaster user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

		EmployeeMaster employee = employeeRepository.findByIdWithSite(user.getSourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        CompSiteMasterEntity site = employee.getSite();

        return new ResponseEntity(
                "Site fetched successfully",
                200,
                site
        );
    }
}