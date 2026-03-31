package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu_master")
public class MenuMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Integer menuId;

	@Column(name = "menu_name", nullable = false, length = 100)
	private String menuName;

	@Column(name = "parent_menu_id")
	private Integer parentMenuId;

	@Column(name = "is_active", nullable = false, length = 1)
	private String isActive;

	@Lob
	@Column(name = "icon_image")
	private byte[] iconImage;

	@Column(name = "path", length = 255)
	private String path;

	@Column(name = "sequence_no", nullable = false)
	private Integer sequence;

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

	@OneToMany(mappedBy = "menuMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<MenuFunctionality> functionalities;

	public MenuMaster() {
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getParentMenuId() {
		return parentMenuId;
	}

	public byte[] getIconImage() {
		return iconImage;
	}

	public void setIconImage(byte[] iconImage) {
		this.iconImage = iconImage;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public void setParentMenuId(Integer parentMenuId) {
		this.parentMenuId = parentMenuId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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

	public List<MenuFunctionality> getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(List<MenuFunctionality> functionalities) {
		this.functionalities = functionalities;
	}
}