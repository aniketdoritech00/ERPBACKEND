package com.doritech.EmployeeService.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class RoleMenuAccessRequest {

	@NotNull(message = "{roleMenuAccess.roleMaster.notnull}")
	private Integer roleId;

	private Integer createdBy;

	@Valid
	@NotNull(message = "{roleMenuAccess.menus.notnull}")
	private List<MenuRequest> menus;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public List<MenuRequest> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuRequest> menus) {
		this.menus = menus;
	}
}