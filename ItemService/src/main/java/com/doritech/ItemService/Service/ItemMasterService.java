package com.doritech.ItemService.Service;

import java.util.List;

import com.doritech.ItemService.Request.ItemMasterRequestDTO;
import com.doritech.ItemService.Response.ItemMasterResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;

public interface ItemMasterService {

	ItemMasterResponseDTO saveItem(ItemMasterRequestDTO dto);

	ItemMasterResponseDTO updateItem(Integer id, ItemMasterRequestDTO dto);

	ItemMasterResponseDTO getItemById(Integer id);

	PageResponseDTO<ItemMasterResponseDTO> getAllItems(int page, int size);

	List<ItemMasterResponseDTO> getAllItems();

	PageResponseDTO<ItemMasterResponseDTO> itemFilter(String itemName,
			String itemCode, String active, String itemType, String category,
			int page, int size);
	// Page<ItemMasterResponseDTO> itemFilter(String itemName, String itemCode,
	// String active, int page, int size);

	ItemMasterResponseDTO getByItemCode(String code);
	List<ItemMasterResponseDTO> getByItemType(String type);

	List<ItemMasterResponseDTO> getParentItem();

	List<ItemMasterResponseDTO> getComponemtItem();

	Boolean existItemCode(String code);

	void deleteById(Integer id);
	void deleteMultipleItems(List<Integer> ids);

	void deleteMultipleItemCode(List<String> codes);
}