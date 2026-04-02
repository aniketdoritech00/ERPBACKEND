package com.doritech.CustomerService.Response;

import java.time.LocalDate;
import java.util.List;

public class StockRequestResponse {

	private Integer stockRequestId;
	private Integer sourceSiteId;
	private Integer requestedSiteId;
	private String status;
	private Integer approvedBy;
	private LocalDate approvalDate;
	private String sourceSiteName;
	private String requestedSiteName;
	private String sourceSiteCode;
	private String requestedSiteCode;

	private List<StockRequestDetailResponse> items;

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

	public List<StockRequestDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<StockRequestDetailResponse> items) {
		this.items = items;
	}

	public String getSourceSiteName() {
		return sourceSiteName;
	}

	public void setSourceSiteName(String sourceSiteName) {
		this.sourceSiteName = sourceSiteName;
	}

	public String getRequestedSiteName() {
		return requestedSiteName;
	}

	public void setRequestedSiteName(String requestedSiteName) {
		this.requestedSiteName = requestedSiteName;
	}

	public String getSourceSiteCode() {
		return sourceSiteCode;
	}

	public void setSourceSiteCode(String sourceSiteCode) {
		this.sourceSiteCode = sourceSiteCode;
	}

	public String getRequestedSiteCode() {
		return requestedSiteCode;
	}

	public void setRequestedSiteCode(String requestedSiteCode) {
		this.requestedSiteCode = requestedSiteCode;
	}

}