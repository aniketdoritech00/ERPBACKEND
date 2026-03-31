package com.doritech.EmployeeService.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EmployeeRequest {

	@NotNull(message = "{employee.company.required}")
	private Integer companyId;

	@NotBlank(message = "{employee.name.required}")
	@Size(max = 255, message = "{employee.name.size}")
	private String employeeName;

	@NotBlank(message = "{employee.code.required}")
	@Size(max = 50, message = "{employee.code.size}")
	private String employeeCode;

	@Email(message = "{employee.email.invalid}")
	private String email;

	@Size(max = 20, message = "{employee.phone.size}")
	private String phone;

	@Size(max = 2, message = "{employee.designation.size}")
	private String designation;

	@Size(max = 2, message = "{employee.department.size}")
	private String department;

	@Size(max = 2, message = "{employee.role.size}")
	private String role;

	@Size(max = 500, message = "{employee.address.size}")
	private String address;

	@Size(max = 100, message = "{employee.city.size}")
	private String city;
	@Size(max = 100, message = "{employee.district.size}")
	private String district;

	@Size(max = 100, message = "{employee.state.size}")
	private String state;

	@Size(max = 100, message = "{employee.country.size}")
	private String country;

	@Size(max = 20, message = "{employee.postalCode.size}")
	private String postalCode;

	@NotNull(message = "{employee.parent.required}")
	private Integer parentId;

	@NotNull(message = "{employee.site.required}")
	private Integer siteId;

	@NotBlank(message = "{employee.active.required}")
	private String isActive;

	private LocalDate dateOfJoining;
	private LocalDate dateOfLeaving;

	private Integer createdBy;
	private Integer modifiedBy;

	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * @param district
	 *            the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public LocalDate getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(LocalDate dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public LocalDate getDateOfLeaving() {
		return dateOfLeaving;
	}

	public void setDateOfLeaving(LocalDate dateOfLeaving) {
		this.dateOfLeaving = dateOfLeaving;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}