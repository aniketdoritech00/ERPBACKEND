package com.doritech.EmployeeService.Mapper;

import com.doritech.EmployeeService.entity.CompSiteMappingEntity;
import com.doritech.EmployeeService.entity.CompSiteMasterEntity;
import com.doritech.EmployeeService.entity.CompanyEntity;
import com.doritech.EmployeeService.request.CompanySiteMappingRequest;
import com.doritech.EmployeeService.response.CompanySiteMappingResponse;

public class CompSiteMappingMapper {

	public static CompanySiteMappingResponse mapToResponse(CompSiteMappingEntity entity) {
		CompanySiteMappingResponse response = new CompanySiteMappingResponse();
		response.setCompSiteId(entity.getCompSiteId());
		if (entity.getCompanyEntity() != null) {
			response.setCompId(entity.getCompanyEntity().getId());
			response.setCompanyName(entity.getCompanyEntity().getCompanyName());
			response.setCompCode(entity.getCompanyEntity().getCompanyCode());
		}else {
			response.setCompId(null);
			response.setCompanyName(null);
		}
		if (entity.getCompSiteMaster() != null) {
			response.setSiteId(entity.getCompSiteMaster().getSiteId());
			response.setSiteName(entity.getCompSiteMaster().getSiteName());
			response.setSiteCode(entity.getCompSiteMaster().getSiteCode());
		}else {
			response.setSiteId(null);
			response.setSiteName(null);
		}
		response.setCreatedBy(entity.getCreatedBy());
		response.setCreatedOn(entity.getCreatedOn());
		response.setIsActive(entity.getIsActive());
		response.setModifiedBy(entity.getModifiedBy());
		response.setModifiedOn(entity.getModifiedOn());

		return response;
	}

	public static CompSiteMappingEntity mapToEntity(CompanySiteMappingRequest request) {
		CompSiteMappingEntity entity = new CompSiteMappingEntity();
		CompSiteMasterEntity site = new CompSiteMasterEntity();

		CompanyEntity company = new CompanyEntity();
		entity.setCompanyEntity(company);
		entity.setCreatedBy(request.getCreatedBy());
		entity.setIsActive(request.getIsActive());

		entity.setCreatedBy(request.getCreatedBy());
		site.setSiteId(request.getSiteId());
		entity.setCompSiteMaster(site);

		return entity;
	}
}
