package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.AmcExpenseMaster;
import com.doritech.CustomerService.Entity.AmcExpenseMultiplier;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Repository.AmcExpenseMasterRepository;
import com.doritech.CustomerService.Repository.AmcExpenseRangeRepository;
import com.doritech.CustomerService.Repository.EmployeeAssignmentRepository;
import com.doritech.CustomerService.Request.AmcExpenseRequest;
import com.doritech.CustomerService.Service.AmcExpenseService;
//
//@Service
//public class AmcExpenseServiceImpl implements AmcExpenseService {
//
//	private static final Logger logger = LoggerFactory.getLogger(AmcExpenseServiceImpl.class);
//
//	private static final String DEFAULT_DISTRICT = "Noida";
//
//	@Autowired
//	private EmployeeAssignmentRepository employeeAssignmentRepository;
//
//	@Autowired
//	private AmcExpenseMasterRepository expenseMasterRepository;
//
//	@Autowired
//	private AmcExpenseRangeRepository multiplierRepository;
//
//	@Override
//	public List<Map<String, Object>> getAllAssignmentExpense(Integer employeeId, LocalDateTime startDate,
//			LocalDateTime endDate) {
//
//		List<Map<String, Object>> finalResult = new ArrayList<>();
//
//		try {
//
//			if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
//				logger.warn("StartDate is after EndDate. Swapping values.");
//				LocalDateTime temp = startDate;
//				startDate = endDate;
//				endDate = temp;
//			}
//
//			logger.info("Fetching data for EmployeeId: {}, StartDate: {}, EndDate: {}", employeeId, startDate, endDate);
//
//			List<Object[]> results = employeeAssignmentRepository.findAllAssignmentItemsByFilter(employeeId, startDate,
//					endDate);
//
//			if (results == null || results.isEmpty()) {
//				logger.warn("No assignment data found for given filters");
//				return finalResult;
//			}
//
//			Map<Integer, Set<String>> assignmentCategoryMap = new HashMap<>();
//
//			for (Object[] row : results) {
//				try {
//					if (row == null || row.length < 2 || row[0] == null)
//						continue;
//
//					Integer assignmentId = ((Number) row[0]).intValue();
//					String category = row[1] != null ? row[1].toString() : "";
//
//					assignmentCategoryMap.computeIfAbsent(assignmentId, k -> new HashSet<>()).add(category);
//
//				} catch (Exception e) {
//					logger.error("Error processing row", e);
//				}
//			}
//
//			AmcExpenseMaster master = expenseMasterRepository.findByDistrict(DEFAULT_DISTRICT);
//
//			if (master == null) {
//				throw new RuntimeException("AMC master missing for district: " + DEFAULT_DISTRICT);
//			}
//
//			Integer basePrice = master.getDistrictBasePrice();
//
//			for (Map.Entry<Integer, Set<String>> entry : assignmentCategoryMap.entrySet()) {
//
//				try {
//					Integer assignmentId = entry.getKey();
//					int categoryCount = entry.getValue().size();
//
//					Integer multiplier = multiplierRepository.findMultiplier(categoryCount, master.getAmcExpenseId());
//
//					if (multiplier == null) {
//						multiplier = 1;
//					}
//
//					Integer totalExpense = basePrice * multiplier;
//
//					Map<String, Object> response = new HashMap<>();
//					response.put("assignmentId", assignmentId);
//					response.put("basePrice", basePrice);
//					response.put("multiplier", multiplier);
//					response.put("district", DEFAULT_DISTRICT);
//					response.put("totalExpense", totalExpense);
//
//					finalResult.add(response);
//
//				} catch (Exception e) {
//					logger.error("Error calculating expense for assignmentId: {}", entry.getKey(), e);
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error("Unexpected error in getAllAssignmentExpense()", e);
//			throw new RuntimeException("Failed to calculate AMC expenses", e);
//		}
//
//		return finalResult;
//	}
//}

@Service
public class AmcExpenseServiceImpl implements AmcExpenseService {

	private static final Logger logger = LoggerFactory.getLogger(AmcExpenseServiceImpl.class);

	private static final String DEFAULT_DISTRICT = " ";

	@Autowired
	private EmployeeAssignmentRepository employeeAssignmentRepository;

	@Autowired
	private AmcExpenseMasterRepository expenseMasterRepository;

	@Autowired
	private AmcExpenseRangeRepository multiplierRepository;
	
	@Override
	public List<Map<String, Object>> getAllAssignmentExpense(Integer employeeId, Integer siteId,
	        LocalDateTime startDate, LocalDateTime endDate) {

	    List<Map<String, Object>> finalResult = new ArrayList<>();

	    try {

	        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
	            logger.warn("StartDate is after EndDate. Swapping values.");
	            LocalDateTime temp = startDate;
	            startDate = endDate;
	            endDate = temp;
	        }

	        logger.info("Fetching data for EmployeeId: {}, StartDate: {}, EndDate: {}",
	                employeeId, startDate, endDate);

	        List<Object[]> results =
	                employeeAssignmentRepository.findAllAssignmentItemsWithDistrict(
	                        employeeId, siteId, startDate, endDate);

	        if (results == null || results.isEmpty()) {
	            logger.warn("No assignment data found");
	            return finalResult;
	        }

	        Map<Integer, Set<String>> assignmentCategoryMap = new HashMap<>();
	        Map<Integer, String> assignmentDistrictMap = new HashMap<>();
	        Map<Integer, Integer> assignmentEmployeeMap = new HashMap<>();
	        Map<Integer, String> assignmentEmployeeNameMap = new HashMap<>();
	        Map<Integer, Integer> assignmentSiteMap = new HashMap<>();
	        Map<Integer, String> assignmentSiteNameMap = new HashMap<>();
	        Map<Integer, LocalDateTime> assignmentVisitDateMap = new HashMap<>();

	        for (Object[] row : results) {
	            try {
	                if (row == null || row.length < 8) {
	                    continue;
	                }

	                // ================= INDEX MAPPING =================
	                Integer assignmentId = ((Number) row[0]).intValue();
	                Integer empId = ((Number) row[1]).intValue();
	                Integer siteIdFromRow = ((Number) row[2]).intValue();

	                // ✅ NEW FIELD
	                String siteName = row[3] != null ? row[3].toString() : null;

	                // ================= SAFE VISIT DATE =================
	                LocalDateTime visitDate = null;
	                Object dateObj = row[4];

	                if (dateObj != null) {
	                    if (dateObj instanceof java.sql.Timestamp) {
	                        visitDate = ((java.sql.Timestamp) dateObj).toLocalDateTime();
	                    } else if (dateObj instanceof LocalDateTime) {
	                        visitDate = (LocalDateTime) dateObj;
	                    } else if (dateObj instanceof String) {
	                        visitDate = LocalDateTime.parse((String) dateObj);
	                    }
	                }

	                String district = row[5] != null ? row[5].toString() : DEFAULT_DISTRICT;
	                String category = row[6] != null ? row[6].toString() : "";
	                String employeeName = row[7] != null ? row[7].toString() : "";

	                assignmentCategoryMap
	                        .computeIfAbsent(assignmentId, k -> new HashSet<>())
	                        .add(category);

	                assignmentDistrictMap.put(assignmentId, district);
	                assignmentEmployeeMap.put(assignmentId, empId);
	                assignmentEmployeeNameMap.put(assignmentId, employeeName);
	                assignmentSiteMap.put(assignmentId, siteIdFromRow);
	                assignmentSiteNameMap.put(assignmentId, siteName);
	                assignmentVisitDateMap.put(assignmentId, visitDate);

	            } catch (Exception e) {
	                logger.error("Error processing row", e);
	            }
	        }

	        for (Map.Entry<Integer, Set<String>> entry : assignmentCategoryMap.entrySet()) {

	            try {
	                Integer assignmentId = entry.getKey();
	                String district = assignmentDistrictMap
	                        .getOrDefault(assignmentId, DEFAULT_DISTRICT);

	                AmcExpenseMaster master = expenseMasterRepository.findByDistrict(district);

	                if (master == null) {
	                    throw new RuntimeException("AMC master missing for district: " + district);
	                }

	                int categoryCount = entry.getValue().size();

	                Integer multiplier = multiplierRepository
	                        .findMultiplier(categoryCount, master.getAmcExpenseId());

	                if (multiplier == null) {
	                    multiplier = 1;
	                }

	                Integer basePrice = master.getDistrictBasePrice();
	                Integer totalExpense = basePrice * multiplier;

	                Map<String, Object> response = new HashMap<>();
	                response.put("assignmentId", assignmentId);
	                response.put("employeeId", assignmentEmployeeMap.get(assignmentId));
	                response.put("employeeName", assignmentEmployeeNameMap.get(assignmentId));

	                response.put("siteId", assignmentSiteMap.get(assignmentId));
	                response.put("siteName", assignmentSiteNameMap.get(assignmentId)); // ✅ FIX ADDED

	                response.put("visitDate", assignmentVisitDateMap.get(assignmentId));
	                response.put("district", district);
	                response.put("basePrice", basePrice);
	                response.put("multiplier", multiplier);
	                response.put("totalExpense", totalExpense);
	                response.put("totalProducts", categoryCount);

	                finalResult.add(response);

	            } catch (Exception e) {
	                logger.error("Error calculating expense for assignmentId: {}", entry.getKey(), e);
	            }
	        }

	    } catch (Exception e) {
	        logger.error("Unexpected error in getAllAssignmentExpense()", e);
	        throw new RuntimeException("Failed to calculate AMC expenses", e);
	    }

	    return finalResult;
	}
	
	@Override
	public ResponseEntity saveOrUpdateAmcExpense(AmcExpenseRequest request) {

		try {
			AmcExpenseMaster master;

			if (request.getAmcExpenseId() != null) {
				Optional<AmcExpenseMaster> optionalMaster = expenseMasterRepository.findById(request.getAmcExpenseId());

				if (optionalMaster.isEmpty()) {
					return new ResponseEntity("AMC Expense not found with id: " + request.getAmcExpenseId(), 404, null);
				}
				master = optionalMaster.get();

			} else {
				master = new AmcExpenseMaster();
			}
			if (request.getDistrict() == null || request.getDistrict().isEmpty()) {
				return new ResponseEntity("District is required", 400, null);
			}
			master.setDistrict(request.getDistrict());
			master.setComplaintFootageMultiplier(request.getComplaintFootageMultiplier());
			master.setDistrictBasePrice(request.getDistrictBasePrice());

			master = expenseMasterRepository.save(master);

			List<AmcExpenseMultiplier> existingRanges = multiplierRepository.findByAmcExpenseMaster(master);

			Map<Integer, AmcExpenseMultiplier> existingMap = new HashMap<>();
			for (AmcExpenseMultiplier r : existingRanges) {
				existingMap.put(r.getId(), r);
			}

			List<AmcExpenseMultiplier> toSave = new ArrayList<>();

			if (request.getRanges() != null) {

				for (AmcExpenseRequest.RangeDto dto : request.getRanges()) {

					if (dto.getMinRange() > dto.getMaxRange()) {
						return new ResponseEntity("Invalid range: minRange cannot be greater than maxRange", 400, dto);
					}

					AmcExpenseMultiplier range;

					if (dto.getId() != null && existingMap.containsKey(dto.getId())) {
						range = existingMap.get(dto.getId());
					} else {
						range = new AmcExpenseMultiplier();
						range.setAmcExpenseMaster(master);
					}

					range.setMinRange(dto.getMinRange());
					range.setMaxRange(dto.getMaxRange());
					range.setMultiplier(dto.getMultiplier());

					toSave.add(range);
				}
			}

			multiplierRepository.saveAll(toSave);

			return new ResponseEntity(request.getAmcExpenseId() == null ? "AMC Expense created successfully"
					: "AMC Expense updated successfully", 200, master);

		} catch (Exception e) {
			return new ResponseEntity("Internal server error", 500, e.getMessage());
		}
	}
}