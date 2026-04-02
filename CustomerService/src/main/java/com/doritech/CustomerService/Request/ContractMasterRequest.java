package com.doritech.CustomerService.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContractMasterRequest {

	private Integer contractId;

	@NotBlank(message = "{contract.no.required}")
	@Size(max = 100, message = "{contract.no.size}")
	private String contractNo;

	@NotBlank(message = "{contract.name.required}")
	@Size(max = 255, message = "{contract.name.size}")
	private String contractName;

	@NotNull(message = "{contract.customer.required}")
	private Integer customerId;

	private LocalDate contractStartDate;

	private LocalDate contractEndDate;

	@NotBlank(message = "{contract.status.required}")
	@Size(max = 2, message = "{contract.status.size}")
	private String contractStatus;

	@NotBlank(message = "{contract.type.required}")
	@Size(max = 2, message = "{contract.type.size}")
	private String contractType;

	@NotBlank(message = "{contract.billing.frequency.required}")
	@Size(max = 2, message = "{contract.billing.frequency.size}")
	private String billingFrequency;

	@Size(max = 2, message = "{contract.amc.type.size}")
	private String amcType;

	@Size(max = 500, message = "{contract.term.condition.size}")
	private String termCondition;

	@Size(max = 500, message = "{contract.payment.terms.size}")
	private String paymentTerms;

	@NotBlank(message = "{contract.active.required}")
	@Size(max = 1, message = "{contract.active.size}")
	private String isActive;

	// @NotNull(message = "{contract.created.by.required}")
	private Integer createdBy;

	private Integer modifiedBy;

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public LocalDate getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(LocalDate contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public LocalDate getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(LocalDate contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getBillingFrequency() {
		return billingFrequency;
	}

	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}

	public String getAmcType() {
		return amcType;
	}

	public void setAmcType(String amcType) {
		this.amcType = amcType;
	}

	public String getTermCondition() {
		return termCondition;
	}

	public void setTermCondition(String termCondition) {
		this.termCondition = termCondition;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
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