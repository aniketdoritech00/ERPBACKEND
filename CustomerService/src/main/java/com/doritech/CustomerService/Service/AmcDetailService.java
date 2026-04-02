package com.doritech.CustomerService.Service;
import java.util.List;

import com.doritech.CustomerService.Request.AmcDetailFilterRequest;
import com.doritech.CustomerService.Request.AmcDetailRequest;
import com.doritech.CustomerService.Response.AmcDetailResponse;
import com.doritech.CustomerService.Response.PageResponse;

public interface AmcDetailService {

	AmcDetailResponse create(AmcDetailRequest request);

	AmcDetailResponse update(Integer id, AmcDetailRequest request);

	void deleteById(Integer id);

	List<AmcDetailResponse> getByAmcId(Integer amcId);

	AmcDetailResponse getById(Integer id);
	List<AmcDetailResponse> createBulk(List<AmcDetailRequest> requests);

	// List<AmcDetailResponse> updateBulk(Integer amcId,
	// List<AmcDetailRequest> requests);

	PageResponse<AmcDetailResponse> filter(AmcDetailFilterRequest request);
}