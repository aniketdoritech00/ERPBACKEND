package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class CompMasterRequest {

    @NotBlank(message = "{company.compName.required}")
    @Size(max = 255, message = "{company.compName.size}")
    private String compName;

    @NotBlank(message = "{company.compCode.required}")
    @Size(max = 50, message = "{company.compCode.size}")
    private String compCode;

    @Size(max = 255, message = "{company.contactPerson.size}")
    private String contactPerson;

    @Email(message = "{company.email.invalid}")
    @Size(max = 255, message = "{company.email.size}")
    private String email;

    @Size(max = 20, message = "{company.phone.size}")
    private String phone;

    @Size(max = 500, message = "{company.address.size}")
    private String address;

    @Size(max = 100, message = "{company.city.size}")
    private String city;

    @Size(max = 100, message = "{company.state.size}")
    private String state;

    @Size(max = 100, message = "{company.country.size}")
    private String country;

    @Size(max = 20, message = "{company.postalCode.size}")
    private String postalCode;

    private String isActive;

    @NotNull(message = "{company.createdBy.required}")
    private Integer createdBy;

    // Getters and Setters

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
}
