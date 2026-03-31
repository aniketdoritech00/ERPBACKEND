package com.doritech.ItemService.Request;

import java.util.List;

import com.doritech.ItemService.Service.OnCreate;
import com.doritech.ItemService.Service.OnUpdate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BomDetailRequestDTO {

	@NotNull(message = "{bom.item.id.required}", groups = OnCreate.class)
	private Integer bomItemId;

	@Valid
	@NotEmpty(message = "{bom.raw.items.required}", groups = OnCreate.class)
	private List<BomRawItemDTO> rawItems;

	@NotNull(message = "{bom.active.required}", groups = OnCreate.class)
	private String isActive;

	private Integer createdBy;

	private Integer modifiedBy;

	// getter setter

	/**
	 * @return the bomItemId
	 */
	public Integer getBomItemId() {
		return bomItemId;
	}

	/**
	 * @param bomItemId
	 *            the bomItemId to set
	 */
	public void setBomItemId(Integer bomItemId) {
		this.bomItemId = bomItemId;
	}

	/**
	 * @return the rawItemId
	 */

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @return the rawItems
	 */
	public List<BomRawItemDTO> getRawItems() {
		return rawItems;
	}

	/**
	 * @param rawItems
	 *            the rawItems to set
	 */
	public void setRawItems(List<BomRawItemDTO> rawItems) {
		this.rawItems = rawItems;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}