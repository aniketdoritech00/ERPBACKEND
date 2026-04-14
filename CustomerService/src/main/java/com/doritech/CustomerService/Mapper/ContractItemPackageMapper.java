package com.doritech.CustomerService.Mapper;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Entity.ContractItemPackage;
import com.doritech.CustomerService.Projection.ContractItemPackageProjection;
import com.doritech.CustomerService.Request.ContractItemPackageRequest;
import com.doritech.CustomerService.Response.ContractItemPackageResponse;

@Component
public class ContractItemPackageMapper {

    public ContractItemPackage toEntity(ContractItemPackageRequest request) {
        ContractItemPackage entity = new ContractItemPackage();
        ContractItemMapping mapping = new ContractItemMapping();
        mapping.setContractMappingId(request.getContractMappingId());
        entity.setContractItemMapping(mapping);
        entity.setMappedItemId(request.getMappedItemId());
        entity.setIsActive(request.getIsActive());
        entity.setCreatedBy(request.getCreatedBy());
        entity.setCreatedOn(LocalDateTime.now());
        return entity;
    }

    public ContractItemPackageResponse toResponse(ContractItemPackage entity) {
        ContractItemPackageResponse response = new ContractItemPackageResponse();
        response.setPackageId(entity.getPackageId());
        response.setContractMappingId(entity.getContractItemMapping().getContractMappingId());
        response.setMappedItemId(entity.getMappedItemId());
        response.setIsActive(entity.getIsActive());
        response.setCreatedOn(entity.getCreatedOn());
        response.setModifiedOn(entity.getModifiedOn());
        response.setCreatedBy(entity.getCreatedBy());
        response.setModifiedBy(entity.getModifiedBy());
        return response;
    }

    public ContractItemPackageResponse toResponse(ContractItemPackageProjection p) {
        ContractItemPackageResponse response = new ContractItemPackageResponse();
        response.setPackageId(p.getPackageId());
        response.setContractMappingId(p.getContractMappingId());
        response.setMappedItemId(p.getMappedItemId());
        response.setMappedItemCode(p.getMappedItemCode());
        response.setMappingItemCode(p.getMappingItemCode());
        response.setIsActive(p.getIsActive());
        response.setCreatedOn(p.getCreatedOn());
        response.setModifiedOn(p.getModifiedOn());
        response.setCreatedBy(p.getCreatedBy());
        response.setModifiedBy(p.getModifiedBy());
        response.setContractId(p.getContractId());
        response.setContractName(p.getContractName());
        response.setContractCode(p.getContractCode());
        response.setMappingItemName(p.getMappingItemName());
        response.setItemName(p.getMappedItemName());
        response.setBasePrice(p.getBasePrice());
        response.setQty(p.getQty());
        return response;
    }
}