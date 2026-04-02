package com.doritech.CustomerService.Entity;

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
@Table(name = "contract_entity_mapping")
public class ContractEntityMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mapping_id")
	private Integer mappingId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", nullable = false)
	private ContractMaster contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerMasterEntity customer;

	@Column(name = "site_id", nullable = false)
	private Integer siteId;

	@Column(name = "min_no_visits")
	private Integer minNoVisits;

	@Column(name = "visits_frequency")
	private Integer visitsFrequency;

	@Column(name = "visits_paid")
	private String visitsPaid;

	@Column(name = "is_active", nullable = false)
	private String isActive;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@JoinColumn(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	public Integer getMappingId() {
		return mappingId;
	}

	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
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

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getMinNoVisits() {
		return minNoVisits;
	}

	public void setMinNoVisits(Integer minNoVisits) {
		this.minNoVisits = minNoVisits;
	}

	public Integer getVisitsFrequency() {
		return visitsFrequency;
	}

	public void setVisitsFrequency(Integer visitsFrequency) {
		this.visitsFrequency = visitsFrequency;
	}

	public String getVisitsPaid() {
		return visitsPaid;
	}

	public void setVisitsPaid(String visitsPaid) {
		this.visitsPaid = visitsPaid;
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