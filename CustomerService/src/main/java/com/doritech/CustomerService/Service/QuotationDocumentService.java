package com.doritech.CustomerService.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.QuotationDocumentRequest;

import jakarta.validation.Valid;

public interface QuotationDocumentService {

	// ResponseEntity saveDocument(@Valid QuotationDocumentRequest request);

	//ResponseEntity updateDocument(Integer id, @Valid QuotationDocumentRequest request);

	ResponseEntity getAllDocument(int page, int size);

	ResponseEntity getById(Integer id);

	ResponseEntity deleteMultiple(List<Integer> ids);

	ResponseEntity saveUpdateDocument(@Valid List<@Valid QuotationDocumentRequest> requests, List<MultipartFile> files);

	ResponseEntity getByQuotationId(Integer quotationId);

}