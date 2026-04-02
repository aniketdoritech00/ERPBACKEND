package com.doritech.CustomerService.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class CustomerEmployeeAllocationRequest {

	@NotNull(message = "Customer Id is required")
	private Integer customerId;

	@NotNull(message = "Employee Id is required")
	private Integer employeeId;

	@NotNull(message = "From date is required")
	private LocalDate fromDate;

	// @NotBlank(message = "isActive is required")
	// @Size(max = 1)
	// private String isActive;

	// @NotNull(message = "Created By is required")
	private Integer createdBy;

	private Integer modifiedBy;

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
	 * @return the fromDate
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the isActive //
	 */
	// public String getIsActive() {
	// return isActive;
	// }
	//
	// /**
	// * @param isActive
	// * the isActive to set
	// */
	// public void setIsActive(String isActive) {
	// this.isActive = isActive;
	// }

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

	// getters & setters
}