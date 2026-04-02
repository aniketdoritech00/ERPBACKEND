package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.CustomerContactEntity;

@Repository
public interface CustomerContactRepository extends JpaRepository<CustomerContactEntity, Integer> {

	List<CustomerContactEntity> findByCustomerId(Integer customerId);

}