package com.doritech.EmployeeService.response;

import java.time.LocalDateTime;

public class CompanySiteMappingResponse {

    private Integer compSiteId;
    private Integer compId;
    private String companyName;  
    private String compCode;
    private Integer siteId;
    private String siteName;  
    private String siteCode;
    private Boolean isPrimarySite;
    private String isActive;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private Integer createdBy;
    private Integer modifiedBy;


    public Integer getCompSiteId() {
        return compSiteId;
    }

    public void setCompSiteId(Integer compSiteId) {
        this.compSiteId = compSiteId;
    }

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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
}