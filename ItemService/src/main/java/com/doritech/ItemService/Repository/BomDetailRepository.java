package com.doritech.ItemService.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.ItemService.Entity.BomDetailEntity;

@Repository
public interface BomDetailRepository
		extends
			JpaRepository<BomDetailEntity, Integer> {

	List<BomDetailEntity> findByBomItem_ItemId(Integer bomItemId);

	boolean existsByBomItem_ItemIdAndRawItem_ItemId(Integer bomId,
			Integer rawId);

	void deleteByBomDetailIdIn(List<Integer> ids);

	@Query("""
			    SELECT b FROM BomDetailEntity b
			    WHERE (:bomId IS NULL OR b.bomItem.itemId = :bomId)
			    AND (:rawId IS NULL OR b.rawItem.itemId = :rawId)
			    AND (:active IS NULL OR b.isActive = :active)
			""")
	Page<BomDetailEntity> filterBom(Integer bomId, Integer rawId, String active,
			Pageable pageable);
}