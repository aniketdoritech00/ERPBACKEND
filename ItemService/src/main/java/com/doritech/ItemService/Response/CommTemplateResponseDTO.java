package com.doritech.ItemService.Response;

import java.time.LocalDateTime;

public class CommTemplateResponseDTO {

	private Integer templateId;
	private Integer customerId;
	private String commType;
	private String templateName;
	private String templateSubject;
	private String templateBody;
	private String templateType;
	private String isActive;
	private LocalDateTime createdOn;
	private LocalDateTime modifiedOn;
	private Integer createdBy;
	private Integer modifiedBy;
	/**
	 * @return the templateId
	 */
	public Integer getTemplateId() {
		return templateId;
	}
	/**
	 * @param templateId
	 *            the templateId to set
	 */
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
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
	 * @return the createdOn
	 */
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return the modifiedOn
	 */
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	/**
	 * @param modifiedOn
	 *            the modifiedOn to set
	 */
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
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