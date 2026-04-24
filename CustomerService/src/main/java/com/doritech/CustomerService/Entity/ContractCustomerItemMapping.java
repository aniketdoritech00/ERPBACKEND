package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "contract_customer_item_mapping")
public class ContractCustomerItemMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contractCustomerItemMappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapping_id")
    private ContractEntityMapping contractEntityMapping;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_mapping_id")
    private ContractItemMapping contractItemMapping;

    @Column(name = "quantity", precision = 10, scale = 3)
    private BigDecimal quantity;

    @Column(name = "created_on", nullable = false,updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

    @JoinColumn(name = "created_by", nullable = false,updatable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

    @PrePersist
	public void prePersist() {
		createdOn = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		modifiedOn = LocalDateTime.now();
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

    public Integer getContractCustomerItemMappingId() {
        return contractCustomerItemMappingId;
    }

    public void setContractCustomerItemMappingId(Integer contractCustomerItemMappingId) {
        this.contractCustomerItemMappingId = contractCustomerItemMappingId;
    }

    public ContractEntityMapping getContractEntityMapping() {
        return contractEntityMapping;
    }

    public void setContractEntityMapping(ContractEntityMapping contractEntityMapping) {
        this.contractEntityMapping = contractEntityMapping;
    }

    public ContractItemMapping getContractItemMapping() {
        return contractItemMapping;
    }

    public void setContractItemMapping(ContractItemMapping contractItemMapping) {
        this.contractItemMapping = contractItemMapping;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    
    

}
