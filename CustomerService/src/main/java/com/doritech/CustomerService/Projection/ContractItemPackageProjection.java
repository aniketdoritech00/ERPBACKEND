package com.doritech.CustomerService.Projection;

import java.time.LocalDateTime;

public interface ContractItemPackageProjection {
	Integer getPackageId();

	Integer getContractMappingId();

	Integer getMappedItemId();

	String getMappedItemCode();

	String getMappingItemCode();

	String getIsActive();
	Integer getContractId();
	LocalDateTime getCreatedOn();

	LocalDateTime getModifiedOn();

	Integer getCreatedBy();

	Integer getModifiedBy();

	String getContractName();

	String getContractCode();

	String getMappingItemName();

	String getMappedItemName();

	Double getBasePrice();

	Double getQty();
}