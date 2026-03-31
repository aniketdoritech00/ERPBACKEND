package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserMasterRequest {

    @NotBlank(message = "{user.userType.required}")
    @Size(max = 1, message = "{user.userType.size}")
    private String userType;

    @NotNull(message = "{user.sourceId.required}")
    private Integer sourceId;

    @NotBlank(message = "{user.loginId.required}")
    @Size(max = 255, message = "{user.loginId.size}")
    private String loginId;

    @NotBlank(message = "{user.password.required}")
    private String password;

    @NotNull(message = "{user.roleId.required}")
    private Integer roleId;

    @Size(max = 1, message = "{user.isActive.size}")
    private String isActive;

    private Integer createdBy;

    private Integer modifiedBy;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
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