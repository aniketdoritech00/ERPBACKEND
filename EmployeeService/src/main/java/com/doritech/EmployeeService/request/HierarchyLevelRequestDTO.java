package com.doritech.EmployeeService.request;
import com.doritech.EmployeeService.service.OnCreate;
import com.doritech.EmployeeService.service.OnUpdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HierarchyLevelRequestDTO {

	@NotNull(message = "HierarchyId is required", groups = OnCreate.class)
	private Integer hierarchyId;

	@NotNull(message = "Level Number is required", groups = OnCreate.class)
	private Integer levelNumber;

	@NotBlank(message = "Level Name is required", groups = OnCreate.class)
	private String levelName;

	private Boolean endNode;

	private Integer createdBy;

	private Integer modifiedBy;
	
	

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the hierarchyId
	 */
	public Integer getHierarchyId() {
		return hierarchyId;
	}

	/**
	 * @param hierarchyId
	 *            the hierarchyId to set
	 */
	public void setHierarchyId(Integer hierarchyId) {
		this.hierarchyId = hierarchyId;
	}

	/**
	 * @return the levelNumber
	 */
	public Integer getLevelNumber() {
		return levelNumber;
	}

	/**
	 * @param levelNumber
	 *            the levelNumber to set
	 */
	public void setLevelNumber(Integer levelNumber) {
		this.levelNumber = levelNumber;
	}

	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * @param levelName
	 *            the levelName to set
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	/**
	 * @return the endNode
	 */
	public Boolean getEndNode() {
		return endNode;
	}

	/**
	 * @param endNode
	 *            the endNode to set
	 */
	public void setEndNode(Boolean endNode) {
		this.endNode = endNode;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

}
