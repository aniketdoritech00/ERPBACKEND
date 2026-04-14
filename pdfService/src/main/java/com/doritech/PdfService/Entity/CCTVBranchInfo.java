package com.doritech.PdfService.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "cctv_branch_info")
public class CCTVBranchInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "inspection_date")
	private LocalDate inspectionDate;

	@Column(name = "service_report_number", length = 50)
	private String serviceReportNumber;

	@Column(name = "fa_name")
	private String fieldAssociateName;

	@Column(name = "concerned_person_name", length = 255)
	private String concernedPersonName;

	@Column(name = "concerned_person_phone", length = 50)
	private String concernedPersonPhone;

	@Column(name = "concerned_person_designation", length = 255)
	private String concernedPersonDesignation;

	@Column(name = "cctv_working_status", length = 20)
	private String cctvWorkingStatus;

	private Integer totalHddSlots;
	private Integer hddInstalledQty;

	private Integer customerId;
	private Double totalCapacityTb;
	private Double totalFreeSpaceTb;

	@Column(length = 2000)
	private String faRemarks;

	@Column(name = "productType", length = 50)
	private String productType;

	@Column(name = "status", length = 10)
	private String status;

	@Column(name = "schedule_visit_id", length = 50)
	private Integer scheduleVisitId;

	private String estimateNo;
	private LocalDate estimateDate;
	private Double estimateAmount;

	private String pdfFilePath;

	private Boolean billToBeRaised;
	private String billNo;
	private LocalDate billDate;

	private Boolean isInvoicePaymentFollowed;

	private String paymentRemarks;
	@Column(name = "customerName", length = 20)
	private String customerName;

	@Column(length = 2000)
	private String customerRemarks;

	/* =================== RELATIONS =================== */

	@OneToOne(mappedBy = "cctvBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private CCTVProductDetail cctvProductDetail;

	@OneToMany(mappedBy = "cctvBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CCTVHddStatus> cctvHddStatus;

	@OneToMany(mappedBy = "cctvBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CCTVCameraStatus> cctvCameraStatus;

	@OneToMany(mappedBy = "cctvBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CCTVMaterial> cctvMaterials;

	@OneToMany(mappedBy = "cctvBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CCTVStaffMember> cctvStaffMembers;

	@OneToMany(mappedBy = "cctvBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CCTVProductInstalledStatus> cctvProductInstalledStatuses;

	@OneToMany(mappedBy = "cctvBranchInfo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<CCTVBranchDocument> cctvBranchDocuments;

	/* =================== SYSTEM CHECKS =================== */
	@Column(name = "is_ac_supply_uninterrupted")
	private Boolean isAcSupplyUninterrupted;

	@Column(name = "is_system_on_ups")
	private Boolean isSystemOnUps;

	@Column(name = "are_all_cameras_functional")
	private Boolean areAllCamerasFunctional;

	@Column(name = "is_recording_available")
	private Boolean isRecordingAvailable;

	@Column(name = "is_operation_explained_understood")
	private Boolean isOperationExplainedAndUnderstood;

	@Column(name = "is_motion_detection_set")
	private Boolean isMotionDetectionSet;

	@Column(name = "is_system_cleaned")
	private Boolean isSystemCleaned;

	@Column(name = "is_password_changed")
	private Boolean isPasswordChanged;

	@Column(length = 2000)
	private String estimateProductDetails;

	/* =================== AUDIT =================== */

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(LocalDate inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getFieldAssociateName() {
		return fieldAssociateName;
	}

	public void setFieldAssociateName(String fieldAssociateName) {
		this.fieldAssociateName = fieldAssociateName;
	}

	public List<CCTVBranchDocument> getCctvBranchDocuments() {
		return cctvBranchDocuments;
	}

	public void setCctvBranchDocuments(List<CCTVBranchDocument> cctvBranchDocuments) {
		this.cctvBranchDocuments = cctvBranchDocuments;
	}

	public String getConcernedPersonName() {
		return concernedPersonName;
	}

	public void setConcernedPersonName(String concernedPersonName) {
		this.concernedPersonName = concernedPersonName;
	}

	public String getConcernedPersonPhone() {
		return concernedPersonPhone;
	}

	public void setConcernedPersonPhone(String concernedPersonPhone) {
		this.concernedPersonPhone = concernedPersonPhone;
	}

	public String getConcernedPersonDesignation() {
		return concernedPersonDesignation;
	}

	public void setConcernedPersonDesignation(String concernedPersonDesignation) {
		this.concernedPersonDesignation = concernedPersonDesignation;
	}

	public String getCctvWorkingStatus() {
		return cctvWorkingStatus;
	}

	public void setCctvWorkingStatus(String cctvWorkingStatus) {
		this.cctvWorkingStatus = cctvWorkingStatus;
	}

	public Integer getTotalHddSlots() {
		return totalHddSlots;
	}

	public void setTotalHddSlots(Integer totalHddSlots) {
		this.totalHddSlots = totalHddSlots;
	}

	public Integer getHddInstalledQty() {
		return hddInstalledQty;
	}

	public void setHddInstalledQty(Integer hddInstalledQty) {
		this.hddInstalledQty = hddInstalledQty;
	}

	public Double getTotalCapacityTb() {
		return totalCapacityTb;
	}

	public void setTotalCapacityTb(Double totalCapacityTb) {
		this.totalCapacityTb = totalCapacityTb;
	}

	public Double getTotalFreeSpaceTb() {
		return totalFreeSpaceTb;
	}

	public void setTotalFreeSpaceTb(Double totalFreeSpaceTb) {
		this.totalFreeSpaceTb = totalFreeSpaceTb;
	}

	public String getFaRemarks() {
		return faRemarks;
	}

	public void setFaRemarks(String faRemarks) {
		this.faRemarks = faRemarks;
	}

	public String getEstimateNo() {
		return estimateNo;
	}

	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}

	public LocalDate getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(LocalDate estimateDate) {
		this.estimateDate = estimateDate;
	}

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public String getPdfFilePath() {
		return pdfFilePath;
	}

	public void setPdfFilePath(String pdfFilePath) {
		this.pdfFilePath = pdfFilePath;
	}

	public Boolean getBillToBeRaised() {
		return billToBeRaised;
	}

	public void setBillToBeRaised(Boolean billToBeRaised) {
		this.billToBeRaised = billToBeRaised;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public LocalDate getBillDate() {
		return billDate;
	}

	public void setBillDate(LocalDate billDate) {
		this.billDate = billDate;
	}

	public Boolean getIsInvoicePaymentFollowed() {
		return isInvoicePaymentFollowed;
	}

	public Boolean getIsSystemOnUps() {
		return isSystemOnUps;
	}

	public void setIsSystemOnUps(Boolean isSystemOnUps) {
		this.isSystemOnUps = isSystemOnUps;
	}

	public Boolean getIsRecordingAvailable() {
		return isRecordingAvailable;
	}

	public void setIsRecordingAvailable(Boolean isRecordingAvailable) {
		this.isRecordingAvailable = isRecordingAvailable;
	}

	public void setIsInvoicePaymentFollowed(Boolean isInvoicePaymentFollowed) {
		this.isInvoicePaymentFollowed = isInvoicePaymentFollowed;
	}

	public Boolean getIsOperationExplainedAndUnderstood() {
		return isOperationExplainedAndUnderstood;
	}

	public void setIsOperationExplainedAndUnderstood(Boolean isOperationExplainedAndUnderstood) {
		this.isOperationExplainedAndUnderstood = isOperationExplainedAndUnderstood;
	}

	public Boolean getIsMotionDetectionSet() {
		return isMotionDetectionSet;
	}

	public void setIsMotionDetectionSet(Boolean isMotionDetectionSet) {
		this.isMotionDetectionSet = isMotionDetectionSet;
	}

	public Boolean getIsSystemCleaned() {
		return isSystemCleaned;
	}

	public void setIsSystemCleaned(Boolean isSystemCleaned) {
		this.isSystemCleaned = isSystemCleaned;
	}

	public Boolean getIsPasswordChanged() {
		return isPasswordChanged;
	}

	public void setIsPasswordChanged(Boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}

	public String getPaymentRemarks() {
		return paymentRemarks;
	}

	public void setPaymentRemarks(String paymentRemarks) {
		this.paymentRemarks = paymentRemarks;
	}

	public String getCustomerRemarks() {
		return customerRemarks;
	}

	public void setCustomerRemarks(String customerRemarks) {
		this.customerRemarks = customerRemarks;
	}

	public CCTVProductDetail getCctvProductDetail() {
		return cctvProductDetail;
	}

	public void setCctvProductDetail(CCTVProductDetail cctvProductDetail) {
		this.cctvProductDetail = cctvProductDetail;
	}

	public List<CCTVHddStatus> getCctvHddStatus() {
		return cctvHddStatus;
	}

	public void setCctvHddStatus(List<CCTVHddStatus> cctvHddStatus) {
		this.cctvHddStatus = cctvHddStatus;
	}

	public List<CCTVCameraStatus> getCctvCameraStatus() {
		return cctvCameraStatus;
	}

	public void setCctvCameraStatus(List<CCTVCameraStatus> cctvCameraStatus) {
		this.cctvCameraStatus = cctvCameraStatus;
	}

	public List<CCTVMaterial> getCctvMaterials() {
		return cctvMaterials;
	}

	public void setCctvMaterials(List<CCTVMaterial> cctvMaterials) {
		this.cctvMaterials = cctvMaterials;
	}

	public List<CCTVStaffMember> getCctvStaffMembers() {
		return cctvStaffMembers;
	}

	public void setCctvStaffMembers(List<CCTVStaffMember> cctvStaffMembers) {
		this.cctvStaffMembers = cctvStaffMembers;
	}

	public List<CCTVProductInstalledStatus> getCctvProductInstalledStatuses() {
		return cctvProductInstalledStatuses;
	}

	public void setCctvProductInstalledStatuses(List<CCTVProductInstalledStatus> cctvProductInstalledStatuses) {
		this.cctvProductInstalledStatuses = cctvProductInstalledStatuses;
	}

	public Boolean getIsAcSupplyUninterrupted() {
		return isAcSupplyUninterrupted;
	}

	public void setIsAcSupplyUninterrupted(Boolean isAcSupplyUninterrupted) {
		this.isAcSupplyUninterrupted = isAcSupplyUninterrupted;
	}

	public Boolean getAreAllCamerasFunctional() {
		return areAllCamerasFunctional;
	}

	public void setAreAllCamerasFunctional(Boolean areAllCamerasFunctional) {
		this.areAllCamerasFunctional = areAllCamerasFunctional;
	}

	public String getEstimateProductDetails() {
		return estimateProductDetails;
	}

	public void setEstimateProductDetails(String estimateProductDetails) {
		this.estimateProductDetails = estimateProductDetails;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getScheduleVisitId() {
		return scheduleVisitId;
	}

	public void setScheduleVisitId(Integer scheduleVisitId) {
		this.scheduleVisitId = scheduleVisitId;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getServiceReportNumber() {
		return serviceReportNumber;
	}

	public void setServiceReportNumber(String serviceReportNumber) {
		this.serviceReportNumber = serviceReportNumber;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
