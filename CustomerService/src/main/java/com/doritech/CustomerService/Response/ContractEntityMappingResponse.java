package com.doritech.CustomerService.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ContractEntityMappingResponse {

	private Integer mappingId;
	private Integer contractId;
	private String contractName;
	private String contractNo;
	private String contractType;
	private Integer customerId;
	private String customerCode;
	private LocalDate contractStartDate;
	private LocalDate contractEndDate;
	private String customerName;
	private Integer siteId;
	private String siteName;
	private String siteCode;
	private String siteDistrictName;
	private String ifsc;
	private Integer faId;
	private String faName;
	private String zoneName;
	private List<String> productList;
	private Integer minNoVisits;
	private Integer visitsFrequency;
	private String visitsPaid;
	private String isActive;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private Integer createdBy;
	private Integer modifiedBy;
	private String billNumber;
	private String salesOrderNumber;
	private String status;
	private String productCategories;

	private String hierarchyLevelName;

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getSiteDistrictName() {
		return siteDistrictName;
	}

	public void setSiteDistrictName(String siteDistrictName) {
		this.siteDistrictName = siteDistrictName;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public List<String> getProductList() {
		return productList;
	}

	public void setProductList(List<String> productList) {
		this.productList = productList;
	}

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

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
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

	public Integer getFaId() {
		return faId;
	}

	public void setFaId(Integer faId) {
		this.faId = faId;
	}

	public String getFaName() {
		return faName;
	}

	public void setFaName(String faName) {
		this.faName = faName;
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

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getSalesOrderNumber() {
		return salesOrderNumber;
	}

	public void setSalesOrderNumber(String salesOrderNumber) {
		this.salesOrderNumber = salesOrderNumber;
	}

	public String getHierarchyLevelName() {
		return hierarchyLevelName;
	}

	public void setHierarchyLevelName(String hierarchyLevelName) {
		this.hierarchyLevelName = hierarchyLevelName;
	}

	public String getProductCategories() {
		return productCategories;
	}

	public void setProductCategories(String productCategories) {
		this.productCategories = productCategories;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}