package com.doritech.CustomerService.Request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public class EmployeeTaskAssignmentRequest {

	@NotNull(message = "{assignment.mapping.id.required}")
	private Integer mappingId;

	@NotNull(message = "{assignment.employee.id.required}")
	private Integer employeeId;

	@NotNull(message = "{assignment.site.id.required}")
	private Integer siteId;

	private Integer helperId;

	private LocalDateTime assignmentStartDate;

	private LocalDateTime assignmentEndDate;

	private Integer createdBy;

	private Integer modifiedBy;
	private String visitType;
	public Integer getMappingId() {
		return mappingId;
	}

	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public LocalDateTime getAssignmentStartDate() {
		return assignmentStartDate;
	}

	public void setAssignmentStartDate(LocalDateTime assignmentStartDate) {
		this.assignmentStartDate = assignmentStartDate;
	}

	public LocalDateTime getAssignmentEndDate() {
		return assignmentEndDate;
	}

	public void setAssignmentEndDate(LocalDateTime assignmentEndDate) {
		this.assignmentEndDate = assignmentEndDate;
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

	public Integer getHelperId() {
		return helperId;
	}

	public void setHelperId(Integer helperId) {
		this.helperId = helperId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	
}