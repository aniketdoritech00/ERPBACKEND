package com.doritech.EmployeeService.Specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.EmployeeService.entity.HierarchyMasterEntity;
import com.doritech.EmployeeService.request.HierarchyMasterFilterDTO;

import jakarta.persistence.criteria.Predicate;

public class HierarchyMasterSpecification {

	public static Specification<HierarchyMasterEntity> filter(
			HierarchyMasterFilterDTO dto) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (dto.getHierarchyName() != null
					&& !dto.getHierarchyName().isBlank()) {

				predicates.add(cb.like(cb.lower(root.get("hierarchyName")),
						"%" + dto.getHierarchyName().toLowerCase() + "%"));
			}

			if (dto.getEntityType() != null) {
				predicates.add(
						cb.equal(root.get("entityType"), dto.getEntityType()));
			}

			if (dto.getCompanyId() != null) {
				predicates.add(cb.equal(root.get("company").get("id"),
						dto.getCompanyId()));
			}

			if (dto.getOrganizationId() != null) {
				predicates.add(cb.equal(root.get("organization").get("id"),
						dto.getOrganizationId()));
			}

			if (dto.getActive() != null) {
				predicates.add(cb.equal(root.get("active"), dto.getActive()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}