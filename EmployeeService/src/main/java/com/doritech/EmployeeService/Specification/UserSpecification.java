package com.doritech.EmployeeService.Specification;

import org.springframework.data.jpa.domain.Specification;
import com.doritech.EmployeeService.entity.UserMaster;

public class UserSpecification {

    public static Specification<UserMaster> filter(
            String loginId,
            String userType,
            Integer sourceId,
            Integer roleId,
            String isActive) { 

        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (loginId != null && !loginId.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("loginId")),
                                "%" + loginId.toLowerCase() + "%"));
            }

            if (userType != null && !userType.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("userType"), userType));
            }

            if (sourceId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("sourceId"), sourceId));
            }

            if (roleId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("role").get("roleId"), roleId));
            }

            if (isActive != null && !isActive.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("isActive"), isActive)); // compare String "Y"/"N"
            }

            return predicate;
        };
    }
}