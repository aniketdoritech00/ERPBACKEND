package com.doritech.ItemService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.doritech.ItemService.Entity.BomDetailEntity;
import com.doritech.ItemService.Entity.ItemMasterEntity;
import com.doritech.ItemService.Exception.BusinessException;
import com.doritech.ItemService.Exception.ResourceNotFoundException;
import com.doritech.ItemService.Mapper.BomDetailMapper;
import com.doritech.ItemService.Repository.BomDetailRepository;
import com.doritech.ItemService.Repository.ItemMasterRepository;
import com.doritech.ItemService.Request.BomDetailRequestDTO;
import com.doritech.ItemService.Request.BomRawItemDTO;
import com.doritech.ItemService.Response.BomDetailResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;
import com.doritech.ItemService.Response.ParamResponseDTO;
import com.doritech.ItemService.Service.BomDetailService;
import com.doritech.ItemService.Validation.ValidationService;

import jakarta.transaction.Transactional;

@Service
public class BomDetailServiceImpl implements BomDetailService {

	@Autowired
	private BomDetailRepository repo;

	@Autowired
	private ItemMasterRepository itemRepo;

	@Autowired
	private BomDetailMapper mapper;

	@Autowired
	private ValidationService validationService;

	@Override
	@Transactional
	public BomDetailResponseDTO saveBomDetail(BomDetailRequestDTO dto) {

		var bomItem = itemRepo.findById(dto.getBomItemId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"BOM item not found with id : " + dto.getBomItemId()));
		List<BomDetailEntity> entityList = new ArrayList<>();
		for (BomRawItemDTO raw : dto.getRawItems()) {
			if (repo.existsByBomItem_ItemIdAndRawItem_ItemId(dto.getBomItemId(),
					raw.getRawItemId())) {
				throw new BusinessException(
						"BOM already exists for raw item id : "
								+ raw.getRawItemId());
			}
			BomDetailEntity entity = new BomDetailEntity();
			entity.setBomItem(bomItem);
			entity.setRawItem(itemRepo.findById(raw.getRawItemId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Raw item not found with id : "
									+ raw.getRawItemId())));

			entity.setQuantity(raw.getQuantity());
			entity.setIsActive(dto.getIsActive());
			entity.setCreatedBy(dto.getCreatedBy());
			entity.setCreatedOn(LocalDateTime.now());

			entityList.add(entity);
		}

		repo.saveAll(entityList);

		return mapper.toBomResponse(dto.getBomItemId(), entityList);
	}

	@Override
	@Transactional
	public BomDetailResponseDTO updateBomDetail(Integer bomItemId,
			BomDetailRequestDTO dto) {

		var bomItem = itemRepo.findById(bomItemId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"BOM item not found with id : " + bomItemId));

		List<BomDetailEntity> existingDetails = repo
				.findByBomItem_ItemId(bomItemId);

		Map<Integer, BomDetailEntity> existingMap = existingDetails.stream()
				.collect(Collectors.toMap(e -> e.getRawItem().getItemId(),
						e -> e));

		List<BomDetailEntity> updatedEntities = new ArrayList<>();

		for (BomRawItemDTO rawDto : dto.getRawItems()) {

			BomDetailEntity entity;

			if (existingMap.containsKey(rawDto.getRawItemId())) {

				entity = existingMap.get(rawDto.getRawItemId());
				entity.setQuantity(rawDto.getQuantity());
				entity.setIsActive(dto.getIsActive());
				entity.setModifiedBy(dto.getModifiedBy());
				entity.setModifiedOn(LocalDateTime.now());

			} else {

				entity = new BomDetailEntity();
				entity.setBomItem(bomItem);
				entity.setRawItem(itemRepo.findById(rawDto.getRawItemId())
						.orElseThrow(() -> new ResourceNotFoundException(
								"Raw item not found with id : "
										+ rawDto.getRawItemId())));

				entity.setQuantity(rawDto.getQuantity());
				entity.setIsActive(dto.getIsActive());
				entity.setCreatedBy(dto.getModifiedBy());
				entity.setCreatedOn(LocalDateTime.now());
			}

			updatedEntities.add(entity);
		}

		repo.saveAll(updatedEntities);

		return mapper.toBomResponse(bomItemId,
				repo.findByBomItem_ItemId(bomItemId));
	}

	@Override
	public BomDetailResponseDTO getBomDetailById(Integer bomItemId) {

		List<BomDetailEntity> list = repo.findByBomItem_ItemId(bomItemId);

		if (list.isEmpty())
			throw new ResourceNotFoundException(
					"BOM details not found for bom item id : " + bomItemId);

		return mapper.toBomResponse(bomItemId, list);
	}

	@Override
	public PageResponseDTO<BomDetailResponseDTO> getAllBomDetails(int page,
			int size) {

		Page<BomDetailEntity> entityPage = repo
				.findAll(PageRequest.of(page, size));

		// Group by bomItemId
		Map<Integer, List<BomDetailEntity>> grouped = entityPage.getContent()
				.stream().collect(
						Collectors.groupingBy(e -> e.getBomItem().getItemId()));

		List<BomDetailResponseDTO> responseList = grouped
				.entrySet().stream().map(entry -> mapper
						.toBomResponse(entry.getKey(), entry.getValue()))
				.toList();

		return new PageResponseDTO<>(responseList, entityPage.getNumber(),
				entityPage.getSize(), entityPage.getTotalElements(),
				entityPage.getTotalPages());
	}

	@Override
	public PageResponseDTO<BomDetailResponseDTO> filterBomDetails(Integer bomId,
			Integer rawId, String active, int page, int size) {

		Page<BomDetailEntity> entityPage = repo.filterBom(bomId, rawId, active,
				PageRequest.of(page, size));

		Map<Integer, List<BomDetailEntity>> grouped = entityPage.getContent()
				.stream().collect(
						Collectors.groupingBy(e -> e.getBomItem().getItemId()));

		List<BomDetailResponseDTO> responseList = grouped
				.entrySet().stream().map(entry -> mapper
						.toBomResponse(entry.getKey(), entry.getValue()))
				.toList();

		return new PageResponseDTO<>(responseList, entityPage.getNumber(),
				entityPage.getSize(), entityPage.getTotalElements(),
				entityPage.getTotalPages());
	}

	@Override
	public List<BomDetailResponseDTO> getBomDetailByBomId(Integer bomId) {

		ItemMasterEntity item = itemRepo.findById(bomId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Item not found with id: " + bomId));

		List<ParamResponseDTO> paramList = validationService
				.validateParamList("ITEM", "TYPE");

		boolean isBomValid = paramList.stream().anyMatch(
				param -> item.getItemType().equalsIgnoreCase(param.getDesp1()));

		if (item.getItemType() == null
				|| !item.getItemType().equalsIgnoreCase("B") || !isBomValid) {

			throw new IllegalArgumentException("Please select a BOM item");
		}

		List<BomDetailEntity> entities = repo.findByBomItem_ItemId(bomId);

		if (entities.isEmpty()) {
			throw new ResourceNotFoundException(
					"BOM details not found for bom item id: " + bomId);
		}

		return List.of(mapper.toBomResponse(bomId, entities));
	}

	@Override
	public Boolean existBomCombination(Integer bomId, Integer rawId) {
		return repo.existsByBomItem_ItemIdAndRawItem_ItemId(bomId, rawId);
	}

	@Override
	public void deleteById(Integer id) {

		if (!repo.existsById(id)) {
			throw new ResourceNotFoundException(
					"BOM detail not found with id : " + id);
		}

		repo.deleteById(id);
	}

	@Override
	public void deleteMultiple(List<Integer> ids) {
		if (ids == null || ids.isEmpty())
			throw new BusinessException("BOM ID list cannot be empty");

		List<BomDetailEntity> existing = repo.findAllById(ids);

		if (existing.size() != ids.size()) {
			throw new ResourceNotFoundException(
					"Some BOM IDs not found in database");
		}

		repo.deleteAllById(ids);
	}
}