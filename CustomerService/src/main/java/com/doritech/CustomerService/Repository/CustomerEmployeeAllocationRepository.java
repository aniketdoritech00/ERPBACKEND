package com.doritech.CustomerService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.CustomerEmployeeAllocation;

@Repository
public interface CustomerEmployeeAllocationRepository
		extends
			JpaRepository<CustomerEmployeeAllocation, Integer>,
			JpaSpecificationExecutor<CustomerEmployeeAllocation> {

	Optional<CustomerEmployeeAllocation> findTopByCustomerCustomerIdAndIsActiveOrderByFromDateDesc(
			Integer customerId, String isActive);

	Optional<CustomerEmployeeAllocation> findByCustomerCustomerIdAndIsActive(
			Integer customerId, String string);

	List<CustomerEmployeeAllocation> findByIsActive(String string);

	boolean existsByCustomer_CustomerIdAndEmployeeIdAndIsActive(
			Integer customerId, Integer employeeId, String string);

	Page<CustomerEmployeeAllocation> findByIsActive(String string,
			Pageable pageable);

    CustomerEmployeeAllocation findByCustomer_CustomerIdAndEmployeeIdAndIsActive(Integer customerId, Integer employeeId,
            String string);

    List<CustomerEmployeeAllocation> findByEmployeeIdAndIsActive(Integer employeeId, String string);
}