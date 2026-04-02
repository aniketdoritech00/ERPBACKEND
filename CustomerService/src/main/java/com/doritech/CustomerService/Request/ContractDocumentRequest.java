package com.doritech.CustomerService.Request;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ContractDocumentRequest {

	private Integer documentId;

	@NotNull(message = "{contract.document.contractId.required}")
	private Integer contractId;

	@NotBlank(message = "{contract.document.documentType.required}")
	@Size(max = 2, message = "{contract.document.documentType.size}")
	private String documentType;

	@NotNull(message = "{contract.document.file.required}")
	private MultipartFile document;

	@NotBlank(message = "{contract.document.isActive.required}")
	@Size(max = 1, message = "{contract.document.isActive.size}")
	private String isActive;

	// @NotNull(message = "{contract.document.createdBy.required}")
	private Integer createdBy;

	private Integer modifiedBy;

	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public MultipartFile getDocument() {
		return document;
	}

	public void setDocument(MultipartFile document) {
		this.document = document;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

}