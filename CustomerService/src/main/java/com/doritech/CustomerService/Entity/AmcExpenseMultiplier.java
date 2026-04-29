package com.doritech.CustomerService.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "amc_range_multiplier")
public class AmcExpenseMultiplier {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Min(0)
	private Integer minRange;
	
	@Min(0)
	private Integer maxRange;

	private Integer multiplier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "amc_expense_id", nullable = false)
	private AmcExpenseMaster amcExpenseMaster;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMinRange() {
		return minRange;
	}

	public void setMinRange(Integer minRange) {
		this.minRange = minRange;
	}

	public Integer getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(Integer maxRange) {
		this.maxRange = maxRange;
	}

	public Integer getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Integer multiplier) {
		this.multiplier = multiplier;
	}

	public AmcExpenseMaster getAmcExpenseMaster() {
		return amcExpenseMaster;
	}

	public void setAmcExpenseMaster(AmcExpenseMaster amcExpenseMaster) {
		this.amcExpenseMaster = amcExpenseMaster;
	}

}