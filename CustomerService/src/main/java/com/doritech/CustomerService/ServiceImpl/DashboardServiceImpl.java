package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.FeignClient.EmployeeFeignClient;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Repository.QuotationMasterRepository;
import com.doritech.CustomerService.Service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	@Autowired
	private ContractMasterRepository contractMasterRepository;

	@Autowired
	private QuotationMasterRepository quotationMasterRepository;

	@Autowired
	private EmployeeFeignClient employeeFeignClient;

	public ResponseEntity getCustomerCountSummary() {

	    log.info("Fetching customer count summary");

	    ResponseEntity response = new ResponseEntity();

	    try {

	        long totalCustomer = customerMasterRepository.count();
	        long activeCustomer = customerMasterRepository.countByIsActive("Y");
	        long inactiveCustomer = customerMasterRepository.countByIsActive("N");

	        log.info("Customer counts fetched - Total: {}, Active: {}, Inactive: {}",
	                totalCustomer, activeCustomer, inactiveCustomer);

	        long activeContract = contractMasterRepository.countByIsActive("Y");
	        log.info("Active contracts count: {}", activeContract);

	        long sendQuotations = quotationMasterRepository.countByStatus("SE");
	        long createdQuotations = quotationMasterRepository.countByStatus("CR");
	        long approvedQuotations = quotationMasterRepository.countByStatus("AP");

	        log.info("Quotation counts - Sent: {}, Created: {}, Approved: {}",
	                sendQuotations, createdQuotations, approvedQuotations);

	        Long employeeCount = 0L;

	        try {
	            ResponseEntity empResponse = employeeFeignClient.getEmployeeCount();

	            if (empResponse != null && empResponse.getPayload() != null) {
	                employeeCount = Long.valueOf(empResponse.getPayload().toString());
	                log.info("Employee count fetched successfully: {}", employeeCount);
	            } else {
	                log.warn("Employee response is null or payload is empty");
	            }

	        } catch (Exception ex) {
	            log.error("Error while calling Employee Service: {}", ex.getMessage(), ex);
	            employeeCount = 0L;
	        }

	        Map<String, Object> data = new HashMap<>();
	        data.put("totalCustomers", totalCustomer);
	        data.put("activeCustomers", activeCustomer);
	        data.put("inactiveCustomers", inactiveCustomer);
	        data.put("activeContract", activeContract);
	        data.put("sendQuotations", sendQuotations);
	        data.put("createdQuotations", createdQuotations);
	        data.put("approvedQuotations", approvedQuotations);
	        data.put("totalEmployee", employeeCount);

	        response.setMessage("Dashboard data fetched successfully");
	        response.setStatusCode(HttpStatus.OK.value());
	        response.setPayload(data);

	    } catch (Exception e) {

	        log.error("Critical error while fetching dashboard summary: {}", e.getMessage(), e);

	        response.setMessage("Failed to fetch dashboard data");
	        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        response.setPayload(null);
	    }

	    return response;
	}
	
	public ResponseEntity getMonthlyContractSummary(int year) {

	    log.info("Fetching monthly contract summary for year: {}", year);

	    ResponseEntity response = new ResponseEntity();

	    try {

	        List<Map<String, Object>> monthlyData = new ArrayList<>();

	        for (int month = 1; month <= 12; month++) {

	            LocalDate startDate = LocalDate.of(year, month, 1);
	            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

	            long started = contractMasterRepository
	                    .countByContractStartDateBetween(startDate, endDate);

	            long ended = contractMasterRepository
	                    .countByContractEndDateBetween(startDate, endDate);

	            Map<String, Object> map = new HashMap<>();
	            map.put("month", startDate.getMonth().toString());
	            map.put("startCount", started);
	            map.put("endCount", ended);

	            monthlyData.add(map);
	        }

	        response.setMessage("Monthly contract data fetched successfully");
	        response.setStatusCode(200);
	        response.setPayload(monthlyData);

	    } catch (Exception e) {
	        log.error("Error while fetching monthly contract summary: {}", e.getMessage(), e);

	        response.setMessage("Failed to fetch monthly contract data");
	        response.setStatusCode(500);
	        response.setPayload(null);
	    }

	    return response;
	}
}
