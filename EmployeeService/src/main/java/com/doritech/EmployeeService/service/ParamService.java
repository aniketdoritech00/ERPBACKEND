package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.request.ParamRequestDTO;
import com.doritech.EmployeeService.response.ParamResponseDTO;

public interface ParamService {

	ParamResponseDTO save(ParamRequestDTO dto);
	List<ParamResponseDTO> saveAllParamRecords(List<ParamRequestDTO> dtos);

	ParamResponseDTO update(Integer id, ParamRequestDTO dto);

	ParamResponseDTO getById(Integer id);

	List<ParamResponseDTO> getByCode(String code);

	List<ParamResponseDTO> getByCodeAndSerial(String code, String serial);

	void deleteById(Integer id);

	void deleteByCode(String code);
}