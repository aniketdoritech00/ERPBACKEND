package com.doritech.CustomerService.Entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_master")
public class CustomerMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Integer customerId;

	@Column(name = "customer_name", nullable = false, length = 255)
	private String customerName;

	@Column(name = "customer_code", nullable = false, unique = true, length = 50)
	private String customerCode;

	@Column(name = "org_id", nullable = false)
	private Integer orgId;

	@Column(name = "comp_id", nullable = false)
	private Integer compId;

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

	@Column(name = "gstin", length = 16)
	private String gstin;
	@Column(name = "parent_id")
	private Integer parentId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private CustomerMasterEntity parentCustomer;

	@Column(name = "hierarchy_level_id")
	private Integer hierarchyLevelId;

	@Column(name = "is_active", nullable = false, length = 1)
	private String isActive;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
	private List<CustomerEntityTypeEntity> entityTypes;

	@PrePersist
	protected void onCreate() {
		createdOn = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		modifiedOn = LocalDateTime.now();
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public List<CustomerEntityTypeEntity> getEntityTypes() {
		return entityTypes;
	}

	public void setEntityTypes(List<CustomerEntityTypeEntity> entityTypes) {
		this.entityTypes = entityTypes;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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

	public CustomerMasterEntity getParentCustomer() {
		return parentCustomer;
	}

	public void setParentCustomer(CustomerMasterEntity parentCustomer) {
		this.parentCustomer = parentCustomer;
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

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
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

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	

}