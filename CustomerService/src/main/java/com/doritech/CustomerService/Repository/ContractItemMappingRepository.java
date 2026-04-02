package com.doritech.CustomerService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.ContractItemMapping;

@Repository
public interface ContractItemMappingRepository extends JpaRepository<ContractItemMapping, Integer> {

    List<ContractItemMapping> findByContract_ContractId(Integer contractId);

    boolean existsByContract_ContractIdAndItemId(Integer contractId, Integer itemId);

    Optional<ContractItemMapping> findByContract_ContractIdAndItemId(Integer contractId, Integer itemId);
}
