package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerRequest;

public interface CustomerService {
	com.doritech.CustomerService.Entity.ResponseEntity createCustomer(
			CustomerRequest request);

	ResponseEntity getCustomerById(Integer id);

	ResponseEntity updateCustomer(Integer id, CustomerRequest request);

	ResponseEntity getParentCustomerNames();

	ResponseEntity filterCustomers(String name, String status,
			String entityType);

	ResponseEntity getAllCustomerNames();

	ResponseEntity deleteCustomer(List<Integer> ids);

	ResponseEntity getCustomerDistrict(Integer customerId);

	ResponseEntity getCustomerByHierarchy(Integer customerId);

	ResponseEntity getAllCustomers(int page, int size);

	ResponseEntity getCustomerNamesForFillter();

    ResponseEntity getCustomerDetailsByCustomerId(Integer customerId);
}