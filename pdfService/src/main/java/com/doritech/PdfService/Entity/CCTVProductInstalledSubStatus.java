package com.doritech.PdfService.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cctv_product_installed_sub_status")
public class CCTVProductInstalledSubStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_status_id", nullable = false)
    private CCTVProductInstalledStatus parentStatus;

    @Column(name = "item_name", length = 255)
    private String itemName;

    @Column(name = "quantity", length = 50)
    private String quantity;

    @Column(name = "details", length = 255)
    private String details;

    @Column(name = "working_status", length = 10)
    private String workingStatus;

    @Column(name = "safety_cabinet_status", length = 10)
    private String safetyCabinetStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CCTVProductInstalledStatus getParentStatus() {
        return parentStatus;
    }

    public void setParentStatus(CCTVProductInstalledStatus parentStatus) {
        this.parentStatus = parentStatus;
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
