package com.doritech.CustomerService.Response;

import java.time.LocalDate;
import java.util.List;

public class ContractFullResponseDTO {

    private Integer contractId;
    private String contractNo;
    private String contractName;
    private Integer customerId;
    private String customerName;
    // ✅ ADD THESE (Missing Fields)
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private String contractStatus;
    private String contractType;
    private String billingFrequency;
    private String amcType;
    private String termCondition;
    private String paymentTerms;
    private String isActive;

    private List<ContractEntityMappingResponse> entityMappings;
    private List<ContractItemMappingResponse> itemMappings;
    private List<ContractDocumentResponse> documents;
    private List<QuotationMasterResponse> quotations;

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getBillingFrequency() {
        return billingFrequency;
    }

    public void setBillingFrequency(String billingFrequency) {
        this.billingFrequency = billingFrequency;
    }

    public String getAmcType() {
        return amcType;
    }

    public void setAmcType(String amcType) {
        this.amcType = amcType;
    }

    public String getTermCondition() {
        return termCondition;
    }

    public void setTermCondition(String termCondition) {
        this.termCondition = termCondition;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    

    public List<ContractEntityMappingResponse> getEntityMappings() {
        return entityMappings;
    }

    public void setEntityMappings(List<ContractEntityMappingResponse> entityMappings) {
        this.entityMappings = entityMappings;
    }

    public List<ContractItemMappingResponse> getItemMappings() {
        return itemMappings;
    }

    public void setItemMappings(List<ContractItemMappingResponse> itemMappings) {
        this.itemMappings = itemMappings;
    }

    public List<ContractDocumentResponse> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ContractDocumentResponse> documents) {
        this.documents = documents;
    }

    public List<QuotationMasterResponse> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<QuotationMasterResponse> quotations) {
        this.quotations = quotations;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    
}