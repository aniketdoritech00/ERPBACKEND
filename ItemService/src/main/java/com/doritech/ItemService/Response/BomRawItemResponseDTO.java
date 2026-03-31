package com.doritech.ItemService.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BomRawItemResponseDTO {

	private Integer bomDetailId;
	private Integer rawItemId;
	private String rawItemName;
	private BigDecimal quantity;
	private String isActive;
	private Integer createdBy;
	private Integer modifiedBy;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

	/**
	 * @return the rawItemName
	 */
	public String getRawItemName() {
		return rawItemName;
	}
	/**
	 * @param rawItemName
	 *            the rawItemName to set
	 */
	public void setRawItemName(String rawItemName) {
		this.rawItemName = rawItemName;
	}
	/**
	 * @return the bomDetailId
	 */
	public Integer getBomDetailId() {
		return bomDetailId;
	}
	/**
	 * @param bomDetailId
	 *            the bomDetailId to set
	 */
	public void setBomDetailId(Integer bomDetailId) {
		this.bomDetailId = bomDetailId;
	}
	/**
	 * @return the rawItemId
	 */
	public Integer getRawItemId() {
		return rawItemId;
	}
	/**
	 * @param rawItemId
	 *            the rawItemId to set
	 */
	public void setRawItemId(Integer rawItemId) {
		this.rawItemId = rawItemId;
	}
	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
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

	// getters setters
}