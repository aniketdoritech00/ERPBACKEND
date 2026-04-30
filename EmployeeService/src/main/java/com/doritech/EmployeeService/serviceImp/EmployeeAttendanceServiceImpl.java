package com.doritech.EmployeeService.serviceImp;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.entity.EmployeeAttendance;
import com.doritech.EmployeeService.entity.EmployeeMaster;
import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.entity.UserMaster;
import com.doritech.EmployeeService.exception.AlreadyCheckedOutException;
import com.doritech.EmployeeService.exception.AttendanceNotFoundException;
import com.doritech.EmployeeService.exception.EmployeeNotFoundException;
import com.doritech.EmployeeService.repository.EmployeeAttendanceRepo;
import com.doritech.EmployeeService.repository.EmployeeMasterRepository;
import com.doritech.EmployeeService.repository.UserMasterRepository;
import com.doritech.EmployeeService.request.AttendanceCheckInRequest;
import com.doritech.EmployeeService.request.AttendanceCheckOutRequest;
import com.doritech.EmployeeService.response.AttendanceResponse;
import com.doritech.EmployeeService.service.EmployeeAttendanceService;

@Service
public class EmployeeAttendanceServiceImpl implements EmployeeAttendanceService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeAttendanceServiceImpl.class);

	@Autowired
	private EmployeeAttendanceRepo attendanceRepository;

	@Autowired
	private UserMasterRepository userMasterRepository;
	@Autowired
	private EmployeeMasterRepository employeeRepository;

	public ResponseEntity markCheckIn(AttendanceCheckInRequest request) {

		logger.info("Check-in request received for userId={}", request.getCreatedBy());

		UserMaster userMaster = userMasterRepository.findById(request.getCreatedBy()).orElseThrow(() -> {
			logger.error("User not found for id={}", request.getCreatedBy());
			return new EmployeeNotFoundException("User not found");
		});

		Integer employeeId = userMaster.getSourceId();

		EmployeeMaster employee = employeeRepository.findById(employeeId).orElseThrow(() -> {
			logger.error("Employee not found for id={}", employeeId);
			return new EmployeeNotFoundException("Employee not found");
		});

		LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

		EmployeeAttendance existingAttendance = attendanceRepository
				.findByEmployee_EmployeeIdAndCheckInTimeBetweenAndCheckOutTimeIsNull(employeeId, startOfDay, endOfDay);

		if (existingAttendance != null) {
			logger.warn("Employee already checked in for employeeId={}", employeeId);
			return new ResponseEntity("Employee already checked in for today", 200, existingAttendance.getId());
		}

		EmployeeAttendance attendance = new EmployeeAttendance();
		attendance.setEmployee(employee);
		attendance.setAddress(request.getAddress());
		attendance.setLatitude(request.getLatitude());
		attendance.setLongitude(request.getLongitude());
		attendance.setStatus("CHECK_IN");
		attendance.setCreatedBy(request.getCreatedBy());
		attendance.setCheckInTime(LocalDateTime.now());
		attendance.setCreatedOn(LocalDateTime.now());

		attendanceRepository.save(attendance);

		logger.info("Check-in successful for employeeId={}, attendanceId={}", employeeId, attendance.getId());

		return new ResponseEntity("Check-in successful", 200, attendance.getId());
	}

	public ResponseEntity markCheckOut(AttendanceCheckOutRequest request) {

		logger.info("Check-out request received for attendanceId={}", request.getAttendanceId());

		UserMaster userMaster = userMasterRepository.findById(request.getCreatedBy()).orElseThrow(() -> {
			logger.error("User not found for id={}", request.getCreatedBy());
			return new EmployeeNotFoundException("User not found");
		});

		Integer employeeId = userMaster.getSourceId();

		EmployeeAttendance attendance = attendanceRepository.findById(request.getAttendanceId()).orElseThrow(() -> {
			logger.error("Attendance not found for id={}", request.getAttendanceId());
			return new AttendanceNotFoundException("Attendance not found");
		});

		if (!attendance.getEmployee().getEmployeeId().equals(employeeId)) {
			logger.error("Attendance does not belong to employeeId={}", employeeId);
			throw new AttendanceNotFoundException("Attendance not found for this employee");
		}

		if (attendance.getCheckOutTime() != null) {
			logger.warn("Already checked out for attendanceId={}", request.getAttendanceId());
			throw new AlreadyCheckedOutException("Already checked out");
		}

		attendance.setCheckOutTime(LocalDateTime.now());
		attendance.setStatus("CHECK_OUT");
		attendanceRepository.save(attendance);

		logger.info("Check-out successful for attendanceId={}", request.getAttendanceId());

		return new ResponseEntity("Check-out successful", 200, attendance.getId());
	}

	@Override
	public ResponseEntity getAttendanceRecord(String month, Integer userId, Integer siteId) {

		YearMonth yearMonth;
		try {
			yearMonth = (month != null) ? YearMonth.parse(month) : YearMonth.now();
		} catch (Exception e) {
			logger.error("Invalid month format={}", month);
			return new ResponseEntity("Invalid month format. Use yyyy-MM", 400, null);
		}

		LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

		List<EmployeeAttendance> attendanceList;

		if (userId != null) {

			UserMaster userMaster = userMasterRepository.findById(userId).orElse(null);
			if (userMaster == null) {
				logger.error("User not found for id={}", userId);
				return new ResponseEntity("User not found", 404, null);
			}

			Integer employeeId = userMaster.getSourceId();
			if (employeeId == null) {
				logger.error("Source employee not linked for userId={}", userId);
				return new ResponseEntity("Employee not linked to this user", 404, null);
			}

			boolean employeeExists = employeeRepository.existsById(employeeId);
			if (!employeeExists) {
				logger.error("Employee not found for id={}", employeeId);
				return new ResponseEntity("Employee not found", 404, null);
			}

			attendanceList = attendanceRepository.findByEmployee_EmployeeIdAndCheckInTimeBetween(employeeId, start,
					end);

		} else if (siteId != null) {

			attendanceList = attendanceRepository.findByEmployee_Site_SiteIdAndCheckInTimeBetween(siteId, start, end);

			if (attendanceList.isEmpty()) {
				logger.warn("No attendance found for siteId={}", siteId);
				return new ResponseEntity("No attendance found for this site", 404, null);
			}

		} else {
			attendanceList = attendanceRepository.findByCheckInTimeBetween(start, end);
		}

		if (attendanceList.isEmpty()) {
			return new ResponseEntity("No attendance record found for " + yearMonth, 200, null);
		}

		List<AttendanceResponse> responseList = attendanceList.stream().map(this::mapToResponse)
				.collect(Collectors.toList());

		logger.info("Attendance fetched successfully count={} for yearMonth={}", responseList.size(), yearMonth);

		return new ResponseEntity("Attendance fetched successfully for " + yearMonth, 200, responseList);
	}

	private AttendanceResponse mapToResponse(EmployeeAttendance attendance) {
		AttendanceResponse response = new AttendanceResponse();
		response.setId(attendance.getId());
		response.setEmployeeId(attendance.getEmployee().getEmployeeId());
		response.setEmployeeName(attendance.getEmployee().getEmployeeName());
		response.setAddress(attendance.getAddress());
		response.setLatitude(attendance.getLatitude());
		response.setLongitude(attendance.getLongitude());
		response.setCheckInTime(attendance.getCheckInTime());
		response.setCheckOutTime(attendance.getCheckOutTime());
		response.setStatus(attendance.getStatus());
		response.setCreatedBy(attendance.getCreatedBy());
		response.setCreatedOn(attendance.getCreatedOn());
		return response;
	}
}