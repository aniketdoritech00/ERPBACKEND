package com.doritech.CustomerService.Service;

import java.util.List;


import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.StockDeliveryChallanRequest;


public interface StockDeliveryChallanService {
	
	ResponseEntity saveStockDelivery(StockDeliveryChallanRequest request);
	
	ResponseEntity getAllStockDelivery(int page, int size);

	ResponseEntity deleteMultipleStockDelivery(List<Integer> ids);

	ResponseEntity getStockDeliveryById(Integer id);

}
