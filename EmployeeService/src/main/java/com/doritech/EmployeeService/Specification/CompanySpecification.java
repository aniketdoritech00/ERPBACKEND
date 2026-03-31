package com.doritech.EmployeeService.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.EmployeeService.entity.CompanyEntity;


public class CompanySpecification {

	public static Specification<CompanyEntity> filter(String companyName,
			String companyCode, String contactPerson, String email,
			String phone, String city, String state, String country,
			String postalCode, String active, Long createdBy,
			LocalDate fromDate, LocalDate toDate) {

		return (root, query, cb) -> {

			var predicate = cb.conjunction();

			if (companyName != null && !companyName.isBlank())
				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("companyName")),
								"%" + companyName.toLowerCase() + "%"));

			if (companyCode != null && !companyCode.isBlank())
				predicate = cb.and(predicate,
						cb.equal(root.get("companyCode"), companyCode));

			if (contactPerson != null && !contactPerson.isBlank())
				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("contactPerson")),
								"%" + contactPerson.toLowerCase() + "%"));

			if (email != null && !email.isBlank())
				predicate = cb.and(predicate,
						cb.like(cb.lower(root.get("email")),
								"%" + email.toLowerCase() + "%"));

			if (phone != null && !phone.isBlank())
				predicate = cb.and(predicate,
						cb.like(root.get("phone"), "%" + phone + "%"));

			if (city != null && !city.isBlank())
				predicate = cb.and(predicate, cb.equal(root.get("city"), city));

			if (state != null && !state.isBlank())
				predicate = cb.and(predicate,
						cb.equal(root.get("state"), state));

			if (country != null && !country.isBlank())
				predicate = cb.and(predicate,
						cb.equal(root.get("country"), country));

			if (postalCode != null && !postalCode.isBlank())
				predicate = cb.and(predicate,
						cb.equal(root.get("postalCode"), postalCode));

			if (active != null && !active.isBlank())
				predicate = cb.and(predicate,
						cb.equal(root.get("active"), active));

			if (createdBy != null)
				predicate = cb.and(predicate,
						cb.equal(root.get("createdBy"), createdBy));

			if (fromDate != null)
				predicate = cb.and(predicate, cb
						.greaterThanOrEqualTo(root.get("createdOn"), fromDate));

			if (toDate != null)
				predicate = cb.and(predicate,
						cb.lessThanOrEqualTo(root.get("createdOn"), toDate));

			return predicate;
		};
	}
}
