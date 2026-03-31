package com.doritech.EmployeeService.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "menuId", "menuName", "parentMenuId", "isActive", "path", "sequence", "createdBy", "createdOn",
		"modifiedBy", "modifiedOn", "iconImage", "functionalities", "childMenus" })
public class MenuMasterResponse {

	private Integer menuId;
	private String menuName;
	private Integer parentMenuId;
	private String isActive;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private Integer createdBy;
	private Integer modifiedBy;
	private Integer sequence;
	private String iconImage;
	private String path;

	private List<MenuFunctionalityResponse> functionalities;

	private List<MenuMasterResponse> childMenus;

	public MenuMasterResponse() {
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getIconImage() {
		return iconImage;
	}

	public void setIconImage(String iconImage) {
		this.iconImage = iconImage;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<MenuFunctionalityResponse> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(List<MenuFunctionalityResponse> functionalities) {
		this.functionalities = functionalities;
	}

	public List<MenuMasterResponse> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<MenuMasterResponse> childMenus) {
		this.childMenus = childMenus;
	}

}