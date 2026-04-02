package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_request_detail")
public class StockRequestDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_request_detail_id")
	private Integer stockRequestDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_request_id", nullable = false)
	private StockRequestEntity stockRequest;

	@Column(name = "contract_id")
	private Integer contractId;

	@Column(name = "item_id", nullable = false)
	private Integer itemId;

	@Column(name = "requested_qty", precision = 10, scale = 2, nullable = false)
	private BigDecimal requestedQty;

	@Column(name = "approved_qty", precision = 10, scale = 2)
	private BigDecimal approvedQty;

	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	public Integer getStockRequestDetailId() {
		return stockRequestDetailId;
	}

	public void setStockRequestDetailId(Integer stockRequestDetailId) {
		this.stockRequestDetailId = stockRequestDetailId;
	}

	public StockRequestEntity getStockRequest() {
		return stockRequest;
	}

	public void setStockRequest(StockRequestEntity stockRequest) {
		this.stockRequest = stockRequest;
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

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
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