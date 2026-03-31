package com.doritech.ItemService.Service;

import java.util.List;

import com.doritech.ItemService.Request.BomDetailRequestDTO;
import com.doritech.ItemService.Response.BomDetailResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;

public interface BomDetailService {

	BomDetailResponseDTO saveBomDetail(BomDetailRequestDTO dto);

	BomDetailResponseDTO updateBomDetail(Integer id, BomDetailRequestDTO dto);

	BomDetailResponseDTO getBomDetailById(Integer id);

	PageResponseDTO<BomDetailResponseDTO> getAllBomDetails(int page, int size);

	PageResponseDTO<BomDetailResponseDTO> filterBomDetails(Integer bomId,
			Integer rawId, String active, int page, int size);

	List<BomDetailResponseDTO> getBomDetailByBomId(Integer bomId);

	Boolean existBomCombination(Integer bomId, Integer rawId);

	void deleteById(Integer id);

	void deleteMultiple(List<Integer> ids);
}