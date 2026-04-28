package com.doritech.CustomerService.Request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ContractInstallationRequest {

	private Integer contractId;

	private String salesOrderNumber;
	private LocalDate salesOrderDate;
	private Boolean isMaterialRequired;
	private String movementStatus;

	private String docketNumber;
	private String brfNumber;
	private String logisticsRemarks;

	private String billNumber;
	private LocalDate billDate;
	private BigDecimal billAmount;
	private Boolean isBillSubmitted;

	private String salesOrderCreatedBy;
	private LocalDateTime salesOrderCreatedAt;

	

	private String materialRequestedBy;
	private LocalDateTime materialRequestedAt;

	private String storeProcessedBy;
	private LocalDateTime storeProcessedAt;

	private String logisticsProcessedBy;
	private LocalDateTime logisticsProcessedAt;

	private String billProcessedBy;
	private LocalDateTime billProcessedAt;



	private String isActive;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getSalesOrderNumber() {
		return salesOrderNumber;
	}

	public void setSalesOrderNumber(String salesOrderNumber) {
		this.salesOrderNumber = salesOrderNumber;
	}

	public LocalDate getSalesOrderDate() {
		return salesOrderDate;
	}

	public void setSalesOrderDate(LocalDate salesOrderDate) {
		this.salesOrderDate = salesOrderDate;
	}

	public Boolean getIsMaterialRequired() {
		return isMaterialRequired;
	}

	public void setIsMaterialRequired(Boolean isMaterialRequired) {
		this.isMaterialRequired = isMaterialRequired;
	}

	public String getMovementStatus() {
		return movementStatus;
	}

	public void setMovementStatus(String movementStatus) {
		this.movementStatus = movementStatus;
	}

	public String getDocketNumber() {
		return docketNumber;
	}

	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}

	public String getBrfNumber() {
		return brfNumber;
	}

	public void setBrfNumber(String brfNumber) {
		this.brfNumber = brfNumber;
	}

	public String getLogisticsRemarks() {
		return logisticsRemarks;
	}

	public void setLogisticsRemarks(String logisticsRemarks) {
		this.logisticsRemarks = logisticsRemarks;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public LocalDate getBillDate() {
		return billDate;
	}

	public void setBillDate(LocalDate billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public Boolean getIsBillSubmitted() {
		return isBillSubmitted;
	}

	public void setIsBillSubmitted(Boolean isBillSubmitted) {
		this.isBillSubmitted = isBillSubmitted;
	}

	public String getSalesOrderCreatedBy() {
		return salesOrderCreatedBy;
	}

	public void setSalesOrderCreatedBy(String salesOrderCreatedBy) {
		this.salesOrderCreatedBy = salesOrderCreatedBy;
	}

	public LocalDateTime getSalesOrderCreatedAt() {
		return salesOrderCreatedAt;
	}

	public void setSalesOrderCreatedAt(LocalDateTime salesOrderCreatedAt) {
		this.salesOrderCreatedAt = salesOrderCreatedAt;
	}


	public String getMaterialRequestedBy() {
		return materialRequestedBy;
	}

	public void setMaterialRequestedBy(String materialRequestedBy) {
		this.materialRequestedBy = materialRequestedBy;
	}

	public LocalDateTime getMaterialRequestedAt() {
		return materialRequestedAt;
	}

	public void setMaterialRequestedAt(LocalDateTime materialRequestedAt) {
		this.materialRequestedAt = materialRequestedAt;
	}

	public String getStoreProcessedBy() {
		return storeProcessedBy;
	}

	public void setStoreProcessedBy(String storeProcessedBy) {
		this.storeProcessedBy = storeProcessedBy;
	}

	public LocalDateTime getStoreProcessedAt() {
		return storeProcessedAt;
	}

	public void setStoreProcessedAt(LocalDateTime storeProcessedAt) {
		this.storeProcessedAt = storeProcessedAt;
	}


	public String getLogisticsProcessedBy() {
		return logisticsProcessedBy;
	}

	public void setLogisticsProcessedBy(String logisticsProcessedBy) {
		this.logisticsProcessedBy = logisticsProcessedBy;
	}

	public LocalDateTime getLogisticsProcessedAt() {
		return logisticsProcessedAt;
	}

	public void setLogisticsProcessedAt(LocalDateTime logisticsProcessedAt) {
		this.logisticsProcessedAt = logisticsProcessedAt;
	}

	public String getBillProcessedBy() {
		return billProcessedBy;
	}

	public void setBillProcessedBy(String billProcessedBy) {
		this.billProcessedBy = billProcessedBy;
	}

	public LocalDateTime getBillProcessedAt() {
		return billProcessedAt;
	}

	public void setBillProcessedAt(LocalDateTime billProcessedAt) {
		this.billProcessedAt = billProcessedAt;
	}


	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}