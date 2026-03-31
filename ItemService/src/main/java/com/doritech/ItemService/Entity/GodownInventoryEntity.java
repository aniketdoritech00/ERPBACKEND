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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "godown_inventory", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"godown_id", "item_id"})})
public class GodownInventoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inventoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "godown_id", nullable = false)
	private GodownMasterEntity godown;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private ItemMasterEntity item;

	@Column(precision = 10, scale = 3)
	private BigDecimal reorderLevel;

	@Column(precision = 10, scale = 3)
	private BigDecimal reorderQuantity;

	private LocalDateTime lastUpdated;

	private Integer createdBy;
	private Integer modifiedBy;

	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;

	/**
	 * @return the inventoryId
	 */
	public Integer getInventoryId() {
		return inventoryId;
	}
	/**
	 * @param inventoryId
	 *            the inventoryId to set
	 */
	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}
	/**
	 * @return the godown
	 */
	public GodownMasterEntity getGodown() {
		return godown;
	}
	/**
	 * @param godown
	 *            the godown to set
	 */
	public void setGodown(GodownMasterEntity godown) {
		this.godown = godown;
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
	 * @return the lastUpdated
	 */
	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}
	/**
	 * @param lastUpdated
	 *            the lastUpdated to set
	 */
	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
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