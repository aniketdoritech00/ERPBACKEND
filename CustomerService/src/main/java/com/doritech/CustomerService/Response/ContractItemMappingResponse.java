package com.doritech.CustomerService.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ContractItemMappingResponse {

	private Integer contractMappingId;
	private Integer contractId;
	private Integer itemId;
	private BigDecimal quantity;
	private BigDecimal unitPrice;
	private Integer buyBackItemId;
	private BigDecimal buyBackUnitPrice;
	private String mandatoryQuotation;
	private Integer warrantyPeriod;
	private BigDecimal amcRate;
	private String approvalRequired;
	private String isActive;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private Integer createdBy;
	private Integer modifiedBy;
	private String itemName;
	private String itemCode;

	public Integer getContractMappingId() {
		return contractMappingId;
	}

	public void setContractMappingId(Integer contractMappingId) {
		this.contractMappingId = contractMappingId;
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

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getMandatoryQuotation() {
		return mandatoryQuotation;
	}

	public void setMandatoryQuotation(String mandatoryQuotation) {
		this.mandatoryQuotation = mandatoryQuotation;
	}

	public Integer getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public void setWarrantyPeriod(Integer warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public BigDecimal getAmcRate() {
		return amcRate;
	}

	public void setAmcRate(BigDecimal amcRate) {
		this.amcRate = amcRate;
	}

	public String getApprovalRequired() {
		return approvalRequired;
	}

	public void setApprovalRequired(String approvalRequired) {
		this.approvalRequired = approvalRequired;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public Integer getBuyBackItemId() {
		return buyBackItemId;
	}

	public void setBuyBackItemId(Integer buyBackItemId) {
		this.buyBackItemId = buyBackItemId;
	}

	public BigDecimal getBuyBackUnitPrice() {
		return buyBackUnitPrice;
	}

	public void setBuyBackUnitPrice(BigDecimal buyBackUnitPrice) {
		this.buyBackUnitPrice = buyBackUnitPrice;
	}

}