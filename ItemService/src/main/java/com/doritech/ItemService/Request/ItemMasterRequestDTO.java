package com.doritech.ItemService.Request;

import java.math.BigDecimal;

import com.doritech.ItemService.Service.OnCreate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ItemMasterRequestDTO {

	@NotBlank(message = "{item.type.required}", groups = OnCreate.class)
	@Size(max = 2, message = "Category max length is 2", groups = OnCreate.class)
	private String itemType;

	@NotBlank(message = "{item.name.required}", groups = OnCreate.class)
	@Size(max = 100, message = "{item.name.size}")
	private String itemName;

	@NotBlank(message = "{item.code.required}", groups = OnCreate.class)
	@Size(max = 50, message = "{item.code.size}")
	private String itemCode;

	private String itemDescription;
	private Integer vendorId;
	@Size(max = 2, message = "Category max length is 2", groups = OnCreate.class)
	private String category;
	@Size(max = 2, message = "Unit of measure must not exceed 2 characters", groups = OnCreate.class)
	private String unitOfMeasure;
	private BigDecimal basePrice;

	@Pattern(regexp = "\\d{4,8}", message = "{item.hsn.invalid}")
	private String hsnCode;

	@DecimalMin(value = "0.0", message = "{item.gst.invalid}")
	@DecimalMax(value = "100.0", message = "{item.gst.invalid}")
	private BigDecimal gstRate;

	private String makeModel;

	@NotNull(message = "{item.active.required}", groups = OnCreate.class)
	private String isActive;

	private Integer createdBy;

	private Integer modifiedBy;

	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * @return the itemDescription
	 */
	public String getItemDescription() {
		return itemDescription;
	}

	/**
	 * @param itemDescription the itemDescription to set
	 */
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	/**
	 * @return the vendorId
	 */
	public Integer getVendorId() {
		return vendorId;
	}

	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the unitOfMeasure
	 */
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	/**
	 * @param unitOfMeasure the unitOfMeasure to set
	 */
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	/**
	 * @return the basePrice
	 */
	public BigDecimal getBasePrice() {
		return basePrice;
	}

	/**
	 * @param basePrice the basePrice to set
	 */
	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	/**
	 * @return the hsnCode
	 */
	public String getHsnCode() {
		return hsnCode;
	}

	/**
	 * @param hsnCode the hsnCode to set
	 */
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	/**
	 * @return the gstRate
	 */
	public BigDecimal getGstRate() {
		return gstRate;
	}

	/**
	 * @param gstRate the gstRate to set
	 */
	public void setGstRate(BigDecimal gstRate) {
		this.gstRate = gstRate;
	}

	/**
	 * @return the makeModel
	 */
	public String getMakeModel() {
		return makeModel;
	}

	/**
	 * @param makeModel the makeModel to set
	 */
	public void setMakeModel(String makeModel) {
		this.makeModel = makeModel;
	}

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}