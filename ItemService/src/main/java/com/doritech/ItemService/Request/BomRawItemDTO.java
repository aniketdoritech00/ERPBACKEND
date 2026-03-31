package com.doritech.ItemService.Request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class BomRawItemDTO {

	@NotNull(message = "{bom.raw.item.id.required}")
	private Integer rawItemId;

	@NotNull(message = "{bom.raw.quantity.required}")
	@DecimalMin(value = "0.0", inclusive = false, message = "{bom.raw.quantity.invalid}")
	private BigDecimal quantity;

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

	// getter setter
}