package com.doritech.PdfService.Dto;

public class FASHardwareItemDTO {

    private Integer serialNumber;
    private String itemName;
    private String zoneLabel;
    private Integer quantity;
    private String workingStatus;
    private String location;

    public FASHardwareItemDTO() {
    }

    public FASHardwareItemDTO(Integer serialNumber, String itemName, String zoneLabel, Integer quantity, String workingStatus, String location) {
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.zoneLabel = zoneLabel;
        this.quantity = quantity;
        this.workingStatus = workingStatus;
        this.location = location;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getZoneLabel() {
        return zoneLabel;
    }

    public void setZoneLabel(String zoneLabel) {
        this.zoneLabel = zoneLabel;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}