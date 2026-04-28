package com.doritech.CustomerService.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "amc_expense_master")
public class AmcExpenseMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer amcExpenseId;

    @Column(length = 100, nullable = false)
    private String district;

    @Min(0)
    private Integer complaintFootageMultiplier;

    @Min(0)
    private Integer districtBasePrice;

    public Integer getAmcExpenseId() {
        return amcExpenseId;
    }

    public void setAmcExpenseId(Integer amcExpenseId) {
        this.amcExpenseId = amcExpenseId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getComplaintFootageMultiplier() {
        return complaintFootageMultiplier;
    }

    public void setComplaintFootageMultiplier(Integer complaintFootageMultiplier) {
        this.complaintFootageMultiplier = complaintFootageMultiplier;
    }

    public Integer getDistrictBasePrice() {
        return districtBasePrice;
    }

    public void setDistrictBasePrice(Integer districtBasePrice) {
        this.districtBasePrice = districtBasePrice;
    }
}