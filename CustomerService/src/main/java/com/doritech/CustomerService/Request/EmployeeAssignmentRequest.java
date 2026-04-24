package com.doritech.CustomerService.Request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeAssignmentRequest {

	@NotNull(message = "{assignment.mapping.id.required}")
	private Integer mappingId;

	@NotNull(message = "{assignment.employee.id.required}")
	private Integer employeeId;

	@NotNull(message = "{assignment.site.id.required}")
	private Integer siteId;

	private LocalDateTime assignmentStartDate;

	private LocalDateTime assignmentEndDate;

	@NotNull(message = "{assignment.visit.date.required}")
	//@Future(message = "{assignment.visit.date.invalid}")
	private LocalDateTime visitDate;

	@NotBlank(message = "{assignment.status.required}")
	private String status;

	private String remark;

	// @NotNull(message = "{assignment.created.by.required}")
	private Integer createdBy;

	private Integer modifiedBy;

	

	/**
	 * @return the visitDate
	 */
	public LocalDateTime getVisitDate() {
		return visitDate;
	}

	/**
	 * @param visitDate
	 *            the visitDate to set
	 */
	public void setVisitDate(LocalDateTime visitDate) {
		this.visitDate = visitDate;
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
	public LocalDateTime getAssignmentStartDate() {
		return assignmentStartDate;
	}

	/**
	 * @param assignmentStartDate
	 *            the assignmentStartDate to set
	 */
	public void setAssignmentStartDate(LocalDateTime assignmentStartDate) {
		this.assignmentStartDate = assignmentStartDate;
	}

	/**
	 * @return the assignmentEndDate
	 */
	public LocalDateTime getAssignmentEndDate() {
		return assignmentEndDate;
	}

	/**
	 * @param assignmentEndDate
	 *            the assignmentEndDate to set
	 */
	public void setAssignmentEndDate(LocalDateTime assignmentEndDate) {
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

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}