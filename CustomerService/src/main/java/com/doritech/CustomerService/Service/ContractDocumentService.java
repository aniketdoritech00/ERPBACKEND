package com.doritech.CustomerService.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.ContractDocumentRequest;

import jakarta.validation.Valid;

public interface ContractDocumentService {

   // ResponseEntity saveDocument(ContractDocumentRequest request);

    ResponseEntity updateDocument(ContractDocumentRequest request);

    ResponseEntity deleteDocument(Integer documentId);

    ResponseEntity getDocument(Integer documentId);

    ResponseEntity getAllDocuments();

	ResponseEntity saveOrUpdateDocument(@Valid ContractDocumentRequest request);

	ResponseEntity getDocumentByContractId(Integer contractId);

}