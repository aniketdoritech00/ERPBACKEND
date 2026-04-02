package com.doritech.CustomerService.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.CustomerService.Entity.AmcMasterEntity;

public class AmcMasterSpecification {

	public static Specification<AmcMasterEntity> hasStatus(String amcStatus) {
		return (root, query, cb) -> amcStatus == null
				? null
				: cb.equal(root.get("amcStatus"), amcStatus);
	}

	public static Specification<AmcMasterEntity> hasCustomerId(
			Integer customerId) {
		return (root, query, cb) -> customerId == null
				? null
				: cb.equal(root.get("customer").get("customerId"), customerId);
	}

	// 🔥 NEW FILTERS

	public static Specification<AmcMasterEntity> hasCategory(String category) {
		return (root, query, cb) -> category == null
				? null
				: cb.equal(root.get("amcCategory"), category);
	}

	public static Specification<AmcMasterEntity> hasNameLike(String name) {
		return (root, query, cb) -> name == null
				? null
				: cb.like(cb.lower(root.get("amcName")),
						"%" + name.toLowerCase() + "%");
	}

	public static Specification<AmcMasterEntity> startDateAfter(
			LocalDate fromDate) {
		return (root, query, cb) -> fromDate == null
				? null
				: cb.greaterThanOrEqualTo(root.get("amcStartDate"), fromDate);
	}

	public static Specification<AmcMasterEntity> endDateBefore(
			LocalDate toDate) {
		return (root, query, cb) -> toDate == null
				? null
				: cb.lessThanOrEqualTo(root.get("amcEndDate"), toDate);
	}

	public static Specification<AmcMasterEntity> valueGreaterThan(
			BigDecimal minValue) {
		return (root, query, cb) -> minValue == null
				? null
				: cb.greaterThanOrEqualTo(root.get("amcValue"), minValue);
	}

	public static Specification<AmcMasterEntity> valueLessThan(
			BigDecimal maxValue) {
		return (root, query, cb) -> maxValue == null
				? null
				: cb.lessThanOrEqualTo(root.get("amcValue"), maxValue);
	}

	public static Specification<AmcMasterEntity> createdBy(Integer createdBy) {
		return (root, query, cb) -> createdBy == null
				? null
				: cb.equal(root.get("createdBy"), createdBy);
	}

	public static Specification<AmcMasterEntity> createdAfter(
			LocalDateTime fromDateTime) {
		return (root, query, cb) -> fromDateTime == null
				? null
				: cb.greaterThanOrEqualTo(root.get("createdOn"), fromDateTime);
	}

	public static Specification<AmcMasterEntity> createdBefore(
			LocalDateTime toDateTime) {
		return (root, query, cb) -> toDateTime == null
				? null
				: cb.lessThanOrEqualTo(root.get("createdOn"), toDateTime);
	}
}