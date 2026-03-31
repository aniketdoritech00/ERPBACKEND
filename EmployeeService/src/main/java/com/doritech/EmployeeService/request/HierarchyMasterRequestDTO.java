package com.doritech.EmployeeService.request;

import com.doritech.EmployeeService.service.OnCreate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HierarchyMasterRequestDTO {

	@NotBlank(message = "Hierarchy Name is Required", groups = OnCreate.class)
	private String hierarchyName;

	@NotBlank(message = "Entity Type is Required", groups = OnCreate.class)
	private String entityType;

	@NotNull(message = "Company Id is Required", groups = OnCreate.class)
	private Integer companyId;

	private Integer organizationId;

	private Integer hierarchyLevels;
	private String description;

	private String active;
	private Integer createdBy;
	private Integer modifiedBy;

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the hierarchyName
	 */
	public String getHierarchyName() {
		return hierarchyName;
	}

	/**
	 * @param hierarchyName the hierarchyName to set
	 */
	public void setHierarchyName(String hierarchyName) {
		this.hierarchyName = hierarchyName;
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return the companyId
	 */
	public Integer getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the organizationId
	 */
	public Integer getOrganizationId() {
		return organizationId;
	}

	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @return the hierarchyLevels
	 */
	public Integer getHierarchyLevels() {
		return hierarchyLevels;
	}

	/**
	 * @param hierarchyLevels the hierarchyLevels to set
	 */
	public void setHierarchyLevels(Integer hierarchyLevels) {
		this.hierarchyLevels = hierarchyLevels;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
