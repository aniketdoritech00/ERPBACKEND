package com.doritech.PdfService.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.PdfService.Request.FASBranchInfoRequestDTO;
import com.doritech.PdfService.Response.FASBranchInfoResponseDTO;
import com.doritech.PdfService.Response.ResponseEntity;
import com.doritech.PdfService.Service.FASBranchInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/pdf/api/fas")
public class FASBranchInfoController {

	private static final Logger logger = LoggerFactory.getLogger(FASBranchInfoController.class);

	@Autowired
	private FASBranchInfoService fasService;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping(value = "/createFASInfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity createFASInfo(@RequestPart("data") String requestDTOJson,
			@RequestParam Map<String, MultipartFile> allFiles) throws Exception {

		logger.info("POST /pdf/api/fas/createFASInfo - Request received");

		FASBranchInfoRequestDTO requestDTO = objectMapper.readValue(requestDTOJson, FASBranchInfoRequestDTO.class);

		FASBranchInfoResponseDTO data = fasService.createFASInfoAndGeneratePdf(requestDTO, allFiles);

		if (data.getMessage() != null && data.getMessage().contains("already exists")) {
			return new ResponseEntity(data.getMessage(), HttpStatus.OK.value(), data);
		}

		return new ResponseEntity("FAS info created successfully", HttpStatus.CREATED.value(), data);
	}

	@GetMapping("/getFASInfoById/{id}")
	public ResponseEntity getFASInfoById(@PathVariable Long id) {

		logger.info("GET /pdf/api/fas/getFASInfoById/{}", id);

		FASBranchInfoResponseDTO data = fasService.getFASInfoById(id);

		return new ResponseEntity("FAS info fetched successfully", HttpStatus.OK.value(), data);
	}

	@GetMapping("/getAllFASInfo")
	public ResponseEntity getAllFASInfo(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir) {

		logger.info("GET /pdf/api/fas/getAllFASInfo page={} size={}", page, size);

		Page<FASBranchInfoResponseDTO> data = fasService.getAllFASInfo(page, size, sortBy, sortDir);

		return new ResponseEntity("FAS records fetched successfully", HttpStatus.OK.value(), data);
	}

	@GetMapping("/getFASInfoByScheduleVisitAndProductType")
	public ResponseEntity getFASInfoByScheduleVisitAndProduct(@RequestParam Integer scheduleVisitId,
			@RequestParam String productType) {

		logger.info("GET FAS by scheduleVisitId={} productType={}", scheduleVisitId, productType);

		FASBranchInfoResponseDTO data = fasService.getFASInfoByScheduleVisitAndProductType(scheduleVisitId,
				productType);

		return new ResponseEntity("FAS info fetched successfully", HttpStatus.OK.value(), data);
	}

	@GetMapping("/getPdfById/{id}")
	public org.springframework.http.ResponseEntity<byte[]> getPdfById(@PathVariable Long id) throws Exception {

		logger.info("GET /pdf/api/fas/getPdfById/{}", id);

		byte[] pdfBytes = fasService.getPdfBytesById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("inline", "fas_report_" + id + ".pdf");
		headers.setContentLength(pdfBytes.length);

		return org.springframework.http.ResponseEntity.ok().headers(headers).body(pdfBytes);
	}
}