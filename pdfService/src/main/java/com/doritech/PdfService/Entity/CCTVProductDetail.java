package com.doritech.PdfService.Entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "cctv_product_detail")
public class CCTVProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cctv_branch_info_id", nullable = false)
    private CCTVBranchInfo cctvBranchInfo;

    @Column(name = "system_type", length = 50)
    private String cctvSystemType;

    @Column(name = "make", length = 255)
    private String dvrNvrMake;

    @Column(name = "model", length = 255)
    private String dvrNvrModel;

    @Column(name = "built_date")
    private LocalDate builtDate;

    @Column(name = "total_camera_port")
    private Integer totalCameraPort;

    @Column(name = "is_rack_installed")
    private Boolean isRackInstalled;

    @Column(name = "date_of_installation")
    private LocalDate dateOfInstallation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CCTVBranchInfo getCctvBranchInfo() {
        return cctvBranchInfo;
    }

    public void setCctvBranchInfo(CCTVBranchInfo cctvBranchInfo) {
        this.cctvBranchInfo = cctvBranchInfo;
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
