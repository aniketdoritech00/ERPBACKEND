package com.doritech.ItemService.Request;

import java.math.BigDecimal;

import com.doritech.ItemService.Service.OnCreate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class GodownTransferItemDTO {

	@NotNull(message = "{transaction.item.required}", groups = OnCreate.class)
	private Integer itemId;

	@NotNull(message = "{transaction.quantity.required}", groups = OnCreate.class)
	@DecimalMin(value = "0.001", message = "{transaction.quantity.invalid}", groups = OnCreate.class)
	private BigDecimal quantity;
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

}