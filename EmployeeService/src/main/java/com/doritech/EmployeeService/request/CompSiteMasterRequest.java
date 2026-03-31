package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CompSiteMasterRequest {

	@NotBlank(message = "{site.siteName.required}")
	@Size(max = 255, message = "{site.siteName.size}")
	@Pattern(regexp = "^(|[a-zA-Z0-9 ]+)$", message = "{site.name.invalid}")
	private String siteName;

	@NotBlank(message = "{site.siteCode.required}")
	@Size(max = 50, message = "{site.siteCode.size}")
	private String siteCode;
	@Size(max = 50, message = "site.ifsc.size")
	private String ifsc;

	@Size(max = 50, message = "site.siteType.size")
	private String siteType;

	@NotNull(message = "{site.hierarchyLevelId.required}")
	private Integer hierarchyLevelId;

	@Size(max = 50, message = "site.siteLongitude.size")
	private String siteLongitude;

	@Size(max = 50, message = "{site.siteLatitude.size}")
	private String siteLatitude;

	@Size(max = 255, message = "{site.contactPerson.size}")
	private String contactPerson;

	@Email(message = "{site.email.invalid}")
	@Size(max = 255, message = "{site.email.size}")
	private String email;

	@Size(max = 20, message = "{site.phone.size}")
	private String phone;

	@Size(max = 500, message = "{site.address.size}")
	private String address;

	@Size(max = 100, message = "{site.city.size}")
	private String city;

	@Size(max = 100, message = "{site.district.size}")
	private String district;

	@Size(max = 100, message = "{site.state.size}")
	private String state;

	@Size(max = 100, message = "{site.country.size}")
	private String country;

	@Size(max = 20, message = "{site.postalCode.size}")
	private String postalCode;

	@Pattern(regexp = "^[YN]$", message = "{sitemapping.isActive.invalid}")
	private String isActive;

	private Integer createdBy;

	private Integer modifiedBy;

	/**
	 * @return the ifsc
	 */
	public String getIfsc() {
		return ifsc;
	}

	/**
	 * @param ifsc the ifsc to set
	 */
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Integer getHierarchyLevelId() {
		return hierarchyLevelId;
	}

	public void setHierarchyLevelId(Integer hierarchyLevelId) {
		this.hierarchyLevelId = hierarchyLevelId;
	}

	public String getSiteLongitude() {
		return siteLongitude;
	}

	public void setSiteLongitude(String siteLongitude) {
		this.siteLongitude = siteLongitude;
	}

	public String getSiteLatitude() {
		return siteLatitude;
	}

	public void setSiteLatitude(String siteLatitude) {
		this.siteLatitude = siteLatitude;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
}