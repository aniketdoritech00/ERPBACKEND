package com.doritech.PdfService.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cctv_hdd_status")
public class CCTVHddStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cctv_branch_info_id", nullable = false)
    private CCTVBranchInfo cctvBranchInfo;

    @Column(name = "item_name", length = 255)
    private String itemName;

    @Column(name = "capacity", length = 255)
    private String capacity;

    @Column(name = "working_status", length = 20)
    private String workingStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CCTVBranchInfo getCctvBranchInfo() {
        return cctvBranchInfo;
    }

    public void setCctvBranchInfo(CCTVBranchInfo cctvBranchInfo) {
        this.cctvBranchInfo = cctvBranchInfo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(String workingStatus) {
        this.workingStatus = workingStatus;
    }
}
