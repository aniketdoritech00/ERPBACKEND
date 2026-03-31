package com.doritech.ItemService.Response;

import java.util.List;

public class ParamPayloadDTO {

	private List<ParamResponseDTO> data;

	/**
	 * @return the data
	 */
	public List<ParamResponseDTO> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<ParamResponseDTO> data) {
		this.data = data;
	}

	// getters & setters
}