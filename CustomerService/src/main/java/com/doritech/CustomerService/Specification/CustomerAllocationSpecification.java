package com.doritech.CustomerService.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.CustomerService.Entity.CustomerAllocationEntity;

import jakarta.persistence.criteria.Predicate;

public class CustomerAllocationSpecification {

    public static Specification<CustomerAllocationEntity> filter(
            Integer customerId,
            Integer employeeId,
            LocalDate fromDate,
            Integer createdBy, Integer modifiedBy, String isActive) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("customerId"), customerId));
            }
            
            if (employeeId != null) {
                predicates.add(cb.equal(root.get("employeeId"), employeeId));
            }

            if (fromDate != null) {
                predicates.add(cb.equal(root.get("fromDate"), fromDate));
            }

            if (createdBy != null) {
                predicates.add(cb.equal(root.get("createdBy"), createdBy));
            }
            
            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}