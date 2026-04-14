package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.CustomerEmployeeAllocation;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.CustomerEmployeeAllocationRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.CustomerEmployeeAllocationRequest;
import com.doritech.CustomerService.Response.CustomerEmployeeAllocationResponse;
import com.doritech.CustomerService.Response.EmployeeDTO;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Service.CustomerEmployeeAllocationService;
import com.doritech.CustomerService.Specification.CustomerEmployeeAllocationSpecification;
import com.doritech.CustomerService.ValidationService.ValidationService;

@Service
public class CustomerEmployeeAllocationServiceImpl
		implements
			CustomerEmployeeAllocationService {

	@Autowired
	private CustomerEmployeeAllocationRepository repo;

	@Autowired
	private CustomerMasterRepository customerRepo;

	@Autowired
	private ValidationService validationService;

	@Override
	public CustomerEmployeeAllocationResponse allocateEmployee(
			CustomerEmployeeAllocationRequest request) {

		CustomerMasterEntity customer = customerRepo
				.findById(request.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found"));

		EmployeeDTO emp = validationService
				.validateEmployeeExists(request.getEmployeeId());

		Optional<CustomerEmployeeAllocation> existingOpt = repo
				.findTopByCustomerCustomerIdAndIsActiveOrderByFromDateDesc(
						request.getCustomerId(), "Y");

		if (existingOpt.isPresent()) {
			CustomerEmployeeAllocation existing = existingOpt.get();

			// 🔥 SAME EMPLOYEE CHECK
			if (existing.getEmployeeId().equals(request.getEmployeeId())) {
				throw new BadRequestException(
						"Same employee already assigned to this customer");
			}

			// 🔥 DATE VALIDATION (NEW LOGIC)
			if (!request.getFromDate().isAfter(existing.getFromDate())) {
				throw new BadRequestException(
						"From date must be greater than last allocation date: "
								+ existing.getFromDate());
			}

			// make old inactive
			existing.setIsActive("N");
			existing.setModifiedBy(request.getCreatedBy());
			repo.save(existing);
		}

		// create new allocation
		CustomerEmployeeAllocation entity = new CustomerEmployeeAllocation();
		entity.setCustomer(customer);
		entity.setEmployeeId(request.getEmployeeId());
		entity.setFromDate(request.getFromDate());
		entity.setIsActive("Y");
		entity.setCreatedBy(request.getCreatedBy());

		CustomerEmployeeAllocation saved = repo.save(entity);

		CustomerEmployeeAllocationResponse res = new CustomerEmployeeAllocationResponse();

		res.setAllocationId(saved.getAllocationId());
		res.setCustomerId(saved.getCustomer().getCustomerId());
		res.setCustomerName(saved.getCustomer().getCustomerName());
		res.setEmployeeId(saved.getEmployeeId());
		res.setEmployeeName(emp.getEmployeeName());
		res.setFromDate(saved.getFromDate());
		res.setIsActive(saved.getIsActive());

		return res;
	}

	@Override
	public CustomerEmployeeAllocationResponse getActiveByCustomerId(
			Integer customerId) {

		CustomerEmployeeAllocation entity = repo
				.findByCustomerCustomerIdAndIsActive(customerId, "Y")
				.orElseThrow(() -> new ResourceNotFoundException(
						"Active allocation not found for this customer"));

		EmployeeDTO emp = validationService
				.validateEmployeeExists(entity.getEmployeeId());

		CustomerEmployeeAllocationResponse res = new CustomerEmployeeAllocationResponse();
		res.setAllocationId(entity.getAllocationId());
		res.setCustomerId(entity.getCustomer().getCustomerId());
		res.setCustomerName(entity.getCustomer().getCustomerName());
		res.setEmployeeId(entity.getEmployeeId());
		res.setEmployeeName(emp.getEmployeeName());
		res.setEmployeeCode(emp.getEmployeeCode());
		res.setFromDate(entity.getFromDate());
		res.setIsActive(entity.getIsActive());

		return res;
	}

	@Override
	public List<CustomerEmployeeAllocationResponse> getAllActive() {

		List<CustomerEmployeeAllocation> list = repo.findByIsActive("Y");

		if (list.isEmpty()) {
			throw new ResourceNotFoundException("No active employee allocations found");
		}

		List<EmployeeDTO> empList = validationService.validateEmployees();

		Map<Integer, EmployeeDTO> empMap = empList.stream()
				.collect(Collectors.toMap(EmployeeDTO::getEmployeeId, e -> e));

		return list.stream().map(entity -> {

			CustomerEmployeeAllocationResponse res = new CustomerEmployeeAllocationResponse();

			EmployeeDTO emp = empMap.get(entity.getEmployeeId());

			res.setAllocationId(entity.getAllocationId());
			res.setCustomerId(entity.getCustomer().getCustomerId());
			res.setCustomerName(entity.getCustomer().getCustomerName());
			res.setEmployeeId(entity.getEmployeeId());
			res.setEmployeeName(emp != null ? emp.getEmployeeName() : "N/A");
			res.setFromDate(entity.getFromDate());
			res.setIsActive(entity.getIsActive());

			return res;

		}).toList();
	}

	@Override
	public PageResponse<CustomerEmployeeAllocationResponse> filterAllocations(
			Integer customerId, Integer employeeId, String isActive,
			LocalDate fromDate, LocalDate toDate, Integer page, Integer size) {

		Pageable pageable = PageRequest.of(page, size,
				Sort.by("allocationId").descending());

		Specification<CustomerEmployeeAllocation> spec = CustomerEmployeeAllocationSpecification
				.filter(customerId, employeeId, isActive, fromDate, toDate);

		Page<CustomerEmployeeAllocation> pageResult = repo.findAll(spec,
				pageable);

		if (pageResult.isEmpty()) {
			throw new ResourceNotFoundException(
					"No active employee allocations found for given filters combination");
		}

		List<EmployeeDTO> empList = validationService.validateEmployees();

		Map<Integer, EmployeeDTO> empMap = empList.stream()
				.collect(Collectors.toMap(EmployeeDTO::getEmployeeId, e -> e));

		List<CustomerEmployeeAllocationResponse> responses = pageResult.stream()
				.map(entity -> {

					CustomerEmployeeAllocationResponse res = new CustomerEmployeeAllocationResponse();

					EmployeeDTO emp = empMap.get(entity.getEmployeeId());

					res.setAllocationId(entity.getAllocationId());
					res.setCustomerId(entity.getCustomer().getCustomerId());
					res.setCustomerName(entity.getCustomer().getCustomerName());
					res.setEmployeeId(entity.getEmployeeId());
					res.setEmployeeName(
							emp != null ? emp.getEmployeeName() : "N/A");
					res.setFromDate(entity.getFromDate());
					res.setIsActive(entity.getIsActive());

					return res;

				}).toList();

		return new PageResponse<CustomerEmployeeAllocationResponse>(responses,
				pageResult.getNumber(), pageResult.getSize(),
				pageResult.getTotalElements(), pageResult.getTotalPages(),
				pageResult.isLast());
	}

	@Override
	public PageResponse<CustomerEmployeeAllocationResponse> getAllAllocationWithPagination(
			Integer page, Integer size) {

		if (page == null || page < 0) {
			throw new IllegalArgumentException("Page number must be >= 0");
		}

		if (size == null || size <= 0 || size > 100) {
			throw new IllegalArgumentException(
					"Size must be between 1 and 100");
		}

		Pageable pageable = PageRequest.of(page, size,
				Sort.by("allocationId").descending());

		Page<CustomerEmployeeAllocation> pageResult = repo.findByIsActive("Y",
				pageable);

		if (pageResult.isEmpty()) {
			throw new ResourceNotFoundException("No active employee allocations found");
		}

		List<EmployeeDTO> empList = validationService.validateEmployees();

		Map<Integer, EmployeeDTO> empMap = empList.stream()
				.collect(Collectors.toMap(EmployeeDTO::getEmployeeId, e -> e));

		List<CustomerEmployeeAllocationResponse> list = pageResult.getContent()
				.stream().map(entity -> {

					CustomerEmployeeAllocationResponse res = new CustomerEmployeeAllocationResponse();

					EmployeeDTO emp = empMap.get(entity.getEmployeeId());

					res.setAllocationId(entity.getAllocationId());
					res.setCustomerId(entity.getCustomer().getCustomerId());
					res.setCustomerName(entity.getCustomer().getCustomerName());
					res.setEmployeeId(entity.getEmployeeId());
					res.setEmployeeName(
							emp != null ? emp.getEmployeeName() : "N/A");
					res.setFromDate(entity.getFromDate());
					res.setIsActive(entity.getIsActive());

					return res;

				}).toList();

		return new PageResponse<CustomerEmployeeAllocationResponse>(list,
				pageResult.getNumber(), pageResult.getSize(),
				pageResult.getTotalElements(), pageResult.getTotalPages(),
				pageResult.isLast());
	}

}