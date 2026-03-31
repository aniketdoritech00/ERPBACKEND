package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RoleMenuFunctionalityRequest {

    @NotNull(message = "{roleMenuFunc.menuFuncId.notnull}")
    private Integer menuFuncId;

    @NotNull(message = "{roleMenuFunc.isActive.notnull}")
    @Size(min = 1, max = 1, message = "{roleMenuFunc.isActive.size}")
    private String isActive;

    public Integer getMenuFuncId() {
        return menuFuncId;
    }

    public void setMenuFuncId(Integer menuFuncId) {
        this.menuFuncId = menuFuncId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}