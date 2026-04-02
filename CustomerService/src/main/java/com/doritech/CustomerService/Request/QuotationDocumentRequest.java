package com.doritech.CustomerService.Request;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class QuotationDocumentRequest {

	@NotNull(message = "{quotation.document.id.required}")
	private Integer quotationId;

	private Integer quotationDocumentId;
	@NotBlank(message = "{quotation.document.type.required}")
	@Size(max = 2, message = "{quotation.document.type.size}")
	private String documentType;

	@NotBlank(message = "{quotation.document.source.required}")
	@Size(max = 2, message = "{quotation.document.source.size}")
	private String documentSource;

	@NotNull(message = "{quotation.document.date.required}")
	private Date documentDate;

	// @NotNull(message = "{quotation.document.file.required}")
	// private byte[] document;

	@NotBlank(message = "{quotation.document.active.required}")
	@Size(max = 1, message = "{quotation.document.active.size}")
	private String isActive;

	// @NotNull(message = "{quotation.document.createdBy.required}")
	private Integer createdBy;

	private Integer modifiedBy;

	// ================== GETTERS & SETTERS ==================

	public Integer getQuotationId() {
		return quotationId;
	}

	public void setQuotationId(Integer quotationId) {
		this.quotationId = quotationId;
	}

	public Integer getQuotationDocumentId() {
		return quotationDocumentId;
	}

	public void setQuotationDocumentId(Integer quotationDocumentId) {
		this.quotationDocumentId = quotationDocumentId;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentSource() {
		return documentSource;
	}

	public void setDocumentSource(String documentSource) {
		this.documentSource = documentSource;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
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