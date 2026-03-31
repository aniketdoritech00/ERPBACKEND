package com.doritech.ItemService.Specification;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.ItemService.Entity.GodownMasterEntity;

public class GodownSpecification {

	public static Specification<GodownMasterEntity> filter(String name,
			String code, String type, String active) {

		return (root, query, cb) -> {

			var predicate = cb.conjunction();

			if (name != null && !name.isBlank()) {
				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("godownName")),
								"%" + name.toLowerCase() + "%"));
			}

			if (code != null && !code.isBlank()) {
				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("godownCode")),
								"%" + code.toLowerCase() + "%"));
			}

			if (type != null && !type.isBlank()) {
				predicate = cb.and(predicate,
						cb.equal(root.get("godownType"), type));
			}

			if (active != null) {
				predicate = cb.and(predicate,
						cb.equal(root.get("isActive"), active));
			}

			return predicate;
		};
	}
}