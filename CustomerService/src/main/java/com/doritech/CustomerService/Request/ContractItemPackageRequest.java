package com.doritech.CustomerService.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContractItemPackageRequest {

	@NotNull(message = "{contract.mapping.id.required}")
	private Integer contractMappingId;

	@NotNull(message = "{mapped.item.id.required}")
	private Integer mappedItemId;

	@NotBlank(message = "{is.active.required}")
	@Size(max = 1, message = "{is.active.size}")
	private String isActive;
	private Integer packageId;
	// @NotNull(message = "{created.by.required}")
	private Integer createdBy;
	private Integer modifiedBy;

	public Integer getContractMappingId() {
		return contractMappingId;
	}

	public void setContractMappingId(Integer contractMappingId) {
		this.contractMappingId = contractMappingId;
	}

	public Integer getMappedItemId() {
		return mappedItemId;
	}

	public void setMappedItemId(Integer mappedItemId) {
		this.mappedItemId = mappedItemId;
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

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
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