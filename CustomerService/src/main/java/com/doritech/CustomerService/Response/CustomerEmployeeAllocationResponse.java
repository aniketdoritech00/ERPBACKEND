package com.doritech.CustomerService.Response;

import java.time.LocalDate;

public class CustomerEmployeeAllocationResponse {

	private Integer allocationId;
	private Integer customerId;
	private String customerName;
	private Integer employeeId;
	private String employeeName;
	private String employeeCode;
	private LocalDate fromDate;
	private String isActive;

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName
	 *            the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName
	 *            the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * @return the allocationId
	 */
	public Integer getAllocationId() {
		return allocationId;
	}
	/**
	 * @param allocationId
	 *            the allocationId to set
	 */
	public void setAllocationId(Integer allocationId) {
		this.allocationId = allocationId;
	}
	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the employeeId
	 */
	public Integer getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId
	 *            the employeeId to set
	 */
	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the fromDate
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}
	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	
}