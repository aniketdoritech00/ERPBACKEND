package com.doritech.CustomerService.Response;

public class AmcDetailResponse {

	private Integer amcDetailId;
	private Integer amcId;
	private String amcName;
	private Integer itemId;
	private String description;

	// Getters & Setters

	public Integer getAmcDetailId() {
		return amcDetailId;
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

	public void setAmcDetailId(Integer amcDetailId) {
		this.amcDetailId = amcDetailId;
	}

	public Integer getAmcId() {
		return amcId;
	}

	public void setAmcId(Integer amcId) {
		this.amcId = amcId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}