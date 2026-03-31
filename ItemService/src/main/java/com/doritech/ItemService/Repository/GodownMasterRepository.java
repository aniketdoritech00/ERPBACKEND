package com.doritech.ItemService.Repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.doritech.ItemService.Entity.GodownMasterEntity;

public interface GodownMasterRepository
		extends
			JpaRepository<GodownMasterEntity, Integer>,
			JpaSpecificationExecutor<GodownMasterEntity> {

	boolean existsByGodownCode(String godownCode);

	List<GodownMasterEntity> findByGodownNameNotIn(List<String> excludedGodowns,
			Sort ascending);

	List<GodownMasterEntity> findByGodownNameNotInAndIsActive(
			List<String> excludedGodowns, String string, Sort ascending);
}