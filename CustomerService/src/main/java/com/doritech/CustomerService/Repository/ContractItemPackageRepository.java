package com.doritech.CustomerService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Entity.ContractItemPackage;
import com.doritech.CustomerService.Projection.ContractItemPackageProjection;

import feign.Param;

@Repository
public interface ContractItemPackageRepository extends JpaRepository<ContractItemPackage, Integer> {

	boolean existsByContractItemMappingAndMappedItemIdAndPackageIdNot(ContractItemMapping mapping, Integer mappedItemId,
			Integer packageId);

	boolean existsByContractItemMappingAndMappedItemId(ContractItemMapping mapping, Integer mappedItemId);

	List<ContractItemPackage> findByContractItemMapping_ContractMappingId(Integer contractMappingId);

	@Query(value = """
			SELECT
			    cip.package_id              AS packageId,
			    cip.contract_mapping_id     AS contractMappingId,
			    cip.mapped_item_id          AS mappedItemId,
			    cip.is_active               AS isActive,
			    cmast.contract_id           AS contractId,
			    cmast.contract_name         AS contractName,
			    cmast.contract_no           AS contractCode,
			    im1.item_name               AS mappingItemName,
				im1.item_code               AS mappingItemCode,
			    im2.item_name               AS mappedItemName,
				im2.item_code               AS mappedItemCode,
			    im2.base_price              AS basePrice,
			    cm.quantity                 AS qty
			FROM  contract_item_package  cip
			JOIN  contract_item_mapping  cm    ON cm.contract_mapping_id = cip.contract_mapping_id
			JOIN  contract_master        cmast ON cmast.contract_id      = cm.contract_id
			JOIN  item_master            im1   ON im1.item_id            = cm.item_id
			JOIN  item_master            im2   ON im2.item_id            = cip.mapped_item_id
			""", countQuery = """
			SELECT COUNT(*) FROM contract_item_package cip
			JOIN contract_item_mapping cm   ON cm.contract_mapping_id = cip.contract_mapping_id
			JOIN contract_master cmast      ON cmast.contract_id      = cm.contract_id
			JOIN item_master im1            ON im1.item_id            = cm.item_id
			JOIN item_master im2            ON im2.item_id            = cip.mapped_item_id
			""", nativeQuery = true)
	Page<ContractItemPackageProjection> findAllWithDetails(Pageable pageable);

	@Query(value = """
			SELECT
			    cip.package_id              AS packageId,
			    cip.contract_mapping_id     AS contractMappingId,
			    cip.mapped_item_id          AS mappedItemId,
			    cip.is_active               AS isActive,
			    cmast.contract_id           AS contractId,
			    cmast.contract_name         AS contractName,
			    cmast.contract_no           AS contractCode,
			    im1.item_name               AS mappingItemName,
			    im2.item_name               AS mappedItemName,
			    im2.base_price              AS basePrice,
			    cm.quantity                 AS qty
			FROM  contract_item_package  cip
			JOIN  contract_item_mapping  cm    ON cm.contract_mapping_id = cip.contract_mapping_id
			JOIN  contract_master        cmast ON cmast.contract_id      = cm.contract_id
			JOIN  item_master            im1   ON im1.item_id            = cm.item_id
			JOIN  item_master            im2   ON im2.item_id            = cip.mapped_item_id
			WHERE cip.package_id = :id
			""", nativeQuery = true)
	Optional<ContractItemPackageProjection> findDetailsById(@Param("id") Integer id);

	
	 @Query(value = """
	            SELECT
	                cip.package_id              AS packageId,
	                cip.contract_mapping_id     AS contractMappingId,
	                cip.mapped_item_id          AS mappedItemId,
	                cip.is_active               AS isActive,
	                cmast.contract_id           AS contractId,
	                cmast.contract_name         AS contractName,
	                cmast.contract_no           AS contractCode,
	                im1.item_name               AS mappingItemName,
	                im2.item_name               AS mappedItemName,
	                im2.base_price              AS basePrice,
	                cm.quantity                 AS qty
	            FROM  contract_item_package  cip
	            JOIN  contract_item_mapping  cm    ON cm.contract_mapping_id = cip.contract_mapping_id
	            JOIN  contract_master        cmast ON cmast.contract_id      = cm.contract_id
	            JOIN  item_master            im1   ON im1.item_id            = cm.item_id
	            JOIN  item_master            im2   ON im2.item_id            = cip.mapped_item_id
	            WHERE cm.contract_id = :contractId
	            """, nativeQuery = true)
	    List<ContractItemPackageProjection> findAllByContractId(@Param("contractId") Integer contractId);

}