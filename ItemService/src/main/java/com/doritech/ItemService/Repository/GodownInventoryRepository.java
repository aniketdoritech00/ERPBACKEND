package com.doritech.ItemService.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.doritech.ItemService.Entity.GodownInventoryEntity;

@Repository
public interface GodownInventoryRepository
		extends
			JpaRepository<GodownInventoryEntity, Integer>,
			JpaSpecificationExecutor<GodownInventoryEntity> {

	@EntityGraph(attributePaths = {"godown", "item"})
	Page<GodownInventoryEntity> findAll(
			Specification<GodownInventoryEntity> spec, Pageable pageable);

	Optional<GodownInventoryEntity> findByGodown_GodownIdAndItem_ItemId(
			Integer godownId, Integer itemId);

	boolean existsByGodown_GodownIdAndItem_ItemId(Integer godownId,
			Integer itemId);

}