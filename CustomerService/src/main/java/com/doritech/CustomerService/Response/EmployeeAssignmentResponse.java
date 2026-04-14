package com.doritech.CustomerService.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EmployeeAssignmentResponse {

	private Integer assignmentId;
	private Integer mappingId;
	private Integer employeeId;
	private String employeeName;
	private Integer siteId;
	private String ifsc;
	private String district;
	private Integer customerId;
	private String customerName;
	private String zoneName;
	private String siteName;
	private List<String> productName;

	private LocalDate assignmentStartDate;
	private LocalDate assignmentEndDate;

	private LocalDate visitDate;
	private String visitType;

	private String status;
	private String remark;

	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

	private Integer createdBy;
	private Integer modifiedBy;

	/**
	 * @return the ifsc
	 */
	public String getIfsc() {
		return ifsc;
	}
	/**
	 * @param ifsc
	 *            the ifsc to set
	 */
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName
	 *            the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * @return the visitType
	 */
	public String getVisitType() {
		return visitType;
	}
	/**
	 * @param visitType
	 *            the visitType to set
	 */
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	/**
	 * @return the visitDate
	 */
	public LocalDate getVisitDate() {
		return visitDate;
	}
	/**
	 * @param visitDate
	 *            the visitDate to set
	 */
	public void setVisitDate(LocalDate visitDate) {
		this.visitDate = visitDate;
	}
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName
	 *            the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the zoneName
	 */
	public String getZoneName() {
		return zoneName;
	}
	/**
	 * @param zoneName
	 *            the zoneName to set
	 */
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	/**
	 * @return the siteName
	 */
	public String getSiteName() {
		return siteName;
	}
	/**
	 * @param siteName
	 *            the siteName to set
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * @return the productName
	 */
	public List<String> getProductName() {
		return productName;
	}
	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(List<String> productName) {
		this.productName = productName;
	}
	/**
	 * @return the assignmentId
	 */
	public Integer getAssignmentId() {
		return assignmentId;
	}
	/**
	 * @param assignmentId
	 *            the assignmentId to set
	 */
	public void setAssignmentId(Integer assignmentId) {
		this.assignmentId = assignmentId;
	}
	/**
	 * @return the mappingId
	 */
	public Integer getMappingId() {
		return mappingId;
	}
	/**
	 * @param mappingId
	 *            the mappingId to set
	 */
	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}
	/**
	 * @return the employeeId
	 */
	public Integer getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId
	 *            the employeeId to set
	 */
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId
	 *            the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the assignmentStartDate
	 */
	public LocalDate getAssignmentStartDate() {
		return assignmentStartDate;
	}
	/**
	 * @param assignmentStartDate
	 *            the assignmentStartDate to set
	 */
	public void setAssignmentStartDate(LocalDate assignmentStartDate) {
		this.assignmentStartDate = assignmentStartDate;
	}
	/**
	 * @return the assignmentEndDate
	 */
	public LocalDate getAssignmentEndDate() {
		return assignmentEndDate;
	}
	/**
	 * @param assignmentEndDate
	 *            the assignmentEndDate to set
	 */
	public void setAssignmentEndDate(LocalDate assignmentEndDate) {
		this.assignmentEndDate = assignmentEndDate;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
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
	/**
	 * @return the modifiedBy
	 */
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}