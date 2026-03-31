package com.doritech.EmployeeService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.doritech.EmployeeService.entity.AuditLogEntity;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
}