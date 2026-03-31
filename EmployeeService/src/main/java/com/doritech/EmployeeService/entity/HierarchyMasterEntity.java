package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "hierarchy_master")
public class HierarchyMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hierarchy_id")
	private Integer id;

	@NotBlank
	@Column(name = "hierarchy_name", length = 100)
	private String hierarchyName;

	@NotBlank
	@Column(name = "entity_type", length = 2)
	private String entityType;

	@ManyToOne
	@JoinColumn(name = "comp_id")
	private CompanyEntity company;

	@ManyToOne
	@JoinColumn(name = "org_id")
	private CustomerOrganizationEntity organization;

	@Column(name = "hierarchy_levels")
	private Integer hierarchyLevels;

	@Column(length = 500)
	private String description;

	@Column(name = "is_active", length = 2)
	private String active = "Y";

	@OneToMany(mappedBy = "hierarchy")
	@JsonManagedReference
	private List<HierarchyLevelEntity> levels;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@PrePersist
	void prePersist() {
		createdOn = LocalDateTime.now();
	}

	@PreUpdate
	void preUpdate() {
		modifiedOn = LocalDateTime.now();
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the hierarchyName
	 */
	public String getHierarchyName() {
		return hierarchyName;
	}

	/**
	 * @param hierarchyName
	 *            the hierarchyName to set
	 */
	public void setHierarchyName(String hierarchyName) {
		this.hierarchyName = hierarchyName;
	}

	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}

	/**
	 * @param entityType
	 *            the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	/**
	 * @return the company
	 */
	public CompanyEntity getCompany() {
		return company;
	}

	/**
	 * @param company
	 *            the company to set
	 */
	public void setCompany(CompanyEntity company) {
		this.company = company;
	}

	/**
	 * @return the organization
	 */
	public CustomerOrganizationEntity getOrganization() {
		return organization;
	}

	/**
	 * @param organization
	 *            the organization to set
	 */
	public void setOrganization(CustomerOrganizationEntity organization) {
		this.organization = organization;
	}

	/**
	 * @return the hierarchyLevels
	 */
	public Integer getHierarchyLevels() {
		return hierarchyLevels;
	}

	/**
	 * @param hierarchyLevels
	 *            the hierarchyLevels to set
	 */
	public void setHierarchyLevels(Integer hierarchyLevels) {
		this.hierarchyLevels = hierarchyLevels;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @return the levels
	 */
	public List<HierarchyLevelEntity> getLevels() {
		return levels;
	}

	/**
	 * @param levels
	 *            the levels to set
	 */
	public void setLevels(List<HierarchyLevelEntity> levels) {
		this.levels = levels;
	}

	/**
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the modifiedOn
	 */
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * @param modifiedOn
	 *            the modifiedOn to set
	 */
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}