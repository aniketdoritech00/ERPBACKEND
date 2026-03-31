package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comp_site_master")
public class CompSiteMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "site_id")
	private Integer siteId;

	@Column(name = "site_name", length = 255, nullable = false)
	private String siteName;

	@Column(name = "site_code", length = 50, unique = true, nullable = false)
	private String siteCode;

	@Column(name = "ifsc", length = 50)
	private String ifsc;

	@Column(name = "site_type", length = 50)
	private String siteType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hierarchy_level_id", nullable = false)
	private HierarchyLevelEntity hierarchyLevelEntity;

	@Column(name = "site_longitude", length = 50)
	private String siteLongitude;

	@Column(name = "site_latitude", length = 50)
	private String siteLatitude;

	@Column(name = "contact_person", length = 255)
	private String contactPerson;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "city", length = 100)
	private String city;
	@Column(name = "district", length = 100)
	private String district;

	@Column(name = "state", length = 100)
	private String state;

	@Column(name = "country", length = 100)
	private String country;

	@Column(name = "postal_code", length = 20)
	private String postalCode;

	@Column(name = "is_active", length = 1)
	private String isActive;

	@CreationTimestamp
	@Column(name = "created_on", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
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

	public HierarchyLevelEntity getHierarchyLevelEntity() {
		return hierarchyLevelEntity;
	}

	public void setHierarchyLevelEntity(HierarchyLevelEntity hierarchyLevelEntity) {
		this.hierarchyLevelEntity = hierarchyLevelEntity;
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

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
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