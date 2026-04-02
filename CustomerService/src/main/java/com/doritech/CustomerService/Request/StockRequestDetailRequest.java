package com.doritech.CustomerService.Request;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public class StockRequestDetailRequest {

	private Integer stockRequestDetailId;

	private Integer contractId;

	@NotNull(message = "{stock.item.itemId.notNull}")
	private Integer itemId;

	@NotNull(message = "{stock.item.requestedQty.notNull}")
	@Digits(integer = 10, fraction = 2, message = "{stock.item.requestedQty.digits}")
	@DecimalMin(value = "0.01", message = "{stock.item.requestedQty.min}")
	private BigDecimal requestedQty;


	@Digits(integer = 10, fraction = 2, message = "{stock.item.approvedQty.digits}")
	private BigDecimal approvedQty;

	public Integer getStockRequestDetailId() {
		return stockRequestDetailId;
	}

	public void setStockRequestDetailId(Integer stockRequestDetailId) {
		this.stockRequestDetailId = stockRequestDetailId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getRequestedQty() {
		return requestedQty;
	}

	public void setRequestedQty(BigDecimal requestedQty) {
		this.requestedQty = requestedQty;
	}

	public BigDecimal getApprovedQty() {
		return approvedQty;
	}

	public void setApprovedQty(BigDecimal approvedQty) {
		this.approvedQty = approvedQty;
	}

}