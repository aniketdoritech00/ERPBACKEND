package com.doritech.ItemService.Request;

import java.math.BigDecimal;

import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.validation.constraints.NotNull;

public class GodownInventoryRequestDTO {

	@NotNull(message = "{inventory.godown.required}", groups = OnCreate.class)
	private Integer godownId;

	@NotNull(message = "{inventory.item.required}", groups = OnCreate.class)
	private Integer itemId;

	private BigDecimal reorderLevel;
	private BigDecimal reorderQuantity;

	private Integer createdBy;

	private Integer modifiedBy;

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

}