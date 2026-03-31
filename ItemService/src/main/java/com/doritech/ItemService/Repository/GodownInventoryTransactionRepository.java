package com.doritech.ItemService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.ItemService.Entity.GodownInventoryTransactionEntity;

@Repository
public interface GodownInventoryTransactionRepository
		extends
			JpaRepository<GodownInventoryTransactionEntity, Integer>,
			JpaSpecificationExecutor<GodownInventoryTransactionEntity> {

	@Query("SELECT COALESCE(MAX(t.transactionBatchId),0) FROM GodownInventoryTransactionEntity t")
	Integer findMaxBatchId();

	List<GodownInventoryTransactionEntity> findByTransactionBatchId(
			Integer batchId);

	List<GodownInventoryTransactionEntity> findByTransactionBatchIdAndStatus(
			Integer batchId, String status);

}