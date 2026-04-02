package com.doritech.CustomerService.Response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AmcMasterResponse {

	private Integer amcId;
	private String amcNumber;
	private String amcName;
	private Integer customerId;
	private LocalDate amcStartDate;
	private LocalDate amcEndDate;
	private BigDecimal amcValue;
	private String currency;
	private String amcStatus;
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
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

}