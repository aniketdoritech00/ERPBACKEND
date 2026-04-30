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
	public ResponseEntity getAllEmployeeAttendanceRecord(String month) {

		YearMonth yearMonth = (month != null) ? YearMonth.parse(month) : YearMonth.now();

		LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
		LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

		List<AttendanceResponse> responseList = attendanceRepository.findByCheckInTimeBetween(start, end).stream()
				.map(this::mapToResponse).collect(Collectors.toList());

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
	
	@Override
	public ResponseEntity getEmployeeAttendanceRecord(String month, Integer userId) {

	    UserMaster userMaster = userMasterRepository.findById(userId).orElseThrow(() -> {
	        logger.error("User not found for id={}", userId);
	        return new EmployeeNotFoundException("User not found");
	    });

	    Integer employeeId = userMaster.getSourceId();

	    YearMonth yearMonth = (month != null) ? YearMonth.parse(month) : YearMonth.now();

	    LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
	    LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

	    List<AttendanceResponse> responseList = attendanceRepository
	            .findByEmployee_EmployeeIdAndCheckInTimeBetween(employeeId, start, end)
	            .stream()
	            .map(this::mapToResponse)
	            .collect(Collectors.toList());

	    return new ResponseEntity("Attendance fetched successfully for " + yearMonth, 200, responseList);
	}
}