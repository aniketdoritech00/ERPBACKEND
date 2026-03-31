package com.doritech.ItemService.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.doritech.ItemService.Entity.BomDetailEntity;
import com.doritech.ItemService.Request.BomDetailRequestDTO;
import com.doritech.ItemService.Response.BomDetailResponseDTO;
import com.doritech.ItemService.Response.BomRawItemResponseDTO;

@Mapper(componentModel = "spring")
public interface BomDetailMapper {

	@Mapping(source = "rawItem.itemId", target = "rawItemId")
	@Mapping(source = "rawItem.itemName", target = "rawItemName")
	BomRawItemResponseDTO toRawItemDto(BomDetailEntity entity);

	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	List<BomDetailResponseDTO> toDtos(List<BomDetailEntity> entities);

	default BomDetailResponseDTO toBomResponse(Integer bomItemId,
			List<BomDetailEntity> entities) {

		BomDetailResponseDTO dto = new BomDetailResponseDTO();
		dto.setBomItemId(bomItemId);

		if (!entities.isEmpty()) {
			dto.setBomItemName(entities.get(0).getBomItem().getItemName());
		}

		dto.setRawItems(entities.stream().map(this::toRawItemDto).toList());

		return dto;
	}

	@Mapping(target = "bomItem", ignore = true)
	@Mapping(target = "rawItem", ignore = true)
	@Mapping(target = "bomDetailId", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	BomDetailEntity toEntity(BomDetailRequestDTO dto);
}