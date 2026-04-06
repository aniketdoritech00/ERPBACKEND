package com.doritech.CustomerService.Repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.CustomerBranchAllocation;

@Repository
public interface CustomerBranchAllocationRepository
		extends JpaRepository<CustomerBranchAllocation, Integer>, JpaSpecificationExecutor<CustomerBranchAllocation> {

	Optional<CustomerBranchAllocation> findTopByCustomerCustomerIdAndSiteIdAndIsActive(Integer customerId,
			Integer siteId, String isActive);

	Optional<CustomerBranchAllocation> findByCustomerCustomerIdAndSiteIdAndFromDate(Integer customerId, Integer siteId,
			LocalDate fromDate);

	Optional<CustomerBranchAllocation> findByCustomerCustomerIdAndIsActive(Integer customerId, String isActive);

	boolean existsByCustomer_CustomerIdAndSiteIdAndIsActive(Integer customerId, Integer siteId, String string);

    CustomerBranchAllocation findByCustomer_CustomerIdAndSiteIdAndIsActive(Integer customerId, Integer siteId,
            String string);
}