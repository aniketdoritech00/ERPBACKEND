package com.doritech.ItemService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doritech.ItemService.Entity.AuditLogEntity;

public interface AuditLogRepository
		extends
			JpaRepository<AuditLogEntity, Integer> {

}
