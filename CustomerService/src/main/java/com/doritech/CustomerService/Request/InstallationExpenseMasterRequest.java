package com.doritech.CustomerService.Request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InstallationExpenseMasterRequest {

	private Long id;

	@NotBlank(message = "Dis Branch District is required")
	@Size(max = 100, message = "Max 100 characters allowed")
	private String disBranchDistt;

	@NotBlank(message = "Bank Branch District is required")
	@Size(max = 100, message = "Max 100 characters allowed")
	private String bankBranchDistt;

	@DecimalMin(value = "0.0", inclusive = false, message = "Local fixed rate must be greater than 0")
	private BigDecimal localFixedRate;

	@DecimalMin(value = "0.0", inclusive = false, message = "Intercity per KM must be greater than 0")
	private BigDecimal intercityPerKm;

	@DecimalMin(value = "0.0", inclusive = false, message = "PVC per meter must be greater than 0")
	private BigDecimal pvcPerMeter;

	@DecimalMin(value = "0.0", inclusive = false, message = "Band per number must be greater than 0")
	private BigDecimal bandPerNo;

	@Max(value = 999, message = "Other amount cannot exceed 999")
	@Min(value = 0, message = "Other amount cannot be negative")
	private Integer otherAmount;

	@DecimalMin(value = "0.0", inclusive = true, message = "Stay amount cannot be negative")
	private BigDecimal stayAmount;

	@DecimalMin(value = "0.0", inclusive = true, message = "External helper cannot be negative")
	private BigDecimal externalHelper;

	@DecimalMin(value = "0.0", inclusive = true, message = "Local transport cannot be negative")
	private BigDecimal localTransport;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisBranchDistt() {
		return disBranchDistt;
	}

	public void setDisBranchDistt(String disBranchDistt) {
		this.disBranchDistt = disBranchDistt;
	}

	public String getBankBranchDistt() {
		return bankBranchDistt;
	}

	public void setBankBranchDistt(String bankBranchDistt) {
		this.bankBranchDistt = bankBranchDistt;
	}

	public BigDecimal getLocalFixedRate() {
		return localFixedRate;
	}

	public void setLocalFixedRate(BigDecimal localFixedRate) {
		this.localFixedRate = localFixedRate;
	}

	public BigDecimal getIntercityPerKm() {
		return intercityPerKm;
	}

	public void setIntercityPerKm(BigDecimal intercityPerKm) {
		this.intercityPerKm = intercityPerKm;
	}

	public BigDecimal getPvcPerMeter() {
		return pvcPerMeter;
	}

	public void setPvcPerMeter(BigDecimal pvcPerMeter) {
		this.pvcPerMeter = pvcPerMeter;
	}

	public BigDecimal getBandPerNo() {
		return bandPerNo;
	}

	public void setBandPerNo(BigDecimal bandPerNo) {
		this.bandPerNo = bandPerNo;
	}

	public Integer getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(Integer otherAmount) {
		this.otherAmount = otherAmount;
	}

	public BigDecimal getStayAmount() {
		return stayAmount;
	}

	public void setStayAmount(BigDecimal stayAmount) {
		this.stayAmount = stayAmount;
	}

	public BigDecimal getExternalHelper() {
		return externalHelper;
	}

	public void setExternalHelper(BigDecimal externalHelper) {
		this.externalHelper = externalHelper;
	}

	public BigDecimal getLocalTransport() {
		return localTransport;
	}

	public void setLocalTransport(BigDecimal localTransport) {
		this.localTransport = localTransport;
	}
}