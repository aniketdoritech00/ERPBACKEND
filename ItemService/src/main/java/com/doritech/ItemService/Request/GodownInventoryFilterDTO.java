package com.doritech.ItemService.Request;

import java.math.BigDecimal;

public class GodownInventoryFilterDTO {

	private Integer godownId;
	private Integer itemId;
	private BigDecimal minReorderLevel;
	private BigDecimal maxReorderLevel;
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
	 * @return the minReorderLevel
	 */
	public BigDecimal getMinReorderLevel() {
		return minReorderLevel;
	}
	/**
	 * @param minReorderLevel
	 *            the minReorderLevel to set
	 */
	public void setMinReorderLevel(BigDecimal minReorderLevel) {
		this.minReorderLevel = minReorderLevel;
	}
	/**
	 * @return the maxReorderLevel
	 */
	public BigDecimal getMaxReorderLevel() {
		return maxReorderLevel;
	}
	/**
	 * @param maxReorderLevel
	 *            the maxReorderLevel to set
	 */
	public void setMaxReorderLevel(BigDecimal maxReorderLevel) {
		this.maxReorderLevel = maxReorderLevel;
	}

}