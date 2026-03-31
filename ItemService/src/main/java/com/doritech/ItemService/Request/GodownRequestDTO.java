package com.doritech.ItemService.Request;

import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GodownRequestDTO {

	@NotBlank(message = "{godown.name.required}", groups = OnCreate.class)
	private String godownName;

	@NotBlank(message = "{godown.type.required}", groups = OnCreate.class)
	private String godownType;

	@NotBlank(message = "{godown.code.required}", groups = OnCreate.class)
	@Size(max = 50, message = "{godown.code.size}")
	private String godownCode;

	private String address;
	private String city;
	private String district;
	private String state;
	private String country;
	private String postalCode;

	private Integer inchargeEmployeeId;

	private String isActive;

	private Integer createdBy;

	private Integer modifiedBy;

	/**
	 * @return the godownName
	 */
	public String getGodownName() {
		return godownName;
	}

	/**
	 * @param godownName
	 *            the godownName to set
	 */
	public void setGodownName(String godownName) {
		this.godownName = godownName;
	}

	/**
	 * @return the godownType
	 */
	public String getGodownType() {
		return godownType;
	}

	/**
	 * @param godownType
	 *            the godownType to set
	 */
	public void setGodownType(String godownType) {
		this.godownType = godownType;
	}

	/**
	 * @return the godownCode
	 */
	public String getGodownCode() {
		return godownCode;
	}

	/**
	 * @param godownCode
	 *            the godownCode to set
	 */
	public void setGodownCode(String godownCode) {
		this.godownCode = godownCode;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode
	 *            the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the inchargeEmployeeId
	 */
	public Integer getInchargeEmployeeId() {
		return inchargeEmployeeId;
	}

	/**
	 * @param inchargeEmployeeId
	 *            the inchargeEmployeeId to set
	 */
	public void setInchargeEmployeeId(Integer inchargeEmployeeId) {
		this.inchargeEmployeeId = inchargeEmployeeId;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}