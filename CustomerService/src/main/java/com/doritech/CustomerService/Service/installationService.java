package com.doritech.CustomerService.Service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.InstallationRequest;

public interface installationService {

    ResponseEntity saveInstallation(InstallationRequest request, MultipartFile hddImage, MultipartFile deviceImage,
            List<MultipartFile> serviceImages, Integer userId) throws Exception;

    ResponseEntity saveInstallationForFasAndSas(InstallationRequest request, List<MultipartFile> serviceImages, Integer userId) throws Exception;

    ResponseEntity getInstallation(Long id);

    ResponseEntity getInstallationByAssignmentId(Integer assignmentId);

    ResponseEntity getAllInstallations(Integer page, Integer size);

    public org.springframework.http.ResponseEntity<byte[]> getImage(String path);

}
