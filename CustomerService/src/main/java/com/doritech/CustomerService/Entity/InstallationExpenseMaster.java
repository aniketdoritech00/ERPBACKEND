package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "installation_expense_master")
public class InstallationExpenseMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "dis_branch_distt", length = 100)
	private String disBranchDistt;

	@Column(name = "bank_branch_distt", length = 100)
	private String bankBranchDistt;

	@Column(name = "local_fixed_rate", precision = 10, scale = 2)
	private BigDecimal localFixedRate;

	@DecimalMin(value = "0.0", inclusive = false, message = "Intercity per KM must be greater than 0")
	@Column(name = "intercity_per_km", precision = 10, scale = 2)
	private BigDecimal intercityPerKm;

	@Column(name = "pvc_per_meter", precision = 10, scale = 2)
	private BigDecimal pvcPerMeter;

	@Column(name = "band_per_no", precision = 10, scale = 2)
	private BigDecimal bandPerNo;

	@Max(value = 999, message = "Other amount cannot exceed 999")
	@Column(name = "other_amount", precision = 3, scale = 0)
	private Integer otherAmount;

	@Column(name = "stay_amount", precision = 10, scale = 2)
	private BigDecimal stayAmount;

	@Column(name = "external_helper", precision = 10, scale = 2)
	private BigDecimal externalHelper;

	@Column(name = "local_transport", precision = 10, scale = 2)
	private BigDecimal localTransport;

	public Long getId() {
		return id;
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