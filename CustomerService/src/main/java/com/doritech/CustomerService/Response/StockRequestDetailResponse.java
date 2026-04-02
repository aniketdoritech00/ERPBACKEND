package com.doritech.CustomerService.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StockRequestDetailResponse {

	private Integer stockRequestDetailId;
	private Integer contractId;
	private Integer itemId;
	private BigDecimal requestedQty;
	private BigDecimal approvedQty;
	private String itemName;
	private String contractName;
	private String contractNo;
	private String itemCode;
	private String sourceSiteName;
	private String requestedSiteName;
	private String sourceSiteCode;
	private String requestedSiteCode;

	public Integer getStockRequestDetailId() {
		return stockRequestDetailId;
	}

	public void setStockRequestDetailId(Integer stockRequestDetailId) {
		this.stockRequestDetailId = stockRequestDetailId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getRequestedQty() {
		return requestedQty;
	}

	public void setRequestedQty(BigDecimal requestedQty) {
		this.requestedQty = requestedQty;
	}

	public BigDecimal getApprovedQty() {
		return approvedQty;
	}

	public void setApprovedQty(BigDecimal approvedQty) {
		this.approvedQty = approvedQty;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
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
