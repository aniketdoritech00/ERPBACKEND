package com.doritech.CustomerService.Service;

import java.util.List;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.ContractDocumentRequest;

import jakarta.validation.Valid;

public interface ContractDocumentService {

	ResponseEntity updateDocument(ContractDocumentRequest request);

	ResponseEntity deleteDocument(Integer documentId);

	ResponseEntity saveOrUpdateDocument(@Valid ContractDocumentRequest request);

	ResponseEntity getDocumentByContractId(Integer contractId);

	ResponseEntity deleteBulkDocument(List<Integer> documentIds);

	ResponseEntity getAllDocuments(int page, int size);

	ResponseEntity getDocumentById(Integer documentId);

}