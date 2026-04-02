package com.doritech.CustomerService.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

public class CustomerAllocationRequest {

	@NotNull(message = "{customer.allocation.customerId.required}")
	private Integer customerId;

	@NotNull(message = "{customer.allocation.employeeId.required}")
	private Integer employeeId;

	@NotNull(message = "{customer.allocation.fromDate.required}")
	@PastOrPresent(message = "{customer.allocation.fromDate.invalid}")
	private LocalDate fromDate;

	// @NotNull(message = "{customer.allocation.createdBy.required}")
	private Integer createdBy;

	@NotNull(message = "{stock.isActive.required}")
	@Pattern(regexp = "^[YN]$", message = "{stock.isActive.invalid}")
	private String isActive;

	private Integer modifiedBy;

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
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
