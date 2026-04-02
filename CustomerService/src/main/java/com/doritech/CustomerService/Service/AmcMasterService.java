package com.doritech.CustomerService.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.doritech.CustomerService.Request.AmcMasterRequest;
import com.doritech.CustomerService.Response.AmcMasterResponse;
import com.doritech.CustomerService.Response.PageResponse;

public interface AmcMasterService {

	AmcMasterResponse create(AmcMasterRequest request);

	AmcMasterResponse getById(Integer id);

	AmcMasterResponse update(Integer id, AmcMasterRequest request);

	void deleteMultipleByIds(List<Integer> ids);

	AmcMasterResponse getByAmcNumber(String amcNumber);

	PageResponse<AmcMasterResponse> filter(String amcStatus, Integer customerId,
			String amcCategory, String amcName, LocalDate startDate,
			LocalDate endDate, BigDecimal minValue, BigDecimal maxValue,
			Integer createdBy, Integer page, Integer size);
}