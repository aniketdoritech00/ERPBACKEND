package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.NotNull;

public class AttendanceCheckInRequest {

	private String address;

	@NotNull(message = "{attendance.latitude.required}")
	private Double latitude;

	@NotNull(message = "{attendance.longitude.required}")
	private Double longitude;

	private Integer createdBy;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
}