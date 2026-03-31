package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.request.OrganizationRequestDTO;
import com.doritech.EmployeeService.response.OrganizationResponseDTO;
import com.doritech.EmployeeService.response.PageResponseDTO;

public interface OrganizationService {

	OrganizationResponseDTO saveOrganization(OrganizationRequestDTO dto);

	OrganizationResponseDTO updateOrganization(Integer id,
			OrganizationRequestDTO dto);

	OrganizationResponseDTO getByOrganizationId(Integer id);

	PageResponseDTO<OrganizationResponseDTO> getAllOrganization(int page,
			int size);

	void deleteOrganization(Integer id);

	PageResponseDTO<OrganizationResponseDTO> filterOrganization(String orgName,
			String active, int page, int size);

	List<OrganizationResponseDTO> getAllActiveOrganizations();

	void deleteMultiple(List<Integer> ids);

}
