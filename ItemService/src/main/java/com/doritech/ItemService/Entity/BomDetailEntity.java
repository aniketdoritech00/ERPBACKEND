package com.doritech.ItemService.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "bom_detail")
public class BomDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bom_detail_id")
	private Integer bomDetailId;

	/* Parent BOM Item */
	@ManyToOne
	@JoinColumn(name = "bom_id", nullable = false)
	private ItemMasterEntity bomItem;

	/* Raw Item */
	@ManyToOne
	@JoinColumn(name = "raw_item_id", nullable = false)
	private ItemMasterEntity rawItem;

	@NotNull(message = "Quantity required")
	@Column(nullable = false, precision = 10, scale = 3)
	private BigDecimal quantity;

	@NotBlank(message = "Active status required")
	@Column(name = "is_active", nullable = false, length = 1)
	private String isActive;

	@NotNull(message = "Created by required")
	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@NotNull(message = "Created date required")
	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

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
	 * @return the bomItem
	 */
	public ItemMasterEntity getBomItem() {
		return bomItem;
	}

	/**
	 * @param bomItem
	 *            the bomItem to set
	 */
	public void setBomItem(ItemMasterEntity bomItem) {
		this.bomItem = bomItem;
	}

	/**
	 * @return the rawItem
	 */
	public ItemMasterEntity getRawItem() {
		return rawItem;
	}

	/**
	 * @param rawItem
	 *            the rawItem to set
	 */
	public void setRawItem(ItemMasterEntity rawItem) {
		this.rawItem = rawItem;
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

}