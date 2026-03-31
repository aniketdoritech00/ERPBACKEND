package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.request.HierarchyMasterFilterDTO;
import com.doritech.EmployeeService.request.HierarchyMasterRequestDTO;
import com.doritech.EmployeeService.response.HierarchyMasterResponseDTO;
import com.doritech.EmployeeService.response.PageResponseDTO;

public interface HierarchyMasterService {

	HierarchyMasterResponseDTO saveHierarchy(HierarchyMasterRequestDTO dto);
	HierarchyMasterResponseDTO updateHierarchy(Integer id,
			HierarchyMasterRequestDTO dto);
	HierarchyMasterResponseDTO getByHierarchyId(Integer id);
	List<HierarchyMasterResponseDTO> getAllHierarchy();

	PageResponseDTO<HierarchyMasterResponseDTO> getAllHierarchy(int page,
			int size);

	void deleteHierarchy(Integer id);

	void deleteMultipleHierarchy(List<Integer> ids);
	List<HierarchyMasterResponseDTO> getAllActiveHierarchies();

	PageResponseDTO<HierarchyMasterResponseDTO> filterHierarchy(
			HierarchyMasterFilterDTO dto);
}
