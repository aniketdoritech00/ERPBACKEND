package com.doritech.PdfService.Response;

import java.time.LocalDate;
import java.util.List;

import com.doritech.PdfService.Dto.CCTVBranchDocumentDTO;
import com.doritech.PdfService.Dto.CCTVCameraStatusDTO;
import com.doritech.PdfService.Dto.CCTVHddStatusDTO;
import com.doritech.PdfService.Dto.CCTVMaterialDTO;
import com.doritech.PdfService.Dto.CCTVProductDetailDTO;
import com.doritech.PdfService.Dto.CCTVProductInstalledStatusDTO;
import com.doritech.PdfService.Dto.CCTVStaffMemberDTO;

public class CCTVBranchInfoResponseDTO {

	private Long id;
	private String branchName;
	private String fieldAssociateName;
	private LocalDate inspectionDate;
	private String branchAddress;
	private String regionalOffice;
	private String branchEmail;
	private String concernedPersonName;
	private String concernedPersonPhone;
	private String concernedPersonDesignation;
	private String cctvWorkingStatus;
	private Integer totalHddSlots;
	private Integer hddInstalledQty;
	private String serviceReportNumber;
	private Double totalCapacityTb;
	private Double totalFreeSpaceTb;
	private Boolean isPasswordChanged;
	private Boolean isSystemCleaned;
	private Boolean isMotionDetectionSet;
	private Boolean isOperationExplainedAndUnderstood;
	private Boolean isRecordingAvailable;
	private Boolean areAllCamerasFunctional;
	private Boolean isAcSupplyUninterrupted;
	private Boolean isSystemOnUps;
	private String faRemarks;
	private String estimateNo;
	private LocalDate estimateDate;
	private String estimateProductDetails;
	private Double estimateAmount;
	private Boolean billToBeRaised;
	private String billNo;
	private LocalDate billDate;
	private String customerCode;
	private Boolean isInvoicePaymentFollowed;
	private String paymentRemarks;
	private String customerRemarks;
	private String customerName;
	private String ifsc;
	private String district;
	private String pdfDownloadUrl;
	private String message;
	private String productType;
	private String status;
	private Integer scheduleVisitId;
	private Integer minNoVisits;
	private Integer customerId;

	private CCTVProductDetailDTO cctvProductDetail;
	private List<CCTVHddStatusDTO> cctvHddStatus;
	private List<CCTVCameraStatusDTO> cctvCameraStatus;
	private List<CCTVMaterialDTO> materials;
	private List<CCTVStaffMemberDTO> staffMembers;
	private List<CCTVProductInstalledStatusDTO> cctvProductInstalledStatus;
	private List<CCTVBranchDocumentDTO> documents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getFieldAssociateName() {
		return fieldAssociateName;
	}

	public void setFieldAssociateName(String fieldAssociateName) {
		this.fieldAssociateName = fieldAssociateName;
	}

	public LocalDate getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(LocalDate inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getRegionalOffice() {
		return regionalOffice;
	}

	public void setRegionalOffice(String regionalOffice) {
		this.regionalOffice = regionalOffice;
	}

	public String getBranchEmail() {
		return branchEmail;
	}

	public void setBranchEmail(String branchEmail) {
		this.branchEmail = branchEmail;
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

	public Boolean getIsPasswordChanged() {
		return isPasswordChanged;
	}

	public void setIsPasswordChanged(Boolean isPasswordChanged) {
		this.isPasswordChanged = isPasswordChanged;
	}

	public Boolean getIsSystemCleaned() {
		return isSystemCleaned;
	}

	public void setIsSystemCleaned(Boolean isSystemCleaned) {
		this.isSystemCleaned = isSystemCleaned;
	}

	public Boolean getIsMotionDetectionSet() {
		return isMotionDetectionSet;
	}

	public void setIsMotionDetectionSet(Boolean isMotionDetectionSet) {
		this.isMotionDetectionSet = isMotionDetectionSet;
	}

	public Boolean getIsOperationExplainedAndUnderstood() {
		return isOperationExplainedAndUnderstood;
	}

	public void setIsOperationExplainedAndUnderstood(Boolean isOperationExplainedAndUnderstood) {
		this.isOperationExplainedAndUnderstood = isOperationExplainedAndUnderstood;
	}

	public Boolean getIsRecordingAvailable() {
		return isRecordingAvailable;
	}

	public void setIsRecordingAvailable(Boolean isRecordingAvailable) {
		this.isRecordingAvailable = isRecordingAvailable;
	}

	public Boolean getAreAllCamerasFunctional() {
		return areAllCamerasFunctional;
	}

	public void setAreAllCamerasFunctional(Boolean areAllCamerasFunctional) {
		this.areAllCamerasFunctional = areAllCamerasFunctional;
	}

	public Boolean getIsAcSupplyUninterrupted() {
		return isAcSupplyUninterrupted;
	}

	public void setIsAcSupplyUninterrupted(Boolean isAcSupplyUninterrupted) {
		this.isAcSupplyUninterrupted = isAcSupplyUninterrupted;
	}

	public Boolean getIsSystemOnUps() {
		return isSystemOnUps;
	}

	public void setIsSystemOnUps(Boolean isSystemOnUps) {
		this.isSystemOnUps = isSystemOnUps;
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

	public String getEstimateProductDetails() {
		return estimateProductDetails;
	}

	public void setEstimateProductDetails(String estimateProductDetails) {
		this.estimateProductDetails = estimateProductDetails;
	}

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
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

	public void setIsInvoicePaymentFollowed(Boolean isInvoicePaymentFollowed) {
		this.isInvoicePaymentFollowed = isInvoicePaymentFollowed;
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

	public String getPdfDownloadUrl() {
		return pdfDownloadUrl;
	}

	public void setPdfDownloadUrl(String pdfDownloadUrl) {
		this.pdfDownloadUrl = pdfDownloadUrl;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CCTVProductDetailDTO getCctvProductDetail() {
		return cctvProductDetail;
	}

	public void setCctvProductDetail(CCTVProductDetailDTO cctvProductDetail) {
		this.cctvProductDetail = cctvProductDetail;
	}

	public List<CCTVHddStatusDTO> getCctvHddStatus() {
		return cctvHddStatus;
	}

	public void setCctvHddStatus(List<CCTVHddStatusDTO> cctvHddStatus) {
		this.cctvHddStatus = cctvHddStatus;
	}

	public List<CCTVCameraStatusDTO> getCctvCameraStatus() {
		return cctvCameraStatus;
	}

	public void setCctvCameraStatus(List<CCTVCameraStatusDTO> cctvCameraStatus) {
		this.cctvCameraStatus = cctvCameraStatus;
	}

	public List<CCTVMaterialDTO> getMaterials() {
		return materials;
	}

	public void setMaterials(List<CCTVMaterialDTO> materials) {
		this.materials = materials;
	}

	public List<CCTVStaffMemberDTO> getStaffMembers() {
		return staffMembers;
	}

	public void setStaffMembers(List<CCTVStaffMemberDTO> staffMembers) {
		this.staffMembers = staffMembers;
	}

	public List<CCTVProductInstalledStatusDTO> getCctvProductInstalledStatus() {
		return cctvProductInstalledStatus;
	}

	public void setCctvProductInstalledStatus(List<CCTVProductInstalledStatusDTO> cctvProductInstalledStatus) {
		this.cctvProductInstalledStatus = cctvProductInstalledStatus;
	}

	public List<CCTVBranchDocumentDTO> getDocuments() {
		return documents;
	}

	public void setDocuments(List<CCTVBranchDocumentDTO> documents) {
		this.documents = documents;
	}

	public Integer getScheduleVisitId() {
		return scheduleVisitId;
	}

	public void setScheduleVisitId(Integer scheduleVisitId) {
		this.scheduleVisitId = scheduleVisitId;
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

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getServiceReportNumber() {
		return serviceReportNumber;
	}

	public void setServiceReportNumber(String serviceReportNumber) {
		this.serviceReportNumber = serviceReportNumber;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public Integer getMinNoVisits() {
		return minNoVisits;
	}

	public void setMinNoVisits(Integer minNoVisits) {
		this.minNoVisits = minNoVisits;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}