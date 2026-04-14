package com.doritech.PdfService.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fas_dialer_contact")
public class FASDialerContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contact_order")
    private Integer contactOrder;

    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fas_branch_info_id")
    private FASBranchInfo fasBranchInfo;

    public FASDialerContact() {
    }

    public FASDialerContact(Long id, Integer contactOrder, String staffName, String mobileNumber, FASBranchInfo fasBranchInfo) {
        this.id = id;
        this.contactOrder = contactOrder;
        this.staffName = staffName;
        this.mobileNumber = mobileNumber;
        this.fasBranchInfo = fasBranchInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public FASBranchInfo getFasBranchInfo() {
        return fasBranchInfo;
    }

    public void setFasBranchInfo(FASBranchInfo fasBranchInfo) {
        this.fasBranchInfo = fasBranchInfo;
    }
}