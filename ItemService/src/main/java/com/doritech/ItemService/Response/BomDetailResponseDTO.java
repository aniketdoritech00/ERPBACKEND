package com.doritech.ItemService.Response;

import java.util.List;

public class BomDetailResponseDTO {

	private Integer bomItemId;
	private String bomItemName;
	private String bomItemCode;
	private List<BomRawItemResponseDTO> rawItems;

	/**
	 * @return the bomItemName
	 */
	public String getBomItemName() {
		return bomItemName;
	}
	/**
	 * @param bomItemName
	 *            the bomItemName to set
	 */
	public void setBomItemName(String bomItemName) {
		this.bomItemName = bomItemName;
	}
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
	 * @return the rawItems
	 */
	public List<BomRawItemResponseDTO> getRawItems() {
		return rawItems;
	}
	/**
	 * @param rawItems
	 *            the rawItems to set
	 */
	public void setRawItems(List<BomRawItemResponseDTO> rawItems) {
		this.rawItems = rawItems;
	}
	public String getBomItemCode() {
		return bomItemCode;
	}
	public void setBomItemCode(String bomItemCode) {
		this.bomItemCode = bomItemCode;
	}

	

}