package com.doritech.CustomerService.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.doritech.CustomerService.Entity.StockRequestEntity;

public interface StockRequestRepository
		extends
			JpaRepository<StockRequestEntity, Integer> {

	Page<StockRequestEntity> findAll(Pageable pageable);

	Page<StockRequestEntity> findAll(Specification<StockRequestEntity> filter,
			Pageable pageable);

	boolean existsBySourceSiteIdAndRequestedSiteId(Integer sourceSiteId,
			Integer requestedSiteId);

	boolean existsBySourceSiteIdAndRequestedSiteIdAndStockRequestIdNot(
			Integer sourceSiteId, Integer requestedSiteId,
			Integer stockRequestId);

	Page<StockRequestEntity> findByStatus(String string, PageRequest pageable);

}
