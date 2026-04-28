package com.doritech.CustomerService.Request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class DispatchRequest {

    @NotBlank(message = "{dispatch.deliveryChallanNo.required}")
    @Size(max = 100, message = "{dispatch.deliveryChallanNo.size}")
    private String deliveryChallanNo;

    @Size(max = 50, message = "{dispatch.consignmentNo.size}")
    private String consignmentNo;

    @NotBlank(message = "{dispatch.dispatchMode.required}")
    @Size(max = 2,message = "{dispatch.dispatchMode.size}")
    private String dispatchMode;

    @NotBlank(message = "{dispatch.dispatchVendor.required}")
    @Size(max = 2, message = "{dispatch.dispatchVendor.size}")
    private String dispatchVendor;

    private LocalDate dispatchDate;

    @Size(max = 500, message = "{dispatch.remarks.size}")
    private String remarks;

    private String status;

    @Size(max = 50, message = "{dispatch.mrnNo.size}")
    private String mrnNo;

    private Integer receivedBy;

    private LocalDate receivedDate;

    private Integer createdBy;

    private Integer modifiedBy;

 

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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}