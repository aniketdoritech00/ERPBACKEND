package com.doritech.EmployeeService.service;

import java.util.List;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.ParamRequestDTO;
import com.doritech.EmployeeService.response.PageResponse;
import com.doritech.EmployeeService.response.ParamResponseDTO;

public interface ParamService {

	ParamResponseDTO save(ParamRequestDTO dto);

	List<ParamResponseDTO> saveAllParamRecords(List<ParamRequestDTO> dtos);

	ParamResponseDTO update(Integer id, ParamRequestDTO dto);

	List<ParamResponseDTO> updateMultipleParam(List<ParamRequestDTO> paramList);

	ParamResponseDTO getById(Integer id);

	PageResponse<ParamResponseDTO> getAllParams(int page, int size);

	List<ParamResponseDTO> getByCode(String code);

	List<ParamResponseDTO> getByCodeAndSerial(String code, String serial);

	void deleteById(Integer id);

	void deleteByCode(String code);

	ResponseEntity generateCode(String type);

	ResponseEntity updateCodeValue(String code);
}