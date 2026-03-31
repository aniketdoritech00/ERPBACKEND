package com.doritech.ItemService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.doritech.ItemService.Entity.CommTemplateMasterEntity;

@Repository
public interface CommTemplateRepository
		extends
			JpaRepository<CommTemplateMasterEntity, Integer>,
			JpaSpecificationExecutor<CommTemplateMasterEntity> {

	boolean existsByTemplateNameAndCustomerId(String templateName,
			Integer customerId);
}