package com.doritech.CustomerService.Response;

import java.time.LocalDate;

public class DispatchResponse {

	private Integer dispatchId;
	private String deliveryChallanNo;
	private String consignmentNo;
	private String dispatchMode;
	private String dispatchVendor;
	private LocalDate dispatchDate;
	private String remarks;
	private String status;
	private String mrnNo;
	private Integer receivedBy;
	private LocalDate receivedDate;

	public Integer getDispatchId() {
		return dispatchId;
	}

	public void setDispatchId(Integer dispatchId) {
		this.dispatchId = dispatchId;
	}

	public String getDeliveryChallanNo() {
		return deliveryChallanNo;
	}

	public void setDeliveryChallanNo(String deliveryChallanNo) {
		this.deliveryChallanNo = deliveryChallanNo;
	}

	public String getConsignmentNo() {
		return consignmentNo;
	}

	public void setConsignmentNo(String consignmentNo) {
		this.consignmentNo = consignmentNo;
	}

	public String getDispatchMode() {
		return dispatchMode;
	}

	public void setDispatchMode(String dispatchMode) {
		this.dispatchMode = dispatchMode;
	}

	public String getDispatchVendor() {
		return dispatchVendor;
	}

	public void setDispatchVendor(String dispatchVendor) {
		this.dispatchVendor = dispatchVendor;
	}

	public LocalDate getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(LocalDate dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMrnNo() {
		return mrnNo;
	}

	public void setMrnNo(String mrnNo) {
		this.mrnNo = mrnNo;
	}

	public Integer getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(Integer receivedBy) {
		this.receivedBy = receivedBy;
	}

	public LocalDate getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDate receivedDate) {
		this.receivedDate = receivedDate;
	}

}