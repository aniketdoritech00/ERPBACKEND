package com.doritech.PdfService.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fas_hardware_item")
public class FASHardwareItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number")
    private Integer serialNumber;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "zone_label")
    private String zoneLabel;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "working_status")
    private String workingStatus;

    @Column(name = "location")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fas_branch_info_id")
    private FASBranchInfo fasBranchInfo;

    public FASHardwareItem() {
    }

    public FASHardwareItem(Long id, Integer serialNumber, String itemName, String zoneLabel,
                           Integer quantity, String workingStatus, String location,
                           FASBranchInfo fasBranchInfo) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.itemName = itemName;
        this.zoneLabel = zoneLabel;
        this.quantity = quantity;
        this.workingStatus = workingStatus;
        this.location = location;
        this.fasBranchInfo = fasBranchInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public FASBranchInfo getFasBranchInfo() {
        return fasBranchInfo;
    }

    public void setFasBranchInfo(FASBranchInfo fasBranchInfo) {
        this.fasBranchInfo = fasBranchInfo;
    }
}