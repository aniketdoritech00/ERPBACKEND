package com.doritech.PdfService.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.PdfService.Request.CCTVBranchInfoRequestDTO;
import com.doritech.PdfService.Response.CCTVBranchInfoResponseDTO;
import com.doritech.PdfService.Response.ResponseEntity;
import com.doritech.PdfService.Service.CCTVBranchInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pdf/api/cctv")
public class CCTVBranchInfoController {

    private static final Logger logger = LoggerFactory.getLogger(CCTVBranchInfoController.class);

    @Autowired
    private CCTVBranchInfoService cctvBranchInfoService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/createCCTVInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity createCCTVInfo(@Valid @RequestPart("data") String requestDTOJson,
                                         @RequestParam Map<String, MultipartFile> allFiles) throws Exception {

        logger.info("POST /api/cctv/createCCTVInfo - Request received");

        CCTVBranchInfoRequestDTO requestDTO = objectMapper.readValue(requestDTOJson,
                CCTVBranchInfoRequestDTO.class);

        CCTVBranchInfoResponseDTO data =
                cctvBranchInfoService.createCCTVInfoAndGeneratePdf(requestDTO, allFiles);

        logger.info("POST /api/cctv/createCCTVInfo - Success. ID: {}", data.getId());

        return new ResponseEntity("CCTV info created successfully",
                HttpStatus.CREATED.value(), data);
    }

    @GetMapping("/getCCTVInfoById/{id}")
    public ResponseEntity getCCTVInfoById(@PathVariable Long id) {

        logger.info("GET /api/cctv/{} - Request received", id);

        CCTVBranchInfoResponseDTO data = cctvBranchInfoService.getCCTVInfoById(id);

        logger.info("GET /api/cctv/{} - Success", id);

        return new ResponseEntity("CCTV info fetched successfully",
                HttpStatus.OK.value(), data);
    }

    @GetMapping("/getAllCCTVInfo")
    public ResponseEntity getAllCCTVInfo(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id") String sortBy,
                                         @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("GET /api/cctv/all - page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);

        Page<CCTVBranchInfoResponseDTO> data =
                cctvBranchInfoService.getAllCCTVInfo(page, size, sortBy, sortDir);

        return new ResponseEntity("CCTV records fetched successfully",
                HttpStatus.OK.value(), data);
    }

    @GetMapping("/getPdfById/{id}")
    public org.springframework.http.ResponseEntity<byte[]> getPdfById(@PathVariable Long id) {

        logger.info("GET /api/cctv/{}/pdf - Request received", id);

        byte[] pdfBytes = cctvBranchInfoService.getPdfBytesById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "cctv_report_" + id + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return org.springframework.http.ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/getCCTVInfoByScheduleVisitAndProductType")
    public ResponseEntity getCCTVInfoByScheduleVisitAndProduct(
            @RequestParam Integer scheduleVisitId,
            @RequestParam String productType) {

        logger.info("GET /api/cctv?scheduleVisitId={}&productType={} - Request received",
                scheduleVisitId, productType);

        CCTVBranchInfoResponseDTO data =
                cctvBranchInfoService.getCCTVInfoByScheduleVisitAndProductType(
                        scheduleVisitId, productType);

        return new ResponseEntity("CCTV info fetched successfully",
                HttpStatus.OK.value(), data);
    }
}