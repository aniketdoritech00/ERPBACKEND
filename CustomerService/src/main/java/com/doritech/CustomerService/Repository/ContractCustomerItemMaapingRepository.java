package com.doritech.CustomerService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.ContractCustomerItemMapping;

@Repository
public interface ContractCustomerItemMaapingRepository extends JpaRepository<ContractCustomerItemMapping, Integer> {

    Optional<ContractCustomerItemMapping> findByContractEntityMapping_MappingIdAndContractItemMapping_ContractMappingId(
        Integer mappingId,
        Integer contractMappingId);

}
