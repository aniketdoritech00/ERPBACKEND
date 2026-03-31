package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comp_site_mapping")
public class CompSiteMappingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comp_site_id")
    private Integer compSiteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comp_id", nullable = false)
    private CompanyEntity companyEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private CompSiteMasterEntity compSiteMaster;

    @Column(name = "is_active", length = 1)
    private String isActive;

    @CreationTimestamp
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;

	public Integer getCompSiteId() {
		return compSiteId;
	}

	public void setCompSiteId(Integer compSiteId) {
		this.compSiteId = compSiteId;
	}
    
	public CompanyEntity getCompanyEntity() {
		return companyEntity;
	}

	public void setCompanyEntity(CompanyEntity companyEntity) {
		this.companyEntity = companyEntity;
	}

	public CompSiteMasterEntity getCompSiteMaster() {
		return compSiteMaster;
	}

	public void setCompSiteMaster(CompSiteMasterEntity compSiteMaster) {
		this.compSiteMaster = compSiteMaster;
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
