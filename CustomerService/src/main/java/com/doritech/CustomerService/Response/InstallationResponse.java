package com.doritech.CustomerService.Response;

import java.time.LocalDateTime;
import java.util.List;

public class InstallationResponse {

	private Long id;
	private String branch;
	private Integer assignmentId;
	private String salesOrder;
	private String visitType;

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
	private Integer size1TB;
	private Integer size2TB;
	private Integer size4TB;
	private Integer size6TB;
	private Integer size8TB;
	private Integer size10TB;

	private List<String> hddImages;
	private List<String> deviceImages;
	private List<String> serviceImages;
	
	

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
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
