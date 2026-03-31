package com.doritech.EmployeeService.Specification;

import org.springframework.data.jpa.domain.Specification;
import com.doritech.EmployeeService.entity.RoleMaster;

public class RoleSpecification {

    public static Specification<RoleMaster> filter(
            String roleName,
            String isActive, // changed to String
            Integer createdBy,
            Integer modifiedBy) {

        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (roleName != null && !roleName.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("roleName")),
                                "%" + roleName.toLowerCase() + "%"));
            }

            if (isActive != null && !isActive.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("isActive"), isActive)); // compare String "Y"/"N"
            }

            if (createdBy != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("createdBy"), createdBy));
            }

            if (modifiedBy != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("modifiedBy"), modifiedBy));
            }

            return predicate;
        };
    }
}