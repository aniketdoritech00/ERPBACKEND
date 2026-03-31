package com.doritech.EmployeeService.Specification;

import org.springframework.data.jpa.domain.Specification;
import com.doritech.EmployeeService.entity.CompSiteMasterEntity;

public class SiteSpecification {

	public static Specification<CompSiteMasterEntity> filter(
	        String siteName,
	        String siteCode,
	        String city,
	        String state,
	        String country,
	        String contactPerson,
	        String email,
	        String postalCode,
	        Integer createdBy,       // added
	        Integer hierarchyLevelId,
	        String isActive) {

	    return (root, query, cb) -> {

	        var predicate = cb.conjunction();

	        if (siteName != null && !siteName.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.like(cb.lower(root.get("siteName")),
	                            "%" + siteName.toLowerCase() + "%"));
	        }

	        if (siteCode != null && !siteCode.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.like(cb.lower(root.get("siteCode")),
	                            "%" + siteCode.toLowerCase() + "%"));
	        }

	        if (city != null && !city.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
	        }

	        if (state != null && !state.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.equal(cb.lower(root.get("state")), state.toLowerCase()));
	        }

	        if (country != null && !country.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.equal(cb.lower(root.get("country")), country.toLowerCase()));
	        }

	        if (contactPerson != null && !contactPerson.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.like(cb.lower(root.get("contactPerson")),
	                            "%" + contactPerson.toLowerCase() + "%"));
	        }

	        if (email != null && !email.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.like(cb.lower(root.get("email")),
	                            "%" + email.toLowerCase() + "%"));
	        }

	        if (postalCode != null && !postalCode.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.equal(root.get("postalCode"), postalCode));
	        }

	        if (createdBy != null) {
	            predicate = cb.and(predicate,
	                    cb.equal(root.get("createdBy"), createdBy));
	        }

	        if (hierarchyLevelId != null) {
	            predicate = cb.and(predicate,
	                    cb.equal(root.join("hierarchyLevelEntity").get("id"), hierarchyLevelId));
	        }

	        if (isActive != null && !isActive.isBlank()) {
	            predicate = cb.and(predicate,
	                    cb.equal(root.get("isActive"), isActive));
	        }

	        return predicate;
	    };
	}}