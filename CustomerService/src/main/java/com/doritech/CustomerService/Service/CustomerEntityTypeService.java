package com.doritech.CustomerService.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerEntityTypeRequest;

public interface CustomerEntityTypeService {

    ResponseEntity saveCustomerEntityType(CustomerEntityTypeRequest request);

    ResponseEntity getCustomerEntityTypeByCustomerId(Integer customerId);

	ResponseEntity getAllCustomerEntityTypes(int page, int size);

	ResponseEntity getCustomerEntityTypesByCustomerId(Integer customerId);
}