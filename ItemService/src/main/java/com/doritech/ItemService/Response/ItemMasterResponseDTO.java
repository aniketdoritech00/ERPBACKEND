package com.doritech.ItemService.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ItemMasterResponseDTO {

	private Integer itemId;
	private String itemType;
	private String itemName;
	private String itemCode;
	private String itemDescription;
	private Integer vendorId;
	private String category;
	private String unitOfMeasure;
	private BigDecimal basePrice;
	private String hsnCode;
	private BigDecimal gstRate;
	private String makeModel;
	private String isActive;
	private Integer createdBy;
	private Integer modifiedBy;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	/**
	 * @return the itemId
	 */
	public Integer getItemId() {
		return itemId;
	}
	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	/**
	 * @param itemType
	 *            the itemType to set
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
	 * @param itemName
	 *            the itemName to set
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
	 * @param itemCode
	 *            the itemCode to set
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
	 * @param itemDescription
	 *            the itemDescription to set
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
	 * @param vendorId
	 *            the vendorId to set
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
	 * @param category
	 *            the category to set
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
	 * @param unitOfMeasure
	 *            the unitOfMeasure to set
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
	 * @param basePrice
	 *            the basePrice to set
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
	 * @param hsnCode
	 *            the hsnCode to set
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
	 * @param gstRate
	 *            the gstRate to set
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
	 * @param makeModel
	 *            the makeModel to set
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
	 * @param isActive
	 *            the isActive to set
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
	 * @param createdBy
	 *            the createdBy to set
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
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the modifiedOn
	 */
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn
	 *            the modifiedOn to set
	 */
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}