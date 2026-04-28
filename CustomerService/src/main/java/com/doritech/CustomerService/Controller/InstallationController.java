package com.doritech.CustomerService.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.InstallationRequest;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/customer/api/installations")
public class InstallationController {

    @Autowired
    private com.doritech.CustomerService.Service.installationService installationService;

    @PostMapping(value = "/save-installation", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity saveInstallation(
            @Valid @RequestPart("data") InstallationRequest request,
            @RequestPart(value = "hddImage", required = false) MultipartFile hddImage,
            @RequestPart(value = "deviceImage", required = false) MultipartFile deviceImage,
            @RequestPart(value = "serviceImages", required = false) List<MultipartFile> serviceImages,
            @RequestHeader("X-User-Id") String userId) throws Exception {

        return installationService.saveInstallation(request, hddImage, deviceImage, serviceImages,
                Integer.parseInt(userId));
    }

    @PostMapping(value = "/save-installation-for-fas-sas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity saveInstallationForFasAndSas(
            @Valid @RequestPart("data") InstallationRequest request,
            @RequestPart(value = "serviceImages", required = false) List<MultipartFile> serviceImages,
            @RequestHeader("X-User-Id") String userId) throws Exception {
        return installationService.saveInstallationForFasAndSas(request, serviceImages, Integer.parseInt(userId));
    }

    @GetMapping("/get-installation/{id}")
    public ResponseEntity getInstallation(@PathVariable Long id) {
        return installationService.getInstallation(id);
    }

    @GetMapping("/get-installation-by-assignment-id/{assignmentId}")
    public ResponseEntity getInstallationByAssignmentId(@PathVariable Integer assignmentId) {
        return installationService.getInstallationByAssignmentId(assignmentId);
    }

    @GetMapping("/image")
    public org.springframework.http.ResponseEntity<byte[]> getImage(@RequestParam String path) {
        return installationService.getImage(path);
    }

    @GetMapping("/get-all-installations")
    public ResponseEntity getAllInstallations(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return installationService.getAllInstallations(page, size);
    }
}
