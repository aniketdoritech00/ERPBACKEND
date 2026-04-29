package com.doritech.CustomerService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.ContractItemMapping;

@Repository
public interface ContractItemMappingRepository extends JpaRepository<ContractItemMapping, Integer> {

    List<ContractItemMapping> findByContract_ContractId(Integer contractId);

    boolean existsByContract_ContractIdAndItemId(Integer contractId, Integer itemId);

    Optional<ContractItemMapping> findByContract_ContractIdAndItemId(Integer contractId, Integer itemId);
    @Query(value = "SELECT DISTINCT cim.item_id " +
            "FROM contract_item_mapping cim " +
            "WHERE cim.contract_id = :contractId",
    nativeQuery = true)
List<Integer> findItemIdsByContractId(@Param("contractId") Integer contractId);
}
