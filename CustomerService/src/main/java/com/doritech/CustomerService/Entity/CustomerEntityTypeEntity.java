package com.doritech.CustomerService.Entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_entity_type")
public class CustomerEntityTypeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cust_entity_id")
	private Integer custEntityId;

	@Column(name = "customer_id", nullable = false)
	private Integer customerId;

	@Column(name = "entity_type", nullable = false, length = 2)
	private String entityType;

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

	@PrePersist
	protected void onCreate() {
		createdOn = LocalDateTime.now();
	}

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "customer_id", insertable = false, updatable = false)
	private CustomerMasterEntity customer;

	@PreUpdate
	protected void onUpdate() {
		modifiedOn = LocalDateTime.now();
	}

	public Integer getCustEntityId() {
		return custEntityId;
	}

	public void setCustEntityId(Integer custEntityId) {
		this.custEntityId = custEntityId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public CustomerMasterEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerMasterEntity customer) {
		this.customer = customer;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
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

}