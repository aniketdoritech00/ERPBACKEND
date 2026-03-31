package com.doritech.EmployeeService.response;

import java.time.LocalDateTime;

public class MenuFunctionalityResponse {

    private Integer menuFuncId;
    private Integer menuId;
    private String functionality;
    private String isActive;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private Integer createdBy;
    private Integer modifiedBy;

    public MenuFunctionalityResponse() {
    }

    public Integer getMenuFuncId() {
        return menuFuncId;
    }

    public void setMenuFuncId(Integer menuFuncId) {
        this.menuFuncId = menuFuncId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
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
}