package com.doritech.ItemService.Specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.doritech.ItemService.Entity.GodownInventoryTransactionEntity;

public class TransactionSpecification {

	public static Specification<GodownInventoryTransactionEntity> filter(
			Integer batchId, LocalDateTime transactionDate,
			LocalDateTime fromDate, LocalDateTime toDate,
			String transactionType, String status, Integer godownId,
			Integer itemId) {

		return (root, query, cb) -> {

			var predicate = cb.conjunction();

			if (batchId != null) {
				predicate = cb.and(predicate,
						cb.equal(root.get("transactionBatchId"), batchId));
			}

			if (transactionDate != null) {
				predicate = cb.and(predicate,
						cb.equal(root.get("transactionDate"), transactionDate));
			}

			if (fromDate != null && toDate != null) {
				predicate = cb.and(predicate, cb.between(
						root.get("transactionDate"), fromDate, toDate));
			}

			if (transactionType != null && !transactionType.isBlank()) {
				predicate = cb.and(predicate,
						cb.equal(root.get("transactionType"), transactionType));
			}

			if (status != null && !status.isBlank()) {
				predicate = cb.and(predicate,
						cb.equal(root.get("status"), status));
			}

			if (godownId != null) {
				predicate = cb.and(predicate, cb.or(
						cb.equal(root.get("sourceGodown").get("godownId"),
								godownId),
						cb.equal(root.get("destinationGodown").get("godownId"),
								godownId)));
			}

			if (itemId != null) {
				predicate = cb.and(predicate,
						cb.equal(root.get("item").get("itemId"), itemId));
			}

			return predicate;
		};
	}
}