package com.doritech.EmployeeService.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MenuRequest {

	@NotNull(message = "{roleMenuAccess.menuMaster.notnull}")
	private Integer menuId;

	@NotBlank(message = "{roleMenuAccess.isActive.notnull}")
	@Size(max = 1, message = "{roleMenuAccess.isActive.size}")
	private String isActive;

	@Valid
	private List<RoleMenuFunctionalityRequest> functionalities;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public List<RoleMenuFunctionalityRequest> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(List<RoleMenuFunctionalityRequest> functionalities) {
		this.functionalities = functionalities;
	}
}