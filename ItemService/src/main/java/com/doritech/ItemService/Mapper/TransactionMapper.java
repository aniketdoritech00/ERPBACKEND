package com.doritech.ItemService.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doritech.ItemService.Entity.GodownInventoryTransactionEntity;
import com.doritech.ItemService.Response.TransactionResponseDTO;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

	@Mapping(source = "transactionBatchId", target = "batchId")

	@Mapping(source = "sourceGodown.godownId", target = "sourceGodownId")
	@Mapping(source = "sourceGodown.godownName", target = "sourceGodownName")

	@Mapping(source = "destinationGodown.godownId", target = "destinationGodownId")
	@Mapping(source = "destinationGodown.godownName", target = "destinationGodownName")

	@Mapping(source = "item.itemId", target = "itemId")
	@Mapping(source = "item.itemName", target = "itemName")

	TransactionResponseDTO toDTO(GodownInventoryTransactionEntity entity);

	List<TransactionResponseDTO> toDTOList(
			List<GodownInventoryTransactionEntity> entities);

}