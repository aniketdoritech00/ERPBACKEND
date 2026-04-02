package com.doritech.CustomerService.Request;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class QuotationMasterRequest {

	@NotBlank(message = "{quotation.code.required}")
	@Size(max = 50, message = "{quotation.code.size}")
	private String quotationCode;

	private Integer contractId;

	@NotNull(message = "{quotation.customer.required}")
	private Integer customerId;

	@NotNull(message = "{quotation.date.required}")
	private Date quotationDate;

	@NotBlank(message = "{quotation.status.required}")
	@Size(max = 2, message = "{quotation.status.size}")
	private String status;

	@Size(max = 100, message = "{quotation.po.size}")
	private String poNo;

	@Size(max = 100, message = "{quotation.sales.order.size}")
	private String salesOrderNo;
	private String remarks;
	@NotBlank(message = "{quotation.active.required}")
	@Size(max = 1, message = "{quotation.active.size}")
	private String isActive;
	@NotBlank(message = "{quotation.communication.mode.required}")

	@Pattern(regexp = "E|H", message = "{quotation.communication.mode.invalid}")
	private String communicationMode;

	// @NotNull(message = "{quotation.createdBy.required}")
	private Integer createdBy;

	private Integer modifiedBy;

	public String getQuotationCode() {
		return quotationCode;
	}

	public void setQuotationCode(String quotationCode) {
		this.quotationCode = quotationCode;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCommunicationMode() {
		return communicationMode;
	}

	public void setCommunicationMode(String communicationMode) {
		this.communicationMode = communicationMode;
	}

	public Date getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(Date quotationDate) {
		this.quotationDate = quotationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getSalesOrderNo() {
		return salesOrderNo;
	}

	public void setSalesOrderNo(String salesOrderNo) {
		this.salesOrderNo = salesOrderNo;
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