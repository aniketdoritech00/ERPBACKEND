package com.doritech.PdfService.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cctv_camera_status")
public class CCTVCameraStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cctv_branch_info_id", nullable = false)
    private CCTVBranchInfo cctvBranchInfo;

    @Column(name = "camera_location", length = 255)
    private String cameraLocation;

    @Column(name = "camera_type", length = 255)
    private String cameraType;

    @Column(name = "camera_working_status", length = 20)
    private String cameraWorkingStatus;

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

    public String getCameraLocation() {
        return cameraLocation;
    }

    public void setCameraLocation(String cameraLocation) {
        this.cameraLocation = cameraLocation;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getCameraWorkingStatus() {
        return cameraWorkingStatus;
    }

    public void setCameraWorkingStatus(String cameraWorkingStatus) {
        this.cameraWorkingStatus = cameraWorkingStatus;
    }
}
