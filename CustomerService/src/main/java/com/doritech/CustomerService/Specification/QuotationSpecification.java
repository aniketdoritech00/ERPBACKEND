package com.doritech.CustomerService.Specification;

import org.springframework.data.jpa.domain.Specification;
import com.doritech.CustomerService.Entity.QuotationMaster;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class QuotationSpecification {

    public static Specification<QuotationMaster> filterQuotations(
            String quotationCode,
            Integer customerId,
            Integer contractId,
            String status,
            String isActive) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (quotationCode != null && !quotationCode.isEmpty()) {
                predicates.add(cb.equal(root.get("quotationCode"), quotationCode));
            }

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("customerId"), customerId));
            }

            if (contractId != null) {
                predicates.add(cb.equal(root.get("contract").get("contractId"), contractId));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}