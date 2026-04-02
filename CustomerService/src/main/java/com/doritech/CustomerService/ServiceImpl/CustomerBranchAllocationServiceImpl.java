package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.CustomerService.Entity.CustomerBranchAllocation;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.ResourceAlreadyExistsException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.CustomerBranchAllocationRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.CustomerBranchAllocationRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.CustomerBranchAllocationResponse;
import com.doritech.CustomerService.Service.CustomerBranchAllocationService;
import com.doritech.CustomerService.Specification.CustomerBranchAllocationSpecification;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
public class CustomerBranchAllocationServiceImpl implements CustomerBranchAllocationService {

	@Autowired
	private CustomerBranchAllocationRepository allocationRepository;

	@Autowired
	private CustomerMasterRepository customerRepository;

	@Autowired
	private ValidationService validationService;

	@Override
	@Transactional
	public ResponseEntity createOrUpdateAllocation(CustomerBranchAllocationRequest request) {

		CompSiteResponse site = validationService.validateAndGetSite(request.getSiteId(), "Customer");

		Optional<CustomerBranchAllocation> existingAllocationOpt = allocationRepository
				.findTopByCustomerCustomerIdAndSiteIdAndIsActive(request.getCustomerId(), request.getSiteId(), "Y");

		if (existingAllocationOpt.isPresent()) {
			CustomerBranchAllocation existing = existingAllocationOpt.get();

			if (!request.getFromDate().isAfter(existing.getFromDate())) {
				throw new BadRequestException("From date must be after existing allocation's from date");
			}

			existing.setIsActive("N");
			existing.setModifiedBy(request.getModifiedBy());
			allocationRepository.save(existing);
		}

		Optional<CustomerBranchAllocation> duplicateCheck = allocationRepository
				.findByCustomerCustomerIdAndSiteIdAndFromDate(request.getCustomerId(), request.getSiteId(),
						request.getFromDate());

		if (duplicateCheck.isPresent()) {
			throw new ResourceAlreadyExistsException(
					"Allocation already exists for the given customer, site, and from date");
		}

		CustomerMasterEntity customer = customerRepository.findById(request.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		CustomerBranchAllocation newAllocation = new CustomerBranchAllocation();
		newAllocation.setCustomer(customer);
		newAllocation.setSiteId(request.getSiteId());
		newAllocation.setFromDate(request.getFromDate());
		newAllocation.setIsActive("Y");
		newAllocation.setCreatedBy(request.getCreatedBy());

		CustomerBranchAllocation saved = allocationRepository.save(newAllocation);

		CustomerBranchAllocationResponse res = new CustomerBranchAllocationResponse();
		res.setAllocationId(saved.getAllocationId());
		res.setCustomerId(saved.getCustomer().getCustomerId());
		res.setSiteId(saved.getSiteId());
		res.setSiteName(site.getSiteName());
		res.setFromDate(saved.getFromDate());
		res.setIsActive(saved.getIsActive());

		return new ResponseEntity("Customer branch allocation saved successfully", 200, res);

	}

	public ResponseEntity getActiveByCustomerId(Integer customerId) {

		CustomerBranchAllocation allocation = allocationRepository.findByCustomerCustomerIdAndIsActive(customerId, "Y")
				.orElse(null);

		if (allocation == null) {
			return new ResponseEntity("Active allocation not found", 404, null);
		}

		CustomerBranchAllocationResponse response = new CustomerBranchAllocationResponse();

		response.setAllocationId(allocation.getAllocationId());
		response.setCustomerId(allocation.getCustomer().getCustomerId());
		response.setCustomerName(allocation.getCustomer().getCustomerName());
		response.setSiteId(allocation.getSiteId());
		CompSiteResponse site = validationService.validateAndGetSite(allocation.getSiteId(), "Customer");
		response.setSiteName(site.getSiteName());
		response.setFromDate(allocation.getFromDate());
		response.setIsActive(allocation.getIsActive());
		response.setCreatedOn(allocation.getCreatedOn());
		response.setModifiedOn(allocation.getModifiedOn());

		return new ResponseEntity("Active allocation fetched successfully", 200, response);
	}

	@Override
	public ResponseEntity getCustomerBranchAllocationById(Integer allocationId) {
		try {
			CustomerBranchAllocation allocation = allocationRepository.findById(allocationId)
					.orElseThrow(() -> new RuntimeException("Allocation not found"));

			List<CompSiteResponse> activeSites = validationService.getAllSites();
			Map<Integer, String> siteMap = activeSites.stream()
					.collect(Collectors.toMap(CompSiteResponse::getSiteId, CompSiteResponse::getSiteName));

			CustomerBranchAllocationResponse res = mapToResponseWithSiteMap(allocation, siteMap);

			Map<String, Object> responsePayload = new HashMap<>();
			responsePayload.put("allocation", res);

			return new ResponseEntity("Allocation fetched successfully", 200, responsePayload);

		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), 404, null);
		}
	}

	@Override
	public ResponseEntity getAllCustomerBranchAllocations(int page, int size) {
		try {
			Page<CustomerBranchAllocation> allocationsPage = allocationRepository.findAll(PageRequest.of(page, size));
			List<CustomerBranchAllocation> allocations = allocationsPage.getContent();

			List<CompSiteResponse> activeSites = validationService.getAllSites();
			Map<Integer, String> siteMap = activeSites.stream()
					.collect(Collectors.toMap(CompSiteResponse::getSiteId, CompSiteResponse::getSiteName));

			List<CustomerBranchAllocationResponse> responses = allocations.stream()
					.map(allocation -> mapToResponseWithSiteMap(allocation, siteMap)).collect(Collectors.toList());

			Map<String, Object> paginationData = new HashMap<>();
			paginationData.put("content", responses);
			paginationData.put("currentPage", allocationsPage.getNumber());
			paginationData.put("totalItems", allocationsPage.getTotalElements());
			paginationData.put("totalPages", allocationsPage.getTotalPages());

			return new ResponseEntity("Allocations fetched successfully", 200, paginationData);

		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), 500, null);
		}
	}

	@Override
	public ResponseEntity filterCustomerBranchAllocations(Integer customerId, Integer siteId, String isActive,
			String fromDateStr, int page, int size) {
		try {
			LocalDate fromDate = null;
			if (fromDateStr != null && !fromDateStr.isBlank()) {
				fromDate = LocalDate.parse(fromDateStr);
			}

			Page<CustomerBranchAllocation> allocationsPage = allocationRepository.findAll(
					CustomerBranchAllocationSpecification.filter(customerId, siteId, isActive, fromDate),
					PageRequest.of(page, size));

			List<CustomerBranchAllocation> allocations = allocationsPage.getContent();

			List<CompSiteResponse> activeSites = validationService.getAllSites();
			Map<Integer, String> siteMap = activeSites.stream()
					.collect(Collectors.toMap(CompSiteResponse::getSiteId, CompSiteResponse::getSiteName));

			List<CustomerBranchAllocationResponse> responses = allocations.stream()
					.map(allocation -> mapToResponseWithSiteMap(allocation, siteMap)).collect(Collectors.toList());

			Map<String, Object> paginationData = new HashMap<>();
			paginationData.put("content", responses);
			paginationData.put("currentPage", allocationsPage.getNumber());
			paginationData.put("totalItems", allocationsPage.getTotalElements());
			paginationData.put("totalPages", allocationsPage.getTotalPages());

			return new ResponseEntity("Filtered allocations fetched successfully", 200, paginationData);

		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), 500, null);
		}
	}

	private CustomerBranchAllocationResponse mapToResponseWithSiteMap(CustomerBranchAllocation allocation,
			Map<Integer, String> siteMap) {
		CustomerBranchAllocationResponse res = new CustomerBranchAllocationResponse();
		res.setAllocationId(allocation.getAllocationId());
		res.setCustomerId(allocation.getCustomer().getCustomerId());
		res.setCustomerName(allocation.getCustomer().getCustomerName());
		res.setSiteId(allocation.getSiteId());
		res.setSiteName(siteMap.getOrDefault(allocation.getSiteId(), "Unknown"));
		res.setFromDate(allocation.getFromDate());
		res.setIsActive(allocation.getIsActive());
		res.setCreatedOn(allocation.getCreatedOn());
		res.setModifiedOn(allocation.getModifiedOn());
		return res;
	}
}