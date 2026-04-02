package com.doritech.CustomerService.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.AmcMasterEntity;

@Repository
public interface AmcMasterRepository
		extends
			JpaRepository<AmcMasterEntity, Integer>,
			JpaSpecificationExecutor<AmcMasterEntity> {

	Optional<AmcMasterEntity> findByAmcNumber(String amcNumber);

	List<AmcMasterEntity> findByAmcStatus(String amcStatus);

	List<AmcMasterEntity> findByCustomer_CustomerId(Integer customerId);

	List<AmcMasterEntity> findByAmcStatusAndCustomer_CustomerId(
			String amcStatus, Integer customerId);
}