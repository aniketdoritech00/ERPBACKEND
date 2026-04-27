package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contract_master")
public class ContractMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contract_id")
	private Integer contractId;

	@Column(name = "contract_no", length = 100, nullable = false, unique = true)
	private String contractNo;

	@Column(name = "contract_name", length = 255, nullable = false)
	private String contractName;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerMasterEntity customer;

	@Column(name = "contract_start_date")
	private LocalDate contractStartDate;

	@Column(name = "contract_end_date")
	private LocalDate contractEndDate;

	@Column(name = "contract_status", length = 2, nullable = false)
	private String contractStatus;

	@Column(name = "contract_type", length = 2, nullable = false)
	private String contractType;

	@Column(name = "billing_frequency", length = 2, nullable = false)
	private String billingFrequency;

	@Column(name = "amc_type", length = 2)
	private String amcType;

	@Column(name = "term_condition", length = 500)
	private String termCondition;

	@Column(name = "payment_terms", length = 500)
	private String paymentTerms;

	@Column(name = "is_active", length = 1, nullable = false)
	private String isActive;
	
    @Column(name = "sales_order_number", nullable = false, unique = true)
    private String salesOrderNumber;

    @Column(name = "sales_order_date")
    private LocalDate salesOrderDate;

    @Column(name = "is_material_required")
    private Boolean isMaterialRequired;

    @Column(name = "movement_status")
    private String movementStatus;

    @Column(name = "docket_number")
    private String docketNumber;

    @Column(name = "brf_number")
    private String brfNumber;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "bill_number")
    private String billNumber;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Column(name = "bill_amount")
    private BigDecimal billAmount;

    @Column(name = "is_bill_submitted")
    private Boolean isBillSubmitted;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

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


	public CustomerMasterEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerMasterEntity customer) {
		this.customer = customer;
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