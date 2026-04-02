package com.doritech.CustomerService.Specification;

import org.springframework.data.jpa.domain.Specification;
import com.doritech.CustomerService.Entity.CustomerBranchAllocation;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerBranchAllocationSpecification {

    public static Specification<CustomerBranchAllocation> filter(
            Integer customerId,
            Integer siteId,
            String isActive,
            LocalDate fromDate) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("customerId"), customerId));
            }
            if (siteId != null) {
                predicates.add(cb.equal(root.get("siteId"), siteId));
            }
            if (isActive != null && !isActive.isBlank()) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }
            if (fromDate != null) {
                predicates.add(cb.equal(root.get("fromDate"), fromDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}