package com.doritech.EmployeeService.serviceImp;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.entity.AuditLogEntity;
import com.doritech.EmployeeService.repository.AuditLogRepository;

import tools.jackson.databind.ObjectMapper;

@Service
public class AuditService {

	 private final AuditLogRepository repository;
	    private final ObjectMapper objectMapper;

	    public AuditService(AuditLogRepository repository, ObjectMapper objectMapper) {
	        this.repository = repository;
	        this.objectMapper = objectMapper;
	    }

	    public void logAudit(
	            Integer userId,
	            Integer clientId,
	            String tableName,
	            Integer recordId,
	            String operationType,
	            Object oldData,
	            Object newData,
	            String changedFields,
	            String ipAddress,
	            String sessionId
	    ) {
	        try {
	            AuditLogEntity audit = new AuditLogEntity();

	            audit.setUserId(userId);
	            audit.setClientId(clientId);
	            audit.setTableName(tableName);
	            audit.setRecordId(recordId);
	            audit.setOperationType(operationType);
	            audit.setChangedFields(changedFields);
	            audit.setIpAddress(ipAddress);
	            audit.setSessionId(sessionId);
	            audit.setOperationDate(LocalDateTime.now());

	            if (oldData != null) {
	                audit.setOldValue(objectMapper.writeValueAsString(oldData));
	            }

	            if (newData != null) {
	                audit.setNewValue(objectMapper.writeValueAsString(newData));
	            }

	            repository.save(audit);

	        } catch (Exception e) {
	            // Use logger in real project
	            e.printStackTrace();
	        }
	    }
	}