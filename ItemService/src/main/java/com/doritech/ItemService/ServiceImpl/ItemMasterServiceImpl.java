package com.doritech.ItemService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.doritech.ItemService.Entity.ItemMasterEntity;
import com.doritech.ItemService.Exception.BusinessException;
import com.doritech.ItemService.Exception.ResourceNotFoundException;
import com.doritech.ItemService.Mapper.ItemMapper;
import com.doritech.ItemService.Repository.ItemMasterRepository;
import com.doritech.ItemService.Request.ItemMasterRequestDTO;
import com.doritech.ItemService.Response.ItemMasterResponseDTO;
import com.doritech.ItemService.Response.PageResponseDTO;
import com.doritech.ItemService.Service.ItemMasterService;
import com.doritech.ItemService.Specification.ItemMasterSpecification;

@Service
public class ItemMasterServiceImpl implements ItemMasterService {

	@Autowired
	private ItemMasterRepository repo;

	@Autowired
	private ItemMapper mapper;

	@Override
	public ItemMasterResponseDTO saveItem(ItemMasterRequestDTO dto) {

		if (repo.existsByItemCode(dto.getItemCode()))
			throw new BusinessException("Item code already exists");

		ItemMasterEntity entity = mapper.toEntity(dto);
		entity.setCreatedOn(LocalDateTime.now());

		return mapper.toDTO(repo.save(entity));
	}

	@Override
	public ItemMasterResponseDTO updateItem(Integer id,
			ItemMasterRequestDTO dto) {

		ItemMasterEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Item not found with id : " + id));

		mapper.updateEntityFromDto(dto, entity);

		entity.setModifiedOn(LocalDateTime.now());

		return mapper.toDTO(repo.save(entity));
	}

	@Override
	public ItemMasterResponseDTO getItemById(Integer id) {

		return mapper.toDTO(repo.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Item not found")));
	}

	@Override
	public PageResponseDTO<ItemMasterResponseDTO> getAllItems(int page,
			int size) {

		Page<ItemMasterResponseDTO> result = repo
				.findAll(PageRequest.of(page, size)).map(mapper::toDTO);

		return new PageResponseDTO<>(result);
	}

	@Override
	public List<ItemMasterResponseDTO> getAllItems() {

		List<ItemMasterEntity> entityEntities = repo.findByIsActive("Y");

		return mapper.toDTOList(entityEntities);
	}

	@Override
	public PageResponseDTO<ItemMasterResponseDTO> itemFilter(String itemName,
			String itemCode, String active, String itemType, String category,
			int page, int size) {

		if (page < 0)
			throw new BusinessException("Page number cannot be negative");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1 and 100");

		Page<ItemMasterResponseDTO> resultPage = repo
				.findAll(
						ItemMasterSpecification.filter(itemName, itemCode,
								active, itemType, category),
						PageRequest.of(page, size))
				.map(mapper::toDTO);

		return new PageResponseDTO<>(resultPage);
	}

	@Override
	public ItemMasterResponseDTO getByItemCode(String code) {

		return mapper.toDTO(repo.findByItemCode(code)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Item not found with code : " + code)));
	}

	@Override
	public List<ItemMasterResponseDTO> getByItemType(String type) {

		List<ItemMasterEntity> entities = repo.findByItemType(type);

		if (entities.isEmpty())
			throw new ResourceNotFoundException(
					"No items found for type : " + type);

		return mapper.toDTOList(entities);
	}

	@Override
	public List<ItemMasterResponseDTO> getParentItem() {
		return getByItemType("B");
	}

	@Override
	public List<ItemMasterResponseDTO> getComponemtItem() {

		return mapper.toDTOList(repo.findAll());
	}

	@Override
	public Boolean existItemCode(String code) {
		return repo.existsByItemCode(code);
	}

	@Override
	public void deleteById(Integer id) {

		if (!repo.existsById(id)) {
			throw new ResourceNotFoundException(
					"Item not found with id : " + id);
		}

		repo.deleteById(id);
	}

	@Override
	public void deleteMultipleItemCode(List<String> codes) {

		if (codes == null || codes.isEmpty())
			throw new BusinessException("Item codes list cannot be empty");

		List<String> validCodes = codes.stream()
				.filter(code -> code != null && !code.isBlank()).distinct()
				.toList();

		if (validCodes.isEmpty())
			throw new BusinessException("No valid item codes provided");

		List<ItemMasterEntity> existingItems = repo
				.findAllByItemCodeIn(validCodes);

		if (existingItems.size() != validCodes.size())
			throw new ResourceNotFoundException(
					"Some item codes not found in database");

		repo.deleteByItemCodeIn(validCodes);
	}

	@Override
	public void deleteMultipleItems(List<Integer> ids) {

		if (ids == null || ids.isEmpty())
			throw new BusinessException("Item IDs list cannot be empty");

		List<Integer> validIds = ids.stream().filter(Objects::nonNull)
				.distinct().toList();

		if (validIds.isEmpty())
			throw new BusinessException("No valid item IDs provided");

		List<ItemMasterEntity> existingItems = repo.findAllById(validIds);

		if (existingItems.size() != validIds.size())
			throw new ResourceNotFoundException("Some item IDs not found");

		repo.deleteAllById(validIds);
	}
}