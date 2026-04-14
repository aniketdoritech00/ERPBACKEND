package com.doritech.PdfService.Dto;

public class FASMaterialDTO {

    private String itemDescription;
    private Integer quantity;
    private Boolean isChargeable;
    private MaterialType materialType;
    private String remarks;

    public FASMaterialDTO() {
    }

    public FASMaterialDTO(String itemDescription, Integer quantity, Boolean isChargeable, MaterialType materialType, String remarks) {
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.isChargeable = isChargeable;
        this.materialType = materialType;
        this.remarks = remarks;
    }

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public enum MaterialType {
        REPLACED,
        REQUIRED
    }
}