package com.doritech.CustomerService.Controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.EmployeeAssignmentRequest;
import com.doritech.CustomerService.Response.UserResponse;
import com.doritech.CustomerService.Service.EmployeeAssignmentService;
import com.doritech.CustomerService.ValidationService.ValidationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/employeeAssignment")
public class EmployeeAssignmentController {

	@Autowired
	private EmployeeAssignmentService assignmentService;

	@Autowired
	private ValidationService validationService;

	@PostMapping("/saveEmployeeAssignment")
	public ResponseEntity saveEmployeeAssignment(
			@Valid @RequestBody EmployeeAssignmentRequest request,
			@RequestHeader("X-User-Id") String userId,

			HttpServletRequest httpServletRequest) throws BadRequestException {

		request.setCreatedBy(Integer.parseInt(userId));

		return new ResponseEntity("Employee Assignment save Successfully",
				HttpStatus.OK.value(),
				assignmentService.saveEmployeeAssignment(request));
	}

	@PutMapping("/updateEmployeeAssignmentStatus/{assignmentId}")
	public ResponseEntity updateEmployeeAssignmentStatus(
			@PathVariable Integer assignmentId,
			@RequestBody EmployeeAssignmentRequest request,
			@RequestHeader("X-User-Id") String userId,

			HttpServletRequest httpServletRequest) throws BadRequestException {

		request.setModifiedBy(Integer.parseInt(userId));

		return new ResponseEntity("Employee Assignment Status updated successfully",
				HttpStatus.OK.value(),
				assignmentService.updateEmployeeAssignmentStatus(assignmentId, request));
	}

	@PostMapping("/saveBulkEmployeeAssignment")
	public ResponseEntity saveBulkEmployeeAssignment(
			@Valid @RequestBody List<EmployeeAssignmentRequest> requests,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest httpServletRequest) {
		Integer userIdInt = Integer.parseInt(userId);

		requests.forEach(req -> req.setCreatedBy(userIdInt));

		return new ResponseEntity("Employee Assignments saved successfully",
				HttpStatus.OK.value(),
				assignmentService.saveBulkEmployeeAssignment(requests));
	}

	@GetMapping("/getEmployeeAssignments")
	public ResponseEntity getEmployeeAssignments(
			@RequestParam(required = false) Integer employeeId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size,
			@RequestParam(defaultValue = "assignmentId") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir, @RequestHeader("X-User-Id") String userId) {

		return new ResponseEntity("Assignments fetched successfully",
				HttpStatus.OK.value(), assignmentService.getEmployeeAssignments(
						employeeId, page, size, sortBy, sortDir));
	}

	@GetMapping("/getEmployeeAssignmentsByUserId")
	public ResponseEntity getEmployeeAssignments(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int size,
			@RequestParam(defaultValue = "assignmentId") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir, @RequestHeader("X-User-Id") String userId) {

		Integer userIdInt = Integer.parseInt(userId);

		UserResponse userResponse = validationService.validateAndGetUser(userIdInt);

		return new ResponseEntity("Assignments fetched successfully",
				HttpStatus.OK.value(), assignmentService.getEmployeeAssignments(
						userResponse.getSourceId(), page, size, sortBy, sortDir));
	}

	@GetMapping("/customer-details")
	public ResponseEntity getCustomerDetails(
			@RequestParam Integer assignmentId) {
		return assignmentService.getCustomerDetailsByAssignmentId(assignmentId);
	}

	@GetMapping("/updateStatusAfterPdfGenerate")
	public ResponseEntity updateStatusAfterPdfGenerate(@RequestParam Integer assignmentId) {
		return assignmentService.updateStatusAfterPdfGenerate(assignmentId);
	}

	@GetMapping("/updateVerifyStatus")
	public ResponseEntity updateVerifyStatus(@RequestParam Integer assignmentId, @RequestParam String verifyStatus,@RequestParam(required = false) String verifyRemark,
			@RequestHeader("X-User-Id") String userId) {
		return assignmentService.updateVerifyStatus(assignmentId, verifyStatus, verifyRemark, Integer.parseInt(userId));

	}

	@GetMapping("/getAssignmentByIds")
	public ResponseEntity getAssignmentByIds(@RequestParam List<Integer> assignmentIds) {
		return new ResponseEntity("Assignments fetched successfully",
				HttpStatus.OK.value(), assignmentService.getAssignmentByIds(assignmentIds));
	}
}
