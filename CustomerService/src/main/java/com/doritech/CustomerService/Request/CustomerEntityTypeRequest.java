package com.doritech.CustomerService.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerEntityTypeRequest {
	private Integer custEntityId;
	@NotBlank(message = "{entityType.type.notblank}")
	@Size(max = 2, message = "{entityType.type.size}")
	private String entityType;

	@NotBlank(message = "{entityType.isActive.notblank}")
	@Size(max = 1, message = "{entityType.isActive.size}")
	@Pattern(regexp = "Y|N", message = "{entityType.isActive.invalid}")
	private String isActive;
	@NotNull(message = "{contact.customerId.notnull}")
	private Integer customerId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@NotNull(message = "{entityType.createdBy.notnull}")
	private Integer createdBy;

	private Integer modifiedBy;

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
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

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getCustEntityId() {
		return custEntityId;
	}

	public void setCustEntityId(Integer custEntityId) {
		this.custEntityId = custEntityId;
	}

}