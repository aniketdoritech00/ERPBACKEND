package com.doritech.ItemService.Entity;

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
@Table(name = "godown_inventory_transaction")
public class GodownInventoryTransactionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transactionId;

	private Integer transactionBatchId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_godown_id")
	private GodownMasterEntity sourceGodown;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_godown_id")
	private GodownMasterEntity destinationGodown;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private ItemMasterEntity item;

	private String transactionType;

	@Column(precision = 10, scale = 3)
	private BigDecimal quantity;

	private LocalDateTime transactionDate;

	private String status;

	private String remarks;

	@Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

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

	/**
	 * @return the transactionId
	 */
	public Integer getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * @return the transactionBatchId
	 */
	public Integer getTransactionBatchId() {
		return transactionBatchId;
	}

	/**
	 * @param transactionBatchId
	 *            the transactionBatchId to set
	 */
	public void setTransactionBatchId(Integer transactionBatchId) {
		this.transactionBatchId = transactionBatchId;
	}

	/**
	 * @return the sourceGodown
	 */
	public GodownMasterEntity getSourceGodown() {
		return sourceGodown;
	}

	/**
	 * @param sourceGodown
	 *            the sourceGodown to set
	 */
	public void setSourceGodown(GodownMasterEntity sourceGodown) {
		this.sourceGodown = sourceGodown;
	}

	/**
	 * @return the destinationGodown
	 */
	public GodownMasterEntity getDestinationGodown() {
		return destinationGodown;
	}

	/**
	 * @param destinationGodown
	 *            the destinationGodown to set
	 */
	public void setDestinationGodown(GodownMasterEntity destinationGodown) {
		this.destinationGodown = destinationGodown;
	}

	/**
	 * @return the item
	 */
	public ItemMasterEntity getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(ItemMasterEntity item) {
		this.item = item;
	}

	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
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
	 * @return the transactionDate
	 */
	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks
	 *            the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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