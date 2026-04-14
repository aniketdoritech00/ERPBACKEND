package com.doritech.PdfService.Dto;

public class CCTVCameraStatusDTO {

    private String cameraLocation;
    private String cameraType;
    private String cameraWorkingStatus;

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
