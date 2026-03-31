package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.request.HierarchyLevelFilterDTO;
import com.doritech.EmployeeService.request.HierarchyLevelRequestDTO;
import com.doritech.EmployeeService.response.HierarchyLevelResponseDTO;
import com.doritech.EmployeeService.response.HierarchyMasterResponseDTO;
import com.doritech.EmployeeService.response.PageResponseDTO;

public interface HierarchyLevelService {

	HierarchyLevelResponseDTO save(HierarchyLevelRequestDTO dto);

	HierarchyLevelResponseDTO getLevelByHierarchylevelId(
			Integer hierarchylevelId);

	HierarchyLevelResponseDTO update(Integer id, HierarchyLevelRequestDTO dto);

	List<HierarchyLevelResponseDTO> saveMultipleHierarchyLevels(
			List<HierarchyLevelRequestDTO> dtoList);

	List<HierarchyLevelResponseDTO> updateMultipleHierarchyLevels(
			Integer hierarchyId, List<HierarchyLevelRequestDTO> dtoList);
	List<HierarchyLevelResponseDTO> getByHierarchy(Integer hierarchyId);

	PageResponseDTO<HierarchyLevelResponseDTO> getAllHierarchyLevel(int page,
			int size);

	HierarchyMasterResponseDTO getHierarchyByHierarchyLevel(
			Integer hierarchyLevelId);

	void delete(Integer id);

	void deleteMultiple(List<Integer> id);
	PageResponseDTO<HierarchyLevelResponseDTO> filterHierarchyLevel(
			HierarchyLevelFilterDTO dto);
}
