package com.doritech.ItemService.Request;

import java.util.List;

import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class GodownTransferRequestDTO {

	@NotNull(message = "{transaction.source.required}", groups = OnCreate.class)
	private Integer sourceGodownId;

	@NotNull(message = "{transaction.destination.required}", groups = OnCreate.class)
	private Integer destinationGodownId;

	@NotNull(message = "{transaction.type.required}", groups = OnCreate.class)
	@Pattern(regexp = "^[PTS]$", message = "{transaction.type.invalid}", groups = OnCreate.class)
	private String transactionType;

	@Size(max = 500, message = "{transaction.remarks.max}", groups = OnCreate.class)
	private String remarks;

	private Integer createdBy;

	private Integer modifiedBy;

	@NotEmpty(message = "{transaction.items.required}", groups = OnCreate.class)
	@Valid
	private List<GodownTransferItemDTO> items;

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

	/**
	 * @return the items
	 */
	public List<GodownTransferItemDTO> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<GodownTransferItemDTO> items) {
		this.items = items;
	}

}