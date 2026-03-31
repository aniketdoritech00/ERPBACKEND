package com.doritech.EmployeeService.request;

public class HierarchyLevelFilterDTO {

	private Integer hierarchyId;
	private Integer levelNumber;
	private String levelName;
	private Boolean endNode;

	private Integer page = 0;
	private Integer size = 10;
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
	 * @return the page
	 */
	public Integer getPage() {
		return page;
	}
	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(Integer page) {
		this.page = page;
	}
	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}
	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	// getters setters

}