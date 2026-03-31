package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "role_menu_functionality")
public class RoleMenuFunctionality {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_menu_func_id", nullable = false)
	private Integer roleMenuFuncId;

	// 🔹 Mapping with RoleMenuAccess
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_role_menu_func_role_menu"))
	private RoleMenuAccess roleMenuAccess;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_func_id", nullable = false, foreignKey = @ForeignKey(name = "fk_role_menu_func_menu_func"))
	private MenuFunctionality menuFunctionality;

	@Column(name = "is_active", nullable = false, length = 1)
	private String isActive;

	@CreationTimestamp
	@Column(name = "created_on", nullable = false, updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	public RoleMenuFunctionality() {
	}

	// ===================== Getters & Setters =====================

	public Integer getRoleMenuFuncId() {
		return roleMenuFuncId;
	}

	public void setRoleMenuFuncId(Integer roleMenuFuncId) {
		this.roleMenuFuncId = roleMenuFuncId;
	}

	public RoleMenuAccess getRoleMenuAccess() {
		return roleMenuAccess;
	}

	public void setRoleMenuAccess(RoleMenuAccess roleMenuAccess) {
		this.roleMenuAccess = roleMenuAccess;
	}

	public MenuFunctionality getMenuFunctionality() {
		return menuFunctionality;
	}

	public void setMenuFunctionality(MenuFunctionality menuFunctionality) {
		this.menuFunctionality = menuFunctionality;
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

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
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