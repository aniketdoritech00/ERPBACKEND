package com.doritech.CustomerService.Entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "installation")
public class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String branch;
    private Integer assignmentId;
    private String salesOrder;
    private String dcAdvBill;

    // Steps
    private Boolean wiring;
    private Boolean mounting;
    private Boolean commissioning;
    private Boolean finalDemo;

    // Expense
    private Integer pvcPipe;
    private Integer pvcBend;
    private Integer externalAssistant;
    private String externalHelperAadhar;

    // Final Info
    private String reportNo;
    private String remarks;

    private Integer createdBy;
    private LocalDateTime createdAt;

    private Integer modifiedBy;
    private LocalDateTime modifiedAt;

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }


    @OneToMany(mappedBy = "installation", cascade = CascadeType.ALL)
    private List<InstallationImage> images;

    

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(String salesOrder) {
        this.salesOrder = salesOrder;
    }

    public String getDcAdvBill() {
        return dcAdvBill;
    }

    public void setDcAdvBill(String dcAdvBill) {
        this.dcAdvBill = dcAdvBill;
    }

    public Boolean getWiring() {
        return wiring;
    }

    public void setWiring(Boolean wiring) {
        this.wiring = wiring;
    }

    public Boolean getMounting() {
        return mounting;
    }

    public void setMounting(Boolean mounting) {
        this.mounting = mounting;
    }

    public Boolean getCommissioning() {
        return commissioning;
    }

    public void setCommissioning(Boolean commissioning) {
        this.commissioning = commissioning;
    }

    public Boolean getFinalDemo() {
        return finalDemo;
    }

    public void setFinalDemo(Boolean finalDemo) {
        this.finalDemo = finalDemo;
    }

    public Integer getPvcPipe() {
        return pvcPipe;
    }

    public void setPvcPipe(Integer pvcPipe) {
        this.pvcPipe = pvcPipe;
    }

    public Integer getPvcBend() {
        return pvcBend;
    }

    public void setPvcBend(Integer pvcBend) {
        this.pvcBend = pvcBend;
    }

    public Integer getExternalAssistant() {
        return externalAssistant;
    }

    public void setExternalAssistant(Integer externalAssistant) {
        this.externalAssistant = externalAssistant;
    }

    public String getExternalHelperAadhar() {
        return externalHelperAadhar;
    }

    public void setExternalHelperAadhar(String externalHelperAadhar) {
        this.externalHelperAadhar = externalHelperAadhar;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<InstallationImage> getImages() {
        return images;
    }

    public void setImages(List<InstallationImage> images) {
        this.images = images;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    
}