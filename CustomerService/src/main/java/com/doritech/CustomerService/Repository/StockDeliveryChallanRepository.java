package com.doritech.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doritech.CustomerService.Entity.StockDeliveryChallanEntity;
import com.doritech.CustomerService.Entity.StockRequestEntity;

public interface StockDeliveryChallanRepository extends JpaRepository<StockDeliveryChallanEntity, Integer>{
	
    boolean existsByDeliveryChallanNo(String deliveryChallanNo);

	boolean existsByStockRequest_StockRequestId(Integer stockRequestId);

	boolean existsByStockRequest(StockRequestEntity sr);





}
