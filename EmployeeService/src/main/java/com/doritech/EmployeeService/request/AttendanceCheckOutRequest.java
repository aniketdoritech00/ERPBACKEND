package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.NotNull;

public class AttendanceCheckOutRequest {

	@NotNull(message = "{attendance.id.required}")
	private Integer attendanceId;

	private Integer createdBy;

	public Integer getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Integer attendanceId) {
		this.attendanceId = attendanceId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

}