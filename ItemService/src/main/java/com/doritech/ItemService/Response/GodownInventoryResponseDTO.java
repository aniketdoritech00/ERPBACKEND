package com.doritech.ItemService.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GodownInventoryResponseDTO {

	private Integer inventoryId;

	private Integer godownId;
	private String godownName;

	private Integer itemId;
	private String itemName;
	private String itemCode;
	private String unitOfMeasure;

	private BigDecimal reorderLevel;
	private BigDecimal reorderQuantity;

	private LocalDateTime lastUpdated;

	/**
	 * @return the inventoryId
	 */
	public Integer getInventoryId() {
		return inventoryId;
	}

	/**
	 * @param inventoryId
	 *            the inventoryId to set
	 */
	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}

	/**
	 * @return the godownId
	 */
	public Integer getGodownId() {
		return godownId;
	}

	/**
	 * @param godownId
	 *            the godownId to set
	 */
	public void setGodownId(Integer godownId) {
		this.godownId = godownId;
	}

	/**
	 * @return the godownName
	 */
	public String getGodownName() {
		return godownName;
	}

	/**
	 * @param godownName
	 *            the godownName to set
	 */
	public void setGodownName(String godownName) {
		this.godownName = godownName;
	}

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
	 * @return the reorderLevel
	 */
	public BigDecimal getReorderLevel() {
		return reorderLevel;
	}

	/**
	 * @param reorderLevel
	 *            the reorderLevel to set
	 */
	public void setReorderLevel(BigDecimal reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	/**
	 * @return the reorderQuantity
	 */
	public BigDecimal getReorderQuantity() {
		return reorderQuantity;
	}

	/**
	 * @param reorderQuantity
	 *            the reorderQuantity to set
	 */
	public void setReorderQuantity(BigDecimal reorderQuantity) {
		this.reorderQuantity = reorderQuantity;
	}

	/**
	 * @return the lastUpdated
	 */
	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}