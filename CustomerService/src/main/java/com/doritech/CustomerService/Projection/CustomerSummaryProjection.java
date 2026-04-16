package com.doritech.CustomerService.Projection;

import java.time.LocalDateTime;

public interface CustomerSummaryProjection {

	Integer getCustomerId();
	String getCustomerName();
	String getCustomerCode();
	Integer getOrgId();
	Integer getCompId();
	String getIfsc();
	String getAddress();
	String getCity();
	String getDistrict();
	String getState();
	String getCountry();
	String getPostalCode();
	String getGstin();
	Integer getParentId();
	Integer getHierarchyLevelId();
	String getIsActive();
	Integer getCreatedBy();
	LocalDateTime getCreatedOn();
	Integer getModifiedBy();
	LocalDateTime getModifiedOn();

	String getOrgName();
	String getCompanyName();
	String getCompanyCode();
	String getHierarchyName();
}