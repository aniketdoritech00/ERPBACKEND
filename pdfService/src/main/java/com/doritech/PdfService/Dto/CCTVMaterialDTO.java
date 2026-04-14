package com.doritech.PdfService.Dto;

public class CCTVMaterialDTO {

	public enum MaterialType {
		REPLACED, REQUIRED
	}

	private String itemDescription;
	private Integer quantity;
	private Boolean isChargeable;
	private MaterialType materialType;

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean getIsChargeable() {
		return isChargeable;
	}

	public void setIsChargeable(Boolean isChargeable) {
		this.isChargeable = isChargeable;
	}

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}
}
