package com.doritech.ItemService.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommTemplateRequestDTO {

	@NotNull
	private Integer customerId;

	@NotBlank
	@Size(max = 1)
	private String commType;

	@NotBlank
	@Size(max = 255)
	private String templateName;

	@Size(max = 255)
	private String templateSubject;

	private String templateBody;

	@Size(max = 2)
	private String templateType;

	@Size(max = 1)
	private String isActive;

	@NotNull
	private Integer createdBy;

	private Integer modifiedBy;

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the commType
	 */
	public String getCommType() {
		return commType;
	}

	/**
	 * @param commType
	 *            the commType to set
	 */
	public void setCommType(String commType) {
		this.commType = commType;
	}

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName
	 *            the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the templateSubject
	 */
	public String getTemplateSubject() {
		return templateSubject;
	}

	/**
	 * @param templateSubject
	 *            the templateSubject to set
	 */
	public void setTemplateSubject(String templateSubject) {
		this.templateSubject = templateSubject;
	}

	/**
	 * @return the templateBody
	 */
	public String getTemplateBody() {
		return templateBody;
	}

	/**
	 * @param templateBody
	 *            the templateBody to set
	 */
	public void setTemplateBody(String templateBody) {
		this.templateBody = templateBody;
	}

	/**
	 * @return the templateType
	 */
	public String getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType
	 *            the templateType to set
	 */
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the createdBy
	 */
	public Integer getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}