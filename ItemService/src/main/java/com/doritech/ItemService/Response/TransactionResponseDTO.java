package com.doritech.ItemService.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {

	private Integer transactionId;
	private Integer batchId;

	private Integer sourceGodownId;
	private String sourceGodownName;

	private Integer destinationGodownId;
	private String destinationGodownName;

	private Integer itemId;
	private String itemName;

	private BigDecimal quantity;
	private String transactionType;
	private String status;
	private LocalDateTime transactionDate;
	private String remarks;
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
	 * @return the batchId
	 */
	public Integer getBatchId() {
		return batchId;
	}
	/**
	 * @param batchId
	 *            the batchId to set
	 */
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	/**
	 * @return the sourceGodownId
	 */
	public Integer getSourceGodownId() {
		return sourceGodownId;
	}
	/**
	 * @param sourceGodownId
	 *            the sourceGodownId to set
	 */
	public void setSourceGodownId(Integer sourceGodownId) {
		this.sourceGodownId = sourceGodownId;
	}
	/**
	 * @return the sourceGodownName
	 */
	public String getSourceGodownName() {
		return sourceGodownName;
	}
	/**
	 * @param sourceGodownName
	 *            the sourceGodownName to set
	 */
	public void setSourceGodownName(String sourceGodownName) {
		this.sourceGodownName = sourceGodownName;
	}
	/**
	 * @return the destinationGodownId
	 */
	public Integer getDestinationGodownId() {
		return destinationGodownId;
	}
	/**
	 * @param destinationGodownId
	 *            the destinationGodownId to set
	 */
	public void setDestinationGodownId(Integer destinationGodownId) {
		this.destinationGodownId = destinationGodownId;
	}
	/**
	 * @return the destinationGodownName
	 */
	public String getDestinationGodownName() {
		return destinationGodownName;
	}
	/**
	 * @param destinationGodownName
	 *            the destinationGodownName to set
	 */
	public void setDestinationGodownName(String destinationGodownName) {
		this.destinationGodownName = destinationGodownName;
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

}