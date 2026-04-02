package com.doritech.CustomerService.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.CustomerService.Entity.CustomerEmployeeAllocation;

import jakarta.persistence.criteria.Predicate;

public class CustomerEmployeeAllocationSpecification {

	public static Specification<CustomerEmployeeAllocation> filter(
			Integer customerId, Integer employeeId, String isActive,
			LocalDate fromDate, LocalDate toDate) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (customerId != null) {
				predicates.add(cb.equal(root.get("customer").get("customerId"),
						customerId));
			}

			if (employeeId != null) {
				predicates.add(cb.equal(root.get("employeeId"), employeeId));
			}

			if (isActive != null && !isActive.isBlank()) {
				predicates.add(cb.equal(root.get("isActive"), isActive));
			}

			if (fromDate != null && toDate != null) {
				predicates.add(
						cb.between(root.get("fromDate"), fromDate, toDate));
			} else if (fromDate != null) {
				predicates.add(cb.equal(root.get("fromDate"), fromDate));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}