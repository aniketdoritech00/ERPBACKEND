package com.doritech.EmployeeService.Specification;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.EmployeeService.entity.HierarchyLevelEntity;
import com.doritech.EmployeeService.request.HierarchyLevelFilterDTO;

import jakarta.persistence.criteria.Predicate;

public class HierarchyLevelSpecification {

	public static Specification<HierarchyLevelEntity> filter(
			HierarchyLevelFilterDTO dto) {

		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (dto.getHierarchyId() != null) {
				predicates.add(cb.equal(root.get("hierarchy").get("id"),
						dto.getHierarchyId()));
			}

			if (dto.getLevelNumber() != null) {
				predicates.add(cb.equal(root.get("levelNumber"),
						dto.getLevelNumber()));
			}

			if (dto.getLevelName() != null && !dto.getLevelName().isBlank()) {

				predicates.add(cb.like(cb.lower(root.get("levelName")),
						"%" + dto.getLevelName().toLowerCase() + "%"));
			}

			if (dto.getEndNode() != null) {
				predicates.add(cb.equal(root.get("endNode"), dto.getEndNode()));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}