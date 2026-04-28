package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contract_installation_details")
public class ContractInstallationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "installation_id")
	private Long installationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", nullable = false)
	private ContractMaster contract;

	@Column(name = "sales_order_number", length = 50, nullable = false)
	private String salesOrderNumber;

	@Column(name = "sales_order_date")
	private LocalDate salesOrderDate;

	@Column(name = "is_material_required")
	private Boolean isMaterialRequired;

	@Column(name = "movement_status", length = 30)
	private String movementStatus;

	@Column(name = "docket_number", length = 50)
	private String docketNumber;

	@Column(name = "brf_number", length = 50)
	private String brfNumber;

	@Column(name = "logistics_remarks", length = 500)
	private String logisticsRemarks;

	@Column(name = "bill_number", length = 50)
	private String billNumber;

	@Column(name = "bill_date")
	private LocalDate billDate;

	@Column(name = "bill_amount", precision = 12, scale = 2)
	private BigDecimal billAmount;

	@Column(name = "is_bill_submitted")
	private Boolean isBillSubmitted;

	@Column(name = "sales_order_created_by", length = 50)
	private String salesOrderCreatedBy;

	@Column(name = "sales_order_created_at")
	private LocalDateTime salesOrderCreatedAt;

	@Column(name = "material_requested_by", length = 50)
	private String materialRequestedBy;

	@Column(name = "material_requested_at")
	private LocalDateTime materialRequestedAt;

	@Column(name = "store_processed_by", length = 50)
	private String storeProcessedBy;

	@Column(name = "store_processed_at")
	private LocalDateTime storeProcessedAt;


	@Column(name = "logistics_processed_by", length = 50)
	private String logisticsProcessedBy;

	@Column(name = "logistics_processed_at")
	private LocalDateTime logisticsProcessedAt;

	@Column(name = "bill_processed_by", length = 50)
	private String billProcessedBy;

	@Column(name = "bill_processed_at")
	private LocalDateTime billProcessedAt;

	@Column(name = "is_active", length = 1, nullable = false)
	private String isActive;

	public Long getInstallationId() {
		return installationId;
	}

	public ContractMaster getContract() {
		return contract;
	}

	public void setContract(ContractMaster contract) {
		this.contract = contract;
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