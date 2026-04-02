package com.doritech.CustomerService.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public class CustomerBranchAllocationRequest {

	public interface Create {
	}

	public interface Update {
	}

	@NotNull(message = "{customer.id.required}", groups = {Create.class,
			Update.class})
	private Integer customerId;

	@NotNull(message = "{site.id.required}", groups = {Create.class,
			Update.class})
	private Integer siteId;

	@NotNull(message = "{from.date.required}", groups = {Create.class,
			Update.class})
	private LocalDate fromDate;

	@NotNull(message = "{created.by.required}", groups = Create.class)
	private Integer createdBy;

	@NotNull(message = "{modified.by.required}", groups = Update.class)
	private Integer modifiedBy;

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