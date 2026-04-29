package com.doritech.CustomerService.ServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.InstallationExpenseMaster;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Repository.EmployeeAssignmentRepository;
import com.doritech.CustomerService.Repository.InstallationExpenseMasterRepository;
import com.doritech.CustomerService.Request.InstallationExpenseMasterRequest;
import com.doritech.CustomerService.Service.InstallationExpenseMasterService;

@Service
public class InstallationExpenseMasterServiceImpl implements InstallationExpenseMasterService {

	@Autowired
	private InstallationExpenseMasterRepository repository;

	@Autowired
	private EmployeeAssignmentRepository employeeAssignmentRepository;

	@Override
	public ResponseEntity saveOrUpdate(InstallationExpenseMasterRequest request) {

		try {
			InstallationExpenseMaster entity;

			if (request.getId() != null) {
				Optional<InstallationExpenseMaster> optional = repository.findById(request.getId());

				if (optional.isEmpty()) {
					return new ResponseEntity("Record not found", 404, null);
				}

				entity = optional.get();
			}
			// 👉 CREATE
			else {
				entity = new InstallationExpenseMaster();
			}

			// Mapping
			entity.setDisBranchDistt(request.getDisBranchDistt());
			entity.setBankBranchDistt(request.getBankBranchDistt());
			entity.setLocalFixedRate(request.getLocalFixedRate());
			entity.setIntercityPerKm(request.getIntercityPerKm());
			entity.setPvcPerMeter(request.getPvcPerMeter());
			entity.setBandPerNo(request.getBandPerNo());
			entity.setOtherAmount(request.getOtherAmount());
			entity.setStayAmount(request.getStayAmount());
			entity.setExternalHelper(request.getExternalHelper());
			entity.setLocalTransport(request.getLocalTransport());

			InstallationExpenseMaster saved = repository.save(entity);

			return new ResponseEntity(request.getId() == null ? "Saved successfully" : "Updated successfully", 200,
					saved);

		} catch (DataAccessException e) {
			return new ResponseEntity("Database error occurred", 500, e.getMostSpecificCause().getMessage());

		} catch (Exception e) {
			return new ResponseEntity("Something went wrong", 500, e.getMessage());
		}
	}

	@Override
	public ResponseEntity getCompletedAssignmentsExpense() {

		List<Object[]> result = employeeAssignmentRepository.getCompletedAssignmentsWithExpense();
		List<Map<String, Object>> responseList = new ArrayList<>();

		Map<String, Double> distanceMap = new HashMap<>();
		distanceMap.put("Noida-Ghaziabad", 25.0);
		distanceMap.put("Ghaziabad-Noida", 25.0);

		for (Object[] row : result) {

			Map<String, Object> map = new HashMap<>();

			// ================= BASIC IDS =================
			Integer assignmentId = ((Number) row[0]).intValue();
			Integer employeeId = ((Number) row[1]).intValue();

			// ================= EMPLOYEE =================
			String employeeName = (String) row[2];

			// ================= SITE =================
			Integer siteId = ((Number) row[3]).intValue();
			String siteName = (String) row[4];
			String employeeDistrict = (String) row[5];

			// ================= CUSTOMER =================
			Integer customerId = ((Number) row[6]).intValue();
			String customerDistrict = (String) row[7];

			// ================= VISIT =================
			String visitType = (String) row[8];

			// ================= RATE MASTER =================
			BigDecimal localFixedRate = (BigDecimal) row[9];
			BigDecimal intercityPerKm = (BigDecimal) row[10];
			BigDecimal pvcPerMeter = (BigDecimal) row[11];
			BigDecimal bandPerNo = (BigDecimal) row[12];
			BigDecimal externalHelper = (BigDecimal) row[13];
			BigDecimal stayAmount = (BigDecimal) row[14];

			// ================= INSTALLATION =================
			Integer pvcPipe = row[15] != null ? ((Number) row[15]).intValue() : 0;
			Integer pvcBend = row[16] != null ? ((Number) row[16]).intValue() : 0;

			Integer helperId = row[17] != null ? ((Number) row[17]).intValue() : null;

			// ================= DATES =================
			java.sql.Timestamp visitTs = (java.sql.Timestamp) row[18];
			java.sql.Timestamp modifiedTs = (java.sql.Timestamp) row[19];

			LocalDateTime visitDate = visitTs != null ? visitTs.toLocalDateTime() : null;
			LocalDateTime modifiedOn = modifiedTs != null ? modifiedTs.toLocalDateTime() : null;

			// ================= TRAVEL COST =================
			double travelCost = 0;

			if ("LO".equalsIgnoreCase(visitType)) {
				if (localFixedRate != null) {
					travelCost = localFixedRate.doubleValue();
				}
			} else if ("IC".equalsIgnoreCase(visitType)) {
				String key = employeeDistrict + "-" + customerDistrict;
				double distance = distanceMap.getOrDefault(key, 0.0);

				if (intercityPerKm != null) {
					travelCost = distance * intercityPerKm.doubleValue();
				}
			}

			// ================= HELPER COST =================
			double helperCost = 0;
			if (helperId != null && helperId > 0 && externalHelper != null) {
				helperCost = externalHelper.doubleValue();
			}

			// ================= BAND COST =================
			double bandCost = 0;
			if (bandPerNo != null) {
				bandCost = pvcBend * bandPerNo.doubleValue();
			}

			// ================= PVC COST =================
			double pvcCost = 0;
			if (pvcPerMeter != null) {
				pvcCost = pvcPipe * pvcPerMeter.doubleValue();
			}

			// ================= RETURN COST =================
			double returnCost = travelCost;

			// ================= STAY COST =================
			double stayCost = 0;

			if (visitDate != null && modifiedOn != null && stayAmount != null) {

				long days = java.time.temporal.ChronoUnit.DAYS.between(visitDate.toLocalDate(),
						modifiedOn.toLocalDate());

				if (days > 0) {
					stayCost = days * stayAmount.doubleValue();
				}
			}

			// ================= TOTAL EXPENSE =================
			double totalExpense = travelCost + helperCost + bandCost + pvcCost + returnCost + stayCost;

			// ================= RESPONSE MAP =================
			map.put("assignmentId", assignmentId);
			map.put("employeeId", employeeId);
			map.put("employeeName", employeeName);

			map.put("siteId", siteId);
			map.put("siteName", siteName);
			map.put("employeeDistrict", employeeDistrict);

			map.put("customerId", customerId);
			map.put("customerDistrict", customerDistrict);

			map.put("visitType", visitType);

			map.put("travelCost", travelCost);
			map.put("helperCost", helperCost);
			map.put("bandCost", bandCost);
			map.put("pvcCost", pvcCost);
			map.put("returnCost", returnCost);
			map.put("stayCost", stayCost);

			map.put("totalExpense", totalExpense);

			responseList.add(map);
		}

		return new ResponseEntity("Expense calculated successfully", 200, responseList);
	}
}