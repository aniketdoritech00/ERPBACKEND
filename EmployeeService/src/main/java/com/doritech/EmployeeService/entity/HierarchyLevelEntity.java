package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "hierarchy_level")
public class HierarchyLevelEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "hierarchy_level_id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "hierarchy_id", nullable = false)
	@JsonBackReference
	private HierarchyMasterEntity hierarchy;

	@Column(name = "level_number", nullable = false)
	private Integer levelNumber;

	@Column(name = "level_name", nullable = false, length = 100)
	private String levelName;

	@Column(name = "is_end_node")
	private Boolean endNode = false;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
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
	 * @return the hierarchy
	 */
	public HierarchyMasterEntity getHierarchy() {
		return hierarchy;
	}

	/**
	 * @param hierarchy
	 *            the hierarchy to set
	 */
	public void setHierarchy(HierarchyMasterEntity hierarchy) {
		this.hierarchy = hierarchy;
	}

	/**
	 * @return the levelNumber
	 */
	public Integer getLevelNumber() {
		return levelNumber;
	}

	/**
	 * @param levelNumber
	 *            the levelNumber to set
	 */
	public void setLevelNumber(Integer levelNumber) {
		this.levelNumber = levelNumber;
	}

	/**
	 * @return the levelName
	 */
	public String getLevelName() {
		return levelName;
	}

	/**
	 * @param levelName
	 *            the levelName to set
	 */
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	/**
	 * @return the endNode
	 */
	public Boolean getEndNode() {
		return endNode;
	}

	/**
	 * @param endNode
	 *            the endNode to set
	 */
	public void setEndNode(Boolean endNode) {
		this.endNode = endNode;
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
