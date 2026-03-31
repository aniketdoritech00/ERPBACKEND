package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "role_menu_access")
public class RoleMenuAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_menu_id", nullable = false)
    private Integer roleMenuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleMaster roleMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuMaster menuMaster;

    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdOn;

    @Column(name = "modified_on",
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifiedOn;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @OneToMany(mappedBy = "roleMenuAccess",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private List<RoleMenuFunctionality> roleMenuFunctionalities;

	public Integer getRoleMenuId() {
		return roleMenuId;
	}

	public void setRoleMenuId(Integer roleMenuId) {
		this.roleMenuId = roleMenuId;
	}

	public RoleMaster getRoleMaster() {
		return roleMaster;
	}

	public void setRoleMaster(RoleMaster roleMaster) {
		this.roleMaster = roleMaster;
	}

	public MenuMaster getMenuMaster() {
		return menuMaster;
	}

	public void setMenuMaster(MenuMaster menuMaster) {
		this.menuMaster = menuMaster;
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

	public List<RoleMenuFunctionality> getRoleMenuFunctionalities() {
		return roleMenuFunctionalities;
	}

	public void setRoleMenuFunctionalities(List<RoleMenuFunctionality> roleMenuFunctionalities) {
		this.roleMenuFunctionalities = roleMenuFunctionalities;
	}
    
    
}