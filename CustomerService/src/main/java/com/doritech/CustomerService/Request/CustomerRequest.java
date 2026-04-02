package com.doritech.CustomerService.Request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CustomerRequest {

	private Integer customerId;

	@NotBlank(message = "{customer.name.notblank}")
	@Size(max = 255, message = "{customer.name.size}")
	private String customerName;

	@NotBlank(message = "{customer.code.notblank}")
	@Size(max = 50, message = "{customer.code.size}")
	private String customerCode;

	@Size(max = 100, message = "{customer.district.size}")
	private String district;

	@NotNull(message = "{customer.orgId.notnull}")
	private Integer orgId;

	@NotNull(message = "{customer.compId.notnull}")
	private Integer compId;

	@Size(max = 500, message = "{customer.address.size}")
	private String address;

	@Size(max = 100, message = "{customer.city.size}")
	private String city;

	@Size(max = 100, message = "{customer.state.size}")
	private String state;

	@Size(max = 100, message = "{customer.country.size}")
	private String country;

	@Size(max = 20, message = "{customer.postalCode.size}")
	private String postalCode;

	@Size(max = 16, message = "{customer.gstin.size}")
	private String gstin;

	@Min(value = 1, message = "{customer.parentId.invalid}")
	private Integer parentId;

	private Integer hierarchyLevelId;

	@NotBlank(message = "{customer.isActive.notblank}")
	@Size(max = 1, message = "{customer.isActive.size}")
	private String isActive;

	// @NotNull(message = "{customer.createdBy.notnull}")
	private Integer createdBy;

	private Integer modifiedBy;

	@Valid
	private List<CustomerEntityTypeRequest> entityTypes;

	@Valid
	private List<CustomerContactRequest> contacts;

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
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

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getHierarchyLevelId() {
		return hierarchyLevelId;
	}

	public void setHierarchyLevelId(Integer hierarchyLevelId) {
		this.hierarchyLevelId = hierarchyLevelId;
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

	public List<CustomerEntityTypeRequest> getEntityTypes() {
		return entityTypes;
	}

	public void setEntityTypes(List<CustomerEntityTypeRequest> entityTypes) {
		this.entityTypes = entityTypes;
	}

	public List<CustomerContactRequest> getContacts() {
		return contacts;
	}

	public void setContacts(List<CustomerContactRequest> contacts) {
		this.contacts = contacts;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}