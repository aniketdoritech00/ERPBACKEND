package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.DispatchRequest;

public interface DispatchService {

	ResponseEntity saveDispatchDeatils(DispatchRequest request);

	ResponseEntity getAllDispatchDetails(int page, int size);

	ResponseEntity getDispathchDetailsById(Integer id);

	ResponseEntity deleteMultipleDispatchDetails(List<Integer> ids);

	ResponseEntity updateDispatchDeatils(Integer id, DispatchRequest request);

}
