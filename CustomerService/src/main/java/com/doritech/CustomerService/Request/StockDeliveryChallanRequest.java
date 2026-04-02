package com.doritech.CustomerService.Request;



import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StockDeliveryChallanRequest{


    @NotBlank(message = "{stock.dc.number.notBlank}")
    @Size(max = 100, message = "{stock.dc.number.size}")
    private String deliveryChallanNo;

    @NotNull(message = "{stock.dc.stockRequestId.notNull}")
    private List<Integer> stockRequestIds;  
    
    private Integer createdBy;

    private Integer modifiedBy;

    
    public String getDeliveryChallanNo() {
        return deliveryChallanNo;
    }

    public void setDeliveryChallanNo(String deliveryChallanNo) {
        this.deliveryChallanNo = deliveryChallanNo;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public List<Integer> getStockRequestIds() {
		return stockRequestIds;
	}

	public void setStockRequestIds(List<Integer> stockRequestIds) {
		this.stockRequestIds = stockRequestIds;
	}

	

	
	

}