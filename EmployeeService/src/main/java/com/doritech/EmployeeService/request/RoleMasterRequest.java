package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleMasterRequest {

	@NotBlank(message = "{role.roleName.required}")
	@Size(max = 100, message = "{role.roleName.size}")
	private String roleName;

	@Size(max = 500, message = "{role.roleDescription.size}")
	private String roleDescription;

	@Size(max = 1, message = "{role.isActive.size}")
	private String isActive;

	private Integer createdBy;

	private Integer modifiedBy;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}