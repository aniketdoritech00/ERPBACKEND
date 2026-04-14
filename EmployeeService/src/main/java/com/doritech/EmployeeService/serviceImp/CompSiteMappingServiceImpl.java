package com.doritech.EmployeeService.serviceImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.Mapper.CompSiteMappingMapper;
import com.doritech.EmployeeService.Specification.CompSiteMappingSpecification;
import com.doritech.EmployeeService.entity.CompSiteMappingEntity;
import com.doritech.EmployeeService.entity.CompSiteMasterEntity;
import com.doritech.EmployeeService.entity.CompanyEntity;
import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.CompSiteMasterRepository;
import com.doritech.EmployeeService.repository.CompanyRepository;
import com.doritech.EmployeeService.repository.CompanySiteMappingRepository;
import com.doritech.EmployeeService.request.CompanySiteMappingRequest;
import com.doritech.EmployeeService.response.CompanySiteMappingResponse;
import com.doritech.EmployeeService.response.PageResponse;
import com.doritech.EmployeeService.service.CompanySiteMappingService;

import jakarta.validation.Valid;

@Service
public class CompSiteMappingServiceImpl implements CompanySiteMappingService {

	private static final Logger logger = LoggerFactory.getLogger(CompSiteMappingServiceImpl.class);

	@Autowired
	private CompanySiteMappingRepository repository;

	@Autowired
	private CompSiteMasterRepository compSiteMasterRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private AuditService auditService;

	@Override
	public ResponseEntity saveCompSiteMapping(@Valid CompanySiteMappingRequest request) {

		logger.info("Saving Company Site Mapping for CompanyId: {}", request.getCompId());

		boolean exists = repository.existsByCompanyEntity_IdAndCompSiteMaster_SiteId(request.getCompId(),
				request.getSiteId());

		if (exists) {

			ResponseEntity response = new ResponseEntity();
			response.setMessage("This company and site mapping already exists.");
			response.setStatusCode(400);
			response.setPayload(null);

			return response;
		}

		CompanyEntity company = companyRepository.findById(request.getCompId())
				.orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + request.getCompId()));

		CompSiteMasterEntity site = compSiteMasterRepository.findById(request.getSiteId())
				.orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + request.getSiteId()));

		CompSiteMappingEntity entity = CompSiteMappingMapper.mapToEntity(request);
		entity.setCompanyEntity(company);
		entity.setCompSiteMaster(site);

		CompSiteMappingEntity saved = repository.save(entity);

		auditService.logAudit(request.getCreatedBy(), 1, "comp_site_mapping", saved.getCompSiteId(), "INSERT", null,
				saved, "ALL", "SYSTEM", "SYSTEM");

		CompanySiteMappingResponse responseData = CompSiteMappingMapper.mapToResponse(saved);

		ResponseEntity response = new ResponseEntity();
		response.setMessage("Data Saved Successfully");
		response.setPayload(responseData);
		response.setStatusCode(201);

		logger.info("Company Site Mapping saved successfully with id: {}", saved.getCompSiteId());

		return response;
	}

	@Override
	public ResponseEntity getCompSiteMappingBYId(Integer Id) {

		ResponseEntity response = new ResponseEntity();
		try {
			logger.info("Fetching Company Site Mapping with id: {}", Id);

			Optional<CompSiteMappingEntity> optional = repository.findById(Id);

			if (optional.isPresent()) {
				CompanySiteMappingResponse data = CompSiteMappingMapper.mapToResponse(optional.get());

				response.setMessage("Data fetched successfully");
				response.setPayload(data);
				response.setStatusCode(200);
			} else {
				response.setMessage("Data Not Found With Id " + Id);
				response.setStatusCode(404);
			}

		} catch (Exception e) {
			logger.error("Error occurred while fetching Company Site Mapping", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
		}
		return response;
	}

	@Override
	public ResponseEntity getAllCompSiteMapping(int page, int size) {

		ResponseEntity response = new ResponseEntity();

		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<CompSiteMappingEntity> pageData = repository.findAll(pageable);

			List<CompanySiteMappingResponse> list = pageData.getContent().stream()
					.map(CompSiteMappingMapper::mapToResponse).toList();

			PageResponse<CompanySiteMappingResponse> pageResponse = new PageResponse<>();
			pageResponse.setContent(list);
			pageResponse.setPageNumber(pageData.getNumber());
			pageResponse.setPageSize(pageData.getSize());
			pageResponse.setTotalElements(pageData.getTotalElements());
			pageResponse.setTotalPages(pageData.getTotalPages());
			pageResponse.setLastPage(pageData.isLast());

			response.setMessage("Data fetched successfully");
			response.setPayload(pageResponse);
			response.setStatusCode(200);

		} catch (Exception e) {
			logger.error("Error occurred while fetching data", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
		}

		return response;
	}

	@Override
	public ResponseEntity updateCompSiteMapping(Integer Id, @Valid CompanySiteMappingRequest request) {
		ResponseEntity response = new ResponseEntity();
		logger.info("Updating Company Site Mapping with id: {}", Id);

		CompSiteMappingEntity entity = repository.findById(Id)
				.orElseThrow(() -> new ResourceNotFoundException("Company Site Mapping not found with id: " + Id));

		boolean exists = repository.existsByCompanyEntity_IdAndCompSiteMaster_SiteIdAndCompSiteIdNot(
				request.getCompId(), request.getSiteId(), Id);

		if (exists) {
			response.setMessage("This company and site mapping already exists.");
			response.setStatusCode(400);
			return response;
		}

		CompanyEntity company = companyRepository.findById(request.getCompId())
				.orElseThrow(() -> new ResourceNotFoundException("Company not found"));

		CompSiteMasterEntity site = compSiteMasterRepository.findById(request.getSiteId())
				.orElseThrow(() -> new ResourceNotFoundException("Site not found"));

		entity.setCompanyEntity(company);
		entity.setCompSiteMaster(site);
		// entity.setCreatedBy(request.getCreatedBy());
		entity.setIsActive(request.getIsActive());
		entity.setModifiedBy(request.getModifiedBy());

		CompSiteMappingEntity updated = repository.save(entity);

		auditService.logAudit(request.getModifiedBy(), 1, "comp_site_mapping", Id, "UPDATE", entity, updated,
				"UPDATED_FIELDS", "SYSTEM", "SYSTEM");

		CompanySiteMappingResponse responseData = CompSiteMappingMapper.mapToResponse(updated);

		response.setMessage("Data Updated Successfully");
		response.setPayload(responseData);
		response.setStatusCode(200);

		return response;
	}

	@Override
	public ResponseEntity deleteMultipleCompSiteMapping(List<Integer> ids) {

		ResponseEntity response = new ResponseEntity();

		try {

			if (ids == null || ids.isEmpty()) {
				response.setStatusCode(400);
				response.setMessage("Please provide at least one Id to delete");
				return response;
			}

			List<CompSiteMappingEntity> entities = repository.findAllById(ids);

			if (entities.isEmpty()) {
				response.setStatusCode(404);
				response.setMessage("No records found for provided Ids");
				return response;
			}

			for (CompSiteMappingEntity entity : entities) {

				auditService.logAudit(entity.getModifiedBy(), 1, "comp_site_mapping", entity.getCompSiteId(), "DELETE",
						entity, null, "ALL", "SYSTEM", "SYSTEM");
			}

			repository.deleteAll(entities);

			if (entities.size() == 1) {
				response.setMessage("Record deleted successfully");
			} else {
				response.setMessage(entities.size() + " records deleted successfully");
			}

			response.setStatusCode(200);

		} catch (Exception e) {
			logger.error("Error while deleting CompSiteMapping", e);
			response.setStatusCode(500);
			response.setMessage("Internal Server Error");
		}

		return response;
	}

	@Override
	public ResponseEntity filterCompSiteMapping(CompanySiteMappingRequest request, int page, int size) {

		ResponseEntity response = new ResponseEntity();

		try {

			Pageable pageable = PageRequest.of(page, size);

			Specification<CompSiteMappingEntity> specification = CompSiteMappingSpecification.filter(request);

			Page<CompSiteMappingEntity> pageData = repository.findAll(specification, pageable);

			List<CompanySiteMappingResponse> list = pageData.getContent().stream()
					.map(CompSiteMappingMapper::mapToResponse).toList();

			PageResponse<CompanySiteMappingResponse> pageResponse = new PageResponse<>();

			pageResponse.setContent(list);
			pageResponse.setPageNumber(pageData.getNumber());
			pageResponse.setPageSize(pageData.getSize());
			pageResponse.setTotalElements(pageData.getTotalElements());
			pageResponse.setTotalPages(pageData.getTotalPages());
			pageResponse.setLastPage(pageData.isLast());

			response.setMessage("Filtered data fetched successfully");
			response.setPayload(pageResponse);
			response.setStatusCode(200);

		} catch (Exception e) {

			logger.error("Error occurred while filtering data", e);

			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
		}

		return response;
	}

	@Override
	public ResponseEntity getActiveCompSiteNameAndCodeAndId() {

		ResponseEntity response = new ResponseEntity();

		try {

			List<CompSiteMasterEntity> entities = compSiteMasterRepository.findByIsActive("Y");

			List<Map<String, Object>> list = new ArrayList<>();

			for (CompSiteMasterEntity entity : entities) {

				Map<String, Object> map = new HashMap<>();

				map.put("siteId", entity.getSiteId());
				map.put("siteName", entity.getSiteName());
				map.put("siteCode", entity.getSiteCode());

				list.add(map);
			}

			response.setMessage("Active site details fetched successfully");
			response.setPayload(list);
			response.setStatusCode(200);

		} catch (Exception e) {

			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllCompSiteMappingByCompId(Integer compId) {
		ResponseEntity response = new ResponseEntity();
		try {
			logger.info("Fetching Company Site Mapping with compId: {}", compId);

			List<CompSiteMappingEntity> mappingEntities = repository.findByCompanyEntity_Id(compId);

			List<CompanySiteMappingResponse> companySiteMappingResponses = new ArrayList<>();

			if (mappingEntities.isEmpty()) {

				response.setMessage("Data Not Found With this CompId " + compId);
				response.setStatusCode(404);
				response.setPayload(null);
			} else {

				for (CompSiteMappingEntity entity : mappingEntities) {
					CompanySiteMappingResponse data = CompSiteMappingMapper.mapToResponse(entity);

					companySiteMappingResponses.add(data);

				}
				response.setMessage("Data fetched successfully");
				response.setPayload(companySiteMappingResponses);
				response.setStatusCode(200);
			}

		} catch (Exception e) {
			logger.error("Error occurred while fetching Company Site Mapping", e);
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
		}
		return response;
	}

	@Override
	public ResponseEntity getAllCompSiteNameAndCodeAndId() {

		ResponseEntity response = new ResponseEntity();

		try {

			List<CompSiteMasterEntity> entities = compSiteMasterRepository.findAll();
			if (entities.isEmpty() || entities == null) {
				response.setMessage("Site details Not Found ");
				response.setStatusCode(404);
				return response;
			}

			List<Map<String, Object>> list = new ArrayList<>();

			for (CompSiteMasterEntity entity : entities) {

				Map<String, Object> map = new HashMap<>();

				map.put("siteId", entity.getSiteId());
				map.put("siteName", entity.getSiteName());
				map.put("siteCode", entity.getSiteCode());

				list.add(map);
			}

			response.setMessage("All site details fetched successfully");
			response.setPayload(list);
			response.setStatusCode(200);

		} catch (Exception e) {
			response.setMessage("Internal Server Error");
			response.setStatusCode(500);
		}

		return response;
	}

	@Override
	public ResponseEntity getAllActiveSiteByCompanyId(Integer comp) {

		List<CompSiteMappingEntity> sites = repository.findByCompanyEntity_IdAndIsActive(comp, "Y");

		if (sites.isEmpty()) {
			throw new ResourceNotFoundException("No active sites found for given company");
		}

		List<Map<String, Object>> list = new ArrayList<>();

		for (CompSiteMappingEntity obj : sites) {
			Map<String, Object> map = new HashMap<>();
			map.put("siteId", obj.getCompSiteMaster().getSiteId());
			map.put("siteName", obj.getCompSiteMaster().getSiteName());
			map.put("siteCode", obj.getCompSiteMaster().getSiteCode());
			list.add(map);
		}

		return new ResponseEntity("Site list fetched successfully for this company" , 200, list);
	}
}