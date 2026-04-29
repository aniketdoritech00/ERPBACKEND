package com.doritech.CustomerService.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.InstallationExpenseMasterRequest;

public interface InstallationExpenseMasterService {

	ResponseEntity saveOrUpdate(InstallationExpenseMasterRequest request);

	ResponseEntity getCompletedAssignmentsExpense();
}