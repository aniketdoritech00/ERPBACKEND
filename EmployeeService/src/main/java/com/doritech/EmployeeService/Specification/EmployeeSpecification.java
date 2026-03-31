package com.doritech.EmployeeService.Specification;

import org.springframework.data.jpa.domain.Specification;
import com.doritech.EmployeeService.entity.EmployeeMaster;

public class EmployeeSpecification {

    public static Specification<EmployeeMaster> filter(
            String employeeName,
            String employeeCode,
            String isActive,
            String email,
            String phone,
            String department,
            String designation,
            String role,
            Integer companyId,
            Integer siteId) {

        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (employeeName != null && !employeeName.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("employeeName")), "%" + employeeName.toLowerCase() + "%"));
            }

            if (employeeCode != null && !employeeCode.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("employeeCode")), "%" + employeeCode.toLowerCase() + "%"));
            }

            if (isActive != null && !isActive.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("isActive"), isActive));
            }

            if (email != null && !email.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }

            if (phone != null && !phone.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(root.get("phone"), "%" + phone + "%"));
            }

            if (department != null && !department.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("department"), department));
            }

            if (designation != null && !designation.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("designation"), designation));
            }

            if (role != null && !role.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("role"), role));
            }

            if (companyId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("company").get("id"), companyId));
            }

            if (siteId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("site").get("siteId"), siteId));
            }

            return predicate;
        };
    }
}