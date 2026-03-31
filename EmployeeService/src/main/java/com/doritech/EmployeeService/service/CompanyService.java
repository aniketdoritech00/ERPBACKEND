package com.doritech.EmployeeService.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.EmployeeService.request.CompanyRequestDTO;
import com.doritech.EmployeeService.response.CompanyResponseDTO;
import com.doritech.EmployeeService.response.PageResponseDTO;

public interface CompanyService {

	CompanyResponseDTO saveCompany(CompanyRequestDTO dto);

	CompanyResponseDTO updateCompany(Integer id, CompanyRequestDTO dto);

	CompanyResponseDTO getCompanyById(Integer id);

	void deleteById(Integer id);

	PageResponseDTO<CompanyResponseDTO> getAllCompany(int page, int size);

	PageResponseDTO<CompanyResponseDTO> companyfilter(String companyName,
			String companyCode, String contactPerson, String email,
			String phone, String city, String state, String country,
			String postalCode, String active, Long createdBy,
			LocalDate fromDate, LocalDate toDate, int page, int size);

	byte[] saveCompaniesFromExcel(MultipartFile file);

	List<CompanyResponseDTO> getByCompanyName(String name);

	CompanyResponseDTO getByCompanyCode(String code);

	List<CompanyResponseDTO> getByCity(String city);

	List<CompanyResponseDTO> getByPostalCode(String postalCode);

	List<CompanyResponseDTO> getAllActiveCompanies();

	// CompanyResponseDTO existCompanyCode(String param);

	boolean existCompanyCode(String companyCode);

	void deleteMultiple(List<Integer> ids);

}
