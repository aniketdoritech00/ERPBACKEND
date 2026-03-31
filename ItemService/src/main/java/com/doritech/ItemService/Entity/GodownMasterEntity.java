package com.doritech.ItemService.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "godown_master")
public class GodownMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer godownId;

	@Column(nullable = false, length = 255)
	private String godownName;

	@Column(nullable = false, length = 1)
	private String godownType;

	@Column(nullable = false, unique = true, length = 50)
	private String godownCode;

	@Column(length = 500)
	private String address;
	@Column(length = 100)
	private String city;
	@Column(length = 100)
	private String district;
	@Column(length = 100)
	private String state;
	@Column(length = 100)
	private String country;
	@Column(length = 20)
	private String postalCode;

	private Integer inchargeEmployeeId;

	@Column(length = 1)
	private String isActive;

	@Column(name = "created_by", nullable = false, updatable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

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

	/**
	 * @return the godownId
	 */
	public Integer getGodownId() {
		return godownId;
	}

	/**
	 * @param godownId
	 *            the godownId to set
	 */
	public void setGodownId(Integer godownId) {
		this.godownId = godownId;
	}

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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}