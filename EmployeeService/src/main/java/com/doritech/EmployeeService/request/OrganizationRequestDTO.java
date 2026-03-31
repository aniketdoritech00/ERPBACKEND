package com.doritech.EmployeeService.request;

import com.doritech.EmployeeService.service.OnCreate;

import jakarta.validation.constraints.NotBlank;

public class OrganizationRequestDTO {

	@NotBlank(message = "Organization name required", groups = OnCreate.class)
	private String orgName;
	@NotBlank(message = "Active Status required", groups = OnCreate.class)
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
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
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

	// getters/setters

}
