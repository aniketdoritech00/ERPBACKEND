package com.doritech.ItemService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.ItemService.Entity.ItemMasterEntity;

@Repository
public interface ItemMasterRepository
		extends
			JpaRepository<ItemMasterEntity, Integer>,
			JpaSpecificationExecutor<ItemMasterEntity> {

	Optional<ItemMasterEntity> findByItemCode(String itemCode);

	boolean existsByItemCode(String itemCode);

	void deleteByItemCodeIn(List<String> codes);

	@Query("""
			    SELECT i FROM ItemMasterEntity i
			    WHERE (:itemName IS NULL OR i.itemName LIKE %:itemName%)
			    AND (:itemCode IS NULL OR i.itemCode LIKE %:itemCode%)
			    AND (:active IS NULL OR i.isActive = :active)
			""")
	Page<ItemMasterEntity> filterItems(String itemName, String itemCode,
			String active, Pageable pageable);

	List<ItemMasterEntity> findAllByItemCodeIn(List<String> validCodes);

	List<ItemMasterEntity> findByItemType(String type);

	List<ItemMasterEntity> findByIsActive(String string);
}