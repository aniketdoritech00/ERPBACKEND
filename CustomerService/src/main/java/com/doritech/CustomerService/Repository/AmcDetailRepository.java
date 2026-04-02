package com.doritech.CustomerService.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.doritech.CustomerService.Entity.AmcDetailEntity;

public interface AmcDetailRepository
		extends
			JpaRepository<AmcDetailEntity, Integer>,
			JpaSpecificationExecutor<AmcDetailEntity> {

	List<AmcDetailEntity> findByAmc_AmcId(Integer amcId);
}