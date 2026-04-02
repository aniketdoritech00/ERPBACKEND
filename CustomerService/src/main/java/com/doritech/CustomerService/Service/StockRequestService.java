package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.StockRequestRequest;

public interface StockRequestService {

	ResponseEntity saveStockRequest(StockRequestRequest request);

	ResponseEntity getAllStockRequest(int page, int size);

	ResponseEntity updateStockRequest(Integer id, StockRequestRequest request);

	ResponseEntity getStockRequestById(Integer id);;

	ResponseEntity deleteMultipleStockRequest(List<Integer> siteIds);

	ResponseEntity filterStockRequests(Integer sourceSiteId,
			Integer requestedSiteId, String status, int page, int size);

	ResponseEntity approveStockRequest(Integer id, StockRequestRequest request);

	ResponseEntity getAllApprovedStockRequests(int page, int size);

	ResponseEntity getApprovedStockWithoutChallan(int page, int size);

}
