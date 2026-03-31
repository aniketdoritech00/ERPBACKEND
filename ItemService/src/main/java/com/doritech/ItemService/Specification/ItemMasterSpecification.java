package com.doritech.ItemService.Specification;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.ItemService.Entity.ItemMasterEntity;

public class ItemMasterSpecification {

	public static Specification<ItemMasterEntity> filter(String itemName,
			String itemCode, String active, String itemType, String category) {

		return (root, query, cb) -> {

			var predicate = cb.conjunction();

			if (itemName != null && !itemName.isBlank()) {
				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("itemName")),
								"%" + itemName.toLowerCase() + "%"));
			}

			if (itemCode != null && !itemCode.isBlank()) {
				predicate = cb.and(predicate,
						cb.equal(root.get("itemCode"), itemCode));
			}

			if (active != null && !active.isBlank()) {
				predicate = cb.and(predicate,
						cb.equal(root.get("isActive"), active));
			}

			if (itemType != null && !itemType.isBlank()) {
				predicate = cb.and(predicate,
						cb.equal(root.get("itemType"), itemType));
			}

			if (category != null && !category.isBlank()) {
				predicate = cb.and(predicate,
						cb.equal(root.get("category"), category));
			}

			return predicate;
		};
	}
}