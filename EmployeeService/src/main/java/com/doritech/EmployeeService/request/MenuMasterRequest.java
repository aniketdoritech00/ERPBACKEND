package com.doritech.EmployeeService.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MenuMasterRequest {

	@NotBlank(message = "{menuMaster.menuName.required}")
	@Size(max = 100, message = "{menuMaster.menuName.size}")
	private String menuName;

	private Integer parentMenuId;

	@NotBlank(message = "{menuMaster.isActive.required}")
	@Size(min = 1, max = 20, message = "{menuMaster.isActive.size}")
	private String isActive;

	@Size(max = 255, message = "{menuMaster.path.size}")
	private String path;

	@NotNull(message = "{menuMaster.sequence.required}")
	private Integer sequence;

	private Integer createdBy;

	private Integer modifiedBy;
	private MultipartFile iconImage;
	@Valid
	private List<MenuFunctionalityRequest> functionalities;

	public MenuMasterRequest() {
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

	public MultipartFile getIconImage() {
		return iconImage;
	}

	public void setIconImage(MultipartFile iconImage) {
		this.iconImage = iconImage;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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

	public List<MenuFunctionalityRequest> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(List<MenuFunctionalityRequest> functionalities) {
		this.functionalities = functionalities;
	}
}