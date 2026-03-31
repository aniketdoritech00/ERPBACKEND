package com.doritech.ItemService.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "comm_template_master")
public class CommTemplateMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "template_id")
	private Integer templateId;

	@Column(name = "customer_id", nullable = false)
	private Integer customerId;

	@Column(name = "comm_type", nullable = false, length = 1)
	private String commType;

	@Column(name = "template_name", nullable = false, length = 255)
	private String templateName;

	@Column(name = "template_subject", length = 255)
	private String templateSubject;

	@Lob
	@Column(name = "template_body")
	private String templateBody;

	@Column(name = "template_type", length = 2)
	private String templateType;

	@Column(name = "is_active", nullable = false, length = 1)
	private String isActive;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "modified_on")
	private LocalDateTime modifiedOn;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
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