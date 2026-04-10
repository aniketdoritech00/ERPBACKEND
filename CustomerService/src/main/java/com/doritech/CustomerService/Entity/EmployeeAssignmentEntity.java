package com.doritech.CustomerService.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_assignment")
public class EmployeeAssignmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "assignment_id")
	private Integer assignmentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mapping_id", nullable = false)
	private ContractEntityMapping contractEntityMapping;

	@Column(name = "employee_id", nullable = false)
	private Integer employeeId;

	@Column(name = "site_id", nullable = false)
	private Integer siteId;

	@Column(name = "assignment_start_date")
	private LocalDateTime assignmentStartDate;

	@Column(name = "assignment_end_date")
	private LocalDateTime assignmentEndDate;

	@Column(name = "visit_date")
	private LocalDateTime visitDate;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "remark")
	private String remark;

	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "modified_by")
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

	@PrePersist
	public void prePersist() {
		this.createdOn = LocalDateTime.now();
	}

	// ✅ Automatically called before update
	@PreUpdate
	public void preUpdate() {
		this.modifiedOn = LocalDateTime.now();
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

	public ContractEntityMapping getContractEntityMapping() {
		return contractEntityMapping;
	}

	public void setContractEntityMapping(ContractEntityMapping contractEntityMapping) {
		this.contractEntityMapping = contractEntityMapping;
	}

}