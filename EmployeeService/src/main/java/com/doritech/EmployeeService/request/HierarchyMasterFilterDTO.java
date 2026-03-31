package com.doritech.EmployeeService.request;

public class HierarchyMasterFilterDTO {

	private String hierarchyName;
	private String entityType;
	private Integer companyId;
	private Integer organizationId;
	private String active;

	private Integer page = 0;
	private Integer size = 10;
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

}