package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.QuotationMasterRequest;

import jakarta.validation.Valid;

public interface QuotationMasterService {

	ResponseEntity createQuotation(@Valid QuotationMasterRequest request);

	ResponseEntity getQuotationById(Integer id);

	ResponseEntity getAllQuotation(int page, int size);

	ResponseEntity updateQuotation(Integer id, @Valid QuotationMasterRequest request);

	ResponseEntity deleteQuotationBulk(List<Integer> ids);

	ResponseEntity getQuotations(String quotationCode, Integer customerId, Integer contractId, String status,
			String isActive, int page, int size);

}