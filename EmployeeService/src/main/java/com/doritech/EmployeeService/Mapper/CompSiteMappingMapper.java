package com.doritech.EmployeeService.Mapper;

import com.doritech.EmployeeService.entity.CompSiteMappingEntity;
import com.doritech.EmployeeService.entity.CompSiteMasterEntity;
import com.doritech.EmployeeService.entity.CompanyEntity;
import com.doritech.EmployeeService.request.CompanySiteMappingRequest;
import com.doritech.EmployeeService.response.CompanySiteMappingResponse;

public class CompSiteMappingMapper {

	public static CompanySiteMappingResponse mapToResponse(
			CompSiteMappingEntity entity) {
		CompanySiteMappingResponse response = new CompanySiteMappingResponse();
		response.setCompSiteId(entity.getCompSiteId());
		response.setCompId(entity.getCompanyEntity().getId());
		response.setCreatedBy(entity.getCreatedBy());
		response.setCreatedOn(entity.getCreatedOn());
		response.setIsActive(entity.getIsActive());
		response.setModifiedBy(entity.getModifiedBy());
		response.setModifiedOn(entity.getModifiedOn());
		response.setSiteId(entity.getCompSiteMaster().getSiteId());

		return response;
	}

	public static CompSiteMappingEntity mapToEntity(
			CompanySiteMappingRequest request) {
		CompSiteMappingEntity entity = new CompSiteMappingEntity();
		CompanyEntity company = new CompanyEntity();
		entity.setCompanyEntity(company);
		entity.setCreatedBy(request.getCreatedBy());
		entity.setIsActive(request.getIsActive());

		// entity.setModifiedBy(request.getModifiedBy());
		entity.setCreatedBy(request.getCreatedBy());
		CompSiteMasterEntity site = new CompSiteMasterEntity();
		site.setSiteId(request.getSiteId());
		entity.setCompSiteMaster(site);

		return entity;
	}
}
