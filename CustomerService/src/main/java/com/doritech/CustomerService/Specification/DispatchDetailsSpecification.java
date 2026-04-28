package com.doritech.CustomerService.Specification;

import org.springframework.data.jpa.domain.Specification;
import com.doritech.CustomerService.Entity.DispatchEntity;


public class DispatchDetailsSpecification {

	public static Specification<DispatchEntity> filter(String deliveryChallanNo, String consignmentNo,
			String dispatchMode, String dispatchVendor, String status) {
		return (root, query, cb) -> {

			var predicates = cb.conjunction();

			if (deliveryChallanNo != null && !deliveryChallanNo.trim().isEmpty()) {
				predicates = cb.and(predicates, cb.like(cb.lower(root.get("deliveryChallanNo")),
						"%" + deliveryChallanNo.trim().toLowerCase() + "%"));
			}

			if (consignmentNo != null && !consignmentNo.trim().isEmpty()) {
				predicates = cb.and(predicates,
						cb.like(cb.lower(root.get("consignmentNo")), "%" + consignmentNo.trim().toLowerCase() + "%"));
			}

			if (dispatchMode != null && !dispatchMode.trim().isEmpty()) {
				predicates = cb.and(predicates,
						cb.equal(cb.upper(cb.trim(root.get("dispatchMode"))), dispatchMode.trim().toUpperCase()));
			}

			if (dispatchVendor != null && !dispatchVendor.trim().isEmpty()) {
				predicates = cb.and(predicates,
						cb.equal(cb.upper(cb.trim(root.get("dispatchVendor"))), dispatchVendor.trim().toUpperCase()));
			}

			if (status != null && !status.trim().isEmpty()) {
				predicates = cb.and(predicates,
						cb.equal(cb.upper(cb.trim(root.get("status"))), status.trim().toUpperCase()));
			}


			return predicates;
		};
	}
}