package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "quotation_detail")
public class QuotationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quotation_detail_id")
    private Integer quotationDetailId;

    @ManyToOne
    @JoinColumn(name = "quotation_id", nullable = false)
    private QuotationMaster quotationMaster;

    @NotNull(message = "Item ID cannot be null")
    @Column(name = "item_id", nullable = false)
    private Integer itemId;
    
    @Column(name = "parent_Item_Id")
    private Integer parentItemId;

    @NotNull(message = "Item price cannot be null")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "item_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal itemPrice;

    @NotNull(message = "GST rate cannot be null")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "gst_rate", precision = 10, scale = 2, nullable = false)
    private BigDecimal gstRate;

    @NotNull(message = "Quantity cannot be null")
    @DecimalMin(value = "0.01", message = "Quantity must be greater than 0")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "qty", precision = 10, scale = 2, nullable = false)
    private BigDecimal qty;

    @NotNull(message = "Site ID cannot be null")
    @Column(name = "site_id", nullable = false)
    private Integer siteId;

    @Pattern(regexp = "Y|N", message = "isActive must be Y or N")
    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private Date modifiedOn;

    @NotNull(message = "Created by cannot be null")
    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    public Integer getQuotationDetailId() {
        return quotationDetailId;
    }

    public void setQuotationDetailId(Integer quotationDetailId) {
        this.quotationDetailId = quotationDetailId;
    }

    public QuotationMaster getQuotationMaster() {
        return quotationMaster;
    }

    public void setQuotationMaster(QuotationMaster quotationMaster) {
        this.quotationMaster = quotationMaster;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getGstRate() {
        return gstRate;
    }

    public void setGstRate(BigDecimal gstRate) {
        this.gstRate = gstRate;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    

    public Integer getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
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