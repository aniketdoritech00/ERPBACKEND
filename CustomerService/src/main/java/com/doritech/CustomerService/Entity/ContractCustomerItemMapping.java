package com.doritech.CustomerService.Entity;

import java.math.BigDecimal;

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
