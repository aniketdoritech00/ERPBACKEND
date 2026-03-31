package com.doritech.EmployeeService.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "client_id", nullable = false)
    private Integer clientId;

    @Column(name = "table_name", length = 100, nullable = false)
    private String tableName;

    @Column(name = "record_id")
    private Integer recordId;

    @Column(name = "operation_type", length = 20, nullable = false)
    private String operationType;   

    @Column(name = "old_value", columnDefinition = "LONGTEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "LONGTEXT")
    private String newValue;

    @Column(name = "changed_fields", columnDefinition = "LONGTEXT")
    private String changedFields;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    @Column(name = "operation_date")
    private LocalDateTime operationDate;

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

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
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
    
    
    
}