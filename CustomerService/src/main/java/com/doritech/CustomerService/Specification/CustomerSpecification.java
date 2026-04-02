package com.doritech.CustomerService.Specification;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import com.doritech.CustomerService.Entity.CustomerEntityTypeEntity;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;

public class CustomerSpecification {

    public static Specification<CustomerMasterEntity> filter(
            String customerName,
            String status,
            String entityType) {

        return (root, query, cb) -> {

            query.distinct(true);

            var predicate = cb.conjunction();

            if (customerName != null && !customerName.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("customerName")),
                                "%" + customerName.toLowerCase() + "%"));
            }

            if (status != null && !status.isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("isActive"), status));
            }

            if (entityType != null && !entityType.isBlank()) {

                Join<CustomerMasterEntity, CustomerEntityTypeEntity> entityJoin =
                        root.join("entityTypes", JoinType.LEFT);

                predicate = cb.and(predicate,
                        cb.equal(entityJoin.get("entityType"), entityType));
            }

            return predicate;
        };
    }
}