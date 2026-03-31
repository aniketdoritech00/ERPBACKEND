package com.doritech.ItemService.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.doritech.ItemService.Request.GodownTransferRequestDTO;
import com.doritech.ItemService.Response.BatchTransactionResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;
import com.doritech.ItemService.Response.TransactionResponseDTO;

public interface GodownTransactionService {

	Object transferStock(GodownTransferRequestDTO dto);

	Object approveTransfer(Integer batchId);

	List<TransactionResponseDTO> getAllTransactionsByBatchId(Integer batchId);

	List<TransactionResponseDTO> updateTransactionById(Integer batchId,
			GodownTransferRequestDTO dto);

	PageResponseDTO<TransactionResponseDTO> getAllTransactions(int page,
			int size);

	List<BatchTransactionResponseDTO> getAllTransactionsBatchWise();

	PageResponseDTO<TransactionResponseDTO> filterTransactions(Integer batchId,
			LocalDateTime transactionDate, LocalDateTime fromDate,
			LocalDateTime toDate, String transactionType, String status,
			Integer godownId, Integer itemId, int page, int size);
}