package com.doritech.ItemService.Mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.doritech.ItemService.Entity.ItemMasterEntity;
import com.doritech.ItemService.Request.ItemMasterRequestDTO;
import com.doritech.ItemService.Response.ItemMasterResponseDTO;

@Mapper(componentModel = "spring")
public interface ItemMapper {

	// Request → Entity
	@Mapping(target = "itemId", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "modifiedOn", ignore = true)
	@Mapping(target = "bomDetails", ignore = true)
	ItemMasterEntity toEntity(ItemMasterRequestDTO dto);

	// Entity → Response
	ItemMasterResponseDTO toDTO(ItemMasterEntity entity);

	// ⭐ List Mapping (IMPORTANT FIX)
	List<ItemMasterResponseDTO> toDTOList(List<ItemMasterEntity> entities);

	// Update mapping (ignore null values)
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "itemId", ignore = true)
	@Mapping(target = "itemCode", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "bomDetails", ignore = true)
	void updateEntityFromDto(ItemMasterRequestDTO dto,
			@MappingTarget ItemMasterEntity entity);
}