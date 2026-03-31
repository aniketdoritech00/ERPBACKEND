package com.doritech.ItemService.Entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_log")
public class AuditLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "audit_id")
	private Long auditId;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "table_name", nullable = false, length = 100)
	private String tableName;

	@Column(name = "record_id")
	private Integer recordId;

	@Column(name = "operation_type", nullable = false, length = 20)
	private String operationType;

	@Column(name = "old_value", columnDefinition = "TEXT")
	private String oldValue;

	@Column(name = "new_value", columnDefinition = "TEXT")
	private String newValue;

	@Column(name = "changed_fields", columnDefinition = "TEXT")
	private String changedFields;

	@Column(name = "ip_address", length = 50)
	private String ipAddress;

	@Column(name = "session_id", length = 255)
	private String sessionId;

	@Column(name = "operation_date")
	private LocalDateTime operationDate;

	// ===============================
	// RELATIONSHIP WITH DETAIL TABLE
	// ===============================

	@OneToMany(mappedBy = "auditLog", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AuditLogDetailEntity> details;

	// ===============================
	// GETTERS & SETTERS
	// ===============================

	public Long getAuditId() {
		return auditId;
	}

	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
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

	public String getChangedFields() {
		return changedFields;
	}

	public void setChangedFields(String changedFields) {
		this.changedFields = changedFields;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public LocalDateTime getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(LocalDateTime operationDate) {
		this.operationDate = operationDate;
	}

	public List<AuditLogDetailEntity> getDetails() {
		return details;
	}

	public void setDetails(List<AuditLogDetailEntity> details) {
		this.details = details;
	}
}