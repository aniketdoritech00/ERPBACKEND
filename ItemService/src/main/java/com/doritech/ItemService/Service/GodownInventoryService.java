package com.doritech.ItemService.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.doritech.ItemService.Request.GodownInventoryFilterDTO;
import com.doritech.ItemService.Request.GodownInventoryRequestDTO;
import com.doritech.ItemService.Response.GodownInventoryResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;

public interface GodownInventoryService {

	GodownInventoryResponseDTO save(GodownInventoryRequestDTO dto);

	GodownInventoryResponseDTO update(Integer id,
			GodownInventoryRequestDTO dto);

	PageResponseDTO<GodownInventoryResponseDTO> getAllGodownInventory(int page,
			int size);

	void delete(Integer id);

	void deleteMultiple(List<Integer> ids);

	PageResponseDTO<GodownInventoryResponseDTO> filter(
			GodownInventoryFilterDTO dto, Pageable pageable);

}