package com.doritech.CustomerService.Projection;

import java.time.LocalDateTime;

public interface ContractItemPackageProjection {
	Integer getPackageId();

	Integer getContractMappingId();

	Integer getMappedItemId();

	String getIsActive();
	Integer getContractId();
	LocalDateTime getCreatedOn();

	LocalDateTime getModifiedOn();

	Integer getCreatedBy();

	Integer getModifiedBy();

	String getContractName();

	String getContractCode();

	String getMappingItemName();
	String getMappedItemCode();

	String getMappedItemName();
	String getMappingItemCode();

	Double getBasePrice();

	Double getQty();
}