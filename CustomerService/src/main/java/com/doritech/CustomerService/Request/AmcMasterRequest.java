package com.doritech.CustomerService.Request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AmcMasterRequest {

	@NotBlank(message = "{amc.number.required}", groups = OnCreate.class)
	@Size(max = 100, message = "{amc.number.size}", groups = OnCreate.class)
	private String amcNumber;

	@NotBlank(message = "{amc.name.required}", groups = OnCreate.class)
	@Size(max = 255, message = "{amc.name.size}", groups = OnCreate.class)
	private String amcName;

	@NotNull(message = "{customer.id.required}", groups = OnCreate.class)
	private Integer customerId;

	@NotNull(message = "{amc.start.date.required}", groups = OnCreate.class)
	private LocalDate amcStartDate;

	@NotNull(message = "{amc.end.date.required}", groups = OnCreate.class)
	private LocalDate amcEndDate;

	@DecimalMin(value = "0.0", inclusive = true, message = "{amc.value.positive}", groups = OnUpdate.class)
	private BigDecimal amcValue;

	@Size(max = 10, message = "{currency.size}", groups = OnUpdate.class)
	private String currency;

	@Size(max = 50, message = "{amc.category.size}", groups = OnUpdate.class)
	private String amcCategory;

	@Size(max = 50, message = "{amc.status.size}", groups = OnUpdate.class)
	private String amcStatus;

	@Min(value = 0, message = "{renewal.days.min}", groups = OnUpdate.class)
	private Integer renewalReminderDays;

	private Integer createdBy;

	private Integer modifiedBy;

	// Getters & Setters

	public String getAmcNumber() {
		return amcNumber;
	}

	public void setAmcNumber(String amcNumber) {
		this.amcNumber = amcNumber;
	}

	public String getAmcName() {
		return amcName;
	}

	public void setAmcName(String amcName) {
		this.amcName = amcName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public LocalDate getAmcStartDate() {
		return amcStartDate;
	}

	public void setAmcStartDate(LocalDate amcStartDate) {
		this.amcStartDate = amcStartDate;
	}

	public LocalDate getAmcEndDate() {
		return amcEndDate;
	}

	public void setAmcEndDate(LocalDate amcEndDate) {
		this.amcEndDate = amcEndDate;
	}

	public BigDecimal getAmcValue() {
		return amcValue;
	}

	public void setAmcValue(BigDecimal amcValue) {
		this.amcValue = amcValue;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getAmcCategory() {
		return amcCategory;
	}

	public void setAmcCategory(String amcCategory) {
		this.amcCategory = amcCategory;
	}

	public String getAmcStatus() {
		return amcStatus;
	}

	public void setAmcStatus(String amcStatus) {
		this.amcStatus = amcStatus;
	}

	public Integer getRenewalReminderDays() {
		return renewalReminderDays;
	}

	public void setRenewalReminderDays(Integer renewalReminderDays) {
		this.renewalReminderDays = renewalReminderDays;
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