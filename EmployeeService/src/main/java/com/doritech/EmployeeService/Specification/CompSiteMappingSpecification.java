package com.doritech.EmployeeService.Specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.EmployeeService.entity.CompSiteMappingEntity;
import com.doritech.EmployeeService.request.CompanySiteMappingRequest;

import jakarta.persistence.criteria.Predicate;

public class CompSiteMappingSpecification {

	public static Specification<CompSiteMappingEntity> filter(CompanySiteMappingRequest request) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (request.getCompId() != null) {
				predicates.add(cb.equal(root.get("companyEntity").get("id"), request.getCompId()));
			}

			if (request.getSiteId() != null) {
				predicates.add(cb.equal(root.get("compSiteMaster").get("siteId"), request.getSiteId()));
			}


			if (request.getIsActive() != null && !request.getIsActive().isBlank()) {
				predicates.add(cb.equal(root.get("isActive"), request.getIsActive()));
			}

			if (request.getCreatedBy() != null) {
				predicates.add(cb.equal(root.get("createdBy"), request.getCreatedBy()));
			}

			if (request.getModifiedBy() != null) {
				predicates.add(cb.equal(root.get("modifiedBy"), request.getModifiedBy()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}