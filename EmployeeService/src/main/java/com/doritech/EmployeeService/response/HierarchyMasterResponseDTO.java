package com.doritech.EmployeeService.response;

import java.time.LocalDateTime;
import java.util.List;

public class HierarchyMasterResponseDTO {

	private Integer id;
	private String hierarchyName;
	private String entityType;
	private Integer companyId;
	private Integer organizationId;
	private Integer hierarchyLevels;
	private String description;
	private String active;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private Integer createdBy;
	private Integer modifiedBy;

	private List<HierarchyLevelResponseDTO> levels;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the hierarchyName
	 */
	public String getHierarchyName() {
		return hierarchyName;
	}

	/**
	 * @param hierarchyName
	 *            the hierarchyName to set
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
	 * @param entityType
	 *            the entityType to set
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
	 * @param companyId
	 *            the companyId to set
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
	 * @param organizationId
	 *            the organizationId to set
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
	 * @param hierarchyLevels
	 *            the hierarchyLevels to set
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
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the modifiedOn
	 */
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn
	 *            the modifiedOn to set
	 */
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * @return the levels
	 */
	public List<HierarchyLevelResponseDTO> getLevels() {
		return levels;
	}

	/**
	 * @param levels
	 *            the levels to set
	 */
	public void setLevels(List<HierarchyLevelResponseDTO> levels) {
		this.levels = levels;
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
