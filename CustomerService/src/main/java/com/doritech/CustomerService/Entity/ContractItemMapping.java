package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "contract_item_mapping")
public class ContractItemMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contract_mapping_id")
	private Integer contractMappingId;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", nullable = false)
	private ContractMaster contract;

	@Column(name = "item_id", nullable = false)
	private Integer itemId;

	@Column(name = "quantity", precision = 10, scale = 3)
	private BigDecimal quantity;

	@Column(name = "unit_price", precision = 10, scale = 2)
	private BigDecimal unitPrice;

	@Column(name = "mandatory_quotation", length = 1)
	private String mandatoryQuotation;

	@Column(name = "warranty_period")
	private Integer warrantyPeriod;

	@Column(name = "amc_rate", precision = 10, scale = 2)
	private BigDecimal amcRate;

	@Column(name = "approval_required", length = 1)
	private String approvalRequired;

	@Column(name = "is_active", nullable = false, length = 1)
	private String isActive;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	public Integer getContractMappingId() {
		return contractMappingId;
	}

	public void setContractMappingId(Integer contractMappingId) {
		this.contractMappingId = contractMappingId;
	}

	public ContractMaster getContract() {
		return contract;
	}

	public void setContract(ContractMaster contract) {
		this.contract = contract;
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
}