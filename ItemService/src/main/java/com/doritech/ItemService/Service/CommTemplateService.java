package com.doritech.ItemService.Service;

import java.util.List;

import com.doritech.ItemService.Request.CommTemplateRequestDTO;
import com.doritech.ItemService.Response.CommTemplateResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;

public interface CommTemplateService {

	CommTemplateResponseDTO saveTemplate(CommTemplateRequestDTO dto);

	CommTemplateResponseDTO updateTemplate(Integer id,
			CommTemplateRequestDTO dto);

	void deleteTemplate(Integer id);

	CommTemplateResponseDTO getTemplateById(Integer id);

	List<CommTemplateResponseDTO> getAllTemplate();

	PageResponseDTO<CommTemplateResponseDTO> getAllTemplateWithPagination(
			int page, int size);

	List<CommTemplateResponseDTO> filterTemplate(Integer customerId,
			String commType, String templateType, String isActive);
}