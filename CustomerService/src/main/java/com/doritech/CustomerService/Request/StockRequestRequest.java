package com.doritech.CustomerService.Request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class StockRequestRequest {

	@NotNull(message = "{stock.sourceSiteId.notNull}")
	private Integer sourceSiteId;

	@NotNull(message = "{stock.requestedSiteId.notNull}")
	private Integer requestedSiteId;

	private Integer approvedBy;

	private LocalDate approvalDate;

	private Integer modifiedBy;

	// @NotNull(message = "{stock.createdBy.notNull}")
	private Integer createdBy;

	@NotEmpty(message = "{stock.items.notEmpty}")
	@Valid
	private List<StockRequestDetailRequest> items;

	public Integer getSourceSiteId() {
		return sourceSiteId;
	}

	public void setSourceSiteId(Integer sourceSiteId) {
		this.sourceSiteId = sourceSiteId;
	}

	public Integer getRequestedSiteId() {
		return requestedSiteId;
	}

	public void setRequestedSiteId(Integer requestedSiteId) {
		this.requestedSiteId = requestedSiteId;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDate getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public List<StockRequestDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<StockRequestDetailRequest> items) {
		this.items = items;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

}