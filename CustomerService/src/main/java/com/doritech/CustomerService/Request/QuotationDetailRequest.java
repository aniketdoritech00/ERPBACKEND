package com.doritech.CustomerService.Request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class QuotationDetailRequest {

	@NotNull(message = "{quotation.item.required}")
	private Integer itemId;
    private Integer quotationDetailId;
	@NotNull(message = "{quotation.item.price.required}")
	@DecimalMin(value = "0.0", inclusive = false, message = "{quotation.item.price.invalid}")
	@Digits(integer = 8, fraction = 2, message = "{quotation.item.price.format}")
	private BigDecimal itemPrice;
	private Integer parentItemId;
	@NotNull(message = "{quotation.gst.required}")
	@DecimalMin(value = "0.0", inclusive = true, message = "{quotation.gst.invalid}")
	@DecimalMax(value = "100.0", message = "{quotation.gst.max}")
	@Digits(integer = 3, fraction = 2, message = "{quotation.gst.format}")
	private BigDecimal gstRate;

	@NotNull(message = "{quotation.site.required}")
	private Integer siteId;

	@NotNull(message = "{quotation.qty.required}")
	@Digits(integer = 8, fraction = 2, message = "{quotation.qty.format}")
	private BigDecimal qty;

	@Size(max = 1000, message = "{quotation.remarks.max}")
	private String remarks;

	@NotBlank(message = "{quotation.detail.active.required}")
	@Pattern(regexp = "Y|N", message = "{quotation.detail.active.invalid}")
	private String isActive;

	private Integer createdBy;

	private Integer modifiedBy;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	

	public Integer getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	public Integer getQuotationDetailId() {
		return quotationDetailId;
	}

	public void setQuotationDetailId(Integer quotationDetailId) {
		this.quotationDetailId = quotationDetailId;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
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

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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