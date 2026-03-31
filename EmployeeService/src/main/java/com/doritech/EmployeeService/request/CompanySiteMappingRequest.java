package com.doritech.EmployeeService.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CompanySiteMappingRequest {

    @NotNull(message = "{sitemapping.compId.required}")
    private Integer compId;

    @NotNull(message = "{sitemapping.siteId.required}")
    private Integer siteId;

    private Boolean isPrimarySite;

    @Pattern(regexp = "^[YN]$", message = "{sitemapping.isActive.invalid}")
    private String isActive;

    private Integer createdBy;

    private Integer modifiedBy;

    public Integer getCompId() {
        return compId;
    }

    public void setCompId(Integer compId) {
        this.compId = compId;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Boolean getIsPrimarySite() {
        return isPrimarySite;
    }

    public void setIsPrimarySite(Boolean isPrimarySite) {
        this.isPrimarySite = isPrimarySite;
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