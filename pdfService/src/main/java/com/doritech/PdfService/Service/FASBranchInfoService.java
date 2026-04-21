package com.doritech.PdfService.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.pdfbox.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.PdfService.Dto.FASBranchDocumentDTO;
import com.doritech.PdfService.Dto.FASDialerContactDTO;
import com.doritech.PdfService.Dto.FASHardwareItemDTO;
import com.doritech.PdfService.Dto.FASMaterialDTO;
import com.doritech.PdfService.Dto.FASStaffMemberDTO;
import com.doritech.PdfService.Entity.FASBranchDocument;
import com.doritech.PdfService.Entity.FASBranchInfo;
import com.doritech.PdfService.Entity.FASDialerContact;
import com.doritech.PdfService.Entity.FASHardwareItem;
import com.doritech.PdfService.Entity.FASMaterial;
import com.doritech.PdfService.Entity.FASStaffMember;
import com.doritech.PdfService.Repository.CCTVBranchInfoRepository;
import com.doritech.PdfService.Repository.FASBranchDocumentRepository;
import com.doritech.PdfService.Repository.FASBranchInfoRepository;
import com.doritech.PdfService.Request.FASBranchInfoRequestDTO;
import com.doritech.PdfService.Response.CustomerResponse;
import com.doritech.PdfService.Response.FASBranchInfoResponseDTO;
import com.doritech.PdfService.Response.ResponseEntity;
import com.doritech.PdfService.ValidationService.CompanySiteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.ServletContext;

@Service
public class FASBranchInfoService {

	private static final Logger logger = LoggerFactory.getLogger(FASBranchInfoService.class);
	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private FASBranchInfoRepository fasBranchInfoRepository;

	@Autowired
	private FASBranchDocumentRepository fasBranchDocumentRepository;

	@Autowired
	private ImageStorageService imageStorageService;

	@Autowired
	private CompanySiteService companySiteService;

	@Autowired
	private CCTVBranchInfoRepository cctvBranchInfoRepository;

	private CustomerResponse fetchCustomerDetailsByCustomerId(Integer customerId) {
		if (customerId == null) {
			logger.warn("fetchCustomerDetailsByCustomerId called with null customerId");
			return null;
		}
		try {
			ResponseEntity apiResponse = companySiteService.getCustomerDetailsByCustomerId(customerId);
			if (apiResponse == null || apiResponse.getPayload() == null) {
				logger.warn("Feign client returned null response for customerId: {}", customerId);
				return null;
			}
			if (apiResponse.getStatusCode() == null || !apiResponse.getStatusCode().toString().equals("200")) {
				logger.warn("Customer details API returned non-200 for customerId: {}. Status: {}", customerId,
						apiResponse.getStatusCode());
				return null;
			}
			return new ObjectMapper().convertValue(apiResponse.getPayload(), CustomerResponse.class);
		} catch (feign.FeignException fe) {
			logger.error("Feign error fetching customer details for customerId: {}. Error: {}", customerId,
					fe.getMessage());
			return null;
		} catch (Exception e) {
			logger.error("Unexpected error fetching customer details for customerId: {}. Error: {}", customerId,
					e.getMessage(), e);
			return null;
		}
	}

	private CustomerResponse fetchCustomerPayload(Integer scheduleVisitId) {
		if (scheduleVisitId == null) {
			logger.warn("fetchCustomerPayload called with null scheduleVisitId");
			return null;
		}
		try {
			ResponseEntity res = companySiteService.fetchCustomer(scheduleVisitId);
			if (res == null || res.getPayload() == null) {
				logger.warn("Feign client returned null customer response for scheduleVisitId: {}", scheduleVisitId);
				return null;
			}
			if (res.getStatusCode() == null || !res.getStatusCode().toString().equals("200")) {
				logger.warn("Customer API returned non-200 for scheduleVisitId: {}. Status: {}", scheduleVisitId,
						res.getStatusCode());
				return null;
			}
			return new ObjectMapper().convertValue(res.getPayload(), CustomerResponse.class);
		} catch (feign.FeignException fe) {
			logger.error("Feign error fetching customer for scheduleVisitId: {}. Error: {}", scheduleVisitId,
					fe.getMessage());
			return null;
		} catch (Exception e) {
			logger.error("Unexpected error fetching customer for scheduleVisitId: {}. Error: {}", scheduleVisitId,
					e.getMessage(), e);
			return null;
		}
	}

	public void updateAssignmentStatusToCompletedFromRepo(Integer scheduleVisitId) {

		if (scheduleVisitId == null) {
			logger.warn("scheduleVisitId is null, skipping assignment status update.");
			return;
		}

		try {
			int rowsUpdated = cctvBranchInfoRepository.updateAssignmentStatusToCompleted(scheduleVisitId);

			if (rowsUpdated > 0) {
				logger.info("Assignment status updated to COMPLETED for assignment_id: {}", scheduleVisitId);
			} else {
				logger.warn("No assignment found for assignment_id: {}", scheduleVisitId);
			}

		} catch (Exception e) {
			logger.error("Error updating assignment status for assignment_id: {}. Error: {}", scheduleVisitId,
					e.getMessage(), e);
			throw new RuntimeException("Failed to update assignment status", e);
		}
	}

	@Transactional
	public FASBranchInfoResponseDTO createFASInfoAndGeneratePdf(FASBranchInfoRequestDTO requestDTO,
			Map<String, MultipartFile> allFiles) throws Exception {

		if (requestDTO == null)
			throw new IllegalArgumentException("Request DTO must not be null.");
		if (requestDTO.getCustomerId() == null)
			throw new IllegalArgumentException("Customer ID must not be null.");
		if (requestDTO.getScheduleVisitId() == null)
			throw new IllegalArgumentException("Schedule Visit ID must not be null.");

		logger.info("Starting FAS branch info creation for scheduleVisitId: {}", requestDTO.getScheduleVisitId());

		Optional<FASBranchInfo> optional = fasBranchInfoRepository
				.findByScheduleVisitIdAndProductType(requestDTO.getScheduleVisitId(), requestDTO.getProductType());

		if (optional.isPresent()) {
			logger.warn("FAS data already exists for scheduleVisitId: {} and productType: {}",
					requestDTO.getScheduleVisitId(), requestDTO.getProductType());
			FASBranchInfo existingEntity = optional.get();
			CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(requestDTO.getCustomerId());
			CustomerResponse customerPayload = fetchCustomerPayload(requestDTO.getScheduleVisitId());
			FASBranchInfoResponseDTO responseDTO = mapEntityToResponseDto(existingEntity, customerDetailsPayload,
					customerPayload);
			responseDTO.setMessage("Data already exists for this ScheduleVisitId and ProductType");
			return responseDTO;
		}

		CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(requestDTO.getCustomerId());
		if (customerDetailsPayload == null)
			throw new IllegalArgumentException("Invalid or unreachable customer ID: " + requestDTO.getCustomerId());

		CustomerResponse customerPayload = fetchCustomerPayload(requestDTO.getScheduleVisitId());
		if (customerPayload == null)
			throw new IllegalArgumentException(
					"No customer found for scheduleVisitId: " + requestDTO.getScheduleVisitId());

		FASBranchInfo entity = mapDtoToEntity(requestDTO);
		entity.setCreatedAt(LocalDateTime.now());
		entity.setCreatedBy(requestDTO.getCreatedBy());
		entity.setUpdatedAt(LocalDateTime.now());

		FASBranchInfo saved = fasBranchInfoRepository.save(entity);
		logger.info("FAS branch info saved with ID: {}", saved.getId());

		saveDocuments(saved, allFiles);

		FASBranchInfoRequestDTO enriched = enrichDto(requestDTO, customerDetailsPayload, customerPayload);

		saved = generateAndSavePdfFile(saved, enriched);

		FASBranchInfoResponseDTO resp = mapEntityToResponseDto(saved, customerDetailsPayload, customerPayload);
		resp.setMessage("FAS inspection information saved successfully and PDF generated.");
		logger.info("FAS branch info creation completed for ID: {}", saved.getId());
		return resp;
	}

	public FASBranchInfoResponseDTO getFASInfoById(Long id) {
		logger.info("Fetching FAS branch info for ID: {}", id);
		FASBranchInfo entity = fasBranchInfoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("FAS branch info not found for ID: " + id));
		CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(entity.getCustomerId());
		CustomerResponse customerPayload = fetchCustomerPayload(entity.getScheduleVisitId());
		if (customerDetailsPayload == null)
			logger.warn("Could not fetch customer details for customerId: {} (ID: {}). Proceeding with stored data.",
					entity.getCustomerId(), id);
		if (customerPayload == null)
			logger.warn(
					"Could not fetch customer payload for scheduleVisitId: {} (ID: {}). Proceeding with stored data.",
					entity.getScheduleVisitId(), id);
		return mapEntityToResponseDto(entity, customerDetailsPayload, customerPayload);
	}

	public FASBranchInfoResponseDTO getFASInfoByScheduleVisitAndProductType(Integer scheduleVisitId,
			String productType) {
		logger.info("Fetching FAS info for scheduleVisitId: {} and productType: {}", scheduleVisitId, productType);
		FASBranchInfo entity = fasBranchInfoRepository.findByScheduleVisitIdAndProductType(scheduleVisitId, productType)
				.orElseThrow(() -> new RuntimeException(
						"FAS info not found for scheduleVisitId: " + scheduleVisitId + " productType: " + productType));
		CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(entity.getCustomerId());
		CustomerResponse customerPayload = fetchCustomerPayload(entity.getScheduleVisitId());
		if (customerDetailsPayload == null)
			logger.warn("Could not fetch customer details for customerId: {} during getByScheduleVisit.",
					entity.getCustomerId());
		if (customerPayload == null)
			logger.warn("Could not fetch customer payload for scheduleVisitId: {} during getByScheduleVisit.",
					scheduleVisitId);
		return mapEntityToResponseDto(entity, customerDetailsPayload, customerPayload);
	}

	public Page<FASBranchInfoResponseDTO> getAllFASInfo(int page, int size, String sortBy, String sortDir) {
		logger.info("Fetching all FAS branch info - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy,
				sortDir);
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return fasBranchInfoRepository.findAll(pageable).map(entity -> {
			try {
				CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(entity.getCustomerId());
				CustomerResponse customerPayload = fetchCustomerPayload(entity.getScheduleVisitId());
				if (customerDetailsPayload == null)
					logger.warn("Could not fetch customer details for customerId: {} (ID: {}) during getAll.",
							entity.getCustomerId(), entity.getId());
				if (customerPayload == null)
					logger.warn("Could not fetch customer payload for scheduleVisitId: {} (ID: {}) during getAll.",
							entity.getScheduleVisitId(), entity.getId());
				return mapEntityToResponseDtoList(entity, customerDetailsPayload, customerPayload);
			} catch (Exception e) {
				logger.error("Error mapping entity ID {} during getAll: {}", entity.getId(), e.getMessage(), e);
				return mapEntityToResponseDtoList(entity, null, null);
			}
		});
	}

	public byte[] getPdfBytesById(Long id) throws Exception {
		FASBranchInfo entity = fasBranchInfoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("FAS record not found for ID: " + id));

		String pdfPath = entity.getPdfFilePath();
		if (pdfPath == null || pdfPath.isBlank()) {
			logger.warn("PDF path null for ID: {}. Regenerating.", id);
			return regenerateAndSavePdf(entity);
		}

		Path path = Paths.get(servletContext.getRealPath("/" + pdfPath));
		if (!Files.exists(path)) {
			logger.warn("PDF file missing on disk for ID: {}. Regenerating.", id);
			return regenerateAndSavePdf(entity);
		}

		byte[] pdfBytes = Files.readAllBytes(path);
		logger.info("PDF bytes read successfully for ID: {}. Size: {} bytes", id, pdfBytes.length);
		return pdfBytes;
	}

	private byte[] regenerateAndSavePdf(FASBranchInfo entity) throws Exception {
		logger.info("Regenerating PDF for FAS entity ID: {}", entity.getId());

		CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(entity.getCustomerId());
		CustomerResponse customerPayload = fetchCustomerPayload(entity.getScheduleVisitId());

		if (customerDetailsPayload == null)
			logger.warn("Could not fetch customer details for PDF regeneration. ID: {}, customerId: {}.",
					entity.getId(), entity.getCustomerId());
		if (customerPayload == null)
			logger.warn("Could not fetch customer payload for PDF regeneration. ID: {}, scheduleVisitId: {}.",
					entity.getId(), entity.getScheduleVisitId());

		FASBranchInfoRequestDTO dto = enrichDto(mapEntityToRequestDto(entity), customerDetailsPayload, customerPayload);

		ByteArrayOutputStream baos = generateFASServiceReport(dto);
		if (baos == null || baos.size() == 0)
			throw new RuntimeException("PDF could not be generated for FAS ID: " + entity.getId());

		byte[] pdfBytes = baos.toByteArray();
		entity = savePdfToDiskAndUpdatePath(entity, pdfBytes);
		updateAssignmentStatusToCompletedFromRepo(entity.getScheduleVisitId());
		return pdfBytes;
	}

	private FASBranchInfo generateAndSavePdfFile(FASBranchInfo saved, FASBranchInfoRequestDTO enriched) {
		try {
			ByteArrayOutputStream baos = generateFASServiceReport(enriched);
			if (baos == null || baos.size() == 0) {
				logger.error("PDF generation returned empty output for FAS ID: {}", saved.getId());
				return saved;
			}
			saved = savePdfToDiskAndUpdatePath(saved, baos.toByteArray());
			updateAssignmentStatusToCompletedFromRepo(saved.getScheduleVisitId());
		} catch (Exception e) {
			logger.error("PDF generation/save failed for FAS ID: {}. Error: {}", saved.getId(), e.getMessage(), e);
		}
		return saved;
	}

	private FASBranchInfo savePdfToDiskAndUpdatePath(FASBranchInfo entity, byte[] pdfBytes) {
		try {
			String realPath = servletContext.getRealPath("/fasPdf/");
			Path dir = Paths.get(realPath);
			Files.createDirectories(dir);
			String filename = "fas_report_" + entity.getId() + ".pdf";
			Files.write(dir.resolve(filename), pdfBytes);
			entity.setPdfFilePath("fasPdf/" + filename);
			entity = fasBranchInfoRepository.save(entity);
			logger.info("FAS PDF saved: fasPdf/{}", filename);
		} catch (Exception e) {
			logger.error("Could not save PDF to disk for FAS ID: {}. Error: {}", entity.getId(), e.getMessage(), e);
		}
		return entity;
	}

	private void saveDocuments(FASBranchInfo saved, Map<String, MultipartFile> allFiles) {
		if (allFiles == null || allFiles.isEmpty())
			return;
		allFiles.entrySet().stream().filter(e -> !e.getKey().equals("data")).forEach(e -> {
			String docType = e.getKey();
			MultipartFile file = e.getValue();
			if (file == null || file.isEmpty()) {
				logger.warn("Skipping empty file for docType: {}", docType);
				return;
			}
			try {
				String savedPath = imageStorageService.saveImage(file, docType, saved.getId());
				FASBranchDocument doc = new FASBranchDocument();
				doc.setDocumentName(docType);
				doc.setDocumentPath(savedPath);
				doc.setFasBranchInfo(saved);
				doc.setCreatedOn(LocalDateTime.now());
				doc.setModifiedOn(LocalDateTime.now());
				fasBranchDocumentRepository.save(doc);
				logger.info("Document saved - Type: {}, Path: {}", docType, savedPath);
			} catch (Exception ex) {
				logger.error("Failed to save doc {} for FAS ID {}: {}", docType, saved.getId(), ex.getMessage(), ex);
			}
		});
	}

	public ByteArrayOutputStream generateFASServiceReport(FASBranchInfoRequestDTO dto) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4, 28, 28, 28, 28);
		PdfWriter.getInstance(document, baos);
		document.open();

		Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, BaseColor.BLACK);
		Font boldFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 11, BaseColor.BLACK);
		Font companyFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 11, BaseColor.BLACK);
		Font companyFont2 = FontFactory.getFont(FontFactory.TIMES_BOLD, 9, BaseColor.BLACK);
		Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
		Font smallBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
		Font tinyBold = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
		Font tiny = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
		Font valueBlue = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLUE);
		Font valueBlueTiny = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLUE);

		Image checkIcon = loadIcon("check");
		Image crossIcon = loadIcon("cross");

		Paragraph title = new Paragraph("SERVICE REPORT OF FIRE ALARM SYSTEM", titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(10f);
		document.add(title);

		PdfPTable mainTable = new PdfPTable(2);
		mainTable.setWidthPercentage(100);
		mainTable.setWidths(new float[] { 62, 38 });

		PdfPTable leftTable = new PdfPTable(1);

		PdfPCell companyCell = new PdfPCell();
		companyCell.setBorder(Rectangle.BOX);
		companyCell.setPadding(5);
		Paragraph cp = new Paragraph();
		cp.setLeading(12f);
		cp.add(new Chunk("DIGITALS INDIA SECURITY PRODUCTS PVT. LTD.\n", companyFont));
		cp.add(new Chunk("[An ISO 9001:2015 Company]\n", companyFont2));
		cp.add(new Chunk(
				"SCO 6-7, Saundh Complex, Near Atwal House, Cantt Road, Jalandhar-144005 (Punjab), Ph: 0181-4634072\n",
				smallBoldFont));
		cp.add(new Chunk("di.jalandhar@digitalsindia.com", smallBoldFont));
		companyCell.addElement(cp);
		leftTable.addCell(companyCell);

		addFormRow(leftTable, "GSTIN/UIN:", "03AAACCD4430E1ZD", normalFont, normalFont);
		addFormRow(leftTable, "Name of Field Associate:", safeStr(dto, FASBranchInfoRequestDTO::getFieldAssociateName),
				normalFont, valueBlue);
		addFormRow(leftTable, "Branch/Site Name:", safeStr(dto, FASBranchInfoRequestDTO::getBranchName), normalFont,
				valueBlue);
		addFormRow(leftTable, "Under Regional Office/Zonal Office:",
				safeStr(dto, FASBranchInfoRequestDTO::getRegionalOffice), normalFont, valueBlue);
		addFormRow(leftTable, "Address:", safeStr(dto, FASBranchInfoRequestDTO::getBranchAddress), normalFont,
				valueBlue);

		PdfPCell twoColCell = new PdfPCell();
		twoColCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
		twoColCell.setPadding(0);
		PdfPTable twoCol = new PdfPTable(2);
		twoCol.setWidthPercentage(100);

		Phrase emailPh = new Phrase();
		emailPh.add(new Chunk("Br. E-mail ID:- ", normalFont));
		emailPh.add(new Chunk(safeStr(dto, FASBranchInfoRequestDTO::getBranchEmail), valueBlue));
		PdfPCell c1 = new PdfPCell(emailPh);
		c1.setBorder(Rectangle.BOX);
		c1.setPadding(4);
		twoCol.addCell(c1);

		Phrase codePh = new Phrase();
		codePh.add(new Chunk("Branch Code:- ", normalFont));
		codePh.add(new Chunk(safeStr(dto, FASBranchInfoRequestDTO::getCustomerCode), valueBlue));
		PdfPCell c2 = new PdfPCell(codePh);
		c2.setBorder(Rectangle.BOX);
		c2.setPadding(4);
		twoCol.addCell(c2);

		Phrase cpName = new Phrase();
		cpName.add(new Chunk("Concerned Person:- ", normalFont));
		cpName.add(new Chunk(safeStr(dto, FASBranchInfoRequestDTO::getConcernedPersonName), valueBlue));
		PdfPCell c3 = new PdfPCell(cpName);
		c3.setBorder(Rectangle.BOX);
		c3.setPadding(4);
		twoCol.addCell(c3);

		Phrase cpDesig = new Phrase();
		cpDesig.add(new Chunk("Designation:- ", normalFont));
		cpDesig.add(new Chunk(safeStr(dto, FASBranchInfoRequestDTO::getConcernedPersonDesignation), valueBlue));
		PdfPCell c4 = new PdfPCell(cpDesig);
		c4.setBorder(Rectangle.BOX);
		c4.setPadding(4);
		twoCol.addCell(c4);

		twoColCell.addElement(twoCol);
		leftTable.addCell(twoColCell);

		addFormRow(leftTable, "Phone / Mobile No:-", safeStr(dto, FASBranchInfoRequestDTO::getConcernedPersonPhone),
				normalFont, valueBlue);

		PdfPCell leftCell = new PdfPCell(leftTable);
		leftCell.setBorder(Rectangle.BOX);
		leftCell.setPadding(0);
		mainTable.addCell(leftCell);

		PdfPTable rightTable = new PdfPTable(1);

		PdfPCell contactCell = new PdfPCell();
		contactCell.setBorder(Rectangle.BOX);
		contactCell.setPadding(5);
		Paragraph contactInfo = new Paragraph();
		contactInfo.setLeading(12f);
		contactInfo.add(new Chunk("For Complaints & Technical Support\n", boldFont));
		contactInfo.add(new Chunk("Mobs: ", boldFont));
		contactInfo.add(new Chunk("8448990211, 8448990201", normalFont));
		contactCell.addElement(contactInfo);
		rightTable.addCell(contactCell);

		addRightRow(rightTable, "Service Report No.", "PUBCFAS", normalFont, smallBoldFont);

		String inspDateStr = dto != null && dto.getInspectionDate() != null ? DATE_FMT.format(dto.getInspectionDate())
				: "";
		addRightRow(rightTable, "Date", inspDateStr, normalFont, valueBlue);

		PdfPCell prodHeader = new PdfPCell(new Phrase("Products Details", smallBoldFont));
		prodHeader.setBorder(Rectangle.BOX);
		prodHeader.setPadding(4);
		prodHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		prodHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		rightTable.addCell(prodHeader);

		addRightRow(rightTable, "TYPE", "FIRE ALARM", normalFont, valueBlue);
		addRightRow(rightTable, "MODEL", safeStr(dto, FASBranchInfoRequestDTO::getFaModel), normalFont, valueBlue);
		addRightRow(rightTable, "NO. OF ZONES", safeStr(dto, FASBranchInfoRequestDTO::getNoOfZones), normalFont,
				valueBlue);

		String instDateStr = dto != null && dto.getDateOfInstallation() != null
				? DATE_FMT.format(dto.getDateOfInstallation())
				: "";
		addRightRow(rightTable, "DATE OF INSTALLATION", instDateStr, normalFont, valueBlue);
		addRightRow(rightTable, "BATTERY VOLTAGE", safeStr(dto, FASBranchInfoRequestDTO::getBatteryVoltage) + " VOLTS",
				normalFont, valueBlue);

		PdfPCell rightCell = new PdfPCell(rightTable);
		rightCell.setBorder(Rectangle.BOX);
		rightCell.setPadding(0);
		mainTable.addCell(rightCell);

		document.add(mainTable);

		PdfPTable statusTable = new PdfPTable(2);
		statusTable.setWidthPercentage(100);
		statusTable.setWidths(new float[] { 45, 55 });
		statusTable.setSpacingBefore(4f);

		addStatusRow(statusTable, "Fire Alarm System Working Status", smallBoldFont,
				dto != null && dto.getFaWorkingStatus() != null ? dto.getFaWorkingStatus().trim().toLowerCase() : "",
				new String[] { "Working", "Not Working", "Not Available" }, valueBlueTiny, checkIcon, crossIcon);

		addStatusRow(statusTable, "System Connected Through", smallBoldFont,
				dto != null && dto.getSystemConnectedThrough() != null
						? dto.getSystemConnectedThrough().trim().toLowerCase()
						: "",
				new String[] { "MAINS", "UPS", "Not Available" }, valueBlueTiny, checkIcon, crossIcon);

		addStatusRow(statusTable, "Auto-dialer [PSTN/GSM]", smallBoldFont,
				dto != null && dto.getAutoDialerTypePstnGsm() != null
						? dto.getAutoDialerTypePstnGsm().trim().toLowerCase()
						: "",
				new String[] { "PSTN", "GSM", "Not Available" }, valueBlueTiny, checkIcon, crossIcon);

		addStatusRow(statusTable, "Auto-dialer Type", smallBoldFont,
				dto != null && dto.getAutoDialerInbuiltExternal() != null
						? dto.getAutoDialerInbuiltExternal().trim().toLowerCase()
						: "",
				new String[] { "Inbuilt", "External", "Not Available" }, valueBlueTiny, checkIcon, crossIcon);

		PdfPCell simLabel = new PdfPCell(new Phrase("SIM NUMBER", smallBoldFont));
		simLabel.setBorder(Rectangle.BOX);
		simLabel.setPadding(4);
		statusTable.addCell(simLabel);
		PdfPCell simVal = new PdfPCell(new Phrase(safeStr(dto, FASBranchInfoRequestDTO::getSimNumber), valueBlueTiny));
		simVal.setBorder(Rectangle.BOX);
		simVal.setPadding(4);
		statusTable.addCell(simVal);

		PdfPCell noUpdLabel = new PdfPCell(new Phrase("No. Updated in the Auto Dialer", smallBoldFont));
		noUpdLabel.setBorder(Rectangle.BOX);
		noUpdLabel.setPadding(4);
		statusTable.addCell(noUpdLabel);
		String noVal = dto != null && dto.getNoUpdatedInAutoDialer() != null
				? String.valueOf(dto.getNoUpdatedInAutoDialer())
				: "";
		PdfPCell noUpdVal = new PdfPCell(new Phrase(noVal, valueBlueTiny));
		noUpdVal.setBorder(Rectangle.BOX);
		noUpdVal.setPadding(4);
		statusTable.addCell(noUpdVal);

		document.add(statusTable);

		PdfPTable hwHeaderTable = new PdfPTable(1);
		hwHeaderTable.setWidthPercentage(100);
		hwHeaderTable.setSpacingBefore(5f);
		PdfPCell hwHeader = new PdfPCell(new Phrase("HARDWARE INSTALLED & STATUS", tinyBold));
		hwHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		hwHeader.setPadding(4);
		hwHeader.setBorder(Rectangle.BOX);
		hwHeaderTable.addCell(hwHeader);
		document.add(hwHeaderTable);

		PdfPTable hwTable = new PdfPTable(5);
		hwTable.setWidthPercentage(100);
		hwTable.setWidths(new float[] { 6, 28, 8, 18, 40 });

		for (String h : new String[] { "S.N.", "ITEM DESCRIPTION", "QTY", "WORKING STATUS", "LOCATION" }) {
			PdfPCell hc = new PdfPCell(new Phrase(h, tinyBold));
			hc.setBorder(Rectangle.BOX);
			hc.setPadding(3);
			hc.setBackgroundColor(BaseColor.LIGHT_GRAY);
			hc.setHorizontalAlignment(Element.ALIGN_CENTER);
			hwTable.addCell(hc);
		}

		List<FASHardwareItemDTO> hwItems = dto != null && dto.getHardwareItems() != null ? dto.getHardwareItems()
				: Collections.emptyList();
		for (FASHardwareItemDTO hw : hwItems) {
			try {
				PdfPCell snCell = new PdfPCell(
						new Phrase(hw.getSerialNumber() != null ? String.valueOf(hw.getSerialNumber()) : "", tiny));
				snCell.setBorder(Rectangle.BOX);
				snCell.setPadding(3);
				snCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hwTable.addCell(snCell);

				String itemLabel = hw.getZoneLabel() != null && !hw.getZoneLabel().isBlank() ? hw.getZoneLabel()
						: (hw.getItemName() != null ? hw.getItemName() : "");
				PdfPCell nameCell = new PdfPCell(
						new Phrase(itemLabel, hw.getZoneLabel() != null ? valueBlueTiny : tiny));
				nameCell.setBorder(Rectangle.BOX);
				nameCell.setPadding(3);
				nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				hwTable.addCell(nameCell);

				PdfPCell qtyCell = new PdfPCell(
						new Phrase(hw.getQuantity() != null ? String.valueOf(hw.getQuantity()) : "", valueBlueTiny));
				qtyCell.setBorder(Rectangle.BOX);
				qtyCell.setPadding(3);
				qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				hwTable.addCell(qtyCell);

				hwTable.addCell(createStatusCell(valueBlueTiny, hw.getWorkingStatus(), checkIcon, crossIcon));

				PdfPCell locCell = new PdfPCell(
						new Phrase(hw.getLocation() != null ? hw.getLocation() : "", valueBlueTiny));
				locCell.setBorder(Rectangle.BOX);
				locCell.setPadding(3);
				locCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				locCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				hwTable.addCell(locCell);
			} catch (Exception e) {
				logger.error("Error rendering hardware item row: {}", e.getMessage(), e);
			}
		}
		document.add(hwTable);

		PdfPTable dialerSection = new PdfPTable(1);
		dialerSection.setWidthPercentage(100);
		dialerSection.setSpacingBefore(5f);
		PdfPCell dialerHeader = new PdfPCell(new Phrase("Following contact numbers are fed in the Dialer", tinyBold));
		dialerHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		dialerHeader.setPadding(4);
		dialerHeader.setBorder(Rectangle.BOX);
		dialerSection.addCell(dialerHeader);
		document.add(dialerSection);

		PdfPTable dialerTable = new PdfPTable(4);
		dialerTable.setWidthPercentage(100);
		dialerTable.setWidths(new float[] { 30, 20, 30, 20 });

		for (String dh : new String[] { "Name of Staff / Authority", "Mobile/Tel. Number", "Name of Staff / Authority",
				"Mobile/Tel. Number" }) {
			PdfPCell dhCell = new PdfPCell(new Phrase(dh, tinyBold));
			dhCell.setBorder(Rectangle.BOX);
			dhCell.setPadding(3);
			dhCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			dhCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			dialerTable.addCell(dhCell);
		}

		List<FASDialerContactDTO> contacts = dto != null && dto.getDialerContacts() != null ? dto.getDialerContacts()
				: Collections.emptyList();
		for (int i = 0; i < Math.max(contacts.size(), 2); i += 2) {
			try {
				FASDialerContactDTO left = i < contacts.size() ? contacts.get(i) : null;
				FASDialerContactDTO right = i + 1 < contacts.size() ? contacts.get(i + 1) : null;

				PdfPCell ln = new PdfPCell(new Phrase(
						left != null && left.getStaffName() != null ? left.getStaffName() : "", valueBlueTiny));
				ln.setBorder(Rectangle.BOX);
				ln.setPadding(3);
				ln.setHorizontalAlignment(Element.ALIGN_CENTER);
				dialerTable.addCell(ln);

				PdfPCell lm = new PdfPCell(new Phrase(
						left != null && left.getMobileNumber() != null ? left.getMobileNumber() : "", valueBlueTiny));
				lm.setBorder(Rectangle.BOX);
				lm.setPadding(3);
				lm.setHorizontalAlignment(Element.ALIGN_CENTER);
				dialerTable.addCell(lm);

				PdfPCell rn = new PdfPCell(new Phrase(
						right != null && right.getStaffName() != null ? right.getStaffName() : "", valueBlueTiny));
				rn.setBorder(Rectangle.BOX);
				rn.setPadding(3);
				rn.setHorizontalAlignment(Element.ALIGN_CENTER);
				dialerTable.addCell(rn);

				PdfPCell rm = new PdfPCell(
						new Phrase(right != null && right.getMobileNumber() != null ? right.getMobileNumber() : "",
								valueBlueTiny));
				rm.setBorder(Rectangle.BOX);
				rm.setPadding(3);
				rm.setHorizontalAlignment(Element.ALIGN_CENTER);
				dialerTable.addCell(rm);
			} catch (Exception e) {
				logger.error("Error rendering dialer contact row at index: {}. Error: {}", i, e.getMessage(), e);
			}
		}
		document.add(dialerTable);

		PdfPTable gcpHeader = new PdfPTable(1);
		gcpHeader.setWidthPercentage(100);
		gcpHeader.setSpacingBefore(5f);
		PdfPCell gcpH = new PdfPCell(new Phrase("General Check Points", tinyBold));
		gcpH.setBackgroundColor(BaseColor.LIGHT_GRAY);
		gcpH.setPadding(4);
		gcpH.setBorder(Rectangle.BOX);
		gcpHeader.addCell(gcpH);
		document.add(gcpHeader);

		PdfPTable gcpTable = new PdfPTable(4);
		gcpTable.setWidthPercentage(100);
		gcpTable.setWidths(new float[] { 38, 12, 38, 12 });

		Boolean[] gcpStatuses = { dto != null ? dto.getIsPanelCleaned() : null,
				dto != null ? dto.getIsOperationExplained() : null,
				dto != null ? dto.getAreHootersDifficultToAccess() : null,
				dto != null ? dto.getIsSettingDoneAsPerBank() : null, dto != null ? dto.getAreSensorsCleaned() : null,
				dto != null ? dto.getIsPanelAndSensorsCleaned() : null, dto != null ? dto.getIsBatteryWorking() : null,
				dto != null ? dto.getIsPasswordChanged() : null };
		String[] gcpLabels = { "1. Fire Alarm Panel cleaned from inside & outside",
				"5. Operation of the system is explained again and is understood.",
				"2. Whether both the hooters are difficult to access and located at different places",
				"6. Setting of as per Bank's Guidelines made in the equipment",
				"3. All sensors checked & cleaned for removing dust",
				"7. Panel & All Sensors cleaned from inside & outside",
				"4. BATTERY CHECKED & FOUND TO BE WORKING PROPERLY", "8. Password changed as per BM/SO" };

		for (int i = 0; i < gcpLabels.length; i++) {
			try {
				PdfPCell desc = new PdfPCell(new Phrase(gcpLabels[i], tiny));
				desc.setBorder(Rectangle.BOX);
				desc.setPadding(3);
				gcpTable.addCell(desc);
				gcpTable.addCell(createStatusCell(valueBlueTiny, gcpStatuses[i], checkIcon, crossIcon));
			} catch (Exception e) {
				logger.error("Error rendering GCP row at index: {}. Error: {}", i, e.getMessage(), e);
			}
		}
		document.add(gcpTable);

		List<FASMaterialDTO> allMaterials = dto != null && dto.getMaterials() != null ? dto.getMaterials()
				: Collections.emptyList();
		List<FASMaterialDTO> replaced = allMaterials.stream()
				.filter(m -> m.getMaterialType() != null && m.getMaterialType() == FASMaterialDTO.MaterialType.REPLACED)
				.collect(Collectors.toList());
		List<FASMaterialDTO> required = allMaterials.stream()
				.filter(m -> m.getMaterialType() != null && m.getMaterialType() == FASMaterialDTO.MaterialType.REQUIRED)
				.collect(Collectors.toList());

		PdfPTable matSection = new PdfPTable(2);
		matSection.setWidthPercentage(100);
		matSection.setSpacingBefore(5f);
		matSection.addCell(new PdfPCell(
				buildMaterialTable("MATERIAL REPLACED", replaced, tinyBold, valueBlueTiny, checkIcon, crossIcon)));
		matSection.addCell(new PdfPCell(
				buildMaterialTable("MATERIAL REQUIRED", required, tinyBold, valueBlueTiny, checkIcon, crossIcon)));
		document.add(matSection);

		PdfPTable bottomTable = new PdfPTable(2);
		bottomTable.setWidthPercentage(100);
		bottomTable.setWidths(new float[] { 50, 50 });
		bottomTable.setSpacingBefore(5f);

		PdfPTable leftBottom = new PdfPTable(1);

		PdfPCell defHeader = new PdfPCell(
				new Phrase("Defect found on inspection / Remarks / Recommendation by Field Associate", tinyBold));
		defHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		defHeader.setPadding(3);
		defHeader.setBorder(Rectangle.BOX);
		leftBottom.addCell(defHeader);

		PdfPCell remarksCell = new PdfPCell(
				new Phrase(dto != null && dto.getFaRemarks() != null ? dto.getFaRemarks() : "", valueBlueTiny));
		remarksCell.setFixedHeight(50f);
		remarksCell.setPadding(4);
		remarksCell.setBorder(Rectangle.BOX);
		leftBottom.addCell(remarksCell);

		PdfPCell syHeader = new PdfPCell(
				new Phrase("System is working properly & is in ON condition. It has been explained to:", tinyBold));
		syHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		syHeader.setPadding(3);
		syHeader.setBorder(Rectangle.BOX);
		leftBottom.addCell(syHeader);

		PdfPTable staffTable = new PdfPTable(3);
		staffTable.setWidthPercentage(100);
		staffTable.setWidths(new float[] { 12, 44, 44 });

		PdfPCell sh1 = new PdfPCell(new Phrase("Sr.", tinyBold));
		sh1.setBorder(Rectangle.BOX);
		sh1.setPadding(2);
		staffTable.addCell(sh1);
		PdfPCell sh2 = new PdfPCell(new Phrase("NAME", tinyBold));
		sh2.setBorder(Rectangle.BOX);
		sh2.setPadding(2);
		staffTable.addCell(sh2);
		PdfPCell sh3 = new PdfPCell(new Phrase("DESIGNATION", tinyBold));
		sh3.setBorder(Rectangle.BOX);
		sh3.setPadding(2);
		staffTable.addCell(sh3);

		List<FASStaffMemberDTO> staffList = dto != null && dto.getStaffMembers() != null ? dto.getStaffMembers()
				: Collections.emptyList();
		for (int i = 0; i < staffList.size(); i++) {
			try {
				FASStaffMemberDTO st = staffList.get(i);
				PdfPCell s1 = new PdfPCell(new Phrase(String.valueOf(i + 1), valueBlueTiny));
				s1.setBorder(Rectangle.BOX);
				s1.setPadding(2);
				staffTable.addCell(s1);
				PdfPCell s2 = new PdfPCell(new Phrase(st.getName() != null ? st.getName() : "", valueBlueTiny));
				s2.setBorder(Rectangle.BOX);
				s2.setPadding(2);
				staffTable.addCell(s2);
				PdfPCell s3 = new PdfPCell(
						new Phrase(st.getDesignation() != null ? st.getDesignation() : "", valueBlueTiny));
				s3.setBorder(Rectangle.BOX);
				s3.setPadding(2);
				staffTable.addCell(s3);
			} catch (Exception e) {
				logger.error("Error rendering staff member row at index: {}. Error: {}", i, e.getMessage(), e);
			}
		}
		PdfPCell staffWrapper = new PdfPCell(staffTable);
		staffWrapper.setBorder(Rectangle.BOX);
		staffWrapper.setPadding(0);
		leftBottom.addCell(staffWrapper);

		PdfPCell crHeader = new PdfPCell(new Phrase("Customer's Remarks", tinyBold));
		crHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		crHeader.setPadding(3);
		crHeader.setBorder(Rectangle.BOX);
		leftBottom.addCell(crHeader);

		PdfPCell crVal = new PdfPCell(new Phrase(
				dto != null && dto.getCustomerRemarks() != null ? dto.getCustomerRemarks() : "", valueBlueTiny));
		crVal.setFixedHeight(30f);
		crVal.setPadding(4);
		crVal.setBorder(Rectangle.BOX);
		leftBottom.addCell(crVal);

		PdfPCell sigCell = new PdfPCell(new Phrase("[Signature & Seal]", tinyBold));
		sigCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		sigCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		sigCell.setFixedHeight(25f);
		sigCell.setPadding(3);
		sigCell.setBorder(Rectangle.BOX);
		leftBottom.addCell(sigCell);

		bottomTable.addCell(new PdfPCell(leftBottom));

		PdfPTable rightBottom = new PdfPTable(1);

		PdfPCell estHeader = new PdfPCell(new Phrase("ESTIMATE", tinyBold));
		estHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		estHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		estHeader.setPadding(3);
		estHeader.setBorder(Rectangle.BOX);
		rightBottom.addCell(estHeader);

		String estNo = dto != null && dto.getEstimateNo() != null ? dto.getEstimateNo() : "";
		String estProd = dto != null && dto.getEstimateProductDetails() != null ? dto.getEstimateProductDetails() : "";
		String estAmt = dto != null && dto.getEstimateAmount() != null ? String.valueOf(dto.getEstimateAmount()) : "";
		String estDate = dto != null && dto.getEstimateDate() != null ? DATE_FMT.format(dto.getEstimateDate()) : "";

		addEstimateRow(rightBottom, "ESTIMATE NO.", estNo, "ESTIMATE PRODUCT", estProd, normalFont, valueBlueTiny);
		addEstimateRow(rightBottom, "ESTIMATE AMOUNT", estAmt, "ESTIMATE DATE", estDate, normalFont, valueBlueTiny);

		PdfPCell ppHeader = new PdfPCell(new Phrase("PENDING PAYMENT", tinyBold));
		ppHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		ppHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		ppHeader.setPadding(3);
		ppHeader.setBorder(Rectangle.BOX);
		rightBottom.addCell(ppHeader);

		String invNo = dto != null && dto.getBillNo() != null ? dto.getBillNo() : "";
		String invProd = dto != null && dto.getEstimateProductDetails() != null ? dto.getEstimateProductDetails() : "";
		String invAmt = dto != null && dto.getEstimateAmount() != null ? String.valueOf(dto.getEstimateAmount()) : "";
		String invDate = dto != null && dto.getBillDate() != null ? DATE_FMT.format(dto.getBillDate()) : "";

		addEstimateRow(rightBottom, "INVOICE NO.", invNo, "PRODUCTS", invProd, normalFont, valueBlueTiny);
		addEstimateRow(rightBottom, "AMOUNT", invAmt, "DATE", invDate, normalFont, valueBlueTiny);

		PdfPCell miHeader = new PdfPCell(new Phrase("MANUAL INVOICE", tinyBold));
		miHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
		miHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		miHeader.setPadding(3);
		miHeader.setBorder(Rectangle.BOX);
		rightBottom.addCell(miHeader);

		addEstimateRow(rightBottom, "INVOICE NO.", invNo, "PRODUCTS", invProd, normalFont, valueBlueTiny);
		addEstimateRow(rightBottom, "AMOUNT", invAmt, "DATE", invDate, normalFont, valueBlueTiny);

		PdfPTable billTable = new PdfPTable(2);
		billTable.setWidthPercentage(100);

		PdfPCell brl = new PdfPCell(new Phrase("Bill to be Raised", tiny));
		brl.setBorder(Rectangle.BOX);
		brl.setPadding(3);
		billTable.addCell(brl);
		billTable.addCell(
				createStatusCell(valueBlueTiny, dto != null ? dto.getBillToBeRaised() : null, checkIcon, crossIcon));

		PdfPCell pfl = new PdfPCell(new Phrase("Pending Invoice / Payment Followed", tiny));
		pfl.setBorder(Rectangle.BOX);
		pfl.setPadding(3);
		billTable.addCell(pfl);
		billTable.addCell(createStatusCell(valueBlueTiny, dto != null ? dto.getIsInvoicePaymentFollowed() : null,
				checkIcon, crossIcon));

		PdfPCell billWrapper = new PdfPCell(billTable);
		billWrapper.setBorder(Rectangle.BOX);
		billWrapper.setPadding(0);
		rightBottom.addCell(billWrapper);

		Phrase prPh = new Phrase();
		prPh.add(new Chunk("Remarks for Payment: ", normalFont));
		prPh.add(new Chunk(dto != null && dto.getPaymentRemarks() != null ? dto.getPaymentRemarks() : "",
				valueBlueTiny));
		PdfPCell prCell = new PdfPCell(prPh);
		prCell.setBorder(Rectangle.BOX);
		prCell.setPadding(3);
		rightBottom.addCell(prCell);

		PdfPCell faSig = new PdfPCell(new Phrase("[FA Signature]", tinyBold));
		faSig.setBackgroundColor(BaseColor.LIGHT_GRAY);
		faSig.setHorizontalAlignment(Element.ALIGN_CENTER);
		faSig.setFixedHeight(25f);
		faSig.setPadding(3);
		faSig.setBorder(Rectangle.BOX);
		rightBottom.addCell(faSig);

		bottomTable.addCell(new PdfPCell(rightBottom));
		document.add(bottomTable);

		PdfPTable footerTable = new PdfPTable(1);
		footerTable.setWidthPercentage(100);
		footerTable.setSpacingBefore(4f);
		PdfPCell bankCell = new PdfPCell(
				new Phrase(
						"Do not make cash payment, Please transfer payment in the following company account\n"
								+ "Digitals India Security Products Pvt. Ltd. Union Bank of India, Anand Vihar Delhi "
								+ "A/c No.-542805010000112  IFSC - UBIN0554286\n"
								+ "Regd. Office : Hall No. 1 C Block Market, Suraj Mal Vihar, Delhi -110092",
						tinyBold));
		bankCell.setBorder(Rectangle.BOX);
		bankCell.setPadding(5);
		footerTable.addCell(bankCell);
		document.add(footerTable);

		document.close();
		return baos;
	}

	private void addStatusRow(PdfPTable table, String labelText, Font labelFont, String value, String[] options,
			Font valueFont, Image checkIcon, Image crossIcon) throws Exception {
		PdfPCell label = new PdfPCell(new Phrase(labelText, labelFont));
		label.setBorder(Rectangle.BOX);
		label.setPadding(4);
		table.addCell(label);
		table.addCell(createMultiOptionStatusCell(valueFont, value, options, checkIcon, crossIcon));
	}

	private PdfPCell createMultiOptionStatusCell(Font font, String value, String[] options, Image checkIcon,
			Image crossIcon) throws Exception {
		int n = options.length;
		float[] colWidths = new float[2 * n - 1];
		for (int i = 0; i < 2 * n - 1; i++) {
			colWidths[i] = (i % 2 == 0) ? 30f : 5f;
		}

		PdfPTable inner = new PdfPTable(2 * n - 1);
		inner.setWidthPercentage(100);
		inner.setWidths(colWidths);

		Font dull = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, new BaseColor(80, 80, 80));

		for (int idx = 0; idx < n; idx++) {
			String opt = options[idx];
			boolean matched = value != null && value.equals(opt.trim().toLowerCase());
			final Image icon = matched ? checkIcon : null;
			final String label = opt;

			PdfPCell box = new PdfPCell();
			box.setBorder(Rectangle.BOX);
			box.setHorizontalAlignment(Element.ALIGN_CENTER);
			box.setVerticalAlignment(Element.ALIGN_MIDDLE);
			box.setPadding(1);
			box.setFixedHeight(22f);
			box.setCellEvent((c, pos, canvases) -> {
				try {
					PdfContentByte cb = canvases[PdfPTable.TEXTCANVAS];
					float cx = (pos.getLeft() + pos.getRight()) / 2f;
					float cy = pos.getBottom();
					ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(label, dull), cx, cy + 2f, 0);
					if (icon != null) {
						icon.scaleToFit(10, 10);
						icon.setAbsolutePosition(cx - icon.getScaledWidth() / 2f, cy + 10f);
						canvases[PdfPTable.BACKGROUNDCANVAS].addImage(icon);
					} else {
						cb.setLineWidth(0.5f);
						cb.setColorStroke(new BaseColor(160, 160, 160));
						cb.rectangle(cx - 4f, cy + 10f, 8f, 8f);
						cb.stroke();
					}
				} catch (Exception ex) {
					logger.error("Multi-option cell render error: {}", ex.getMessage());
				}
			});
			inner.addCell(box);

			if (idx < n - 1) {
				PdfPCell gap = new PdfPCell(new Phrase(""));
				gap.setBorder(Rectangle.NO_BORDER);
				gap.setFixedHeight(22f);
				inner.addCell(gap);
			}
		}

		PdfPCell outer = new PdfPCell(inner);
		outer.setBorder(Rectangle.BOX);
		outer.setPadding(2);
		return outer;
	}

	private PdfPCell createStatusCell(Font font, Object statusObj, Image checkIcon, Image crossIcon) throws Exception {
		String s = statusObj == null ? "" : statusObj.toString().trim().toLowerCase();
		Image imgY = null, imgN = null;
		if (s.equals("true") || s.equals("yes") || s.equals("working") || s.equals("y"))
			imgY = checkIcon;
		else if (s.equals("false") || s.equals("no") || s.equals("not working") || s.equals("n"))
			imgN = crossIcon;

		Font dull = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, new BaseColor(120, 120, 120));

		java.util.function.BiFunction<String, Image, PdfPCell> makeCell = (text, icon) -> {
			PdfPCell cell = new PdfPCell();
			cell.setBorder(Rectangle.BOX);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(1);
			cell.setFixedHeight(14f);
			final Image fi = icon;
			final String ft = text;
			cell.setCellEvent((c, pos, canvases) -> {
				try {
					PdfContentByte cb = canvases[PdfPTable.TEXTCANVAS];
					ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Phrase(ft, dull),
							(pos.getLeft() + pos.getRight()) / 2, pos.getBottom() + 2, 0);
					if (fi != null) {
						fi.scaleToFit(11, 11);
						fi.setAbsolutePosition((pos.getLeft() + pos.getRight()) / 2 - fi.getScaledWidth() / 2,
								pos.getBottom() + 2);
						canvases[PdfPTable.BACKGROUNDCANVAS].addImage(fi);
					}
				} catch (Exception ex) {
					logger.error("Status cell render error: {}", ex.getMessage());
				}
			});
			return cell;
		};

		PdfPTable inner = new PdfPTable(3);
		inner.setWidths(new float[] { 45, 10, 45 });

		PdfPCell space = new PdfPCell(new Phrase(""));
		space.setBorder(Rectangle.NO_BORDER);
		space.setFixedHeight(10f);

		inner.addCell(makeCell.apply("Yes", imgY));
		inner.addCell(space);
		inner.addCell(makeCell.apply("No", imgN));

		PdfPCell outer = new PdfPCell(inner);
		outer.setBorder(Rectangle.BOX);
		outer.setPadding(2);
		return outer;
	}

	private PdfPTable buildMaterialTable(String headerText, List<FASMaterialDTO> items, Font hFont, Font vFont,
			Image checkIcon, Image crossIcon) throws Exception {
		PdfPTable t = new PdfPTable(4);
		t.setWidthPercentage(100);
		t.setWidths(new float[] { 8, 48, 24, 20 });

		PdfPCell h = new PdfPCell(new Phrase(headerText, hFont));
		h.setColspan(4);
		h.setBackgroundColor(BaseColor.LIGHT_GRAY);
		h.setHorizontalAlignment(Element.ALIGN_CENTER);
		h.setPadding(3);
		h.setBorder(Rectangle.BOX);
		t.addCell(h);

		for (String ch : new String[] { "S.No", "Material", "Chargeable", "Qty" }) {
			PdfPCell hc = new PdfPCell(new Phrase(ch, hFont));
			hc.setBorder(Rectangle.BOX);
			hc.setPadding(3);
			hc.setHorizontalAlignment(Element.ALIGN_CENTER);
			t.addCell(hc);
		}

		for (int i = 0; i < items.size(); i++) {
			try {
				FASMaterialDTO m = items.get(i);
				PdfPCell sr = new PdfPCell(new Phrase(String.valueOf(i + 1), vFont));
				sr.setBorder(Rectangle.BOX);
				sr.setPadding(3);
				sr.setHorizontalAlignment(Element.ALIGN_CENTER);
				t.addCell(sr);

				PdfPCell desc = new PdfPCell(
						new Phrase(m.getItemDescription() != null ? m.getItemDescription() : "", vFont));
				desc.setBorder(Rectangle.BOX);
				desc.setPadding(3);
				t.addCell(desc);

				t.addCell(createStatusCell(vFont, m.getIsChargeable(), checkIcon, crossIcon));

				PdfPCell qty = new PdfPCell(
						new Phrase(m.getQuantity() != null ? String.valueOf(m.getQuantity()) : "", vFont));
				qty.setBorder(Rectangle.BOX);
				qty.setPadding(3);
				qty.setHorizontalAlignment(Element.ALIGN_CENTER);
				t.addCell(qty);
			} catch (Exception e) {
				logger.error("Error rendering material row at index: {}. Error: {}", i, e.getMessage(), e);
			}
		}
		return t;
	}

	private void addEstimateRow(PdfPTable table, String l1, String v1, String l2, String v2, Font lf, Font vf) {
		PdfPTable inner = new PdfPTable(2);
		inner.setWidthPercentage(100);

		Phrase p1 = new Phrase();
		p1.add(new Chunk(l1 + " ", lf));
		p1.add(new Chunk(v1, vf));
		PdfPCell c1 = new PdfPCell(p1);
		c1.setBorder(Rectangle.BOX);
		c1.setPadding(3);
		inner.addCell(c1);

		Phrase p2 = new Phrase();
		p2.add(new Chunk(l2 + " ", lf));
		p2.add(new Chunk(v2, vf));
		PdfPCell c2 = new PdfPCell(p2);
		c2.setBorder(Rectangle.BOX);
		c2.setPadding(3);
		inner.addCell(c2);

		PdfPCell wrap = new PdfPCell(inner);
		wrap.setBorder(Rectangle.BOX);
		wrap.setPadding(0);
		table.addCell(wrap);
	}

	private void addFormRow(PdfPTable table, String label, String value, Font lf, Font vf) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
		cell.setPadding(4);
		Paragraph p = new Paragraph();
		p.add(new Chunk(label + " ", lf));
		p.add(new Chunk(value, vf));
		cell.addElement(p);
		table.addCell(cell);
	}

	private void addRightRow(PdfPTable table, String label, String value, Font lf, Font vf) {
		PdfPTable inner = new PdfPTable(2);
		inner.setWidthPercentage(100);
		try {
			inner.setWidths(new float[] { 2.5f, 2f });
		} catch (Exception ignored) {
		}

		PdfPCell lc = new PdfPCell(new Phrase(label, lf));
		lc.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
		lc.setPadding(4);
		inner.addCell(lc);

		PdfPCell vc = new PdfPCell(new Phrase(value, vf));
		vc.setBorder(Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
		vc.setPadding(4);
		inner.addCell(vc);

		PdfPCell outer = new PdfPCell(inner);
		outer.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
		outer.setPadding(0);
		table.addCell(outer);
	}

	private Image loadIcon(String baseName) {
		String[] exts = { ".png", ".jpg", ".jpeg" };
		if (servletContext != null) {
			for (String ext : exts) {
				try {
					String rp = servletContext.getRealPath("/icons/" + baseName + ext);
					if (rp != null && Files.exists(Paths.get(rp))) {
						Image img = Image.getInstance(rp);
						img.scaleToFit(12f, 12f);
						return img;
					}
				} catch (Exception ignored) {
				}
			}
		}
		for (String ext : exts) {
			try (InputStream is = getClass().getResourceAsStream("/static/icons/" + baseName + ext)) {
				if (is != null) {
					Image img = Image.getInstance(IOUtils.toByteArray(is));
					img.scaleToFit(12f, 12f);
					return img;
				}
			} catch (Exception ignored) {
			}
		}
		logger.warn("Icon not found for baseName: {}", baseName);
		return null;
	}

	private <T> String safeStr(T obj, java.util.function.Function<T, String> getter) {
		if (obj == null)
			return "";
		String val = getter.apply(obj);
		return val != null ? val : "";
	}

	private FASBranchInfoRequestDTO enrichDto(FASBranchInfoRequestDTO dto, CustomerResponse customerDetailsPayload,
			CustomerResponse customerPayload) {
		try {
			if (customerDetailsPayload != null) {
				if (customerDetailsPayload.getCustomerName() != null
						&& !customerDetailsPayload.getCustomerName().isBlank())
					dto.setBranchName(customerDetailsPayload.getCustomerName());
				if (customerDetailsPayload.getAddress() != null && !customerDetailsPayload.getAddress().isBlank())
					dto.setBranchAddress(customerDetailsPayload.getAddress());
				if (customerDetailsPayload.getLevelName() != null && !customerDetailsPayload.getLevelName().isBlank())
					dto.setRegionalOffice(customerDetailsPayload.getLevelName());
				if (customerDetailsPayload.getEmail() != null && !customerDetailsPayload.getEmail().isBlank())
					dto.setBranchEmail(customerDetailsPayload.getEmail());
				if (customerDetailsPayload.getCustomerCode() != null
						&& !customerDetailsPayload.getCustomerCode().isBlank())
					dto.setCustomerCode(customerDetailsPayload.getCustomerCode());
			}
			if (customerPayload != null) {
				dto.setCustomerName(customerPayload.getCustomerName());
				dto.setDistrict(customerPayload.getDistrict());
				dto.setMinNoVisits(customerPayload.getMinNoVisits());
			}
		} catch (Exception e) {
			logger.warn("Error enriching FAS request DTO with Feign data. Continuing with available data. Error: {}",
					e.getMessage());
		}
		return dto;
	}

	private FASBranchInfo mapDtoToEntity(FASBranchInfoRequestDTO dto) {
		FASBranchInfo e = new FASBranchInfo();
		e.setCustomerId(dto.getCustomerId());
		e.setScheduleVisitId(dto.getScheduleVisitId());
		e.setInspectionDate(dto.getInspectionDate());
		e.setServiceReportNumber(dto.getServiceReportNumber());
		e.setFieldAssociateName(dto.getFieldAssociateName());
		e.setConcernedPersonName(dto.getConcernedPersonName());
		e.setConcernedPersonDesignation(dto.getConcernedPersonDesignation());
		e.setConcernedPersonPhone(dto.getConcernedPersonPhone());
		e.setProductType(dto.getProductType());
		e.setFaModel(dto.getFaModel());
		e.setNoOfZones(dto.getNoOfZones());
		e.setDateOfInstallation(dto.getDateOfInstallation());
		e.setBatteryVoltage(dto.getBatteryVoltage());
		e.setFaWorkingStatus(dto.getFaWorkingStatus());
		e.setSystemConnectedThrough(dto.getSystemConnectedThrough());
		e.setAutoDialerTypePstnGsm(dto.getAutoDialerTypePstnGsm());
		e.setAutoDialerInbuiltExternal(dto.getAutoDialerInbuiltExternal());
		e.setSimNumber(dto.getSimNumber());
		e.setNoUpdatedInAutoDialer(dto.getNoUpdatedInAutoDialer());
		e.setIsPanelCleaned(dto.getIsPanelCleaned());
		e.setAreHootersDifficultToAccess(dto.getAreHootersDifficultToAccess());
		e.setAreSensorsCleaned(dto.getAreSensorsCleaned());
		e.setIsBatteryWorking(dto.getIsBatteryWorking());
		e.setIsOperationExplained(dto.getIsOperationExplained());
		e.setIsSettingDoneAsPerBank(dto.getIsSettingDoneAsPerBank());
		e.setIsPanelAndSensorsCleaned(dto.getIsPanelAndSensorsCleaned());
		e.setIsPasswordChanged(dto.getIsPasswordChanged());
		e.setFaRemarks(dto.getFaRemarks());
		e.setCustomerRemarks(dto.getCustomerRemarks());
		e.setEstimateNo(dto.getEstimateNo());
		e.setEstimateProductDetails(dto.getEstimateProductDetails());
		e.setEstimateAmount(dto.getEstimateAmount());
		e.setEstimateDate(dto.getEstimateDate());
		e.setBillToBeRaised(dto.getBillToBeRaised());
		e.setBillNo(dto.getBillNo());
		e.setBillDate(dto.getBillDate());
		e.setIsInvoicePaymentFollowed(dto.getIsInvoicePaymentFollowed());
		e.setPaymentRemarks(dto.getPaymentRemarks());
		e.setStatus(dto.getStatus());

		if (dto.getHardwareItems() != null) {
			e.setHardwareItems(dto.getHardwareItems().stream().map(h -> {
				FASHardwareItem hw = new FASHardwareItem();
				hw.setFasBranchInfo(e);
				hw.setSerialNumber(h.getSerialNumber());
				hw.setItemName(h.getItemName());
				hw.setZoneLabel(h.getZoneLabel());
				hw.setQuantity(h.getQuantity());
				hw.setWorkingStatus(h.getWorkingStatus());
				hw.setLocation(h.getLocation());
				return hw;
			}).collect(Collectors.toList()));
		}

		if (dto.getDialerContacts() != null) {
			e.setDialerContacts(dto.getDialerContacts().stream().map(d -> {
				FASDialerContact dc = new FASDialerContact();
				dc.setFasBranchInfo(e);
				dc.setContactOrder(d.getContactOrder());
				dc.setStaffName(d.getStaffName());
				dc.setMobileNumber(d.getMobileNumber());
				return dc;
			}).collect(Collectors.toList()));
		}

		if (dto.getMaterials() != null) {
			e.setFasMaterials(dto.getMaterials().stream().map(m -> {
				FASMaterial mat = new FASMaterial();
				mat.setFasBranchInfo(e);
				mat.setItemDescription(m.getItemDescription());
				mat.setQuantity(m.getQuantity());
				mat.setIsChargeable(m.getIsChargeable());
				mat.setRemarks(m.getRemarks());
				if (m.getMaterialType() != null)
					mat.setMaterialType(FASMaterial.MaterialType.valueOf(m.getMaterialType().name()));
				return mat;
			}).collect(Collectors.toList()));
		}

		if (dto.getStaffMembers() != null) {
			e.setStaffMembers(dto.getStaffMembers().stream().map(s -> {
				FASStaffMember sm = new FASStaffMember();
				sm.setFasBranchInfo(e);
				sm.setName(s.getName());
				sm.setDesignation(s.getDesignation());
				return sm;
			}).collect(Collectors.toList()));
		}

		return e;
	}

	private FASBranchInfoResponseDTO mapEntityToResponseDto(FASBranchInfo e, CustomerResponse customerDetailsPayload,
			CustomerResponse customerPayload) {
		FASBranchInfoResponseDTO dto = new FASBranchInfoResponseDTO();
		dto.setId(e.getId());
		dto.setCustomerId(e.getCustomerId());
		dto.setScheduleVisitId(e.getScheduleVisitId());
		dto.setInspectionDate(e.getInspectionDate());
		dto.setServiceReportNumber(e.getServiceReportNumber());
		dto.setFieldAssociateName(e.getFieldAssociateName());
		dto.setConcernedPersonName(e.getConcernedPersonName());
		dto.setConcernedPersonDesignation(e.getConcernedPersonDesignation());
		dto.setConcernedPersonPhone(e.getConcernedPersonPhone());
		dto.setProductType(e.getProductType());
		dto.setFaModel(e.getFaModel());
		dto.setNoOfZones(e.getNoOfZones());
		dto.setDateOfInstallation(e.getDateOfInstallation());
		dto.setBatteryVoltage(e.getBatteryVoltage());
		dto.setFaWorkingStatus(e.getFaWorkingStatus());
		dto.setSystemConnectedThrough(e.getSystemConnectedThrough());
		dto.setAutoDialerTypePstnGsm(e.getAutoDialerTypePstnGsm());
		dto.setAutoDialerInbuiltExternal(e.getAutoDialerInbuiltExternal());
		dto.setSimNumber(e.getSimNumber());
		dto.setNoUpdatedInAutoDialer(e.getNoUpdatedInAutoDialer());
		dto.setIsPanelCleaned(e.getIsPanelCleaned());
		dto.setAreHootersDifficultToAccess(e.getAreHootersDifficultToAccess());
		dto.setAreSensorsCleaned(e.getAreSensorsCleaned());
		dto.setIsBatteryWorking(e.getIsBatteryWorking());
		dto.setIsOperationExplained(e.getIsOperationExplained());
		dto.setIsSettingDoneAsPerBank(e.getIsSettingDoneAsPerBank());
		dto.setIsPanelAndSensorsCleaned(e.getIsPanelAndSensorsCleaned());
		dto.setIsPasswordChanged(e.getIsPasswordChanged());
		dto.setFaRemarks(e.getFaRemarks());
		dto.setCustomerRemarks(e.getCustomerRemarks());
		dto.setEstimateNo(e.getEstimateNo());
		dto.setEstimateProductDetails(e.getEstimateProductDetails());
		dto.setEstimateAmount(e.getEstimateAmount());
		dto.setEstimateDate(e.getEstimateDate());
		dto.setBillToBeRaised(e.getBillToBeRaised());
		dto.setBillNo(e.getBillNo());
		dto.setBillDate(e.getBillDate());
		dto.setIsInvoicePaymentFollowed(e.getIsInvoicePaymentFollowed());
		dto.setPaymentRemarks(e.getPaymentRemarks());
		dto.setStatus(e.getStatus());

		if (customerDetailsPayload != null) {
			dto.setBranchName(customerDetailsPayload.getCustomerName());
			dto.setBranchAddress(customerDetailsPayload.getAddress());
			dto.setRegionalOffice(customerDetailsPayload.getLevelName());
			dto.setBranchEmail(customerDetailsPayload.getEmail());
			dto.setCustomerCode(customerDetailsPayload.getCustomerCode());
		} else {
			logger.warn("customerDetailsPayload is null for FAS entity ID: {}. Branch fields will be empty.",
					e.getId());
		}

		if (customerPayload != null) {
			dto.setCustomerName(customerPayload.getCustomerName());
			dto.setDistrict(customerPayload.getDistrict());
			dto.setMinNoVisits(customerPayload.getMinNoVisits());
		} else {
			logger.warn("customerPayload is null for FAS entity ID: {}. Customer fields will be empty.", e.getId());
		}

		if (e.getHardwareItems() != null) {
			dto.setHardwareItems(e.getHardwareItems().stream().map(h -> {
				FASHardwareItemDTO hd = new FASHardwareItemDTO();
				hd.setSerialNumber(h.getSerialNumber());
				hd.setItemName(h.getItemName());
				hd.setZoneLabel(h.getZoneLabel());
				hd.setQuantity(h.getQuantity());
				hd.setWorkingStatus(h.getWorkingStatus());
				hd.setLocation(h.getLocation());
				return hd;
			}).collect(Collectors.toList()));
		}

		if (e.getDialerContacts() != null) {
			dto.setDialerContacts(e.getDialerContacts().stream().map(d -> {
				FASDialerContactDTO dd = new FASDialerContactDTO();
				dd.setContactOrder(d.getContactOrder());
				dd.setStaffName(d.getStaffName());
				dd.setMobileNumber(d.getMobileNumber());
				return dd;
			}).collect(Collectors.toList()));
		}

		if (e.getFasMaterials() != null) {
			dto.setMaterials(e.getFasMaterials().stream().map(m -> {
				FASMaterialDTO md = new FASMaterialDTO();
				md.setItemDescription(m.getItemDescription());
				md.setQuantity(m.getQuantity());
				md.setIsChargeable(m.getIsChargeable());
				md.setRemarks(m.getRemarks());
				if (m.getMaterialType() != null)
					md.setMaterialType(FASMaterialDTO.MaterialType.valueOf(m.getMaterialType().name()));
				return md;
			}).collect(Collectors.toList()));
		}

		if (e.getStaffMembers() != null) {
			dto.setStaffMembers(e.getStaffMembers().stream().map(s -> {
				FASStaffMemberDTO sd = new FASStaffMemberDTO();
				sd.setName(s.getName());
				sd.setDesignation(s.getDesignation());
				return sd;
			}).collect(Collectors.toList()));
		}

		if (e.getFasBranchDocuments() != null && !e.getFasBranchDocuments().isEmpty()) {
			try {
				List<FASBranchDocument> docs = fasBranchDocumentRepository.findDocumentsByBranchInfoId(e.getId());
				if (docs != null && !docs.isEmpty()) {
					dto.setDocuments(docs.stream().filter(Objects::nonNull).map(doc -> {
						FASBranchDocumentDTO dd = new FASBranchDocumentDTO();
						dd.setDocumentId(doc.getDocumentId());
						dd.setDocumentName(doc.getDocumentName());
						dd.setDocumentPath(doc.getDocumentPath());
						dd.setCreatedOn(doc.getCreatedOn());
						dd.setModifiedOn(doc.getModifiedOn());
						return dd;
					}).filter(Objects::nonNull).collect(Collectors.toList()));
				} else {
					dto.setDocuments(Collections.emptyList());
				}
			} catch (Exception ex) {
				logger.error("Error fetching documents for FAS ID {}: {}", e.getId(), ex.getMessage(), ex);
				dto.setDocuments(Collections.emptyList());
			}
		}

		return dto;
	}

	private FASBranchInfoResponseDTO mapEntityToResponseDtoList(FASBranchInfo e,
			CustomerResponse customerDetailsPayload, CustomerResponse customerPayload) {
		FASBranchInfoResponseDTO dto = new FASBranchInfoResponseDTO();
		dto.setId(e.getId());
		dto.setCustomerId(e.getCustomerId());
		dto.setScheduleVisitId(e.getScheduleVisitId());
		dto.setInspectionDate(e.getInspectionDate());
		dto.setFieldAssociateName(e.getFieldAssociateName());
		dto.setServiceReportNumber(e.getServiceReportNumber());
		dto.setFaWorkingStatus(e.getFaWorkingStatus());
		dto.setProductType(e.getProductType());
		dto.setFaModel(e.getFaModel());
		dto.setNoOfZones(e.getNoOfZones());
		dto.setStatus(e.getStatus());

		if (customerDetailsPayload != null) {
			dto.setBranchName(customerDetailsPayload.getCustomerName());
			dto.setBranchAddress(customerDetailsPayload.getAddress());
			dto.setRegionalOffice(customerDetailsPayload.getLevelName());
			dto.setBranchEmail(customerDetailsPayload.getEmail());
			dto.setCustomerCode(customerDetailsPayload.getCustomerCode());
		} else {
			logger.warn("customerDetailsPayload is null for FAS entity ID: {} in getAll mapping.", e.getId());
		}

		if (customerPayload != null) {
			dto.setCustomerName(customerPayload.getCustomerName());
			dto.setDistrict(customerPayload.getDistrict());
			dto.setMinNoVisits(customerPayload.getMinNoVisits());
		} else {
			logger.warn("customerPayload is null for FAS entity ID: {} in getAll mapping.", e.getId());
		}
		return dto;
	}

	private FASBranchInfoRequestDTO mapEntityToRequestDto(FASBranchInfo e) {
		FASBranchInfoRequestDTO dto = new FASBranchInfoRequestDTO();
		dto.setId(e.getId());
		dto.setCustomerId(e.getCustomerId());
		dto.setScheduleVisitId(e.getScheduleVisitId());
		dto.setInspectionDate(e.getInspectionDate());
		dto.setServiceReportNumber(e.getServiceReportNumber());
		dto.setFieldAssociateName(e.getFieldAssociateName());
		dto.setConcernedPersonName(e.getConcernedPersonName());
		dto.setConcernedPersonDesignation(e.getConcernedPersonDesignation());
		dto.setConcernedPersonPhone(e.getConcernedPersonPhone());
		dto.setProductType(e.getProductType());
		dto.setFaModel(e.getFaModel());
		dto.setNoOfZones(e.getNoOfZones());
		dto.setDateOfInstallation(e.getDateOfInstallation());
		dto.setBatteryVoltage(e.getBatteryVoltage());
		dto.setFaWorkingStatus(e.getFaWorkingStatus());
		dto.setSystemConnectedThrough(e.getSystemConnectedThrough());
		dto.setAutoDialerTypePstnGsm(e.getAutoDialerTypePstnGsm());
		dto.setAutoDialerInbuiltExternal(e.getAutoDialerInbuiltExternal());
		dto.setSimNumber(e.getSimNumber());
		dto.setNoUpdatedInAutoDialer(e.getNoUpdatedInAutoDialer());
		dto.setIsPanelCleaned(e.getIsPanelCleaned());
		dto.setAreHootersDifficultToAccess(e.getAreHootersDifficultToAccess());
		dto.setAreSensorsCleaned(e.getAreSensorsCleaned());
		dto.setIsBatteryWorking(e.getIsBatteryWorking());
		dto.setIsOperationExplained(e.getIsOperationExplained());
		dto.setIsSettingDoneAsPerBank(e.getIsSettingDoneAsPerBank());
		dto.setIsPanelAndSensorsCleaned(e.getIsPanelAndSensorsCleaned());
		dto.setIsPasswordChanged(e.getIsPasswordChanged());
		dto.setFaRemarks(e.getFaRemarks());
		dto.setCustomerRemarks(e.getCustomerRemarks());
		dto.setEstimateNo(e.getEstimateNo());
		dto.setEstimateProductDetails(e.getEstimateProductDetails());
		dto.setEstimateAmount(e.getEstimateAmount());
		dto.setEstimateDate(e.getEstimateDate());
		dto.setBillToBeRaised(e.getBillToBeRaised());
		dto.setBillNo(e.getBillNo());
		dto.setBillDate(e.getBillDate());
		dto.setIsInvoicePaymentFollowed(e.getIsInvoicePaymentFollowed());
		dto.setPaymentRemarks(e.getPaymentRemarks());
		dto.setStatus(e.getStatus());

		if (e.getHardwareItems() != null)
			dto.setHardwareItems(e.getHardwareItems().stream().map(h -> {
				FASHardwareItemDTO hd = new FASHardwareItemDTO();
				hd.setSerialNumber(h.getSerialNumber());
				hd.setItemName(h.getItemName());
				hd.setZoneLabel(h.getZoneLabel());
				hd.setQuantity(h.getQuantity());
				hd.setWorkingStatus(h.getWorkingStatus());
				hd.setLocation(h.getLocation());
				return hd;
			}).collect(Collectors.toList()));

		if (e.getDialerContacts() != null)
			dto.setDialerContacts(e.getDialerContacts().stream().map(d -> {
				FASDialerContactDTO dd = new FASDialerContactDTO();
				dd.setContactOrder(d.getContactOrder());
				dd.setStaffName(d.getStaffName());
				dd.setMobileNumber(d.getMobileNumber());
				return dd;
			}).collect(Collectors.toList()));

		if (e.getFasMaterials() != null)
			dto.setMaterials(e.getFasMaterials().stream().map(m -> {
				FASMaterialDTO md = new FASMaterialDTO();
				md.setItemDescription(m.getItemDescription());
				md.setQuantity(m.getQuantity());
				md.setIsChargeable(m.getIsChargeable());
				md.setRemarks(m.getRemarks());
				if (m.getMaterialType() != null)
					md.setMaterialType(FASMaterialDTO.MaterialType.valueOf(m.getMaterialType().name()));
				return md;
			}).collect(Collectors.toList()));

		if (e.getStaffMembers() != null)
			dto.setStaffMembers(e.getStaffMembers().stream().map(s -> {
				FASStaffMemberDTO sd = new FASStaffMemberDTO();
				sd.setName(s.getName());
				sd.setDesignation(s.getDesignation());
				return sd;
			}).collect(Collectors.toList()));

		return dto;
	}
}