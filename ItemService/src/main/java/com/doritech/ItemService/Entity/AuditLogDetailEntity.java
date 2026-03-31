package com.doritech.ItemService.Entity;

import java.time.LocalDateTime;

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
@Table(name = "audit_log_detail")
public class AuditLogDetailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detail_id")
	private Integer detailId;

	// ===============================
	// FOREIGN KEY MAPPING
	// ===============================

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "audit_id", nullable = false)
	private AuditLogEntity auditLog;

	@Column(name = "field_name", length = 100)
	private String fieldName;

	@Column(name = "old_value", columnDefinition = "TEXT")
	private String oldValue;

	@Column(name = "new_value", columnDefinition = "TEXT")
	private String newValue;

	@Column(name = "created_on")
	private LocalDateTime createdOn;

	// ===============================
	// GETTERS & SETTERS
	// ===============================

	public Integer getDetailId() {
		return detailId;
	}

	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}

	public AuditLogEntity getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(AuditLogEntity auditLog) {
		this.auditLog = auditLog;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
}