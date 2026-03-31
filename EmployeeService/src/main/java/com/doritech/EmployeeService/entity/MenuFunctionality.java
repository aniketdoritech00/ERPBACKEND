package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "menu_functionality")
public class MenuFunctionality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_func_id")
    private Integer menuFuncId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuMaster menuMaster;

    @Column(name = "functionality", nullable = false, length = 2)
    private String functionality;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    public MenuFunctionality() {
    }

    // Getters & Setters

    public Integer getMenuFuncId() {
        return menuFuncId;
    }

    public void setMenuFuncId(Integer menuFuncId) {
        this.menuFuncId = menuFuncId;
    }

    public MenuMaster getMenuMaster() {
        return menuMaster;
    }

    public void setMenuMaster(MenuMaster menuMaster) {
        this.menuMaster = menuMaster;
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

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
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