package com.doritech.ItemService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.doritech.ItemService.Entity.GodownInventoryEntity;
import com.doritech.ItemService.Entity.GodownMasterEntity;
import com.doritech.ItemService.Entity.ItemMasterEntity;
import com.doritech.ItemService.Exception.BusinessException;
import com.doritech.ItemService.Exception.ResourceNotFoundException;
import com.doritech.ItemService.Repository.GodownInventoryRepository;
import com.doritech.ItemService.Repository.GodownMasterRepository;
import com.doritech.ItemService.Repository.ItemMasterRepository;
import com.doritech.ItemService.Request.GodownInventoryFilterDTO;
import com.doritech.ItemService.Request.GodownInventoryRequestDTO;
import com.doritech.ItemService.Response.GodownInventoryResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;
import com.doritech.ItemService.Service.GodownInventoryService;
import com.doritech.ItemService.Specification.GodownInventorySpecification;

@Service
public class GodownInventoryServiceImpl implements GodownInventoryService {

	@Autowired
	private GodownInventoryRepository repository;

	@Autowired
	private GodownMasterRepository godownRepository;

	@Autowired
	private ItemMasterRepository itemRepository; // ✅ ADD THIS

	@Override
	public GodownInventoryResponseDTO save(GodownInventoryRequestDTO dto) {

		GodownMasterEntity godown = godownRepository.findById(dto.getGodownId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Godown not found with id : " + dto.getGodownId()));

		ItemMasterEntity item = itemRepository.findById(dto.getItemId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Item not found with id : " + dto.getItemId()));

		if (repository.existsByGodown_GodownIdAndItem_ItemId(dto.getGodownId(),
				dto.getItemId())) {

			throw new BusinessException(
					"Inventory already exists for this godown "
							+ godown.getGodownName() + " and item "
							+ item.getItemName());
		}

		GodownInventoryEntity entity = new GodownInventoryEntity();

		entity.setGodown(godown);
		entity.setItem(item);
		entity.setReorderLevel(dto.getReorderLevel());
		entity.setReorderQuantity(dto.getReorderQuantity());
		entity.setCreatedBy(dto.getCreatedBy());
		entity.setCreatedOn(LocalDateTime.now());
		entity.setLastUpdated(LocalDateTime.now());

		repository.save(entity);

		return mapToDTO(entity);
	}

	@Override
	public GodownInventoryResponseDTO update(Integer id,
			GodownInventoryRequestDTO dto) {

		GodownInventoryEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Inventory not found with id : " + id));

		entity.setReorderLevel(dto.getReorderLevel());
		entity.setReorderQuantity(dto.getReorderQuantity());
		entity.setModifiedBy(dto.getModifiedBy());
		entity.setModifiedOn(LocalDateTime.now());
		entity.setLastUpdated(LocalDateTime.now());

		repository.save(entity);

		return mapToDTO(entity);
	}

	@Override
	public PageResponseDTO<GodownInventoryResponseDTO> getAllGodownInventory(
			int page, int size) {

		if (page < 0)
			throw new BusinessException("Page number cannot be negative");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1 and 100");

		Pageable pageable = PageRequest.of(page, size,
				Sort.by("inventoryId").descending());

		Page<GodownInventoryEntity> data = repository.findAll(pageable);

		Page<GodownInventoryResponseDTO> resultPage = data.map(this::mapToDTO);

		return new PageResponseDTO<>(resultPage);
	}

	@Override
	public void delete(Integer id) {

		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException(
					"Inventory not found with id : " + id);
		}

		repository.deleteById(id);
	}

	@Override
	public void deleteMultiple(List<Integer> ids) {

		if (ids == null || ids.isEmpty()) {
			throw new BusinessException("Inventory id list cannot be empty");
		}

		List<GodownInventoryEntity> list = repository.findAllById(ids);

		if (list.size() != ids.size()) {
			throw new ResourceNotFoundException(
					"Some inventory ids not found in database");
		}

		repository.deleteAll(list);
	}

	@Override
	public PageResponseDTO<GodownInventoryResponseDTO> filter(
			GodownInventoryFilterDTO dto, Pageable pageable) {

		Page<GodownInventoryEntity> page = repository
				.findAll(GodownInventorySpecification.filter(dto), pageable);

		Page<GodownInventoryResponseDTO> resultPage = page.map(this::mapToDTO);

		return new PageResponseDTO<>(resultPage);
	}

	private GodownInventoryResponseDTO mapToDTO(GodownInventoryEntity entity) {

		GodownInventoryResponseDTO dto = new GodownInventoryResponseDTO();

		dto.setInventoryId(entity.getInventoryId());

		if (entity.getGodown() != null) {
			dto.setGodownId(entity.getGodown().getGodownId());
			dto.setGodownName(entity.getGodown().getGodownName());
		}

		if (entity.getItem() != null) {
			dto.setItemId(entity.getItem().getItemId());
			dto.setItemName(entity.getItem().getItemName());
			dto.setItemCode(entity.getItem().getItemCode());
			dto.setUnitOfMeasure(entity.getItem().getUnitOfMeasure());
		}

		dto.setReorderLevel(entity.getReorderLevel());
		dto.setReorderQuantity(entity.getReorderQuantity());
		dto.setLastUpdated(entity.getLastUpdated());

		return dto;
	}
}