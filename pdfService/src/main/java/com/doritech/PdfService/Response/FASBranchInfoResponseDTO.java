package com.doritech.PdfService.Response;

import com.doritech.PdfService.Dto.*;

import java.time.LocalDate;
import java.util.List;

public class FASBranchInfoResponseDTO {

    private Long id;
    private String message;

    private String branchCode;
    private Integer scheduleVisitId;
    private LocalDate inspectionDate;
    private String serviceReportNumber;
    private String fieldAssociateName;
    private String customerCode;
    private String branchName;
    private String branchAddress;
    private String regionalOffice;
    private String branchEmail;
    private String ifsc;
    private String customerName;
    private String district;
    private Integer minNoVisits;
	private Integer customerId;
    private String concernedPersonName;
    private String concernedPersonDesignation;
    private String concernedPersonPhone;

    private String productType;
    private String faModel;
    private String noOfZones;
    private LocalDate dateOfInstallation;
    private String batteryVoltage;

    private String faWorkingStatus;
    private String systemConnectedThrough;
    private String autoDialerTypePstnGsm;
    private String autoDialerInbuiltExternal;
    private String simNumber;
    private Integer noUpdatedInAutoDialer;

    private List<FASHardwareItemDTO> hardwareItems;
    private List<FASDialerContactDTO> dialerContacts;

    private Boolean isPanelCleaned;
    private Boolean areHootersDifficultToAccess;
    private Boolean areSensorsCleaned;
    private Boolean isBatteryWorking;
    private Boolean isOperationExplained;
    private Boolean isSettingDoneAsPerBank;
    private Boolean isPanelAndSensorsCleaned;
    private Boolean isPasswordChanged;

    private List<FASMaterialDTO> materials;

    private String faRemarks;
    private String customerRemarks;
    private String estimateNo;
    private String estimateProductDetails;
    private Double estimateAmount;
    private LocalDate estimateDate;
    private Boolean billToBeRaised;
    private String billNo;
    private LocalDate billDate;
    private Boolean isInvoicePaymentFollowed;
    private String paymentRemarks;

    private List<FASStaffMemberDTO> staffMembers;

    private List<FASBranchDocumentDTO> documents;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public Integer getScheduleVisitId() {
        return scheduleVisitId;
    }

    public void setScheduleVisitId(Integer scheduleVisitId) {
        this.scheduleVisitId = scheduleVisitId;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getServiceReportNumber() {
        return serviceReportNumber;
    }

    public void setServiceReportNumber(String serviceReportNumber) {
        this.serviceReportNumber = serviceReportNumber;
    }

    public String getFieldAssociateName() {
        return fieldAssociateName;
    }

    public void setFieldAssociateName(String fieldAssociateName) {
        this.fieldAssociateName = fieldAssociateName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getMinNoVisits() {
        return minNoVisits;
    }

    public void setMinNoVisits(Integer minNoVisits) {
        this.minNoVisits = minNoVisits;
    }

    public String getConcernedPersonName() {
        return concernedPersonName;
    }

    public void setConcernedPersonName(String concernedPersonName) {
        this.concernedPersonName = concernedPersonName;
    }

    public String getConcernedPersonDesignation() {
        return concernedPersonDesignation;
    }

    public void setConcernedPersonDesignation(String concernedPersonDesignation) {
        this.concernedPersonDesignation = concernedPersonDesignation;
    }

    public String getConcernedPersonPhone() {
        return concernedPersonPhone;
    }

    public void setConcernedPersonPhone(String concernedPersonPhone) {
        this.concernedPersonPhone = concernedPersonPhone;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getFaModel() {
        return faModel;
    }

    public void setFaModel(String faModel) {
        this.faModel = faModel;
    }

    public String getNoOfZones() {
        return noOfZones;
    }

    public void setNoOfZones(String noOfZones) {
        this.noOfZones = noOfZones;
    }

    public LocalDate getDateOfInstallation() {
        return dateOfInstallation;
    }

    public void setDateOfInstallation(LocalDate dateOfInstallation) {
        this.dateOfInstallation = dateOfInstallation;
    }

    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public String getFaWorkingStatus() {
        return faWorkingStatus;
    }

    public void setFaWorkingStatus(String faWorkingStatus) {
        this.faWorkingStatus = faWorkingStatus;
    }

    public String getSystemConnectedThrough() {
        return systemConnectedThrough;
    }

    public void setSystemConnectedThrough(String systemConnectedThrough) {
        this.systemConnectedThrough = systemConnectedThrough;
    }

    public String getAutoDialerTypePstnGsm() {
        return autoDialerTypePstnGsm;
    }

    public void setAutoDialerTypePstnGsm(String autoDialerTypePstnGsm) {
        this.autoDialerTypePstnGsm = autoDialerTypePstnGsm;
    }

    public String getAutoDialerInbuiltExternal() {
        return autoDialerInbuiltExternal;
    }

    public void setAutoDialerInbuiltExternal(String autoDialerInbuiltExternal) {
        this.autoDialerInbuiltExternal = autoDialerInbuiltExternal;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public Integer getNoUpdatedInAutoDialer() {
        return noUpdatedInAutoDialer;
    }

    public void setNoUpdatedInAutoDialer(Integer noUpdatedInAutoDialer) {
        this.noUpdatedInAutoDialer = noUpdatedInAutoDialer;
    }

    public List<FASHardwareItemDTO> getHardwareItems() {
        return hardwareItems;
    }

    public void setHardwareItems(List<FASHardwareItemDTO> hardwareItems) {
        this.hardwareItems = hardwareItems;
    }

    public List<FASDialerContactDTO> getDialerContacts() {
        return dialerContacts;
    }

    public void setDialerContacts(List<FASDialerContactDTO> dialerContacts) {
        this.dialerContacts = dialerContacts;
    }

    public Boolean getIsPanelCleaned() {
        return isPanelCleaned;
    }

    public void setIsPanelCleaned(Boolean isPanelCleaned) {
        this.isPanelCleaned = isPanelCleaned;
    }

    public Boolean getAreHootersDifficultToAccess() {
        return areHootersDifficultToAccess;
    }

    public void setAreHootersDifficultToAccess(Boolean areHootersDifficultToAccess) {
        this.areHootersDifficultToAccess = areHootersDifficultToAccess;
    }

    public Boolean getAreSensorsCleaned() {
        return areSensorsCleaned;
    }

    public void setAreSensorsCleaned(Boolean areSensorsCleaned) {
        this.areSensorsCleaned = areSensorsCleaned;
    }

    public Boolean getIsBatteryWorking() {
        return isBatteryWorking;
    }

    public void setIsBatteryWorking(Boolean isBatteryWorking) {
        this.isBatteryWorking = isBatteryWorking;
    }

    public Boolean getIsOperationExplained() {
        return isOperationExplained;
    }

    public void setIsOperationExplained(Boolean isOperationExplained) {
        this.isOperationExplained = isOperationExplained;
    }

    public Boolean getIsSettingDoneAsPerBank() {
        return isSettingDoneAsPerBank;
    }

    public void setIsSettingDoneAsPerBank(Boolean isSettingDoneAsPerBank) {
        this.isSettingDoneAsPerBank = isSettingDoneAsPerBank;
    }

    public Boolean getIsPanelAndSensorsCleaned() {
        return isPanelAndSensorsCleaned;
    }

    public void setIsPanelAndSensorsCleaned(Boolean isPanelAndSensorsCleaned) {
        this.isPanelAndSensorsCleaned = isPanelAndSensorsCleaned;
    }

    public Boolean getIsPasswordChanged() {
        return isPasswordChanged;
    }

    public void setIsPasswordChanged(Boolean isPasswordChanged) {
        this.isPasswordChanged = isPasswordChanged;
    }

    public List<FASMaterialDTO> getMaterials() {
        return materials;
    }

    public void setMaterials(List<FASMaterialDTO> materials) {
        this.materials = materials;
    }

    public String getFaRemarks() {
        return faRemarks;
    }

    public void setFaRemarks(String faRemarks) {
        this.faRemarks = faRemarks;
    }

    public String getCustomerRemarks() {
        return customerRemarks;
    }

    public void setCustomerRemarks(String customerRemarks) {
        this.customerRemarks = customerRemarks;
    }

    public String getEstimateNo() {
        return estimateNo;
    }

    public void setEstimateNo(String estimateNo) {
        this.estimateNo = estimateNo;
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

    public LocalDate getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(LocalDate estimateDate) {
        this.estimateDate = estimateDate;
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

    public List<FASStaffMemberDTO> getStaffMembers() {
        return staffMembers;
    }

    public void setStaffMembers(List<FASStaffMemberDTO> staffMembers) {
        this.staffMembers = staffMembers;
    }

    public List<FASBranchDocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<FASBranchDocumentDTO> documents) {
        this.documents = documents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
    
}