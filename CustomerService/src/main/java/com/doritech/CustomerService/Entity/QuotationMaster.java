package com.doritech.CustomerService.Entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "quotation_master")
public class QuotationMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quotation_id")
	private Integer quotationId;

	@NotBlank(message = "Quotation code cannot be blank")
	@Size(max = 50)
	@Column(name = "quotation_code", nullable = false, unique = true, length = 50)
	private String quotationCode;

	@ManyToOne
	@JoinColumn(name = "contract_id")
	private ContractMaster contract;

	@NotNull(message = "Customer cannot be null")
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerMasterEntity customer;

	@NotNull(message = "Quotation date cannot be null")
	@Column(name = "quotation_date", nullable = false)
	private LocalDateTime quotationDate;

	@NotBlank(message = "Status cannot be blank")
	@Size(max = 2)
	@Column(name = "status", nullable = false, length = 2)
	private String status;

	@Size(max = 100)
	@Column(name = "po_no", length = 100)
	private String poNo;

	@Size(max = 100)
	@Column(name = "sales_order_no", length = 100)
	private String salesOrderNo;

	@NotBlank(message = "Communication mode cannot be blank")
	@Size(max = 2)
	@Column(name = "communication_mode", nullable = false, length = 2)
	private String communicationMode;

	@Lob
	@Column(name = "remarks")
	private String remarks;

	@Pattern(regexp = "Y|N", message = "isActive must be Y or N")
	@Column(name = "is_active", length = 1)
	private String isActive;

	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@NotNull(message = "Created by cannot be null")
	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@OneToMany(mappedBy = "quotationMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QuotationDetail> quotationDetails;
	
	@PrePersist
	protected void onCreate() {
	    this.createdOn = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
	    this.modifiedOn = LocalDateTime.now();
	}

	public Integer getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Integer quotationId) {
		this.quotationId = quotationId;
	}

	public String getQuotationCode() {
		return quotationCode;
	}

	public void setQuotationCode(String quotationCode) {
		this.quotationCode = quotationCode;
	}

	public ContractMaster getContract() {
		return contract;
	}

	public void setContract(ContractMaster contract) {
		this.contract = contract;
	}

	public CustomerMasterEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerMasterEntity customer) {
		this.customer = customer;
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

	public LocalDateTime getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(LocalDateTime quotationDate) {
		this.quotationDate = quotationDate;
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

	public List<QuotationDetail> getQuotationDetails() {
		return quotationDetails;
	}

	public void setQuotationDetails(List<QuotationDetail> quotationDetails) {
		this.quotationDetails = quotationDetails;
	}
}