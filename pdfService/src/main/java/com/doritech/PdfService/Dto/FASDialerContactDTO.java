package com.doritech.PdfService.Dto;

public class FASDialerContactDTO {

    private Integer contactOrder;
    private String staffName;
    private String mobileNumber;

    public Integer getContactOrder() {
        return contactOrder;
    }

    public void setContactOrder(Integer contactOrder) {
        this.contactOrder = contactOrder;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}