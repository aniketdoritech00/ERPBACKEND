package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.QuotationDetailRequest;

import jakarta.validation.Valid;

public interface QuotationDetailService {

    ResponseEntity saveAndUpdateQuotationDetails(Integer quotationId, @Valid List<QuotationDetailRequest> requests);
    ResponseEntity deleteQuotationDetails(List<Integer> ids);
    ResponseEntity getQuotationDetailById(Integer id);
    ResponseEntity getByQuotationId(Integer quotationId);
	ResponseEntity getAllQuotationDetails(int page, int size);
}