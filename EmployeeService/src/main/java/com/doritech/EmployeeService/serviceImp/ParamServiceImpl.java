package com.doritech.EmployeeService.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.entity.ParamEntity;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.ParamRepository;
import com.doritech.EmployeeService.request.ParamRequestDTO;
import com.doritech.EmployeeService.response.ParamResponseDTO;
import com.doritech.EmployeeService.service.ParamService;

@Service
public class ParamServiceImpl implements ParamService {

	@Autowired
	private ParamRepository repository;

	private ParamResponseDTO mapToDTO(ParamEntity entity) {
		ParamResponseDTO dto = new ParamResponseDTO();
		dto.setParamId(entity.getParamId());
		dto.setCode(entity.getCode());
		dto.setSerial(entity.getSerial());
		dto.setDesp1(entity.getDesp1());
		dto.setDesp2(entity.getDesp2());
		dto.setDesp3(entity.getDesp3());
		dto.setDesp4(entity.getDesp4());
		dto.setDesp5(entity.getDesp5());
		dto.setSerialNo(entity.getSerialNo());
		return dto;
	}

	@Override
	public ParamResponseDTO save(ParamRequestDTO dto) {

		ParamEntity entity = new ParamEntity();
		entity.setCode(dto.getCode());
		entity.setSerial(dto.getSerial());
		entity.setDesp1(dto.getDesp1());
		entity.setDesp2(dto.getDesp2());
		entity.setDesp3(dto.getDesp3());
		entity.setDesp4(dto.getDesp4());
		entity.setDesp5(dto.getDesp5());
		entity.setSerialNo(dto.getSerialNo());

		return mapToDTO(repository.save(entity));
	}

	@Override
	public List<ParamResponseDTO> saveAllParamRecords(
			List<ParamRequestDTO> dtos) {

		List<ParamResponseDTO> list = new ArrayList<>();

		List<ParamEntity> entities = new ArrayList<>();

		for (ParamRequestDTO dto : dtos) {

			ParamEntity entity = new ParamEntity();
			entity.setCode(dto.getCode());
			entity.setSerial(dto.getSerial());
			entity.setDesp1(dto.getDesp1());
			entity.setDesp2(dto.getDesp2());
			entity.setDesp3(dto.getDesp3());
			entity.setDesp4(dto.getDesp4());
			entity.setDesp5(dto.getDesp5());
			entity.setSerialNo(dto.getSerialNo());

			entities.add(entity);

			list.add(mapToDTO(entity));

		}

		repository.saveAll(entities);

		return list;
	}

	@Override
	public ParamResponseDTO update(Integer id, ParamRequestDTO dto) {

		ParamEntity entity = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Param not found"));

		entity.setCode(dto.getCode());
		entity.setSerial(dto.getSerial());
		entity.setDesp1(dto.getDesp1());
		entity.setDesp2(dto.getDesp2());
		entity.setDesp3(dto.getDesp3());
		entity.setDesp4(dto.getDesp4());
		entity.setDesp5(dto.getDesp5());
		entity.setSerialNo(dto.getSerialNo());

		return mapToDTO(repository.save(entity));
	}

	@Override
	public ParamResponseDTO getById(Integer id) {
		ParamEntity entity = repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Param not found"));
		return mapToDTO(entity);
	}

	@Override
	public List<ParamResponseDTO> getByCode(String code) {
		return repository.findByCode(code).stream().map(this::mapToDTO)
				.toList();
	}

	@Override
	public List<ParamResponseDTO> getByCodeAndSerial(String code,
			String serial) {

		List<ParamEntity> entities = repository
				.findByCodeIgnoreCaseAndSerialIgnoreCaseAndDesp3IgnoreCase(code,
						serial, "Y",
						Sort.by(Sort.Order.asc("serialNo").nullsLast()));

		if (entities.isEmpty()) {
			throw new ResourceNotFoundException("Data not found.");
		}

		return entities.stream().map(this::mapToDTO).toList();
	}

	@Override
	public void deleteById(Integer id) {
		if (!repository.existsById(id))
			throw new ResourceNotFoundException("Param not found");

		repository.deleteById(id);
	}

	@Override
	public void deleteByCode(String code) {
		if (!repository.existsByCode(code))
			throw new ResourceNotFoundException("Param not found");
		repository.deleteByCode(code);
	}
}