package com.doritech.CustomerService.Request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AmcDetailRequest {

	@NotNull(message = "{amc.id.required}")
	private Integer amcId;

	@NotNull(message = "{item.id.required}")
	private Integer itemId;

	@NotBlank(message = "{description.required}")
	@Size(max = 500, message = "{description.size}")
	private String description;

	private Integer createdBy;
	private Integer modifiedBy;

	// Getters & Setters

	public Integer getAmcId() {
		return amcId;
	}

	public void setAmcId(Integer amcId) {
		this.amcId = amcId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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