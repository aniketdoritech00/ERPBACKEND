package com.doritech.CustomerService.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.StockRequestDetailRequest;


public interface StockRequestDetailsService {

	ResponseEntity saveStockRequestDetails(StockRequestDetailRequest request);
	
	ResponseEntity getAllStockRequestDetails(int page, int size);
	
	ResponseEntity getStockRequestDetailsById(Integer id);
	
	ResponseEntity updateStockRequestDetails(Integer id, StockRequestDetailRequest request);
	
	ResponseEntity deleteMultipleStockDetails(List<Integer> ids);
		

}
