package com.doritech.CustomerService.Specification;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.CustomerService.Entity.StockRequestEntity;

public class StockRequestSpecification {

	public static Specification<StockRequestEntity> filter(Integer sourceSiteId,
			Integer requestedSiteId, String status) {
		return (root, query, cb) -> {

			var predicates = cb.conjunction();

			if (sourceSiteId != null) {
				predicates = cb.and(predicates,
						cb.equal(root.get("sourceSiteId"), sourceSiteId));
			}

			if (requestedSiteId != null) {
				predicates = cb.and(predicates,
						cb.equal(root.get("requestedSiteId"), requestedSiteId));
			}
			if (status != null && !status.trim().isEmpty()) {
				predicates = cb.and(predicates,
						cb.equal(cb.upper(cb.trim(root.get("status"))),
								status.trim().toUpperCase()));
			}

			return predicates;
		};
	}
}