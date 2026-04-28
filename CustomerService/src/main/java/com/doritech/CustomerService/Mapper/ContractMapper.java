package com.doritech.CustomerService.Mapper;

import java.time.LocalDateTime;

import com.doritech.CustomerService.Entity.ContractInstallationDetails;
import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Request.ContractMasterRequest;
import com.doritech.CustomerService.Response.ContractInstallationResponse;
import com.doritech.CustomerService.Response.ContractMasterResponse;

public class ContractMapper {

	public static ContractMaster toEntity(ContractMasterRequest request) {

		ContractMaster contract = new ContractMaster();

		contract.setContractNo(request.getContractNo());
		contract.setContractName(request.getContractName());
		CustomerMasterEntity customer = new CustomerMasterEntity();
		customer.setCustomerId(request.getCustomerId());
		contract.setCustomer(customer);
		contract.setContractStartDate(request.getContractStartDate());
		contract.setContractEndDate(request.getContractEndDate());
		contract.setContractStatus(request.getContractStatus());
		contract.setContractType(request.getContractType());
		contract.setBillingFrequency(request.getBillingFrequency());
		contract.setAmcType(request.getAmcType());
		contract.setTermCondition(request.getTermCondition());
		contract.setPaymentTerms(request.getPaymentTerms());
		contract.setIsActive(request.getIsActive());
		contract.setCreatedBy(request.getCreatedBy());
		contract.setModifiedBy(request.getModifiedBy());
		contract.setCreatedOn(LocalDateTime.now());

		return contract;
	}

	public static ContractMasterResponse toResponse(ContractMaster entity) {

		ContractMasterResponse response = new ContractMasterResponse();

		response.setContractId(entity.getContractId());
		response.setContractNo(entity.getContractNo());
		response.setContractName(entity.getContractName());
		if (entity.getCustomer() != null) {
			response.setCustomerId(entity.getCustomer().getCustomerId());
			response.setCustomerName(entity.getCustomer().getCustomerName());
			response.setCustomerCode(entity.getCustomer().getCustomerCode());

		}
		response.setContractStartDate(entity.getContractStartDate());
		response.setContractEndDate(entity.getContractEndDate());
		response.setContractStatus(entity.getContractStatus());
		response.setContractType(entity.getContractType());
		response.setBillingFrequency(entity.getBillingFrequency());
		response.setAmcType(entity.getAmcType());
		response.setTermCondition(entity.getTermCondition());
		response.setPaymentTerms(entity.getPaymentTerms());
		response.setIsActive(entity.getIsActive());
		response.setCreatedOn(entity.getCreatedOn());
		response.setModifiedOn(entity.getModifiedOn());
		response.setCreatedBy(entity.getCreatedBy());
		response.setModifiedBy(entity.getModifiedBy());
		if (entity.getInstallationDetails() != null && !entity.getInstallationDetails().isEmpty()) {

			ContractInstallationDetails inst = entity.getInstallationDetails().get(0);

			ContractInstallationResponse i = new ContractInstallationResponse();

			i.setInstallationId(inst.getInstallationId());
			i.setSalesOrderNumber(inst.getSalesOrderNumber());
			i.setSalesOrderDate(inst.getSalesOrderDate());
			i.setIsMaterialRequired(inst.getIsMaterialRequired());
			i.setMovementStatus(inst.getMovementStatus());

			i.setDocketNumber(inst.getDocketNumber());
			i.setBrfNumber(inst.getBrfNumber());
			i.setLogisticsRemarks(inst.getLogisticsRemarks());
			i.setBillNumber(inst.getBillNumber());
			i.setBillDate(inst.getBillDate());
			i.setBillAmount(inst.getBillAmount());
			i.setIsBillSubmitted(inst.getIsBillSubmitted());

			response.setInstallationDetails(i);
		}
		return response;
	}
}