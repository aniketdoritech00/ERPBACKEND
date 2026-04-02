package com.doritech.CustomerService.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_request")
public class StockRequestEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_request_id")
	private Integer stockRequestId;

	@Column(name = "source_site_id", nullable = false)
	private Integer sourceSiteId;

	@Column(name = "requested_site_id", nullable = false)
	private Integer requestedSiteId;

	@Column(name = "status", length = 2)
	private String status;

	@Column(name = "approved_by")
	private Integer approvedBy;

	@Column(name = "approval_date")
	private LocalDate approvalDate;

	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@OneToMany(mappedBy = "stockRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<StockRequestDetailEntity> items;

	public Integer getStockRequestId() {
		return stockRequestId;
	}

	public void setStockRequestId(Integer stockRequestId) {
		this.stockRequestId = stockRequestId;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
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

	public List<StockRequestDetailEntity> getItems() {
		return items;
	}

	public void setItems(List<StockRequestDetailEntity> items) {
		this.items = items;
	}

}