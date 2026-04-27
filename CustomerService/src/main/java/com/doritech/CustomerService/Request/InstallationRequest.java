package com.doritech.CustomerService.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InstallationRequest {

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotNull(message = "Assignment Id is required")
    private Integer assignmentId;

    @NotBlank(message = "Sales Order is required")
    private String salesOrder;

    @NotBlank(message = "DC/Adv Bill is required")
    private String dcAdvBill;

    @NotNull(message = "Wiring status is required")
    private Boolean wiring;

    @NotNull(message = "Mounting status is required")
    private Boolean mounting;

    @NotNull(message = "Commissioning status is required")
    private Boolean commissioning;

    @NotNull(message = "Final Demo status is required")
    private Boolean finalDemo;

    @Min(value = 0, message = "PVC Pipe cannot be negative")
    private Integer pvcPipe;

    @Min(value = 0, message = "PVC Bend cannot be negative")
    private Integer pvcBend;

    private Integer externalAssistant;

    @Size(min = 12, max = 12, message = "Aadhar must be 12 digits")
    private String externalHelperAadhar;

    @NotBlank(message = "Report No is required")
    private String reportNo;

    private String remarks;

    // HDD fields (optional validation)
    @Min(0) private Integer size1TB;
    @Min(0) private Integer size2TB;
    @Min(0) private Integer size4TB;
    @Min(0) private Integer size6TB;
    @Min(0) private Integer size8TB;
    @Min(0) private Integer size10TB;

    
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
    public Integer getSize1TB() {
        return size1TB;
    }
    public void setSize1TB(Integer size1tb) {
        size1TB = size1tb;
    }
    public Integer getSize2TB() {
        return size2TB;
    }
    public void setSize2TB(Integer size2tb) {
        size2TB = size2tb;
    }
    public Integer getSize4TB() {
        return size4TB;
    }
    public void setSize4TB(Integer size4tb) {
        size4TB = size4tb;
    }
    public Integer getSize6TB() {
        return size6TB;
    }
    public void setSize6TB(Integer size6tb) {
        size6TB = size6tb;
    }
    public Integer getSize8TB() {
        return size8TB;
    }
    public void setSize8TB(Integer size8tb) {
        size8TB = size8tb;
    }
    public Integer getSize10TB() {
        return size10TB;
    }
    public void setSize10TB(Integer size10tb) {
        size10TB = size10tb;
    }
    public Integer getAssignmentId() {
        return assignmentId;
    }
    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    
}
