package com.doritech.CustomerService.Response;

import java.util.List;

public class InstallationResponse {

    private Long id;
    private String branch;
    private Integer assignmentId;
    private String salesOrder;

    private Boolean wiring;
    private Boolean mounting;
    private Boolean commissioning;
    private Boolean finalDemo;

    private Integer pvcPipe;
    private Integer pvcBend;
    private Integer externalAssistant;
    private String externalHelperAadhar;

    private String reportNo;
    private String remarks;

    private List<String> hddImages;
    private List<String> deviceImages;
    private List<String> serviceImages;

    


    
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
    public List<String> getHddImages() {
        return hddImages;
    }
    public void setHddImages(List<String> hddImages) {
        this.hddImages = hddImages;
    }
    public List<String> getDeviceImages() {
        return deviceImages;
    }
    public void setDeviceImages(List<String> deviceImages) {
        this.deviceImages = deviceImages;
    }
    public List<String> getServiceImages() {
        return serviceImages;
    }
    public void setServiceImages(List<String> serviceImages) {
        this.serviceImages = serviceImages;
    }
    public Integer getAssignmentId() {
        return assignmentId;
    }
    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    
}
