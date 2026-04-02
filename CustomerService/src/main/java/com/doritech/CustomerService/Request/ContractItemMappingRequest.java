package com.doritech.CustomerService.Request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContractItemMappingRequest {

	private Integer contractMappingId;

	@NotNull(message = "{contract.id.required}")
	private Integer contractId;

	@NotNull(message = "{item.id.required}")
	private Integer itemId;

	private BigDecimal quantity;

	private BigDecimal unitPrice;

	@Size(max = 1, message = "{mandatory.quotation.size}")
	private String mandatoryQuotation;

	private Integer warrantyPeriod;

	private BigDecimal amcRate;

	@Size(max = 1, message = "{approval.required.size}")
	private String approvalRequired;

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

	public Integer getContractMappingId() {
		return contractMappingId;
	}

	public void setContractMappingId(Integer contractMappingId) {
		this.contractMappingId = contractMappingId;
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