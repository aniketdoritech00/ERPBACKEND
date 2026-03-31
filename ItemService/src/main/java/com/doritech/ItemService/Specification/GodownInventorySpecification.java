package com.doritech.ItemService.Specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.ItemService.Entity.GodownInventoryEntity;
import com.doritech.ItemService.Request.GodownInventoryFilterDTO;

import jakarta.persistence.criteria.Predicate;

public class GodownInventorySpecification {

	public static Specification<GodownInventoryEntity> filter(
			GodownInventoryFilterDTO dto) {
		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (dto.getGodownId() != null) {
				predicates.add(cb.equal(root.get("godown").get("godownId"),
						dto.getGodownId()));
			}

			if (dto.getItemId() != null) {
				predicates.add(cb.equal(root.get("item").get("itemId"),
						dto.getItemId()));
			}

			if (dto.getMinReorderLevel() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("reorderLevel"),
						dto.getMinReorderLevel()));
			}

			if (dto.getMaxReorderLevel() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("reorderLevel"),
						dto.getMaxReorderLevel()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}