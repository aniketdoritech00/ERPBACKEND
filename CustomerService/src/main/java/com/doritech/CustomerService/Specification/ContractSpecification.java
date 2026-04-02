package com.doritech.CustomerService.Specification;

import org.springframework.data.jpa.domain.Specification;
import com.doritech.CustomerService.Entity.ContractMaster;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ContractSpecification {

    public static Specification<ContractMaster> filterContracts(
            String contractNo,
            Integer customerId,
            String contractType,
            String isActive) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (contractNo != null && !contractNo.isEmpty()) {
                predicates.add(cb.equal(root.get("contractNo"), contractNo));
            }

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customerId"), customerId));
            }

            if (contractType != null && !contractType.isEmpty()) {
                predicates.add(cb.equal(root.get("contractType"), contractType));
            }

            if (isActive != null && !isActive.isEmpty()) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}