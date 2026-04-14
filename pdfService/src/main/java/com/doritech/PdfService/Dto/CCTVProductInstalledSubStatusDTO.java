package com.doritech.PdfService.Dto;

public class CCTVProductInstalledSubStatusDTO {

    private Long id;
    private String itemName;
    private String quantity;
    private String details;
    private String workingStatus;
    private String safetyCabinetStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

    public String getSafetyCabinetStatus() {
        return safetyCabinetStatus;
    }

    public void setSafetyCabinetStatus(String safetyCabinetStatus) {
        this.safetyCabinetStatus = safetyCabinetStatus;
    }
}
