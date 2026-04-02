package com.doritech.CustomerService.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract_item_package")
public class ContractItemPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Integer packageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_mapping_id", nullable = false)
    private ContractItemMapping contractItemMapping;

    @Column(name = "mapped_item_id", nullable = false)
    private Integer mappedItemId;

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

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public ContractItemMapping getContractItemMapping() {
        return contractItemMapping;
    }

    public void setContractItemMapping(ContractItemMapping contractItemMapping) {
        this.contractItemMapping = contractItemMapping;
    }

    public Integer getMappedItemId() {
        return mappedItemId;
    }

    public void setMappedItemId(Integer mappedItemId) {
        this.mappedItemId = mappedItemId;
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