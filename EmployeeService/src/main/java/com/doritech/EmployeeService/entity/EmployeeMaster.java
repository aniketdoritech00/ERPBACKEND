package com.doritech.EmployeeService.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "employee_master", uniqueConstraints = {
		@UniqueConstraint(columnNames = "employee_code")})
public class EmployeeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private Integer employeeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comp_id", nullable = false)
	private CompanyEntity company;

	@Column(name = "employee_name", nullable = false, length = 255)
	private String employeeName;

	@Column(name = "employee_code", nullable = false, length = 50, unique = true)
	private String employeeCode;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "designation", length = 2)
	private String designation;

	@Column(name = "department", length = 2)
	private String department;

	@Column(name = "role", length = 2)
	private String role;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "city", length = 100)
	private String city;

	@Column(name = "district", length = 100)
	private String district;

	@Column(name = "state", length = 100)
	private String state;

	@Column(name = "country", length = 100)
	private String country;

	@Column(name = "postal_code", length = 20)
	private String postalCode;

	@Column(name = "parent_id", nullable = false)
	private Integer parent;

	@ManyToOne
	@JoinColumn(name = "site_id", nullable = false)
	private CompSiteMasterEntity site;

	@Column(name = "is_active", length = 1)
	private String isActive;

	@Column(name = "date_of_joining")
	private LocalDate dateOfJoining;

	@Column(name = "date_of_leaving")
	private LocalDate dateOfLeaving;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
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

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public CompSiteMasterEntity getSite() {
		return site;
	}

	public void setSite(CompSiteMasterEntity site) {
		this.site = site;
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

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
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

}