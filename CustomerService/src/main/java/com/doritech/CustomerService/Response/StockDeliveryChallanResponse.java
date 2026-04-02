package com.doritech.CustomerService.Response;

import java.util.List;

public class StockDeliveryChallanResponse {

	private Integer stockDcId;
	private String deliveryChallanNo;
	private List<Integer> stockRequestIds;

	public Integer getStockDcId() {
		return stockDcId;
	}

	public void setStockDcId(Integer stockDcId) {
		this.stockDcId = stockDcId;
	}

	public String getDeliveryChallanNo() {
		return deliveryChallanNo;
	}

	public void setDeliveryChallanNo(String deliveryChallanNo) {
		this.deliveryChallanNo = deliveryChallanNo;
	}

	public List<Integer> getStockRequestIds() {
		return stockRequestIds;
	}

	public void setStockRequestIds(List<Integer> stockRequestIds) {
		this.stockRequestIds = stockRequestIds;
	}
	

}