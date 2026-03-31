package com.doritech.ItemService.Response;

import java.util.List;

public class BatchTransactionResponseDTO {

	private Integer batchId;

	private List<TransactionResponseDTO> transactions;

	/**
	 * @return the batchId
	 */
	public Integer getBatchId() {
		return batchId;
	}

	/**
	 * @param batchId
	 *            the batchId to set
	 */
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	/**
	 * @return the transactions
	 */
	public List<TransactionResponseDTO> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions
	 *            the transactions to set
	 */
	public void setTransactions(List<TransactionResponseDTO> transactions) {
		this.transactions = transactions;
	}

}