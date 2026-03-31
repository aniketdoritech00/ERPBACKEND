package com.doritech.EmployeeService.Mapper;

import com.doritech.EmployeeService.entity.CompSiteMasterEntity;
import com.doritech.EmployeeService.entity.HierarchyLevelEntity;
import com.doritech.EmployeeService.request.CompSiteMasterRequest;
import com.doritech.EmployeeService.response.CompSiteMasterResponse;

public class CompSiteMasterMapper {

	public static CompSiteMasterEntity mapToEntity(
			CompSiteMasterRequest request,
			HierarchyLevelEntity hierarchyLevelEntity) {
		CompSiteMasterEntity entity = new CompSiteMasterEntity();
		entity.setSiteName(request.getSiteName());
		entity.setSiteCode(request.getSiteCode());
		entity.setIfsc(request.getIfsc());
		entity.setHierarchyLevelEntity(hierarchyLevelEntity);
		entity.setSiteLongitude(request.getSiteLongitude());
		entity.setSiteLatitude(request.getSiteLatitude());
		entity.setContactPerson(request.getContactPerson());
		entity.setSiteType(request.getSiteType());
		entity.setIfsc(request.getIfsc());
		entity.setEmail(request.getEmail());
		entity.setPhone(request.getPhone());
		entity.setAddress(request.getAddress());
		entity.setCity(request.getCity());
		entity.setDistrict(request.getDistrict());
		entity.setState(request.getState());
		entity.setCountry(request.getCountry());
		entity.setPostalCode(request.getPostalCode());
		entity.setIsActive(request.getIsActive());
		entity.setCreatedBy(request.getCreatedBy());
		entity.setModifiedBy(request.getModifiedBy());

		return entity;
	}

	public static CompSiteMasterResponse mapToResponse(
			CompSiteMasterEntity entity) {
		CompSiteMasterResponse response = new CompSiteMasterResponse();
		response.setSiteId(entity.getSiteId());
		response.setSiteName(entity.getSiteName());
		response.setSiteCode(entity.getSiteCode());
		response.setIfsc(entity.getIfsc());
		response.setSiteLongitude(entity.getSiteLongitude());
		response.setSiteLatitude(entity.getSiteLatitude());
		response.setContactPerson(entity.getContactPerson());
		response.setHierarchyLevelId(entity.getHierarchyLevelEntity().getId());
		response.setEmail(entity.getEmail());
		response.setIfsc(entity.getIfsc());
		response.setSiteType(entity.getSiteType());
		response.setPhone(entity.getPhone());
		response.setAddress(entity.getAddress());
		response.setCity(entity.getCity());
		response.setDistrict(entity.getDistrict());
		response.setState(entity.getState());
		response.setCountry(entity.getCountry());
		response.setPostalCode(entity.getPostalCode());
		response.setIsActive(entity.getIsActive());
		response.setCreatedOn(entity.getCreatedOn());
		response.setModifiedOn(entity.getModifiedOn());
		response.setCreatedBy(entity.getCreatedBy());
		response.setModifiedBy(entity.getModifiedBy());
		return response;
	}

}