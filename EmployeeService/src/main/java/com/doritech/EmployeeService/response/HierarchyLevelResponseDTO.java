package com.doritech.EmployeeService.response;

import java.time.LocalDateTime;

public class HierarchyLevelResponseDTO {

	private Integer id;
	private Integer hierarchyId;
	private String entityType;
	private Integer levelNumber;
	private String levelName;
	private Boolean endNode;
	private LocalDateTime createdOn;
	private String hierarchyName;

	public HierarchyLevelResponseDTO(Integer id, Integer hierarchyId,String hierarchyName,
			String entityType, Integer levelNumber, String levelName,
			Boolean endNode, LocalDateTime createdOn) {

		this.id = id;
		this.hierarchyId = hierarchyId;
		this.hierarchyName = hierarchyName;
		this.entityType = entityType;
		this.levelNumber = levelNumber;
		this.levelName = levelName;
		this.endNode = endNode;
		this.createdOn = createdOn;
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
	 * @param entityType
	 *            the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

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

	// getters setters
}
