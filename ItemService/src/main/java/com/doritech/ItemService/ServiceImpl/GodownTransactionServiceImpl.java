package com.doritech.ItemService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.doritech.ItemService.Entity.GodownInventoryTransactionEntity;
import com.doritech.ItemService.Entity.GodownMasterEntity;
import com.doritech.ItemService.Entity.ItemMasterEntity;
import com.doritech.ItemService.Exception.BusinessException;
import com.doritech.ItemService.Exception.ResourceNotFoundException;
import com.doritech.ItemService.Mapper.TransactionMapper;
import com.doritech.ItemService.Repository.GodownInventoryTransactionRepository;
import com.doritech.ItemService.Repository.GodownMasterRepository;
import com.doritech.ItemService.Repository.ItemMasterRepository;
import com.doritech.ItemService.Request.GodownTransferItemDTO;
import com.doritech.ItemService.Request.GodownTransferRequestDTO;
import com.doritech.ItemService.Response.BatchTransactionResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;
import com.doritech.ItemService.Response.TransactionResponseDTO;
import com.doritech.ItemService.Service.GodownTransactionService;
import com.doritech.ItemService.Specification.TransactionSpecification;

import jakarta.transaction.Transactional;

@Service
public class GodownTransactionServiceImpl implements GodownTransactionService {

	@Autowired
	private GodownInventoryTransactionRepository transactionRepo;

	@Autowired
	private GodownMasterRepository godownRepo;

	@Autowired
	private ItemMasterRepository itemRepo;

	@Autowired
	private TransactionMapper transactionMapper;

	private static final Integer INTRANSIT_GODOWN_ID = 13;

	@Transactional
	@Override
	public Object transferStock(GodownTransferRequestDTO dto) {

		GodownMasterEntity source = godownRepo.findById(dto.getSourceGodownId())
				.orElseThrow(() -> new RuntimeException("Source Godown not found"));

		GodownMasterEntity destination = godownRepo.findById(dto.getDestinationGodownId())
				.orElseThrow(() -> new RuntimeException("Destination Godown not found"));

		Integer batchId = transactionRepo.findMaxBatchId() + 1;

		for (GodownTransferItemDTO itemDto : dto.getItems()) {

			ItemMasterEntity item = itemRepo.findById(itemDto.getItemId())
					.orElseThrow(() -> new RuntimeException("Item not found"));

			/*
			 * PURCHASE Purchase Godown → Head Godown
			 */
			if ("P".equalsIgnoreCase(dto.getTransactionType())) {

				GodownInventoryTransactionEntity purchaseTxn = new GodownInventoryTransactionEntity();

				purchaseTxn.setTransactionBatchId(batchId);
				purchaseTxn.setSourceGodown(source);
				purchaseTxn.setDestinationGodown(destination);
				purchaseTxn.setItem(item);
				purchaseTxn.setQuantity(itemDto.getQuantity());
				purchaseTxn.setTransactionType("P");
				purchaseTxn.setStatus("C");
				purchaseTxn.setTransactionDate(LocalDateTime.now());
				purchaseTxn.setRemarks(dto.getRemarks());
				purchaseTxn.setCreatedBy(dto.getCreatedBy());
				purchaseTxn.setCreatedOn(LocalDateTime.now());

				transactionRepo.save(purchaseTxn);
			}

			/*
			 * STOCK TRANSFER Source → InTransit → Destination
			 */
			else if ("T".equalsIgnoreCase(dto.getTransactionType())) {

				GodownMasterEntity inTransit = godownRepo.findById(INTRANSIT_GODOWN_ID)
						.orElseThrow(() -> new RuntimeException("InTransit Godown not found"));

				GodownInventoryTransactionEntity t1 = new GodownInventoryTransactionEntity();

				t1.setTransactionBatchId(batchId);
				t1.setSourceGodown(source);
				t1.setDestinationGodown(inTransit);
				t1.setItem(item);
				t1.setQuantity(itemDto.getQuantity());
				t1.setTransactionType("T");
				t1.setStatus("C");
				t1.setTransactionDate(LocalDateTime.now());
				t1.setRemarks(dto.getRemarks());
				t1.setCreatedBy(dto.getCreatedBy());
				t1.setCreatedOn(LocalDateTime.now());

				transactionRepo.save(t1);

				GodownInventoryTransactionEntity t2 = new GodownInventoryTransactionEntity();

				t2.setTransactionBatchId(batchId);
				t2.setSourceGodown(inTransit);
				t2.setDestinationGodown(destination);
				t2.setItem(item);
				t2.setQuantity(itemDto.getQuantity());
				t2.setTransactionType("T");
				t2.setStatus("P");
				t2.setRemarks(dto.getRemarks());
				t2.setCreatedBy(dto.getCreatedBy());
				t2.setCreatedOn(LocalDateTime.now());

				transactionRepo.save(t2);
			}

			/*
			 * SALES Head Godown → Employee Godown
			 */
			else if ("S".equalsIgnoreCase(dto.getTransactionType())) {

				GodownInventoryTransactionEntity saleTxn = new GodownInventoryTransactionEntity();

				saleTxn.setTransactionBatchId(batchId);
				saleTxn.setSourceGodown(source);
				saleTxn.setDestinationGodown(destination);
				saleTxn.setItem(item);
				saleTxn.setQuantity(itemDto.getQuantity());
				saleTxn.setTransactionType("S");
				saleTxn.setStatus("C");
				saleTxn.setTransactionDate(LocalDateTime.now());
				saleTxn.setRemarks(dto.getRemarks());
				saleTxn.setCreatedBy(dto.getCreatedBy());
				saleTxn.setCreatedOn(LocalDateTime.now());

				transactionRepo.save(saleTxn);
			}

		}

		return "Transaction Completed Successfully";
	}

	@Transactional
	@Override
	public List<TransactionResponseDTO> updateTransactionById(Integer batchId, GodownTransferRequestDTO dto) {

		List<GodownInventoryTransactionEntity> existingTransactions = transactionRepo.findByTransactionBatchId(batchId);

		if (existingTransactions.isEmpty()) {
			throw new ResourceNotFoundException("Transaction batch not found with this ID :- " + batchId);
		}

		// Validate godowns
		GodownMasterEntity sourceGodown = godownRepo.findById(dto.getSourceGodownId()).orElseThrow(
				() -> new ResourceNotFoundException("Source godown not found : " + dto.getSourceGodownId()));

		GodownMasterEntity destinationGodown = godownRepo.findById(dto.getDestinationGodownId()).orElseThrow(
				() -> new ResourceNotFoundException("Destination godown not found : " + dto.getDestinationGodownId()));

		if (sourceGodown.getGodownId().equals(destinationGodown.getGodownId())) {
			throw new BusinessException("Source and destination godown cannot be same");
		}

		// Map existing items
		Map<Integer, GodownInventoryTransactionEntity> existingMap = existingTransactions.stream()
				.collect(Collectors.toMap(e -> e.getItem().getItemId(), Function.identity()));

		List<GodownInventoryTransactionEntity> finalList = new ArrayList<>();

		for (GodownTransferItemDTO itemDTO : dto.getItems()) {

			ItemMasterEntity item = itemRepo.findById(itemDTO.getItemId())
					.orElseThrow(() -> new ResourceNotFoundException("Item not found : " + itemDTO.getItemId()));

			GodownInventoryTransactionEntity entity = existingMap.get(itemDTO.getItemId());

			if (entity != null) {

				// Existing item update
				entity.setSourceGodown(sourceGodown);
				entity.setDestinationGodown(destinationGodown);
				entity.setTransactionType(dto.getTransactionType());
				entity.setRemarks(dto.getRemarks());
				entity.setQuantity(itemDTO.getQuantity());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(LocalDateTime.now());

			} else {

				// New item insert
				entity = new GodownInventoryTransactionEntity();
				entity.setTransactionBatchId(batchId);
				entity.setSourceGodown(sourceGodown);
				entity.setDestinationGodown(destinationGodown);
				entity.setItem(item);
				entity.setTransactionType(dto.getTransactionType());
				entity.setQuantity(itemDTO.getQuantity());
				entity.setRemarks(dto.getRemarks());
				entity.setCreatedBy(dto.getModifiedBy());
				entity.setCreatedOn(LocalDateTime.now());
				entity.setStatus("ACTIVE");
			}

			finalList.add(entity);
		}

		// OPTIONAL: delete removed items
		List<Integer> requestItemIds = dto.getItems().stream().map(GodownTransferItemDTO::getItemId).toList();

		for (GodownInventoryTransactionEntity oldEntity : existingTransactions) {

			if (!requestItemIds.contains(oldEntity.getItem().getItemId())) {
				if (!"C".equalsIgnoreCase(oldEntity.getStatus())) {
					transactionRepo.delete(oldEntity);
				}
			}
		}

		transactionRepo.saveAll(finalList);

		return transactionMapper.toDTOList(finalList);
	}

	@Transactional
	@Override
	public Object approveTransfer(Integer batchId) {

		List<GodownInventoryTransactionEntity> transactions = transactionRepo.findByTransactionBatchIdAndStatus(batchId,
				"P");

		if (transactions.isEmpty()) {
			throw new RuntimeException("No pending transfer found");
		}

		for (GodownInventoryTransactionEntity txn : transactions) {

			txn.setStatus("C");
			txn.setTransactionDate(LocalDateTime.now());

			transactionRepo.save(txn);
		}

		return "Transfer Approved Successfully";
	}

	@Override
	public List<TransactionResponseDTO> getAllTransactionsByBatchId(Integer batchId) {

		List<GodownInventoryTransactionEntity> transactions = transactionRepo.findByTransactionBatchId(batchId);

		if (transactions.isEmpty()) {
			throw new RuntimeException("No transactions found for batchId: " + batchId);
		}

		return transactionMapper.toDTOList(transactions);
	}

	@Override
	public PageResponseDTO<TransactionResponseDTO> getAllTransactions(int page, int size) {

		Page<GodownInventoryTransactionEntity> resultPage = transactionRepo
				.findAll(PageRequest.of(page, size, Sort.by("transactionId").descending()));

		Page<TransactionResponseDTO> dtoPage = resultPage.map(transactionMapper::toDTO);

		return new PageResponseDTO<>(dtoPage);
	}

	@Override
	public PageResponseDTO<TransactionResponseDTO> filterTransactions(Integer batchId, LocalDateTime transactionDate,
			LocalDateTime fromDate, LocalDateTime toDate, String transactionType, String status, Integer godownId,
			Integer itemId, int page, int size) {

		Page<GodownInventoryTransactionEntity> resultPage = transactionRepo
				.findAll(
						TransactionSpecification.filter(batchId, transactionDate, fromDate, toDate, transactionType,
								status, godownId, itemId),
						PageRequest.of(page, size, Sort.by("transactionDate").descending()));

		Page<TransactionResponseDTO> dtoPage = resultPage.map(transactionMapper::toDTO);

		return new PageResponseDTO<>(dtoPage);
	}

	@Override
	public List<BatchTransactionResponseDTO> getAllTransactionsBatchWise() {

		List<GodownInventoryTransactionEntity> transactions = transactionRepo.findAll();

		Map<Integer, List<GodownInventoryTransactionEntity>> batchMap = transactions.stream()
				.collect(Collectors.groupingBy(GodownInventoryTransactionEntity::getTransactionBatchId));

		List<BatchTransactionResponseDTO> response = new ArrayList<>();

		for (Map.Entry<Integer, List<GodownInventoryTransactionEntity>> entry : batchMap.entrySet()) {

			BatchTransactionResponseDTO dto = new BatchTransactionResponseDTO();

			dto.setBatchId(entry.getKey());

			dto.setTransactions(transactionMapper.toDTOList(entry.getValue()));

			response.add(dto);
		}

		return response;
	}
}
