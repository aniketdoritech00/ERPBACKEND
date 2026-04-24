package com.doritech.PdfService.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@JsonPropertyOrder({
    "id",
    "customerId",
    "scheduleVisitId",
    "inspectionDate",
    "serviceReportNumber",
    "noOfVisit",
    "fieldAssociateName",
    "concernedPersonName",
    "concernedPersonDesignation",
    "concernedPersonPhone",
    "productType",
    "faModel",
    "noOfZones",
    "dateOfInstallation",
    "batteryVoltage",
    "faWorkingStatus",
    "systemConnectedThrough",
    "autoDialerTypePstnGsm",
    "autoDialerInbuiltExternal",
    "simNumber",
    "noUpdatedInAutoDialer",
    "isPanelCleaned",
    "areHootersDifficultToAccess",
    "areSensorsCleaned",
    "isBatteryWorking",
    "isOperationExplained",
    "isSettingDoneAsPerBank",
    "isPanelAndSensorsCleaned",
    "isPasswordChanged",
    "faRemarks",
    "customerRemarks",
    "estimateNo",
    "estimateProductDetails",
    "estimateAmount",
    "estimateDate",
    "billToBeRaised",
    "billNo",
    "billDate",
    "isInvoicePaymentFollowed",
    "paymentRemarks",
    "status",
    "createdBy",
    "createdAt",
    "updatedAt",
    "pdfFilePath",
    "hardwareItems",
    "dialerContacts",
    "fasMaterials",
    "staffMembers",
    "fasBranchDocuments"
})
@Entity
@Table(name = "fas_branch_info")
public class FASBranchInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_Id")
    private Integer customerId;

    @Column(name = "schedule_visit_id")
    private Integer scheduleVisitId;

    @Column(name = "inspection_date")
    private LocalDate inspectionDate;

    @Column(name = "service_report_number")
    private String serviceReportNumber;

    @Column(name = "no_of_visit")
    private Integer noOfVisit;

    @Column(name = "field_associate_name")
    private String fieldAssociateName;

    @Column(name = "concerned_person_name")
    private String concernedPersonName;

    @Column(name = "concerned_person_designation")
    private String concernedPersonDesignation;

    @Column(name = "concerned_person_phone")
    private String concernedPersonPhone;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "fa_model")
    private String faModel;

    @Column(name = "no_of_zones")
    private String noOfZones;

    @Column(name = "date_of_installation")
    private LocalDate dateOfInstallation;

    @Column(name = "battery_voltage")
    private String batteryVoltage;

    @Column(name = "fa_working_status")
    private String faWorkingStatus;

    @Column(name = "system_connected_through")
    private String systemConnectedThrough;

    @Column(name = "auto_dialer_type_pstn_gsm")
    private String autoDialerTypePstnGsm;

    @Column(name = "auto_dialer_inbuilt_external")
    private String autoDialerInbuiltExternal;

    @Column(name = "sim_number")
    private String simNumber;

    @Column(name = "no_updated_in_auto_dialer")
    private Integer noUpdatedInAutoDialer;

    @Column(name = "is_panel_cleaned")
    private Boolean isPanelCleaned;

    @Column(name = "are_hooters_difficult_to_access")
    private Boolean areHootersDifficultToAccess;

    @Column(name = "are_sensors_cleaned")
    private Boolean areSensorsCleaned;

    @Column(name = "is_battery_working")
    private Boolean isBatteryWorking;

    @Column(name = "is_operation_explained")
    private Boolean isOperationExplained;

    @Column(name = "is_setting_done_as_per_bank")
    private Boolean isSettingDoneAsPerBank;

    @Column(name = "is_panel_and_sensors_cleaned")
    private Boolean isPanelAndSensorsCleaned;

    @Column(name = "is_password_changed")
    private Boolean isPasswordChanged;

    @Column(name = "fa_remarks", columnDefinition = "TEXT")
    private String faRemarks;

    @Column(name = "customer_remarks", columnDefinition = "TEXT")
    private String customerRemarks;

    @Column(name = "estimate_no")
    private String estimateNo;

    @Column(name = "estimate_product_details")
    private String estimateProductDetails;

    @Column(name = "estimate_amount")
    private Double estimateAmount;

    @Column(name = "estimate_date")
    private LocalDate estimateDate;

    @Column(name = "bill_to_be_raised")
    private Boolean billToBeRaised;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Column(name = "is_invoice_payment_followed")
    private Boolean isInvoicePaymentFollowed;

    @Column(name = "payment_remarks")
    private String paymentRemarks;

    @Column(name = "status")
    private String status;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "pdf_file_path")
    private String pdfFilePath;

    @OneToMany(mappedBy = "fasBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FASHardwareItem> hardwareItems;

    @OneToMany(mappedBy = "fasBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FASDialerContact> dialerContacts;

    @OneToMany(mappedBy = "fasBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FASMaterial> fasMaterials;

    @OneToMany(mappedBy = "fasBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FASStaffMember> staffMembers;

    @OneToMany(mappedBy = "fasBranchInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FASBranchDocument> fasBranchDocuments;

    public FASBranchInfo() {}

    public FASBranchInfo(Long id, Integer customerId, Integer scheduleVisitId, LocalDate inspectionDate,
                        String serviceReportNumber, Integer noOfVisit, String fieldAssociateName,
                        String concernedPersonName, String concernedPersonDesignation,
                        String concernedPersonPhone, String productType, String faModel,
                        String noOfZones, LocalDate dateOfInstallation, String batteryVoltage,
                        String faWorkingStatus, String systemConnectedThrough,
                        String autoDialerTypePstnGsm, String autoDialerInbuiltExternal,
                        String simNumber, Integer noUpdatedInAutoDialer, Boolean isPanelCleaned,
                        Boolean areHootersDifficultToAccess, Boolean areSensorsCleaned,
                        Boolean isBatteryWorking, Boolean isOperationExplained,
                        Boolean isSettingDoneAsPerBank, Boolean isPanelAndSensorsCleaned,
                        Boolean isPasswordChanged, String faRemarks, String customerRemarks,
                        String estimateNo, String estimateProductDetails, Double estimateAmount,
                        LocalDate estimateDate, Boolean billToBeRaised, String billNo,
                        LocalDate billDate, Boolean isInvoicePaymentFollowed,
                        String paymentRemarks, String status, String createdBy,
                        LocalDateTime createdAt, LocalDateTime updatedAt, String pdfFilePath,
                        List<FASHardwareItem> hardwareItems, List<FASDialerContact> dialerContacts,
                        List<FASMaterial> fasMaterials, List<FASStaffMember> staffMembers,
                        List<FASBranchDocument> fasBranchDocuments) {

        this.id = id;
        this.customerId = customerId;
        this.scheduleVisitId = scheduleVisitId;
        this.inspectionDate = inspectionDate;
        this.serviceReportNumber = serviceReportNumber;
        this.noOfVisit = noOfVisit;
        this.fieldAssociateName = fieldAssociateName;
        this.concernedPersonName = concernedPersonName;
        this.concernedPersonDesignation = concernedPersonDesignation;
        this.concernedPersonPhone = concernedPersonPhone;
        this.productType = productType;
        this.faModel = faModel;
        this.noOfZones = noOfZones;
        this.dateOfInstallation = dateOfInstallation;
        this.batteryVoltage = batteryVoltage;
        this.faWorkingStatus = faWorkingStatus;
        this.systemConnectedThrough = systemConnectedThrough;
        this.autoDialerTypePstnGsm = autoDialerTypePstnGsm;
        this.autoDialerInbuiltExternal = autoDialerInbuiltExternal;
        this.simNumber = simNumber;
        this.noUpdatedInAutoDialer = noUpdatedInAutoDialer;
        this.isPanelCleaned = isPanelCleaned;
        this.areHootersDifficultToAccess = areHootersDifficultToAccess;
        this.areSensorsCleaned = areSensorsCleaned;
        this.isBatteryWorking = isBatteryWorking;
        this.isOperationExplained = isOperationExplained;
        this.isSettingDoneAsPerBank = isSettingDoneAsPerBank;
        this.isPanelAndSensorsCleaned = isPanelAndSensorsCleaned;
        this.isPasswordChanged = isPasswordChanged;
        this.faRemarks = faRemarks;
        this.customerRemarks = customerRemarks;
        this.estimateNo = estimateNo;
        this.estimateProductDetails = estimateProductDetails;
        this.estimateAmount = estimateAmount;
        this.estimateDate = estimateDate;
        this.billToBeRaised = billToBeRaised;
        this.billNo = billNo;
        this.billDate = billDate;
        this.isInvoicePaymentFollowed = isInvoicePaymentFollowed;
        this.paymentRemarks = paymentRemarks;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.pdfFilePath = pdfFilePath;
        this.hardwareItems = hardwareItems;
        this.dialerContacts = dialerContacts;
        this.fasMaterials = fasMaterials;
        this.staffMembers = staffMembers;
        this.fasBranchDocuments = fasBranchDocuments;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getScheduleVisitId() { return scheduleVisitId; }
    public void setScheduleVisitId(Integer scheduleVisitId) { this.scheduleVisitId = scheduleVisitId; }

    public LocalDate getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }

    public String getServiceReportNumber() { return serviceReportNumber; }
    public void setServiceReportNumber(String serviceReportNumber) { this.serviceReportNumber = serviceReportNumber; }

    public Integer getNoOfVisit() { return noOfVisit; }
    public void setNoOfVisit(Integer noOfVisit) { this.noOfVisit = noOfVisit; }

    public String getFieldAssociateName() { return fieldAssociateName; }
    public void setFieldAssociateName(String fieldAssociateName) { this.fieldAssociateName = fieldAssociateName; }

    public String getConcernedPersonName() { return concernedPersonName; }
    public void setConcernedPersonName(String concernedPersonName) { this.concernedPersonName = concernedPersonName; }

    public String getConcernedPersonDesignation() { return concernedPersonDesignation; }
    public void setConcernedPersonDesignation(String concernedPersonDesignation) { this.concernedPersonDesignation = concernedPersonDesignation; }

    public String getConcernedPersonPhone() { return concernedPersonPhone; }
    public void setConcernedPersonPhone(String concernedPersonPhone) { this.concernedPersonPhone = concernedPersonPhone; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getFaModel() { return faModel; }
    public void setFaModel(String faModel) { this.faModel = faModel; }

    public String getNoOfZones() { return noOfZones; }
    public void setNoOfZones(String noOfZones) { this.noOfZones = noOfZones; }

    public LocalDate getDateOfInstallation() { return dateOfInstallation; }
    public void setDateOfInstallation(LocalDate dateOfInstallation) { this.dateOfInstallation = dateOfInstallation; }

    public String getBatteryVoltage() { return batteryVoltage; }
    public void setBatteryVoltage(String batteryVoltage) { this.batteryVoltage = batteryVoltage; }

    public String getFaWorkingStatus() { return faWorkingStatus; }
    public void setFaWorkingStatus(String faWorkingStatus) { this.faWorkingStatus = faWorkingStatus; }

    public String getSystemConnectedThrough() { return systemConnectedThrough; }
    public void setSystemConnectedThrough(String systemConnectedThrough) { this.systemConnectedThrough = systemConnectedThrough; }

    public String getAutoDialerTypePstnGsm() { return autoDialerTypePstnGsm; }
    public void setAutoDialerTypePstnGsm(String autoDialerTypePstnGsm) { this.autoDialerTypePstnGsm = autoDialerTypePstnGsm; }

    public String getAutoDialerInbuiltExternal() { return autoDialerInbuiltExternal; }
    public void setAutoDialerInbuiltExternal(String autoDialerInbuiltExternal) { this.autoDialerInbuiltExternal = autoDialerInbuiltExternal; }

    public String getSimNumber() { return simNumber; }
    public void setSimNumber(String simNumber) { this.simNumber = simNumber; }

    public Integer getNoUpdatedInAutoDialer() { return noUpdatedInAutoDialer; }
    public void setNoUpdatedInAutoDialer(Integer noUpdatedInAutoDialer) { this.noUpdatedInAutoDialer = noUpdatedInAutoDialer; }

    public Boolean getIsPanelCleaned() { return isPanelCleaned; }
    public void setIsPanelCleaned(Boolean isPanelCleaned) { this.isPanelCleaned = isPanelCleaned; }

    public Boolean getAreHootersDifficultToAccess() { return areHootersDifficultToAccess; }
    public void setAreHootersDifficultToAccess(Boolean areHootersDifficultToAccess) { this.areHootersDifficultToAccess = areHootersDifficultToAccess; }

    public Boolean getAreSensorsCleaned() { return areSensorsCleaned; }
    public void setAreSensorsCleaned(Boolean areSensorsCleaned) { this.areSensorsCleaned = areSensorsCleaned; }

    public Boolean getIsBatteryWorking() { return isBatteryWorking; }
    public void setIsBatteryWorking(Boolean isBatteryWorking) { this.isBatteryWorking = isBatteryWorking; }

    public Boolean getIsOperationExplained() { return isOperationExplained; }
    public void setIsOperationExplained(Boolean isOperationExplained) { this.isOperationExplained = isOperationExplained; }

    public Boolean getIsSettingDoneAsPerBank() { return isSettingDoneAsPerBank; }
    public void setIsSettingDoneAsPerBank(Boolean isSettingDoneAsPerBank) { this.isSettingDoneAsPerBank = isSettingDoneAsPerBank; }

    public Boolean getIsPanelAndSensorsCleaned() { return isPanelAndSensorsCleaned; }
    public void setIsPanelAndSensorsCleaned(Boolean isPanelAndSensorsCleaned) { this.isPanelAndSensorsCleaned = isPanelAndSensorsCleaned; }

    public Boolean getIsPasswordChanged() { return isPasswordChanged; }
    public void setIsPasswordChanged(Boolean isPasswordChanged) { this.isPasswordChanged = isPasswordChanged; }

    public String getFaRemarks() { return faRemarks; }
    public void setFaRemarks(String faRemarks) { this.faRemarks = faRemarks; }

    public String getCustomerRemarks() { return customerRemarks; }
    public void setCustomerRemarks(String customerRemarks) { this.customerRemarks = customerRemarks; }

    public String getEstimateNo() { return estimateNo; }
    public void setEstimateNo(String estimateNo) { this.estimateNo = estimateNo; }

    public String getEstimateProductDetails() { return estimateProductDetails; }
    public void setEstimateProductDetails(String estimateProductDetails) { this.estimateProductDetails = estimateProductDetails; }

    public Double getEstimateAmount() { return estimateAmount; }
    public void setEstimateAmount(Double estimateAmount) { this.estimateAmount = estimateAmount; }

    public LocalDate getEstimateDate() { return estimateDate; }
    public void setEstimateDate(LocalDate estimateDate) { this.estimateDate = estimateDate; }

    public Boolean getBillToBeRaised() { return billToBeRaised; }
    public void setBillToBeRaised(Boolean billToBeRaised) { this.billToBeRaised = billToBeRaised; }

    public String getBillNo() { return billNo; }
    public void setBillNo(String billNo) { this.billNo = billNo; }

    public LocalDate getBillDate() { return billDate; }
    public void setBillDate(LocalDate billDate) { this.billDate = billDate; }

    public Boolean getIsInvoicePaymentFollowed() { return isInvoicePaymentFollowed; }
    public void setIsInvoicePaymentFollowed(Boolean isInvoicePaymentFollowed) { this.isInvoicePaymentFollowed = isInvoicePaymentFollowed; }

    public String getPaymentRemarks() { return paymentRemarks; }
    public void setPaymentRemarks(String paymentRemarks) { this.paymentRemarks = paymentRemarks; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getPdfFilePath() { return pdfFilePath; }
    public void setPdfFilePath(String pdfFilePath) { this.pdfFilePath = pdfFilePath; }

    public List<FASHardwareItem> getHardwareItems() { return hardwareItems; }
    public void setHardwareItems(List<FASHardwareItem> hardwareItems) { this.hardwareItems = hardwareItems; }

    public List<FASDialerContact> getDialerContacts() { return dialerContacts; }
    public void setDialerContacts(List<FASDialerContact> dialerContacts) { this.dialerContacts = dialerContacts; }

    public List<FASMaterial> getFasMaterials() { return fasMaterials; }
    public void setFasMaterials(List<FASMaterial> fasMaterials) { this.fasMaterials = fasMaterials; }

    public List<FASStaffMember> getStaffMembers() { return staffMembers; }
    public void setStaffMembers(List<FASStaffMember> staffMembers) { this.staffMembers = staffMembers; }

    public List<FASBranchDocument> getFasBranchDocuments() { return fasBranchDocuments; }
    public void setFasBranchDocuments(List<FASBranchDocument> fasBranchDocuments) { this.fasBranchDocuments = fasBranchDocuments; }
}