package com.doritech.PdfService.Dto;

import java.time.LocalDate;

public class CCTVProductDetailDTO {

    private Long id;
    private Long cctvBranchInfoId;

    private String cctvSystemType;
    private String dvrNvrMake;
    private String dvrNvrModel;
    private LocalDate builtDate;
    private Integer totalCameraPort;
    private Boolean isRackInstalled;
    private LocalDate dateOfInstallation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCctvBranchInfoId() {
        return cctvBranchInfoId;
    }

    public void setCctvBranchInfoId(Long cctvBranchInfoId) {
        this.cctvBranchInfoId = cctvBranchInfoId;
    }

    public String getCctvSystemType() {
        return cctvSystemType;
    }

    public void setCctvSystemType(String cctvSystemType) {
        this.cctvSystemType = cctvSystemType;
    }

    public String getDvrNvrMake() {
        return dvrNvrMake;
    }

    public void setDvrNvrMake(String dvrNvrMake) {
        this.dvrNvrMake = dvrNvrMake;
    }

    public String getDvrNvrModel() {
        return dvrNvrModel;
    }

    public void setDvrNvrModel(String dvrNvrModel) {
        this.dvrNvrModel = dvrNvrModel;
    }

    public LocalDate getBuiltDate() {
        return builtDate;
    }

    public void setBuiltDate(LocalDate builtDate) {
        this.builtDate = builtDate;
    }

    public Integer getTotalCameraPort() {
        return totalCameraPort;
    }

    public void setTotalCameraPort(Integer totalCameraPort) {
        this.totalCameraPort = totalCameraPort;
    }

    public Boolean getIsRackInstalled() {
        return isRackInstalled;
    }

    public void setIsRackInstalled(Boolean isRackInstalled) {
        this.isRackInstalled = isRackInstalled;
    }

    public LocalDate getDateOfInstallation() {
        return dateOfInstallation;
    }

    public void setDateOfInstallation(LocalDate dateOfInstallation) {
        this.dateOfInstallation = dateOfInstallation;
    }
}
