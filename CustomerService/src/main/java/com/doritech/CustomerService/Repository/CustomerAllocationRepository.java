package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.doritech.CustomerService.Entity.CustomerAllocationEntity;

public interface CustomerAllocationRepository extends JpaRepository<CustomerAllocationEntity, Integer> {

    Page<CustomerAllocationEntity> findAll(Specification<CustomerAllocationEntity> filter, Pageable pageable);

    boolean existsByCustomer_CustomerIdAndEmployeeId(Integer customerId, Integer employeeId);

    boolean existsByCustomer_CustomerIdAndEmployeeIdAndAllocationIdNot(Integer customerId, Integer employeeId, Integer id);

	List<CustomerAllocationEntity> findByCustomer_CustomerId(Integer customerId);

}