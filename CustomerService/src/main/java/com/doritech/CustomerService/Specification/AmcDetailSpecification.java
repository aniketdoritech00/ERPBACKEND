package com.doritech.CustomerService.Specification;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.CustomerService.Entity.AmcDetailEntity;

public class AmcDetailSpecification {

	public static Specification<AmcDetailEntity> hasAmcId(Integer amcId) {
		return (root, query, cb) -> amcId == null
				? null
				: cb.equal(root.get("amc").get("amcId"), amcId);
	}

	public static Specification<AmcDetailEntity> hasItemId(Integer itemId) {
		return (root, query, cb) -> itemId == null
				? null
				: cb.equal(root.get("itemId"), itemId);
	}

	public static Specification<AmcDetailEntity> hasDescription(
			String description) {
		return (root, query,
				cb) -> (description == null || description.isBlank())
						? null
						: cb.like(cb.lower(root.get("description")),
								"%" + description.toLowerCase() + "%");
	}

	public static Specification<AmcDetailEntity> hasCreatedBy(
			Integer createdBy) {
		return (root, query, cb) -> createdBy == null
				? null
				: cb.equal(root.get("createdBy"), createdBy);
	}

	public static Specification<AmcDetailEntity> createdAfter(
			LocalDateTime fromDate) {
		return (root, query, cb) -> fromDate == null
				? null
				: cb.greaterThanOrEqualTo(root.get("createdOn"), fromDate);
	}

	public static Specification<AmcDetailEntity> createdBefore(
			LocalDateTime toDate) {
		return (root, query, cb) -> toDate == null
				? null
				: cb.lessThanOrEqualTo(root.get("createdOn"), toDate);
	}
}