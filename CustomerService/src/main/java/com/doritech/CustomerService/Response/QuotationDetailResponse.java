package com.doritech.CustomerService.Response;

import java.math.BigDecimal;
import java.util.List;

public class QuotationDetailResponse {

    private Integer quotationDetailId;

    private Integer itemId;

    private Double itemPrice;

    private Double gstRate;
    private Integer quotationId;

    private String parentItemName;
    
    private Integer siteId;
    private Integer parentItemId;
    
    private String communicationMode;
    private BigDecimal qty;
    private String remarks;

    private String isActive;

    private String itemName;
    private String siteName;
    private List<QuotationDetailResponse> children;
	public Integer getQuotationDetailId() {
		return quotationDetailId;
	}

	public void setQuotationDetailId(Integer quotationDetailId) {
		this.quotationDetailId = quotationDetailId;
	}

	public Integer getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public Integer getItemId() {
		return itemId;
	}

	public Integer getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Integer quotationId) {
		this.quotationId = quotationId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemName() {
		return itemName;
	}

	public List<QuotationDetailResponse> getChildren() {
		return children;
	}

	public void setChildren(List<QuotationDetailResponse> children) {
		this.children = children;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Double getGstRate() {
		return gstRate;
	}

	public void setGstRate(Double gstRate) {
		this.gstRate = gstRate;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getParentItemName() {
		return parentItemName;
	}

	public void setParentItemName(String parentItemName) {
		this.parentItemName = parentItemName;
	}

	public String getCommunicationMode() {
		return communicationMode;
	}

	public void setCommunicationMode(String communicationMode) {
		this.communicationMode = communicationMode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
    

}