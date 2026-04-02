package com.doritech.CustomerService.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "amc_detail")
public class AmcDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer amcDetailId;

	@ManyToOne
	@JoinColumn(name = "amc_id")
	private AmcMasterEntity amc;

	@Column(name = "item_id", nullable = false)
	private Integer itemId;

	@Column(name = "description", length = 500)
	private String description;

	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdBy;

	private LocalDateTime modifiedOn;

	private Integer modifiedBy;

	@PrePersist
	public void prePersist() {
		this.createdOn = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.modifiedOn = LocalDateTime.now();
	}

	/**
	 * @return the amcDetailId
	 */
	public Integer getAmcDetailId() {
		return amcDetailId;
	}

	/**
	 * @param amcDetailId
	 *            the amcDetailId to set
	 */
	public void setAmcDetailId(Integer amcDetailId) {
		this.amcDetailId = amcDetailId;
	}

	/**
	 * @return the amc
	 */
	public AmcMasterEntity getAmc() {
		return amc;
	}

	/**
	 * @param amc
	 *            the amc to set
	 */
	public void setAmc(AmcMasterEntity amc) {
		this.amc = amc;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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

}