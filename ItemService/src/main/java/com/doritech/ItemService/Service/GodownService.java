package com.doritech.ItemService.Service;

import java.util.List;

import com.doritech.ItemService.Request.GodownRequestDTO;
import com.doritech.ItemService.Response.GodownResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;

public interface GodownService {

	GodownResponseDTO save(GodownRequestDTO dto);

	GodownResponseDTO update(Integer id, GodownRequestDTO dto);

	GodownResponseDTO getById(Integer id);

	PageResponseDTO<GodownResponseDTO> getAll(int page, int size);

	List<GodownResponseDTO> getAllGodowns();

	PageResponseDTO<GodownResponseDTO> filter(String name, String code,
			String type, String active, int page, int size);

	void deleteMultiple(List<Integer> ids);
}