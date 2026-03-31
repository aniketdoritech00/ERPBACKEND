package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MenuFunctionalityRequest {

    @NotBlank(message = "{menuFunctionality.functionality.required}")
    @Size(min = 1, max = 100, message = "{menuFunctionality.functionality.size}")
    private String functionality;

    @NotBlank(message = "{menuFunctionality.isActive.required}")
    @Size(min = 1, max = 100, message = "{menuFunctionality.isActive.size}")
    private String isActive;

    private Integer createdBy;

    private Integer modifiedBy;

    public MenuFunctionalityRequest() {
    }

    public String getFunctionality() {
        return functionality;
    }

    public void setFunctionality(String functionality) {
        this.functionality = functionality;
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

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}