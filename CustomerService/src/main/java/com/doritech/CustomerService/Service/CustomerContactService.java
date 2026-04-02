package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.CustomerContactRequest;

public interface CustomerContactService {


    ResponseEntity getCustomerContacts(Integer customerId);

	ResponseEntity saveCustomerContact(List<CustomerContactRequest> requests);

	ResponseEntity getAllCustomerContacts();
}