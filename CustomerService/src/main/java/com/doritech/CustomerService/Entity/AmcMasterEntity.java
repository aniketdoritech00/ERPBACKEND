package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "amc_master")
public class AmcMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer amcId;

	@Column(name = "amc_number", length = 100, nullable = false, unique = true)
	private String amcNumber;

	@Column(name = "amc_name", length = 255, nullable = false)
	private String amcName;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerMasterEntity customer;

	@Column(name = "amc_start_date", nullable = false)
	private LocalDate amcStartDate;

	@Column(name = "amc_end_date", nullable = false)
	private LocalDate amcEndDate;

	@Column(name = "amc_value", precision = 15, scale = 2)
	private BigDecimal amcValue;

	@Column(name = "currency", length = 10)
	private String currency;

	@Column(name = "amc_category", length = 50)
	private String amcCategory;

	@Column(name = "amc_status", length = 50)
	private String amcStatus;

	@Column(name = "renewal_reminder_days")
	private Integer renewalReminderDays;

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

	// ✅ Automatically called before update
	@PreUpdate
	public void preUpdate() {
		this.modifiedOn = LocalDateTime.now();
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
	 * @return the amcId
	 */
	public Integer getAmcId() {
		return amcId;
	}

	/**
	 * @param amcId
	 *            the amcId to set
	 */
	public void setAmcId(Integer amcId) {
		this.amcId = amcId;
	}

	/**
	 * @return the amcNumber
	 */
	public String getAmcNumber() {
		return amcNumber;
	}

	/**
	 * @param amcNumber
	 *            the amcNumber to set
	 */
	public void setAmcNumber(String amcNumber) {
		this.amcNumber = amcNumber;
	}

	/**
	 * @return the amcName
	 */
	public String getAmcName() {
		return amcName;
	}

	/**
	 * @param amcName
	 *            the amcName to set
	 */
	public void setAmcName(String amcName) {
		this.amcName = amcName;
	}

	/**
	 * @return the customer
	 */
	public CustomerMasterEntity getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(CustomerMasterEntity customer) {
		this.customer = customer;
	}

	/**
	 * @return the amcStartDate
	 */
	public LocalDate getAmcStartDate() {
		return amcStartDate;
	}

	/**
	 * @param amcStartDate
	 *            the amcStartDate to set
	 */
	public void setAmcStartDate(LocalDate amcStartDate) {
		this.amcStartDate = amcStartDate;
	}

	/**
	 * @return the amcEndDate
	 */
	public LocalDate getAmcEndDate() {
		return amcEndDate;
	}

	/**
	 * @param amcEndDate
	 *            the amcEndDate to set
	 */
	public void setAmcEndDate(LocalDate amcEndDate) {
		this.amcEndDate = amcEndDate;
	}

	/**
	 * @return the amcValue
	 */
	public BigDecimal getAmcValue() {
		return amcValue;
	}

	/**
	 * @param amcValue
	 *            the amcValue to set
	 */
	public void setAmcValue(BigDecimal amcValue) {
		this.amcValue = amcValue;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the amcCategory
	 */
	public String getAmcCategory() {
		return amcCategory;
	}

	/**
	 * @param amcCategory
	 *            the amcCategory to set
	 */
	public void setAmcCategory(String amcCategory) {
		this.amcCategory = amcCategory;
	}

	/**
	 * @return the amcStatus
	 */
	public String getAmcStatus() {
		return amcStatus;
	}

	/**
	 * @param amcStatus
	 *            the amcStatus to set
	 */
	public void setAmcStatus(String amcStatus) {
		this.amcStatus = amcStatus;
	}

	/**
	 * @return the renewalReminderDays
	 */
	public Integer getRenewalReminderDays() {
		return renewalReminderDays;
	}

	/**
	 * @param renewalReminderDays
	 *            the renewalReminderDays to set
	 */
	public void setRenewalReminderDays(Integer renewalReminderDays) {
		this.renewalReminderDays = renewalReminderDays;
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