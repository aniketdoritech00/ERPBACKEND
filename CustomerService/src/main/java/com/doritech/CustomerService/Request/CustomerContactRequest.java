package com.doritech.CustomerService.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerContactRequest {
	private Integer custContId;
	@Size(max = 100, message = "{contact.person.size}")
	private String contactPerson;

	@Size(max = 255, message = "{contact.email.size}")
	@Email(message = "{contact.email.invalid}")
	private String email;

	@Size(max = 20, message = "{contact.phone.size}")
	private String phone;

	@Size(max = 2, message = "{contact.designation.size}")
	private String designation;

	@Size(max = 2, message = "{contact.role.size}")
	private String role;

	@Size(max = 2, message = "{contact.department.size}")
	private String department;

	@NotBlank(message = "{contact.isActive.notblank}")
	@Size(max = 1, message = "{contact.isActive.size}")
	@Pattern(regexp = "Y|N", message = "{contact.isActive.invalid}")
	private String isActive;

	// @NotNull(message = "{contact.createdBy.notnull}")
	private Integer createdBy;

	private Integer modifiedBy;
	@NotNull(message = "{contact.customerId.notnull}")
	private Integer customerId;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
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

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}

	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getCustContId() {
		return custContId;
	}

	public void setCustContId(Integer custContId) {
		this.custContId = custContId;
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