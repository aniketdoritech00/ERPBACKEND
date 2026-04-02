package com.doritech.CustomerService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.doritech.CustomerService.Entity.StockRequestDetailEntity;


public interface StockRequestDetailsRepository extends JpaRepository<StockRequestDetailEntity, Integer> {

	boolean existsByItemIdAndContractId(Integer itemId, Integer contractId);

	boolean existsByItemIdAndContractIdAndStockRequestDetailIdNot(Integer itemId, Integer contractId,
			Integer stockRequestDetailId);

	boolean existsByStockRequest_StockRequestId(Integer stockRequestId);

	void deleteByStockRequest_StockRequestIdIn(List<Integer> deletableIds);


	
}
