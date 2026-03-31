package com.doritech.EmployeeService.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
"roleMenuId",
"roleId",
"roleName",
"menuId",
"menuName",
"parentMenuId",
"isActive",
"createdBy",
"createdOn",
"modifiedBy",
"modifiedOn",
"functionalities",
"childMenus"
})
public class RoleMenuAccessResponse {

    private Integer roleMenuId;
    private Integer roleId;
    private String roleName;

    private Integer menuId;
    private String menuName;

    private Integer parentMenuId;

    private String isActive;

    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;

    private Integer createdBy;
    private Integer modifiedBy;

    private List<RoleMenuFunctionalityResponse> functionalities;

    private List<RoleMenuAccessResponse> childMenus;
    private String iconImage;
	public Integer getRoleMenuId() {
		return roleMenuId;
	}

	public void setRoleMenuId(Integer roleMenuId) {
		this.roleMenuId = roleMenuId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getParentMenuId() {
		return parentMenuId;
	}

	public String getIconImage() {
		return iconImage;
	}

	public void setIconImage(String iconImage) {
		this.iconImage = iconImage;
	}

	public void setParentMenuId(Integer parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
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

	public List<RoleMenuFunctionalityResponse> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(List<RoleMenuFunctionalityResponse> functionalities) {
		this.functionalities = functionalities;
	}

	public List<RoleMenuAccessResponse> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<RoleMenuAccessResponse> childMenus) {
		this.childMenus = childMenus;
	}

}