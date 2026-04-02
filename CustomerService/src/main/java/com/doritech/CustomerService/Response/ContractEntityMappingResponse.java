package com.doritech.CustomerService.Response;

import java.time.LocalDateTime;

public class ContractEntityMappingResponse {

    private Integer mappingId;
    private Integer contractId;
    private Integer customerId;
    private Integer siteId;
    private Integer minNoVisits;
    private Integer visitsFrequency;
    private String visitsPaid;
    private String customerName;
    private String siteName;
    private String isActive;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private Integer createdBy;
    private Integer modifiedBy;
	public Integer getMappingId() {
		return mappingId;
	}
	public void setMappingId(Integer mappingId) {
		this.mappingId = mappingId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
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