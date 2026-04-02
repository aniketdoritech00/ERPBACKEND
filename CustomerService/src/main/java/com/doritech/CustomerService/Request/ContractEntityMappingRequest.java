package com.doritech.CustomerService.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContractEntityMappingRequest {

	private Integer mappingId;
	@NotNull(message = "{contract.id.required}")
	private Integer contractId;

	@NotNull(message = "{customer.id.required}")
	private Integer customerId;

	@NotNull(message = "{site.id.required}")
	private Integer siteId;

	private LocalDate siteFromDate;

	private LocalDate employeeFromDate;

	private Integer employeeId;

	private Integer minNoVisits;

	private Integer visitsFrequency;

	@Size(max = 1, message = "{visits.paid.size}")
	private String visitsPaid;

	@NotBlank(message = "{is.active.required}")
	@Size(max = 1, message = "{is.active.size}")
	private String isActive;

	// @NotNull(message = "{created.by.required}")
	private Integer createdBy;

	private Integer modifiedBy;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getMinNoVisits() {
		return minNoVisits;
	}

	/**
	 * @return the siteFromDate
	 */
	public LocalDate getSiteFromDate() {
		return siteFromDate;
	}

	/**
	 * @param siteFromDate
	 *            the siteFromDate to set
	 */
	public void setSiteFromDate(LocalDate siteFromDate) {
		this.siteFromDate = siteFromDate;
	}

	public void setMinNoVisits(Integer minNoVisits) {
		this.minNoVisits = minNoVisits;
	}

	public Integer getVisitsFrequency() {
		return visitsFrequency;
	}

	public void setVisitsFrequency(Integer visitsFrequency) {
		this.visitsFrequency = visitsFrequency;
	}

	public String getVisitsPaid() {
		return visitsPaid;
	}

	public Integer getMappingId() {
		return mappingId;
	}

	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}

	public void setVisitsPaid(String visitsPaid) {
		this.visitsPaid = visitsPaid;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	public LocalDate getEmployeeFromDate() {
		return employeeFromDate;
	}

	public void setEmployeeFromDate(LocalDate employeeFromDate) {
		this.employeeFromDate = employeeFromDate;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

}