package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.CustomerEntityTypeEntity;

@Repository
public interface CustomerEntityTypeRepository extends JpaRepository<CustomerEntityTypeEntity, Integer> {

	List<CustomerEntityTypeEntity> findByCustomerId(Integer customerId);

	List<CustomerEntityTypeEntity> findByEntityTypeIgnoreCase(String entityType);

}