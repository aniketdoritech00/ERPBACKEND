package com.doritech.CustomerService.Response;

import java.time.LocalDateTime;

public class ContractItemPackageResponse {

	private Integer contractMappingId;

	private Integer packageId;
	private String itemName;
	private String isActive;
	private Integer mappedItemId;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private Integer contractId;
	private String contractName;
	private String contractCode;
	private String mappingItemName;
	private Integer createdBy;
	private Double basePrice;
	private Double qty;
	private Integer modifiedBy;

	public Integer getContractMappingId() {
		return contractMappingId;
	}

	public void setContractMappingId(Integer contractMappingId) {
		this.contractMappingId = contractMappingId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getIsActive() {
		return isActive;
	}

	public Integer getMappedItemId() {
		return mappedItemId;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getMappingItemName() {
		return mappingItemName;
	}

	public void setMappingItemName(String mappingItemName) {
		this.mappingItemName = mappingItemName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public void setMappedItemId(Integer mappedItemId) {
		this.mappedItemId = mappedItemId;
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