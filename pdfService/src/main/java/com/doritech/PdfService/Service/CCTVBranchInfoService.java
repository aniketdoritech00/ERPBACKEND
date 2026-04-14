package com.doritech.PdfService.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.PdfService.Dto.CCTVBranchDocumentDTO;
import com.doritech.PdfService.Dto.CCTVCameraStatusDTO;
import com.doritech.PdfService.Dto.CCTVHddStatusDTO;
import com.doritech.PdfService.Dto.CCTVMaterialDTO;
import com.doritech.PdfService.Dto.CCTVProductDetailDTO;
import com.doritech.PdfService.Dto.CCTVProductInstalledStatusDTO;
import com.doritech.PdfService.Dto.CCTVProductInstalledSubStatusDTO;
import com.doritech.PdfService.Dto.CCTVStaffMemberDTO;
import com.doritech.PdfService.Entity.CCTVBranchDocument;
import com.doritech.PdfService.Entity.CCTVBranchInfo;
import com.doritech.PdfService.Entity.CCTVCameraStatus;
import com.doritech.PdfService.Entity.CCTVHddStatus;
import com.doritech.PdfService.Entity.CCTVMaterial;
import com.doritech.PdfService.Entity.CCTVProductDetail;
import com.doritech.PdfService.Entity.CCTVProductInstalledStatus;
import com.doritech.PdfService.Entity.CCTVProductInstalledSubStatus;
import com.doritech.PdfService.Entity.CCTVStaffMember;
import com.doritech.PdfService.Exception.DuplicateResourceException;
import com.doritech.PdfService.Repository.CCTVBranchDocumentRepository;
import com.doritech.PdfService.Repository.CCTVBranchInfoRepository;
import com.doritech.PdfService.Request.CCTVBranchInfoRequestDTO;
import com.doritech.PdfService.Response.CCTVBranchInfoResponseDTO;
import com.doritech.PdfService.Response.CustomerResponse;
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
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.ServletContext;

@Service
public class CCTVBranchInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CCTVBranchInfoService.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private CCTVBranchInfoRepository cctvBranchInfoRepository;

    @Autowired
    private ImageStorageService imageStorageService;

    @Autowired
    private CCTVBranchDocumentRepository cctvBranchDocumentRepository;

    @Autowired
    private CompanySiteService companySiteService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Image checkIcon = null;
    private Image crossIcon = null;
    private Image naIcon = null;

    private void updateAssignmentStatusToCompleted(Integer scheduleVisitId) {
        if (scheduleVisitId == null) {
            logger.warn("scheduleVisitId is null, skipping assignment status update.");
            return;
        }
        try {
            String sql = "UPDATE erp_db.employee_assignment SET status = 'Completed' WHERE assignment_id = ?";
            int rowsUpdated = jdbcTemplate.update(sql, scheduleVisitId);
            if (rowsUpdated > 0) {
                logger.info("Assignment status updated to COMPLETED for assignment_id: {}", scheduleVisitId);
            } else {
                logger.warn("No assignment record found for assignment_id: {}. Status not updated.", scheduleVisitId);
            }
        } catch (Exception e) {
            logger.error("Failed to update assignment status for assignment_id: {}. Error: {}", scheduleVisitId, e.getMessage(), e);
        }
    }

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
                logger.warn("Customer details API returned non-200 status for customerId: {}. Status: {}", customerId, apiResponse.getStatusCode());
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            CustomerResponse payload = objectMapper.convertValue(apiResponse.getPayload(), CustomerResponse.class);
            logger.info("Customer details fetched successfully for customerId: {}", customerId);
            return payload;
        } catch (feign.FeignException fe) {
            logger.error("Feign client error for customerId: {}. Error: {}", customerId, fe.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error fetching customer details for customerId: {}. Error: {}", customerId, e.getMessage(), e);
            return null;
        }
    }

    private CustomerResponse fetchCustomerPayload(Integer scheduleVisitId) {
        if (scheduleVisitId == null) {
            logger.warn("fetchCustomerPayload called with null scheduleVisitId");
            return null;
        }
        try {
            ResponseEntity customerResponse = companySiteService.fetchCustomer(scheduleVisitId);
            if (customerResponse == null || customerResponse.getPayload() == null) {
                logger.warn("Feign client returned null customer response for scheduleVisitId: {}", scheduleVisitId);
                return null;
            }
            if (customerResponse.getStatusCode() == null || !customerResponse.getStatusCode().toString().equals("200")) {
                logger.warn("Customer API returned non-200 status for scheduleVisitId: {}. Status: {}", scheduleVisitId, customerResponse.getStatusCode());
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            CustomerResponse payload = objectMapper.convertValue(customerResponse.getPayload(), CustomerResponse.class);
            logger.info("Customer payload fetched successfully for scheduleVisitId: {}", scheduleVisitId);
            return payload;
        } catch (feign.FeignException fe) {
            logger.error("Feign client error for scheduleVisitId: {}. Error: {}", scheduleVisitId, fe.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error fetching customer for scheduleVisitId: {}. Error: {}", scheduleVisitId, e.getMessage(), e);
            return null;
        }
    }

    @Transactional
    public CCTVBranchInfoResponseDTO createCCTVInfoAndGeneratePdf(CCTVBranchInfoRequestDTO requestDTO,
            Map<String, MultipartFile> allFiles) {

        logger.info("Starting CCTV branch info creation for branch: {}", requestDTO != null ? requestDTO.getBranchName() : "null");

        if (requestDTO == null) {
            throw new IllegalArgumentException("Request DTO must not be null.");
        }
        if (requestDTO.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID must not be null.");
        }
        if (requestDTO.getScheduleVisitId() == null) {
            throw new IllegalArgumentException("Schedule Visit ID must not be null.");
        }

        Optional<CCTVBranchInfo> optional = cctvBranchInfoRepository
                .findByScheduleVisitIdAndProductType(requestDTO.getScheduleVisitId(), requestDTO.getProductType());

        if (optional.isPresent()) {
            logger.warn("CCTV data already exists for scheduleVisitId: {} and productType: {}", requestDTO.getScheduleVisitId(), requestDTO.getProductType());
            throw new DuplicateResourceException("CCTV data already exists for scheduleVisitId: "
                    + requestDTO.getScheduleVisitId() + " and productType: " + requestDTO.getProductType());
        }

        CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(requestDTO.getCustomerId());
        if (customerDetailsPayload == null) {
            throw new IllegalArgumentException("Invalid or unreachable customer ID: '" + requestDTO.getCustomerId()
                    + "'. Please verify the customer ID and ensure the customer service is running.");
        }

        CustomerResponse customerPayload = fetchCustomerPayload(requestDTO.getScheduleVisitId());
        if (customerPayload == null) {
            throw new IllegalArgumentException("No customer found for scheduleVisitId: " + requestDTO.getScheduleVisitId()
                    + ". Please verify the visit ID and ensure the customer service is running.");
        }

        CCTVBranchInfo cctvInfoEntity = mapDtoToEntity(requestDTO);
        cctvInfoEntity.setCreatedAt(LocalDateTime.now());
        cctvInfoEntity.setCreatedBy(requestDTO.getCreatedBy());
        cctvInfoEntity.setUpdatedAt(LocalDateTime.now());

        CCTVBranchInfo savedCCTVInfo;
        try {
            savedCCTVInfo = cctvBranchInfoRepository.save(cctvInfoEntity);
            logger.info("CCTV branch info saved with ID: {}", savedCCTVInfo.getId());
        } catch (Exception e) {
            logger.error("Failed to save CCTV branch info. Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save CCTV branch info: " + e.getMessage(), e);
        }

        saveDocuments(savedCCTVInfo, allFiles);

        CCTVBranchInfoRequestDTO enrichedDto = enrichRequestDtoWithFeignData(requestDTO, customerDetailsPayload, customerPayload);

        try {
            ByteArrayOutputStream baos = generateCCTVServiceReport(enrichedDto);
            if (baos != null) {
                String realPath = servletContext.getRealPath("/cctvPdf/");
                Path dirPath = Paths.get(realPath);
                Files.createDirectories(dirPath);

                String filename = "cctv_report_" + savedCCTVInfo.getId() + ".pdf";
                Path filePath = dirPath.resolve(filename);
                Files.write(filePath, baos.toByteArray());

                savedCCTVInfo.setPdfFilePath("cctvPdf/" + filename);
                savedCCTVInfo = cctvBranchInfoRepository.save(savedCCTVInfo);
                logger.info("PDF saved at: cctvPdf/{}", filename);

                updateAssignmentStatusToCompleted(savedCCTVInfo.getScheduleVisitId());
            }
        } catch (Exception e) {
            logger.error("PDF generation failed for ID: {}. Error: {}", savedCCTVInfo.getId(), e.getMessage(), e);
        }

        CCTVBranchInfoResponseDTO responseDTO = mapEntityToResponseDto(savedCCTVInfo, customerDetailsPayload, customerPayload);
        responseDTO.setMessage("CCTV inspection information saved successfully and PDF URL generated");
        logger.info("CCTV branch info creation completed for ID: {}", savedCCTVInfo.getId());
        return responseDTO;
    }

    private CCTVBranchInfoRequestDTO enrichRequestDtoWithFeignData(CCTVBranchInfoRequestDTO requestDTO,
            CustomerResponse customerDetailsPayload, CustomerResponse customerPayload) {
        try {
            if (customerDetailsPayload != null) {
                if (customerDetailsPayload.getCustomerName() != null && !customerDetailsPayload.getCustomerName().isBlank()) {
                    requestDTO.setBranchName(customerDetailsPayload.getCustomerName());
                }
                if (customerDetailsPayload.getAddress() != null && !customerDetailsPayload.getAddress().isBlank()) {
                    requestDTO.setBranchAddress(customerDetailsPayload.getAddress());
                }
                if (customerDetailsPayload.getLevelName() != null && !customerDetailsPayload.getLevelName().isBlank()) {
                    requestDTO.setRegionalOffice(customerDetailsPayload.getLevelName());
                }
                if (customerDetailsPayload.getEmail() != null && !customerDetailsPayload.getEmail().isBlank()) {
                    requestDTO.setBranchEmail(customerDetailsPayload.getEmail());
                }
                if (customerDetailsPayload.getCustomerCode() != null && !customerDetailsPayload.getCustomerCode().isBlank()) {
                    requestDTO.setCustomerCode(customerDetailsPayload.getCustomerCode());
                }
            }
            if (customerPayload != null) {
                requestDTO.setCustomerName(customerPayload.getCustomerName());
                requestDTO.setDistrict(customerPayload.getDistrict());
                requestDTO.setMinNoVisits(customerPayload.getMinNoVisits());
            }
        } catch (Exception e) {
            logger.warn("Error enriching request DTO with Feign data. Continuing with available data. Error: {}", e.getMessage());
        }
        return requestDTO;
    }

    private void saveDocuments(CCTVBranchInfo savedCCTVInfo, Map<String, MultipartFile> allFiles) {
        if (allFiles == null || allFiles.isEmpty()) return;

        allFiles.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("data"))
                .forEach(entry -> {
                    String docType = entry.getKey();
                    MultipartFile file = entry.getValue();
                    if (file == null || file.isEmpty()) {
                        logger.warn("Skipping empty file for docType: {}", docType);
                        return;
                    }
                    try {
                        String savedPath = imageStorageService.saveImage(file, docType, savedCCTVInfo.getId());
                        CCTVBranchDocument document = new CCTVBranchDocument();
                        document.setDocumentName(docType);
                        document.setDocumentPath(savedPath);
                        document.setCctvBranchInfo(savedCCTVInfo);
                        document.setCreatedOn(LocalDateTime.now());
                        document.setModifiedOn(LocalDateTime.now());
                        cctvBranchDocumentRepository.save(document);
                        logger.info("Document saved - Type: {}, Path: {}", docType, savedPath);
                    } catch (Exception e) {
                        logger.error("Failed to process - DocType: {}, Error: {}", docType, e.getMessage());
                    }
                });
    }

    public CCTVBranchInfoResponseDTO getCCTVInfoById(Long id) {
        logger.info("Fetching CCTV branch info for ID: {}", id);
        try {
            CCTVBranchInfo entity = cctvBranchInfoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CCTV branch info not found for ID: " + id));

            CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(entity.getCustomerId());
            CustomerResponse customerPayload = fetchCustomerPayload(entity.getScheduleVisitId());

            if (customerDetailsPayload == null) {
                logger.warn("Could not fetch customer details for customerId: {} (ID: {}). Proceeding with stored data.", entity.getCustomerId(), id);
            }
            if (customerPayload == null) {
                logger.warn("Could not fetch customer details for scheduleVisitId: {} (ID: {}). Proceeding with stored data.", entity.getScheduleVisitId(), id);
            }

            logger.info("Successfully fetched CCTV branch info for ID: {}", id);
            return mapEntityToResponseDto(entity, customerDetailsPayload, customerPayload);

        } catch (RuntimeException e) {
            logger.error("RuntimeException in getCCTVInfoById for ID: {}. Error: {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error in getCCTVInfoById for ID: {}. Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch CCTV branch info for ID: " + id + ". " + e.getMessage(), e);
        }
    }

    public CCTVBranchInfoResponseDTO getCCTVInfoByScheduleVisitAndProductType(Integer scheduleVisitId, String productType) {
        logger.info("Fetching CCTV info for scheduleVisitId: {} and productType: {}", scheduleVisitId, productType);
        try {
            CCTVBranchInfo entity = cctvBranchInfoRepository
                    .findByScheduleVisitIdAndProductType(scheduleVisitId, productType)
                    .orElseThrow(() -> new RuntimeException("CCTV info not found for scheduleVisitId: "
                            + scheduleVisitId + " and productType: " + productType));

            CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(entity.getCustomerId());
            CustomerResponse customerPayload = fetchCustomerPayload(entity.getScheduleVisitId());

            if (customerDetailsPayload == null) {
                logger.warn("Could not fetch customer details for customerId: {} during getByScheduleVisit.", entity.getCustomerId());
            }
            if (customerPayload == null) {
                logger.warn("Could not fetch customer details for scheduleVisitId: {} during getByScheduleVisit.", scheduleVisitId);
            }

            logger.info("Successfully fetched CCTV info for scheduleVisitId: {} and productType: {}", scheduleVisitId, productType);
            return mapEntityToResponseDto(entity, customerDetailsPayload, customerPayload);

        } catch (RuntimeException e) {
            logger.error("RuntimeException fetching CCTV info. scheduleVisitId: {}, productType: {}, error: {}", scheduleVisitId, productType, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching CCTV info. scheduleVisitId: {}, productType: {}, error: {}", scheduleVisitId, productType, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch CCTV info: " + e.getMessage(), e);
        }
    }

    public Page<CCTVBranchInfoResponseDTO> getAllCCTVInfo(int page, int size, String sortBy, String sortDir) {
        logger.info("Fetching all CCTV branch info - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        try {
            Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<CCTVBranchInfo> cctvPage = cctvBranchInfoRepository.findAll(pageable);

            Page<CCTVBranchInfoResponseDTO> responsePage = cctvPage.map(entity -> {
                try {
                    CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(entity.getCustomerId());
                    CustomerResponse customerPayload = fetchCustomerPayload(entity.getScheduleVisitId());
                    if (customerDetailsPayload == null) {
                        logger.warn("Could not fetch customer details for customerId: {} (ID: {}) during getAll.", entity.getCustomerId(), entity.getId());
                    }
                    if (customerPayload == null) {
                        logger.warn("Could not fetch customer details for scheduleVisitId: {} (ID: {}) during getAll.", entity.getScheduleVisitId(), entity.getId());
                    }
                    return mapEntityToListResponseDto(entity, customerDetailsPayload, customerPayload);
                } catch (Exception e) {
                    logger.error("Error mapping entity ID: {} during getAll. Error: {}", entity.getId(), e.getMessage(), e);
                    return mapEntityToListResponseDto(entity, null, null);
                }
            });

            logger.info("Successfully fetched {} CCTV records (page {}/{})", responsePage.getNumberOfElements(), page + 1, cctvPage.getTotalPages());
            return responsePage;

        } catch (Exception e) {
            logger.error("Unexpected error in getAllCCTVInfo - page: {}, size: {}. Error: {}", page, size, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch CCTV branch info list: " + e.getMessage(), e);
        }
    }

    public ByteArrayOutputStream generateCCTVServiceReport(CCTVBranchInfoRequestDTO dto) throws Exception {
        logger.info("Starting PDF generation for branch: {}", dto != null ? dto.getBranchName() : "null");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font companyNameFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 11, Font.BOLD, BaseColor.BLACK);
            Font companyNameFont2 = FontFactory.getFont(FontFactory.TIMES_BOLD, 9, Font.BOLD, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 11, Font.BOLD, BaseColor.BLACK);
            Font smallBoldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
            Font valueBlueFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLUE);
            Font valueBlueFontTiny = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLUE);

            Paragraph title = new Paragraph("SERVICE REPORT OF CCTV", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15f);
            document.add(title);

            PdfPTable mainTable = new PdfPTable(2);
            mainTable.setWidthPercentage(100);
            mainTable.setWidths(new float[]{65, 35});

            PdfPTable leftTable = new PdfPTable(1);
            leftTable.setWidthPercentage(100);

            PdfPCell companyCell = new PdfPCell();
            companyCell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            companyCell.setPadding(5);
            companyCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            Paragraph companyPara = new Paragraph();
            companyPara.setAlignment(Element.ALIGN_LEFT);
            companyPara.setLeading(12f);
            companyPara.add(new Chunk("DIGITALS INDIA SECURITY PRODUCTS PVT. LTD.\n", companyNameFont));
            companyPara.add(new Chunk("[An ISO 9001:2015 Company]\n", companyNameFont2));
            companyPara.add(new Chunk("SCO 6-7, Saundh Complex, Near Atwal House, Cantt Road, Jalandhar-144005 (Punjab), Ph: 0181-4634072\n", smallBoldFont));
            companyPara.add(new Chunk("di.jalandhar@digitalsindia.com", smallBoldFont));
            companyCell.addElement(companyPara);
            leftTable.addCell(companyCell);

            addFormRow(leftTable, "GSTIN/UIN:", "03AAACCD4430E1ZD", normalFont, headerFont);
            addFormRow(leftTable, "Name of Field Associate :", dto != null && dto.getFieldAssociateName() != null ? dto.getFieldAssociateName() : "", normalFont, valueBlueFont);
            addFormRow(leftTable, "Branch/Site Name :", dto != null && dto.getBranchName() != null ? dto.getBranchName() : "", normalFont, valueBlueFont);
            addFormRow(leftTable, "Under Regional Office/Zonal Office :", dto != null && dto.getRegionalOffice() != null ? dto.getRegionalOffice() : "", normalFont, valueBlueFont);
            addFormRow(leftTable, "Address:-", dto != null && dto.getBranchAddress() != null ? dto.getBranchAddress() : "", normalFont, valueBlueFont);

            PdfPCell twoColCell = new PdfPCell();
            twoColCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            twoColCell.setPadding(0);

            PdfPTable twoColTable = new PdfPTable(2);
            twoColTable.setWidthPercentage(100);
            twoColTable.setWidths(new float[]{65, 35});

            String branchEmail = dto != null && dto.getBranchEmail() != null ? dto.getBranchEmail() : "";
            Phrase emailPhrase = new Phrase();
            emailPhrase.add(new Chunk("Br. E-mail ID:- ", normalFont));
            emailPhrase.add(new Chunk(branchEmail, valueBlueFont));
            PdfPCell col1 = new PdfPCell(emailPhrase);
            col1.setBorder(Rectangle.BOX);
            col1.setPadding(5);
            twoColTable.addCell(col1);

            String branchCode = dto != null && dto.getCustomerCode() != null ? dto.getCustomerCode() : "";
            Phrase codePhrase = new Phrase();
            codePhrase.add(new Chunk("Branch Code:- ", normalFont));
            codePhrase.add(new Chunk(branchCode, valueBlueFont));
            PdfPCell col2 = new PdfPCell(codePhrase);
            col2.setBorder(Rectangle.BOX);
            col2.setPadding(5);
            twoColTable.addCell(col2);

            String concernedName = dto != null && dto.getConcernedPersonName() != null ? dto.getConcernedPersonName() : "";
            Phrase namePhrase = new Phrase();
            namePhrase.add(new Chunk("Concerned Person:- ", normalFont));
            namePhrase.add(new Chunk(concernedName, valueBlueFont));
            PdfPCell col3 = new PdfPCell(namePhrase);
            col3.setBorder(Rectangle.BOX);
            col3.setPadding(5);
            twoColTable.addCell(col3);

            String concernedDesignation = dto != null && dto.getConcernedPersonDesignation() != null ? dto.getConcernedPersonDesignation() : "";
            Phrase designationPhrase = new Phrase();
            designationPhrase.add(new Chunk("Designation:- ", normalFont));
            designationPhrase.add(new Chunk(concernedDesignation, valueBlueFont));
            PdfPCell col4 = new PdfPCell(designationPhrase);
            col4.setBorder(Rectangle.BOX);
            col4.setPadding(5);
            twoColTable.addCell(col4);

            twoColCell.addElement(twoColTable);
            leftTable.addCell(twoColCell);

            addFormRow(leftTable, "Phone / Mobile No:-", dto != null && dto.getConcernedPersonPhone() != null ? dto.getConcernedPersonPhone() : "", normalFont, valueBlueFont);
            addFormRow(leftTable, "Customer Name:-", dto != null && dto.getCustomerName() != null ? dto.getCustomerName() : "", normalFont, valueBlueFont);
            addFormRow(leftTable, "District:-", dto != null && dto.getDistrict() != null ? dto.getDistrict() : "", normalFont, valueBlueFont);

            PdfPCell leftCell = new PdfPCell(leftTable);
            leftCell.setBorder(Rectangle.BOX);
            leftCell.setPadding(0);
            mainTable.addCell(leftCell);

            PdfPTable rightTable = new PdfPTable(1);
            rightTable.setWidthPercentage(100);

            PdfPCell contactCell = new PdfPCell();
            contactCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            contactCell.setPadding(5);
            contactCell.setPaddingTop(2);
            contactCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            Paragraph contactInfo = new Paragraph();
            contactInfo.setLeading(12f);
            contactInfo.add(new Chunk("For Complaints & Technical Support\n", boldFont));
            contactInfo.add(new Chunk("Mobs: ", boldFont));
            contactInfo.add(new Chunk("8448990211, 8448990201", normalFont));
            contactCell.addElement(contactInfo);
            rightTable.addCell(contactCell);

            PdfPTable innerTable1 = new PdfPTable(2);
            innerTable1.setWidthPercentage(100);
            innerTable1.setWidths(new float[]{2.5f, 2f});

            PdfPCell labelCell1 = new PdfPCell(new Phrase("Service Report No.", boldFont));
            labelCell1.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            labelCell1.setPadding(5);
            innerTable1.addCell(labelCell1);

            PdfPCell valueCell1 = new PdfPCell(new Phrase("PUBCCCTV", headerFont));
            valueCell1.setBorder(Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
            valueCell1.setPadding(5);
            innerTable1.addCell(valueCell1);

            PdfPCell serviceNoCell = new PdfPCell(innerTable1);
            serviceNoCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            rightTable.addCell(serviceNoCell);

            PdfPTable innerTable2 = new PdfPTable(2);
            innerTable2.setWidthPercentage(100);
            innerTable2.setWidths(new float[]{2.5f, 2f});

            PdfPCell labelCell2 = new PdfPCell(new Phrase("Date", boldFont));
            labelCell2.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            labelCell2.setPadding(5);
            innerTable2.addCell(labelCell2);

            String inspectionDateStr = "";
            if (dto != null && dto.getInspectionDate() != null) {
                try {
                    inspectionDateStr = DATE_FORMATTER.format(dto.getInspectionDate());
                } catch (Exception ex) {
                    logger.warn("Failed to format inspection date: {}. Error: {}", dto.getInspectionDate(), ex.getMessage());
                    inspectionDateStr = dto.getInspectionDate().toString();
                }
            }

            PdfPCell valueCell2 = new PdfPCell(new Phrase(inspectionDateStr, valueBlueFont));
            valueCell2.setBorder(Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
            valueCell2.setPadding(5);
            innerTable2.addCell(valueCell2);

            PdfPCell dateCell = new PdfPCell(innerTable2);
            dateCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            rightTable.addCell(dateCell);

            PdfPCell productsHeader = new PdfPCell(new Phrase("Products Details", headerFont));
            productsHeader.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            productsHeader.setPadding(5);
            productsHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            productsHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            rightTable.addCell(productsHeader);

            PdfPCell cctvTypeCell = new PdfPCell();
            cctvTypeCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cctvTypeCell.setPadding(5);

            PdfPTable cctvTypeTable = new PdfPTable(2);
            cctvTypeTable.setWidthPercentage(100);
            cctvTypeTable.setWidths(new float[]{40, 60});

            PdfPCell label = new PdfPCell(new Phrase("CCTV Type -", normalFont));
            label.setBorder(Rectangle.NO_BORDER);
            label.setHorizontalAlignment(Element.ALIGN_LEFT);
            label.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cctvTypeTable.addCell(label);

            String cctvTypeValue = dto != null && dto.getCctvProductDetail() != null
                    && dto.getCctvProductDetail().getCctvSystemType() != null
                    ? dto.getCctvProductDetail().getCctvSystemType() : "";

            PdfPCell typeValueCell = new PdfPCell(new Phrase(cctvTypeValue, valueBlueFont));
            typeValueCell.setBorder(Rectangle.NO_BORDER);
            typeValueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            typeValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            typeValueCell.setPadding(3);
            cctvTypeTable.addCell(typeValueCell);

            cctvTypeCell.addElement(cctvTypeTable);
            rightTable.addCell(cctvTypeCell);

            if (dto != null && dto.getCctvProductDetail() != null) {
                addRightFormRow(rightTable, "DVR/NVR Make", dto.getCctvProductDetail().getDvrNvrMake(), normalFont, valueBlueFont);
                addRightFormRow(rightTable, "DVR/NVR Model", dto.getCctvProductDetail().getDvrNvrModel(), normalFont, valueBlueFont);

                String builtDateStr = "";
                try {
                    if (dto.getCctvProductDetail().getBuiltDate() != null) {
                        builtDateStr = DATE_FORMATTER.format(dto.getCctvProductDetail().getBuiltDate());
                    }
                } catch (Exception ex) {
                    logger.warn("Failed to format built date. Error: {}", ex.getMessage());
                    builtDateStr = dto.getCctvProductDetail().getBuiltDate() != null ? dto.getCctvProductDetail().getBuiltDate().toString() : "";
                }
                addRightFormRow(rightTable, "DVR/NVR Built Date", builtDateStr, normalFont, valueBlueFont);
                addRightFormRow(rightTable, "Total Camera Port",
                        dto.getCctvProductDetail().getTotalCameraPort() != null ? String.valueOf(dto.getCctvProductDetail().getTotalCameraPort()) : "",
                        normalFont, valueBlueFont);

                Boolean isRackInstalled = dto.getCctvProductDetail().getIsRackInstalled();
                String rackValue = isRackInstalled != null ? (isRackInstalled ? "Yes" : "No") : "";
                addRightFormRow(rightTable, "DVR/NVR Rack Installed", rackValue, normalFont, valueBlueFont);

                String installationDateStr = "";
                try {
                    if (dto.getCctvProductDetail().getDateOfInstallation() != null) {
                        installationDateStr = DATE_FORMATTER.format(dto.getCctvProductDetail().getDateOfInstallation());
                    }
                } catch (Exception ex) {
                    logger.warn("Failed to format installation date. Error: {}", ex.getMessage());
                    installationDateStr = dto.getCctvProductDetail().getDateOfInstallation() != null ? dto.getCctvProductDetail().getDateOfInstallation().toString() : "";
                }
                addRightFormRow(rightTable, "Date of Installation", installationDateStr, normalFont, valueBlueFont);
            } else {
                logger.warn("CCTV product detail is null for branch: {}", dto != null ? dto.getBranchName() : "null");
                addRightFormRow(rightTable, "DVR/NVR Make", "", normalFont, valueBlueFont);
                addRightFormRow(rightTable, "DVR/NVR Model", "", normalFont, valueBlueFont);
                addRightFormRow(rightTable, "DVR/NVR Built Date", "", normalFont, valueBlueFont);
                addRightFormRow(rightTable, "Total Camera Port", "", normalFont, valueBlueFont);
                addRightFormRow(rightTable, "DVR/NVR Rack Installed", "", normalFont, valueBlueFont);
                addRightFormRow(rightTable, "Date of Installation", "", normalFont, valueBlueFont);
            }

            PdfPCell rightCell = new PdfPCell(rightTable);
            rightCell.setBorder(Rectangle.BOX);
            rightCell.setPadding(0);
            mainTable.addCell(rightCell);

            document.add(mainTable);

            Image localCheckIcon = loadIconFromIconsDir("check");
            Image localCrossIcon = loadIconFromIconsDir("cross");

            PdfPTable cctvStatusTable = new PdfPTable(6);
            cctvStatusTable.setWidthPercentage(100);
            cctvStatusTable.setWidths(new float[]{50, 5, 15, 5, 20, 25});

            PdfPCell statusLabel = new PdfPCell(new Phrase("CCTV System Working Status", boldFont));
            statusLabel.setBorder(Rectangle.BOX);
            statusLabel.setPadding(5);
            statusLabel.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cctvStatusTable.addCell(statusLabel);

            boolean isWorking = dto != null && dto.getCctvWorkingStatus() != null && dto.getCctvWorkingStatus().equalsIgnoreCase("true");

            PdfPCell yBox = new PdfPCell(new Phrase("", boldFont));
            yBox.setBorder(Rectangle.BOX);
            yBox.setFixedHeight(16f);
            yBox.setHorizontalAlignment(Element.ALIGN_CENTER);
            yBox.setVerticalAlignment(Element.ALIGN_MIDDLE);
            yBox.setPaddingTop(2f);
            yBox.setPaddingBottom(2f);
            yBox.setCellEvent(new PdfPCellEvent() {
                @Override
                public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
                    PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
                    float offset = 2f;
                    cb.rectangle(position.getLeft() + offset, position.getBottom() + offset, position.getWidth() - 2 * offset, position.getHeight() - 2 * offset);
                    cb.stroke();
                    if (isWorking && localCheckIcon != null) {
                        try {
                            Image img = Image.getInstance(localCheckIcon);
                            img.scaleToFit(position.getWidth() - 4, position.getHeight() - 4);
                            img.setAbsolutePosition(position.getLeft() + 2, position.getBottom() + 2);
                            cb.addImage(img);
                        } catch (Exception e) {
                            logger.error("Error rendering check icon in yBox: {}", e.getMessage(), e);
                        }
                    }
                }
            });

            PdfPCell workingCell = new PdfPCell(new Phrase("Working", normalFont));
            workingCell.setBorder(Rectangle.BOX);
            workingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            workingCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell nBox = new PdfPCell(new Phrase("", boldFont));
            nBox.setBorder(Rectangle.BOX);
            nBox.setFixedHeight(16f);
            nBox.setHorizontalAlignment(Element.ALIGN_CENTER);
            nBox.setVerticalAlignment(Element.ALIGN_MIDDLE);
            nBox.setPaddingTop(2f);
            nBox.setPaddingBottom(2f);
            nBox.setCellEvent(new PdfPCellEvent() {
                @Override
                public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
                    PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
                    float offset = 2f;
                    cb.rectangle(position.getLeft() + offset, position.getBottom() + offset, position.getWidth() - 2 * offset, position.getHeight() - 2 * offset);
                    cb.stroke();
                    if (!isWorking && localCrossIcon != null) {
                        try {
                            Image img = Image.getInstance(localCrossIcon);
                            img.scaleToFit(position.getWidth() - 4, position.getHeight() - 4);
                            img.setAbsolutePosition(position.getLeft() + 2, position.getBottom() + 2);
                            cb.addImage(img);
                        } catch (Exception e) {
                            logger.error("Error rendering cross icon in nBox: {}", e.getMessage(), e);
                        }
                    }
                }
            });

            PdfPCell notWorkingCell = new PdfPCell(new Phrase("Not working", normalFont));
            notWorkingCell.setBorder(Rectangle.BOX);
            notWorkingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            notWorkingCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell blankCell = new PdfPCell(new Phrase(""));
            blankCell.setBorder(Rectangle.BOX);

            cctvStatusTable.addCell(yBox);
            cctvStatusTable.addCell(workingCell);
            cctvStatusTable.addCell(nBox);
            cctvStatusTable.addCell(notWorkingCell);
            cctvStatusTable.addCell(blankCell);

            document.add(cctvStatusTable);

            Font tinyBold = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
            Font small = new Font(Font.FontFamily.HELVETICA, 9);

            PdfPTable sectionHeaderTable = new PdfPTable(1);
            sectionHeaderTable.setWidthPercentage(100);
            sectionHeaderTable.setSpacingAfter(0f);

            PdfPCell headerCell2 = new PdfPCell(new Phrase("Products Installed & Status", tinyBold));
            headerCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell2.setPadding(6);
            headerCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell2.setBorder(Rectangle.BOX);
            sectionHeaderTable.addCell(headerCell2);

            PdfPTable productTable = new PdfPTable(5);
            productTable.setWidthPercentage(100);
            productTable.setSpacingBefore(0);
            productTable.setWidths(new float[]{7, 20, 6, 15, 16});

            Font smallBold = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);

            String[] productHeaders = {"Sr. No", "Items", "Qty", "Details", "Working"};
            for (String h : productHeaders) {
                PdfPCell headerCell = new PdfPCell(new Phrase(h, smallBold));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setBorder(Rectangle.BOX);
                headerCell.setPadding(4);
                productTable.addCell(headerCell);
            }

            List<CCTVProductInstalledStatusDTO> cctvProductInstalledStatuses = dto != null ? dto.getCctvProductInstalledStatus() : null;
            int serial = 1;

            if (cctvProductInstalledStatuses != null) {
                for (CCTVProductInstalledStatusDTO item : cctvProductInstalledStatuses) {
                    try {
                        PdfPCell cell1 = new PdfPCell(new Phrase(String.valueOf(serial++), small));
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        productTable.addCell(cell1);

                        PdfPCell cell2 = new PdfPCell(new Phrase(item.getItemCategory(), small));
                        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        productTable.addCell(cell2);

                        PdfPCell cell3 = new PdfPCell(new Phrase(item.getQuantity(), small));
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        productTable.addCell(cell3);

                        PdfPCell cell4 = new PdfPCell(new Phrase(item.getDetails() != null ? item.getDetails() : "", valueBlueFont));
                        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        productTable.addCell(cell4);

                        PdfPCell cell5 = new PdfPCell(new Phrase(""));
                        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        productTable.addCell(cell5);

                        if (item.getSubItems() != null && !item.getSubItems().isEmpty()) {
                            for (CCTVProductInstalledSubStatusDTO sub : item.getSubItems()) {
                                try {
                                    PdfPCell srNoCell = new PdfPCell(new Phrase(""));
                                    srNoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    srNoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                    PdfPCell itemCell = new PdfPCell(new Phrase(sub.getItemName(), valueBlueFontTiny));
                                    itemCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    itemCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                    PdfPCell qtyCell = new PdfPCell(new Phrase(sub.getQuantity(), valueBlueFontTiny));
                                    qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    qtyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                    Phrase detailPhrase = new Phrase();
                                    if (sub.getDetails() != null && !sub.getDetails().isEmpty()) {
                                        detailPhrase.add(new Chunk(sub.getDetails(), valueBlueFontTiny));
                                    }
                                    if (sub.getItemName() != null && (sub.getItemName().toLowerCase().contains("dvr") || sub.getItemName().toLowerCase().contains("nvr"))) {
                                        Object safetyObj = sub.getSafetyCabinetStatus();
                                        String safetyStatus;
                                        if (safetyObj instanceof Boolean) {
                                            safetyStatus = (Boolean) safetyObj ? "Yes" : "No";
                                        } else if (safetyObj != null) {
                                            String s = safetyObj.toString().trim().toLowerCase();
                                            safetyStatus = s.equals("true") || s.equals("yes") ? "Yes" : s.equals("false") || s.equals("no") ? "No" : "N.A";
                                        } else {
                                            safetyStatus = "N.A";
                                        }
                                        detailPhrase.add(new Chunk("      | Safety Cabinet:  ", smallBold));
                                        detailPhrase.add(new Chunk(safetyStatus, valueBlueFontTiny));
                                    }

                                    PdfPCell detailsCell = new PdfPCell(detailPhrase);
                                    detailsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    detailsCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                    PdfPCell statusCell = createSmallStatusCell(small, sub.getWorkingStatus());
                                    statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                    srNoCell.setBorder(Rectangle.BOX);
                                    itemCell.setBorder(Rectangle.BOX);
                                    qtyCell.setBorder(Rectangle.BOX);
                                    detailsCell.setBorder(Rectangle.BOX);
                                    statusCell.setBorder(Rectangle.BOX);

                                    productTable.addCell(srNoCell);
                                    productTable.addCell(itemCell);
                                    productTable.addCell(qtyCell);
                                    productTable.addCell(detailsCell);
                                    productTable.addCell(statusCell);
                                } catch (Exception e) {
                                    logger.error("Error rendering sub-item row for item: {}. Error: {}", sub.getItemName(), e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error rendering product installed status row for category: {}. Error: {}", item.getItemCategory(), e.getMessage(), e);
                    }
                }
            } else {
                logger.warn("cctvProductInstalledStatuses is null for branch: {}", dto != null ? dto.getBranchName() : "null");
            }

            PdfPTable combinedTable = new PdfPTable(1);
            combinedTable.setWidthPercentage(100);
            combinedTable.setSpacingBefore(5);
            combinedTable.setSpacingAfter(5);

            PdfPCell combinedHeader = new PdfPCell(sectionHeaderTable);
            combinedHeader.setPadding(0);
            combinedHeader.setBorder(Rectangle.NO_BORDER);
            combinedTable.addCell(combinedHeader);

            PdfPCell combinedBody = new PdfPCell(productTable);
            combinedBody.setPadding(0);
            combinedBody.setBorder(Rectangle.NO_BORDER);
            combinedTable.addCell(combinedBody);

            document.add(combinedTable);

            Font tiny = new Font(Font.FontFamily.HELVETICA, 7);

            PdfPTable cameraTable = new PdfPTable(8);
            cameraTable.setWidthPercentage(100);
            cameraTable.setSpacingBefore(0);
            cameraTable.setSpacingAfter(0);
            cameraTable.setWidths(new float[]{5, 12, 10, 18, 5, 12, 10, 18});

            String[] camHeaders = {"S.No", "Camera Location", "Type", "Working Status", "S.No", "Camera Location", "Type", "Working Status"};
            for (String h : camHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(h, tinyBold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(3);
                cell.setBorder(Rectangle.BOX);
                cameraTable.addCell(cell);
            }

            List<CCTVCameraStatusDTO> camList = dto != null && dto.getCctvCameraStatus() != null ? dto.getCctvCameraStatus() : Collections.emptyList();

            Integer totalCameraPort = dto != null && dto.getCctvProductDetail() != null && dto.getCctvProductDetail().getTotalCameraPort() != null
                    ? dto.getCctvProductDetail().getTotalCameraPort() : camList.size();

            for (int i = 0; i < totalCameraPort; i += 2) {
                try {
                    CCTVCameraStatusDTO camA = i < camList.size() ? camList.get(i) : null;

                    PdfPCell snoA = new PdfPCell(new Phrase(String.valueOf(i + 1), small));
                    snoA.setHorizontalAlignment(Element.ALIGN_CENTER);
                    snoA.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    snoA.setFixedHeight(18f);
                    cameraTable.addCell(snoA);

                    PdfPCell camLocA = new PdfPCell(new Phrase(camA != null && camA.getCameraLocation() != null ? camA.getCameraLocation() : "Camera " + (i + 1), valueBlueFontTiny));
                    camLocA.setHorizontalAlignment(Element.ALIGN_CENTER);
                    camLocA.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    camLocA.setFixedHeight(18f);
                    cameraTable.addCell(camLocA);

                    PdfPCell typeA = new PdfPCell(new Phrase(camA != null && camA.getCameraType() != null ? camA.getCameraType() : "", valueBlueFontTiny));
                    typeA.setHorizontalAlignment(Element.ALIGN_CENTER);
                    typeA.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    typeA.setFixedHeight(18f);
                    cameraTable.addCell(typeA);

                    PdfPCell statusA = createSmallStatusCell(tiny, camA != null ? camA.getCameraWorkingStatus() : null);
                    cameraTable.addCell(statusA);

                    if (i + 1 < totalCameraPort) {
                        CCTVCameraStatusDTO camB = i + 1 < camList.size() ? camList.get(i + 1) : null;

                        PdfPCell snoB = new PdfPCell(new Phrase(String.valueOf(i + 2), small));
                        snoB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        snoB.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        snoB.setFixedHeight(18f);
                        cameraTable.addCell(snoB);

                        PdfPCell camLocB = new PdfPCell(new Phrase(camB != null && camB.getCameraLocation() != null ? camB.getCameraLocation() : "Camera " + (i + 2), valueBlueFontTiny));
                        camLocB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        camLocB.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        camLocB.setFixedHeight(18f);
                        cameraTable.addCell(camLocB);

                        PdfPCell typeB = new PdfPCell(new Phrase(camB != null && camB.getCameraType() != null ? camB.getCameraType() : "", valueBlueFontTiny));
                        typeB.setFixedHeight(18f);
                        typeB.setHorizontalAlignment(Element.ALIGN_CENTER);
                        typeB.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cameraTable.addCell(typeB);

                        PdfPCell statusB = createSmallStatusCell(tiny, camB != null ? camB.getCameraWorkingStatus() : null);
                        cameraTable.addCell(statusB);
                    } else {
                        cameraTable.addCell(new PdfPCell(new Phrase("", tiny)));
                        cameraTable.addCell(new PdfPCell(new Phrase("", tiny)));
                        cameraTable.addCell(new PdfPCell(new Phrase("", tiny)));
                        cameraTable.addCell(createSmallStatusCell(tiny, null));
                    }
                } catch (Exception e) {
                    logger.error("Error rendering camera row at index: {}. Error: {}", i, e.getMessage(), e);
                }
            }

            document.add(cameraTable);

            PdfPTable hddTable = new PdfPTable(8);
            hddTable.setWidthPercentage(100);
            hddTable.setSpacingBefore(5);
            hddTable.setSpacingAfter(0);
            hddTable.setWidths(new float[]{15, 10, 15, 10, 15, 10, 15, 10});

            Font labelFont = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);

            String totalHddSlotsStr = dto != null && dto.getTotalHddSlots() != null ? String.valueOf(dto.getTotalHddSlots()) : "N/A";
            String hddInstalledQtyStr = dto != null && dto.getHddInstalledQty() != null ? String.valueOf(dto.getHddInstalledQty()) : "N/A";
            String totalCapacityStr = dto != null && dto.getTotalCapacityTb() != null ? String.valueOf(dto.getTotalCapacityTb()) : "N/A";
            String freeSpaceStr = dto != null && dto.getTotalFreeSpaceTb() != null ? String.valueOf(dto.getTotalFreeSpaceTb()) : "N/A";

            PdfPCell c1 = new PdfPCell(new Phrase("Total HDD Sata Slot", labelFont));
            PdfPCell c2 = new PdfPCell(new Phrase(totalHddSlotsStr + " Nos.", valueBlueFontTiny));
            PdfPCell c3 = new PdfPCell(new Phrase("HDD Installed Qty.", labelFont));
            PdfPCell c4 = new PdfPCell(new Phrase(hddInstalledQtyStr + " Nos.", valueBlueFontTiny));
            PdfPCell c5 = new PdfPCell(new Phrase("Total Capacity", labelFont));
            PdfPCell c6 = new PdfPCell(new Phrase(totalCapacityStr + " TB", valueBlueFontTiny));
            PdfPCell c7 = new PdfPCell(new Phrase("Free Space", labelFont));
            PdfPCell c8 = new PdfPCell(new Phrase(freeSpaceStr + " MB", valueBlueFontTiny));

            List<PdfPCell> labelCells = Arrays.asList(c1, c3, c5, c7);
            for (PdfPCell hddLabelCell : labelCells) {
                hddLabelCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            }

            List<PdfPCell> cells = Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8);
            for (PdfPCell hddCell : cells) {
                hddCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                hddCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hddCell.setPadding(5);
                hddCell.setBorder(Rectangle.BOX);
                hddTable.addCell(hddCell);
            }

            document.add(hddTable);

            PdfPTable hddTable2 = new PdfPTable(5);
            hddTable2.setWidthPercentage(100);
            hddTable2.setSpacingBefore(5);
            hddTable2.setSpacingAfter(0);
            hddTable2.setWidths(new float[]{6, 12, 14, 11, 11});

            String[] hddHeaders2 = {"S.No", "Items", "Capacity", "Qty", "Working Status"};
            for (String h : hddHeaders2) {
                PdfPCell hddHeaderCell = new PdfPCell(new Phrase(h, tinyBold));
                hddHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hddHeaderCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                hddHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hddHeaderCell.setPadding(3);
                hddHeaderCell.setBorder(Rectangle.BOX);
                hddTable2.addCell(hddHeaderCell);
            }

            List<CCTVHddStatusDTO> hddList = dto != null && dto.getCctvHddStatus() != null ? dto.getCctvHddStatus() : Collections.emptyList();

            int totalHddSlots = dto != null && dto.getTotalHddSlots() != null ? dto.getTotalHddSlots() : hddList.size();
            if (totalHddSlots <= 0) totalHddSlots = 1;

            for (int i = 0; i < totalHddSlots; i++) {
                try {
                    CCTVHddStatusDTO hdd = i < hddList.size() ? hddList.get(i) : null;

                    PdfPCell sno = new PdfPCell(new Phrase(String.valueOf(i + 1), small));
                    sno.setHorizontalAlignment(Element.ALIGN_CENTER);
                    sno.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    sno.setFixedHeight(18f);
                    hddTable2.addCell(sno);

                    PdfPCell hddItem = new PdfPCell(new Phrase("HDD " + (i + 1), valueBlueFontTiny));
                    hddItem.setHorizontalAlignment(Element.ALIGN_CENTER);
                    hddItem.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    hddItem.setFixedHeight(18f);
                    hddTable2.addCell(hddItem);

                    PdfPCell capacity = new PdfPCell(new Phrase(hdd != null && hdd.getCapacity() != null ? hdd.getCapacity() : "", valueBlueFontTiny));
                    capacity.setFixedHeight(18f);
                    capacity.setHorizontalAlignment(Element.ALIGN_CENTER);
                    hddTable2.addCell(capacity);

                    PdfPCell qty = new PdfPCell(new Phrase("1", valueBlueFontTiny));
                    qty.setHorizontalAlignment(Element.ALIGN_CENTER);
                    qty.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    qty.setFixedHeight(18f);
                    hddTable2.addCell(qty);

                    hddTable2.addCell(createSmallStatusCell(tiny, hdd != null ? hdd.getWorkingStatus() : null));
                } catch (Exception e) {
                    logger.error("Error rendering HDD row at index: {}. Error: {}", i, e.getMessage(), e);
                }
            }

            document.add(hddTable2);

            PdfPTable generalTable = new PdfPTable(6);
            generalTable.setWidthPercentage(100);
            generalTable.setSpacingBefore(5);
            generalTable.setWidths(new float[]{35, 6, 6.5f, 35, 6, 6.5f});

            PdfPCell generalHeader = new PdfPCell(new Phrase("General  Check Points", tinyBold));
            generalHeader.setColspan(6);
            generalHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            generalHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
            generalHeader.setPadding(3);
            generalTable.addCell(generalHeader);

            String[][] rows = {
                    {"1. AC supply is coming uninterrupted."},
                    {"2. System is connected to UPS."},
                    {"3. All cameras to be checked & to be in functional state."},
                    {"4. Recording is checked and available from DD/MM/YY till today."},
                    {"5. Operation of the system is explained again and is understood."},
                    {"6. Setting of motion detection is done as per requirement."},
                    {"7. DVR/NVR & Cameras cleaned from inside & outside."},
                    {"8. Password changed as per BM/SO."}
            };

            Boolean[] statuses = new Boolean[]{
                    dto != null ? dto.getIsAcSupplyUninterrupted() : null,
                    dto != null ? dto.getIsSystemOnUps() : null,
                    dto != null ? dto.getAreAllCamerasFunctional() : null,
                    dto != null ? dto.getIsRecordingAvailable() : null,
                    dto != null ? dto.getIsOperationExplainedAndUnderstood() : null,
                    dto != null ? dto.getIsMotionDetectionSet() : null,
                    dto != null ? dto.getIsSystemCleaned() : null,
                    dto != null ? dto.getIsPasswordChanged() : null
            };

            for (int i = 0; i < rows.length; i++) {
                try {
                    String[] r = rows[i];
                    PdfPCell descCell = new PdfPCell(new Phrase(r[0], tiny));
                    descCell.setPadding(3);
                    descCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    generalTable.addCell(descCell);

                    PdfPCell ynCell = createStatusCell(valueBlueFontTiny, statuses[i]);
                    ynCell.setColspan(2);
                    generalTable.addCell(ynCell);
                } catch (Exception e) {
                    logger.error("Error rendering general checkpoint row at index: {}. Error: {}", i, e.getMessage(), e);
                }
            }

            document.add(generalTable);

            PdfPTable materialSection = new PdfPTable(2);
            materialSection.setWidthPercentage(100);
            materialSection.setSpacingBefore(5);
            materialSection.setWidths(new float[]{50, 50});

            PdfPTable replacedTable = new PdfPTable(4);
            replacedTable.setWidthPercentage(100);
            replacedTable.setWidths(new float[]{10, 58, 20, 12});

            PdfPCell replacedHeader = new PdfPCell(new Phrase("Materials REPLACED", tinyBold));
            replacedHeader.setColspan(4);
            replacedHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            replacedHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            replacedHeader.setPadding(4);
            replacedTable.addCell(replacedHeader);

            String[] replacedSubHeaders = {"S.No", "Material", "Chargeable", "Qty"};
            for (String h : replacedSubHeaders) {
                PdfPCell hdrCell = new PdfPCell(new Phrase(h, tinyBold));
                hdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hdrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hdrCell.setPadding(3);
                replacedTable.addCell(hdrCell);
            }

            List<CCTVMaterialDTO> allMaterials = dto != null && dto.getMaterials() != null ? dto.getMaterials() : Collections.emptyList();
            List<CCTVMaterialDTO> replacedMaterials = allMaterials.stream()
                    .filter(m -> m.getMaterialType() != null && m.getMaterialType().name().equalsIgnoreCase("REPLACED"))
                    .collect(Collectors.toList());

            for (int i = 0; i < replacedMaterials.size(); i++) {
                try {
                    CCTVMaterialDTO matDto = replacedMaterials.get(i);
                    PdfPCell sr = new PdfPCell(new Phrase(String.valueOf(i + 1), valueBlueFontTiny));
                    sr.setHorizontalAlignment(Element.ALIGN_CENTER);
                    sr.setFixedHeight(18f);
                    replacedTable.addCell(sr);

                    PdfPCell mat = new PdfPCell(new Phrase(matDto.getItemDescription() != null ? matDto.getItemDescription() : "", valueBlueFontTiny));
                    mat.setPadding(3);
                    replacedTable.addCell(mat);

                    PdfPCell chargeableCell = createStatusCell(valueBlueFontTiny, matDto.getIsChargeable());
                    replacedTable.addCell(chargeableCell);

                    PdfPCell qtyCell = new PdfPCell(new Phrase(matDto.getQuantity() != null ? String.valueOf(matDto.getQuantity()) : "", valueBlueFontTiny));
                    qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    replacedTable.addCell(qtyCell);
                } catch (Exception e) {
                    logger.error("Error rendering replaced material row at index: {}. Error: {}", i, e.getMessage(), e);
                }
            }

            PdfPTable requiredTable = new PdfPTable(4);
            requiredTable.setWidthPercentage(100);
            requiredTable.setWidths(new float[]{10, 58, 20, 12});

            PdfPCell requiredHeader = new PdfPCell(new Phrase("Materials REQUIRED", tinyBold));
            requiredHeader.setColspan(4);
            requiredHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            requiredHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            requiredHeader.setPadding(4);
            requiredTable.addCell(requiredHeader);

            String[] requiredSubHeaders = {"S.No", "Material", "Chargeable", "Qty"};
            for (String h : requiredSubHeaders) {
                PdfPCell hdrCell = new PdfPCell(new Phrase(h, tinyBold));
                hdrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                hdrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hdrCell.setPadding(3);
                requiredTable.addCell(hdrCell);
            }

            List<CCTVMaterialDTO> requiredMaterials = allMaterials.stream()
                    .filter(m -> m.getMaterialType() != null && m.getMaterialType().name().equalsIgnoreCase("REQUIRED"))
                    .collect(Collectors.toList());

            for (int i = 0; i < requiredMaterials.size(); i++) {
                try {
                    CCTVMaterialDTO matDto = requiredMaterials.get(i);

                    PdfPCell sr = new PdfPCell(new Phrase(String.valueOf(i + 1), valueBlueFontTiny));
                    sr.setHorizontalAlignment(Element.ALIGN_CENTER);
                    sr.setFixedHeight(18f);
                    requiredTable.addCell(sr);

                    PdfPCell mat = new PdfPCell(new Phrase(matDto.getItemDescription() != null ? matDto.getItemDescription() : "", valueBlueFontTiny));
                    mat.setPadding(3);
                    requiredTable.addCell(mat);

                    PdfPCell chargeableCell = createStatusCell(valueBlueFontTiny, matDto.getIsChargeable());
                    requiredTable.addCell(chargeableCell);

                    PdfPCell qtyCell = new PdfPCell(new Phrase(matDto.getQuantity() != null ? String.valueOf(matDto.getQuantity()) : "", valueBlueFontTiny));
                    qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    requiredTable.addCell(qtyCell);
                } catch (Exception e) {
                    logger.error("Error rendering required material row at index: {}. Error: {}", i, e.getMessage(), e);
                }
            }

            materialSection.addCell(new PdfPCell(replacedTable));
            materialSection.addCell(new PdfPCell(requiredTable));
            document.add(materialSection);

            PdfPTable detailsTable = new PdfPTable(8);
            detailsTable.setSpacingBefore(5);
            detailsTable.setWidthPercentage(100);
            detailsTable.setWidths(new float[]{7, 20, 6, 14, 7, 20, 6, 14});

            PdfPCell defectFound = new PdfPCell(new Phrase("Defect found on inspection / Remarks / Recommendation by Field Associate", tinyBold));
            defectFound.setColspan(4);
            defectFound.setBackgroundColor(BaseColor.LIGHT_GRAY);
            defectFound.setHorizontalAlignment(Element.ALIGN_LEFT);
            defectFound.setPadding(3);
            detailsTable.addCell(defectFound);

            PdfPCell estimateHeader = new PdfPCell(new Phrase("Estimate Details", tinyBold));
            estimateHeader.setColspan(4);
            estimateHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            estimateHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            estimateHeader.setPadding(3);
            detailsTable.addCell(estimateHeader);

            String faRemarks = dto != null && dto.getFaRemarks() != null ? dto.getFaRemarks() : "";
            PdfPCell emptyRemarks = new PdfPCell(new Phrase(faRemarks, valueBlueFontTiny));
            emptyRemarks.setColspan(4);
            emptyRemarks.setRowspan(3);
            emptyRemarks.setFixedHeight(45f);
            detailsTable.addCell(emptyRemarks);

            String estimateNoValue = dto != null && dto.getEstimateNo() != null ? dto.getEstimateNo() : "";
            Phrase leftHalfPhrase = new Phrase();
            leftHalfPhrase.add(new Chunk("Estimate No. - ", normalFont));
            leftHalfPhrase.add(new Chunk(estimateNoValue, valueBlueFontTiny));
            PdfPCell estimateNoLeft = new PdfPCell(leftHalfPhrase);
            estimateNoLeft.setColspan(2);
            estimateNoLeft.setPadding(3);
            detailsTable.addCell(estimateNoLeft);

            Phrase rightHalfPhrase = new Phrase();
            rightHalfPhrase.add(new Chunk("Product  - ", normalFont));
            rightHalfPhrase.add(new Chunk(dto != null && dto.getEstimateProductDetails() != null ? dto.getEstimateProductDetails() : "", valueBlueFontTiny));
            PdfPCell productNameRight = new PdfPCell(rightHalfPhrase);
            productNameRight.setColspan(2);
            productNameRight.setPadding(3);
            detailsTable.addCell(productNameRight);

            PdfPTable dateAmountTable = new PdfPTable(2);
            dateAmountTable.setWidthPercentage(100);
            dateAmountTable.setWidths(new float[]{50, 50});
            String estimateDateValue = dto != null && dto.getEstimateDate() != null ? dto.getEstimateDate().toString() : "";
            Phrase datePhrase = new Phrase();
            datePhrase.add(new Chunk("Date - ", normalFont));
            datePhrase.add(new Chunk(estimateDateValue, valueBlueFontTiny));
            PdfPCell dateCell2 = new PdfPCell(datePhrase);
            dateCell2.setPadding(3);
            dateAmountTable.addCell(dateCell2);
            String estimateAmountValue = dto != null && dto.getEstimateAmount() != null ? String.valueOf(dto.getEstimateAmount()) : "";
            Phrase amountPhrase = new Phrase();
            amountPhrase.add(new Chunk("Amount - ", normalFont));
            amountPhrase.add(new Chunk(estimateAmountValue, valueBlueFontTiny));
            PdfPCell amountCell = new PdfPCell(amountPhrase);
            amountCell.setPadding(3);
            dateAmountTable.addCell(amountCell);
            PdfPCell dateAmountWrapper = new PdfPCell(dateAmountTable);
            dateAmountWrapper.setColspan(4);
            dateAmountWrapper.setPadding(0);
            detailsTable.addCell(dateAmountWrapper);

            PdfPTable billDetailsContainerTable = new PdfPTable(1);
            billDetailsContainerTable.setWidthPercentage(100);

            PdfPCell billDetailsLabel = new PdfPCell(new Phrase("Manual Invoice", tinyBold));
            billDetailsLabel.setBorder(Rectangle.NO_BORDER);
            billDetailsLabel.setPadding(3);
            billDetailsLabel.setBackgroundColor(BaseColor.LIGHT_GRAY);
            billDetailsLabel.setHorizontalAlignment(Element.ALIGN_CENTER);
            billDetailsContainerTable.addCell(billDetailsLabel);

            PdfPTable billYNTable = new PdfPTable(2);
            billYNTable.setWidthPercentage(100);
            billYNTable.setWidths(new float[]{65, 35});

            PdfPCell billToBeRaisedLabel = new PdfPCell(new Phrase("Bill to be Raised", tiny));
            billToBeRaisedLabel.setBorder(Rectangle.NO_BORDER);
            billToBeRaisedLabel.setPadding(3);
            billYNTable.addCell(billToBeRaisedLabel);

            PdfPCell billYNWrapperInner = createStatusCell(valueBlueFontTiny, dto != null ? dto.getBillToBeRaised() : null);
            billYNTable.addCell(billYNWrapperInner);

            PdfPCell billYNWrapper = new PdfPCell(billYNTable);
            billYNWrapper.setPadding(0);
            billDetailsContainerTable.addCell(billYNWrapper);

            PdfPCell billDetailsWrapper = new PdfPCell(billDetailsContainerTable);
            billDetailsWrapper.setColspan(4);
            billDetailsWrapper.setPadding(0);
            detailsTable.addCell(billDetailsWrapper);

            PdfPCell systemWorking = new PdfPCell(new Phrase("System is working properly & is in OIN condition. It has been explained to:", tinyBold));
            systemWorking.setColspan(4);
            systemWorking.setBackgroundColor(BaseColor.LIGHT_GRAY);
            systemWorking.setPadding(3);
            detailsTable.addCell(systemWorking);

            PdfPTable billNoDateTable = new PdfPTable(2);
            billNoDateTable.setWidthPercentage(100);
            billNoDateTable.setWidths(new float[]{50, 50});
            String billNoValue = dto != null && dto.getBillNo() != null ? dto.getBillNo() : "";
            Phrase billNoPhrase = new Phrase();
            billNoPhrase.add(new Chunk("Bill No. - ", normalFont));
            billNoPhrase.add(new Chunk(billNoValue, valueBlueFontTiny));
            PdfPCell billNoCell = new PdfPCell(billNoPhrase);
            billNoCell.setPadding(3);
            billNoDateTable.addCell(billNoCell);
            String billDateValue = dto != null && dto.getBillDate() != null ? dto.getBillDate().toString() : "";
            Phrase billDatePhrase = new Phrase();
            billDatePhrase.add(new Chunk("Date - ", normalFont));
            billDatePhrase.add(new Chunk(billDateValue, valueBlueFontTiny));
            PdfPCell billDateCell = new PdfPCell(billDatePhrase);
            billDateCell.setPadding(3);
            billNoDateTable.addCell(billDateCell);
            PdfPCell billNoDateWrapper = new PdfPCell(billNoDateTable);
            billNoDateWrapper.setColspan(4);
            billNoDateWrapper.setPadding(0);
            detailsTable.addCell(billNoDateWrapper);

            PdfPTable explainedToTable = new PdfPTable(3);
            explainedToTable.setWidthPercentage(100);
            explainedToTable.setWidths(new float[]{12, 43, 45});
            PdfPCell srHeader = new PdfPCell(new Phrase("Sr. No.-", tinyBold));
            srHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            explainedToTable.addCell(srHeader);
            explainedToTable.addCell(new Phrase("Name", tinyBold));
            explainedToTable.addCell(new Phrase("Designation", tinyBold));

            List<CCTVStaffMemberDTO> staffList = dto != null && dto.getStaffMembers() != null ? dto.getStaffMembers() : Collections.emptyList();

            for (int i = 0; i < staffList.size(); i++) {
                try {
                    PdfPCell srCell = new PdfPCell(new Phrase(String.valueOf(i + 1), valueBlueFontTiny));
                    srCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    explainedToTable.addCell(srCell);

                    CCTVStaffMemberDTO staff = staffList.get(i);
                    PdfPCell nameCell2 = new PdfPCell(new Phrase(staff.getName() != null ? staff.getName() : "", valueBlueFontTiny));
                    nameCell2.setPadding(3);
                    explainedToTable.addCell(nameCell2);

                    PdfPCell desigCell = new PdfPCell(new Phrase(staff.getDesignation() != null ? staff.getDesignation() : "", valueBlueFontTiny));
                    desigCell.setPadding(3);
                    explainedToTable.addCell(desigCell);
                } catch (Exception e) {
                    logger.error("Error rendering staff member row at index: {}. Error: {}", i, e.getMessage(), e);
                }
            }

            PdfPCell explainedToWrapper = new PdfPCell(explainedToTable);
            explainedToWrapper.setColspan(4);
            explainedToWrapper.setPadding(0);
            explainedToWrapper.setRowspan(2);
            detailsTable.addCell(explainedToWrapper);

            PdfPCell pendingPaymentHeader = new PdfPCell(new Phrase("Pending Payment", tinyBold));
            pendingPaymentHeader.setColspan(4);
            pendingPaymentHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
            pendingPaymentHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pendingPaymentHeader.setPadding(3);
            detailsTable.addCell(pendingPaymentHeader);

            PdfPTable pendingInvoiceYNTable = new PdfPTable(2);
            pendingInvoiceYNTable.setWidthPercentage(100);
            pendingInvoiceYNTable.setWidths(new float[]{65, 35});
            PdfPCell pendingInvoiceLabel = new PdfPCell(new Phrase("Pending Invoice / Payment Followed", tiny));
            pendingInvoiceLabel.setBorder(Rectangle.NO_BORDER);
            pendingInvoiceLabel.setPadding(3);
            pendingInvoiceYNTable.addCell(pendingInvoiceLabel);
            PdfPCell pendingInvoiceWrapperInner = createStatusCell(valueBlueFontTiny, dto != null ? dto.getIsInvoicePaymentFollowed() : null);
            pendingInvoiceWrapperInner.setPadding(3);
            pendingInvoiceWrapperInner.setBorder(Rectangle.BOX);
            pendingInvoiceYNTable.addCell(pendingInvoiceWrapperInner);

            PdfPCell pendingInvoiceWrapper = new PdfPCell(pendingInvoiceYNTable);
            pendingInvoiceWrapper.setColspan(4);
            pendingInvoiceWrapper.setPadding(0);
            detailsTable.addCell(pendingInvoiceWrapper);

            PdfPCell customerRemarksLabel = new PdfPCell(new Phrase("Customer's Remarks", tinyBold));
            customerRemarksLabel.setPadding(3);
            customerRemarksLabel.setColspan(2);
            customerRemarksLabel.setBackgroundColor(BaseColor.LIGHT_GRAY);
            customerRemarksLabel.setBorderWidthRight(0f);
            detailsTable.addCell(customerRemarksLabel);

            String customerRemarks = dto != null && dto.getCustomerRemarks() != null ? dto.getCustomerRemarks() : "";
            PdfPCell customerRemarksValue = new PdfPCell(new Phrase(customerRemarks, valueBlueFontTiny));
            customerRemarksValue.setPadding(3);
            customerRemarksValue.setColspan(2);
            customerRemarksValue.setBorderWidthLeft(0f);
            detailsTable.addCell(customerRemarksValue);

            String paymentRemarks = dto != null && dto.getPaymentRemarks() != null ? dto.getPaymentRemarks() : "";
            Phrase paymentRemarksPhrase = new Phrase();
            paymentRemarksPhrase.add(new Chunk("Remarks for Payment: ", normalFont));
            paymentRemarksPhrase.add(new Chunk(paymentRemarks, valueBlueFontTiny));
            PdfPCell paymentRemarksLabel = new PdfPCell(paymentRemarksPhrase);
            paymentRemarksLabel.setPadding(3);
            paymentRemarksLabel.setColspan(4);
            detailsTable.addCell(paymentRemarksLabel);

            PdfPCell customerRemarksContent = new PdfPCell(new Phrase("", valueBlueFontTiny));
            customerRemarksContent.setPadding(3);
            customerRemarksContent.setColspan(4);
            customerRemarksContent.setFixedHeight(30f);
            detailsTable.addCell(customerRemarksContent);

            PdfPCell companyNameContent = new PdfPCell(new Phrase("Digitals India Security Products Pvt Ltd", tinyBold));
            companyNameContent.setPadding(3);
            companyNameContent.setColspan(4);
            detailsTable.addCell(companyNameContent);

            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(100);
            signatureTable.setWidths(new float[]{50, 50});

            PdfPCell signatureCell = new PdfPCell(new Phrase("[Signature & Seal]", tinyBold));
            signatureCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            signatureCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            signatureCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            signatureTable.addCell(signatureCell);

            PdfPCell faSignatureCell = new PdfPCell(new Phrase("[FA Signature]", tinyBold));
            faSignatureCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            faSignatureCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            faSignatureCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            faSignatureCell.setFixedHeight(0f);
            signatureTable.addCell(faSignatureCell);

            PdfPCell signatureWrapper = new PdfPCell(signatureTable);
            signatureWrapper.setColspan(8);
            signatureWrapper.setPadding(0);
            detailsTable.addCell(signatureWrapper);

            PdfPCell bankDetails = new PdfPCell(new Phrase(
                    "Do not make cash payment, Please transfer payment in the following company account\n"
                            + "DIGITALS INDIA SECURITY PRODUCTS PVT. LTD. Union Bank of India, Anand Vihar Delhi A/c No.-"
                            + "542805010000112 IFSC - UBIN0554286\n"
                            + "Regd. Office : Hall No. 1 C Block Market, Suraj Mal Vihar, Delhi - 110092", tinyBold));
            bankDetails.setColspan(8);
            bankDetails.setHorizontalAlignment(Element.ALIGN_LEFT);
            bankDetails.setPadding(5);
            detailsTable.addCell(bankDetails);

            PdfPCell disclaimer = new PdfPCell(new Phrase(
                    "THIS IS NOT A VALID RECEIPT FOR REMOVING MATERIAL FROM YOUR BRANCH. "
                            + "PLEASE ASK FOR SEPARATE OFFICIAL RECEIPT", tinyBold));
            disclaimer.setColspan(8);
            disclaimer.setHorizontalAlignment(Element.ALIGN_CENTER);
            disclaimer.setPadding(3);
            disclaimer.setBorder(Rectangle.NO_BORDER);
            detailsTable.addCell(disclaimer);

            document.add(detailsTable);

            logger.info("PDF generation completed successfully for branch: {}", dto != null ? dto.getBranchName() : "null");

        } catch (Exception e) {
            logger.error("Critical error during PDF generation for branch: {}. Error: {}", dto != null ? dto.getBranchName() : "null", e.getMessage(), e);
            throw new RuntimeException("Failed to generate CCTV PDF report: " + e.getMessage(), e);
        } finally {
            if (document.isOpen()) {
                try {
                    document.close();
                } catch (Exception e) {
                    logger.warn("Error closing PDF document: {}", e.getMessage());
                }
            }
        }

        return baos;
    }

    private Image loadIconFromIconsDir(String baseName) {
        String[] extensions = {".png", ".jpg", ".jpeg"};
        try {
            if (servletContext != null) {
                for (String ext : extensions) {
                    String realPath = servletContext.getRealPath("/icons/" + baseName + ext);
                    if (realPath != null && Files.exists(Paths.get(realPath))) {
                        Image img = Image.getInstance(realPath);
                        img.scaleToFit(12f, 12f);
                        return img;
                    }
                }
            }
            for (String ext : extensions) {
                try (InputStream is = getClass().getResourceAsStream("/static/icons/" + baseName + ext)) {
                    if (is != null) {
                        Image img = Image.getInstance(IOUtils.toByteArray(is));
                        img.scaleToFit(12f, 12f);
                        return img;
                    }
                } catch (Exception e) {
                    logger.warn("Failed to load icon '{}{}' from classpath: {}", baseName, ext, e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load icon '{}': {}", baseName, e.getMessage(), e);
        }
        logger.warn("Icon '{}' not found in any location.", baseName);
        return null;
    }

    private void addFormRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        try {
            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            cell.setPadding(5);
            Paragraph p = new Paragraph();
            p.add(new Chunk(label + " ", labelFont));
            p.add(new Chunk(value, valueFont));
            cell.addElement(p);
            table.addCell(cell);
        } catch (Exception e) {
            logger.error("Error in addFormRow for label '{}': {}", label, e.getMessage(), e);
        }
    }

    private void addRightFormRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        try {
            PdfPTable innerTable = new PdfPTable(2);
            innerTable.setWidthPercentage(100);
            innerTable.setWidths(new float[]{2.5f, 2f});

            PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
            labelCell.setBorder(Rectangle.LEFT | Rectangle.BOTTOM);
            labelCell.setBorderWidthLeft(1f);
            labelCell.setBorderWidthBottom(1f);
            labelCell.setPadding(5);
            innerTable.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", valueFont));
            valueCell.setBorder(Rectangle.RIGHT | Rectangle.LEFT | Rectangle.BOTTOM);
            valueCell.setBorderWidthLeft(1f);
            valueCell.setBorderWidthRight(1f);
            valueCell.setBorderWidthBottom(1f);
            valueCell.setPadding(5);
            innerTable.addCell(valueCell);

            PdfPCell outerCell = new PdfPCell(innerTable);
            outerCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            outerCell.setPadding(0);
            table.addCell(outerCell);
        } catch (Exception e) {
            logger.error("Error in addRightFormRow for label '{}': {}", label, e.getMessage(), e);
        }
    }

    private PdfPCell createSmallStatusCell(Font font, Object statusObj) {
        try {
            if (checkIcon == null) checkIcon = loadIconFromIconsDir("check");
            if (crossIcon == null) crossIcon = loadIconFromIconsDir("cross");
            if (naIcon == null) naIcon = loadIconFromIconsDir("check");

            String s = statusObj == null ? "" : statusObj.toString().trim().toLowerCase();
            Image imgForY = null, imgForN = null, imgForNA = null;

            if (s.equals("true") || s.equals("yes") || s.equals("working")) {
                imgForY = checkIcon;
            } else if (s.equals("false") || s.equals("not working")) {
                imgForN = crossIcon;
            } else if (s.equals("na") || s.equals("n.a") || s.equals("none")) {
                imgForNA = naIcon;
            }

            Font dullFont = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, new BaseColor(120, 120, 120));

            java.util.function.BiFunction<String, Image, PdfPCell> buildOptionCell = (text, icon) -> {
                PdfPCell cell = new PdfPCell();
                cell.setBorder(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(1);
                cell.setFixedHeight(14f);

                final Image finalIcon = icon;
                final String finalText = text;

                cell.setCellEvent(new PdfPCellEvent() {
                    @Override
                    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
                        try {
                            PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
                            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(finalText, dullFont),
                                    (position.getLeft() + position.getRight()) / 2, position.getBottom() + 2, 0);
                            if (finalIcon != null) {
                                finalIcon.scaleToFit(13, 13);
                                float x = (position.getLeft() + position.getRight()) / 2 - (finalIcon.getScaledWidth() / 2);
                                float y = position.getBottom() + (finalText.equalsIgnoreCase("no") ? 1.5f : 2.5f);
                                finalIcon.setAbsolutePosition(x, y);
                                canvases[PdfPTable.BACKGROUNDCANVAS].addImage(finalIcon);
                            }
                        } catch (Exception e) {
                            logger.error("Error in createSmallStatusCell cellLayout for text '{}': {}", finalText, e.getMessage(), e);
                        }
                    }
                });
                return cell;
            };

            PdfPTable inner = new PdfPTable(5);
            try {
                inner.setWidths(new float[]{30, 3, 30, 3, 30});
            } catch (Exception e) {
                logger.warn("Error setting widths for createSmallStatusCell inner table: {}", e.getMessage());
            }

            PdfPCell yCell = buildOptionCell.apply("Yes", imgForY);
            PdfPCell space1 = new PdfPCell(new Phrase(""));
            PdfPCell nCell = buildOptionCell.apply("No", imgForN);
            PdfPCell space2 = new PdfPCell(new Phrase(""));
            PdfPCell naCell = buildOptionCell.apply("N.A", imgForNA);

            space1.setBorder(Rectangle.NO_BORDER);
            space2.setBorder(Rectangle.NO_BORDER);
            space1.setFixedHeight(10f);
            space2.setFixedHeight(10f);

            inner.addCell(yCell);
            inner.addCell(space1);
            inner.addCell(nCell);
            inner.addCell(space2);
            inner.addCell(naCell);

            PdfPCell outer = new PdfPCell(inner);
            outer.setBorder(Rectangle.BOX);
            outer.setPadding(2);
            outer.setFixedHeight(18f);
            return outer;
        } catch (Exception e) {
            logger.error("Error creating small status cell for status '{}': {}", statusObj, e.getMessage(), e);
            PdfPCell fallback = new PdfPCell(new Phrase(""));
            fallback.setBorder(Rectangle.BOX);
            return fallback;
        }
    }

    private PdfPCell createStatusCell(Font font, Object statusObj) {
        try {
            if (checkIcon == null) checkIcon = loadIconFromIconsDir("check");
            if (crossIcon == null) crossIcon = loadIconFromIconsDir("cross");

            String s = statusObj == null ? "" : statusObj.toString().trim().toLowerCase();
            Image imgForY = null, imgForN = null;

            if (s.equals("true") || s.equals("yes") || s.equals("working") || s.equals("y")) {
                imgForY = checkIcon;
            } else if (s.equals("false") || s.equals("not working") || s.equals("n")) {
                imgForN = crossIcon;
            }

            Font dullFont = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, new BaseColor(120, 120, 120));

            java.util.function.BiFunction<String, Image, PdfPCell> buildOptionCell = (text, icon) -> {
                PdfPCell cell = new PdfPCell();
                cell.setBorder(Rectangle.BOX);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(1);
                cell.setFixedHeight(14f);

                final Image finalIcon = icon;
                final String finalText = text;

                cell.setCellEvent(new PdfPCellEvent() {
                    @Override
                    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
                        try {
                            PdfContentByte canvas = canvases[PdfPTable.TEXTCANVAS];
                            ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Phrase(finalText, dullFont),
                                    (position.getLeft() + position.getRight()) / 2, position.getBottom() + 2, 0);
                            if (finalIcon != null) {
                                finalIcon.scaleToFit(11, 11);
                                float x = (position.getLeft() + position.getRight()) / 2 - (finalIcon.getScaledWidth() / 2);
                                float y = position.getBottom() + (finalText.equalsIgnoreCase("no") ? 1.5f : 2.5f);
                                finalIcon.setAbsolutePosition(x, y);
                                canvases[PdfPTable.BACKGROUNDCANVAS].addImage(finalIcon);
                            }
                        } catch (Exception e) {
                            logger.error("Error in createStatusCell cellLayout for text '{}': {}", finalText, e.getMessage(), e);
                        }
                    }
                });
                return cell;
            };

            PdfPTable inner = new PdfPTable(3);
            try {
                inner.setWidths(new float[]{45, 10, 45});
            } catch (Exception e) {
                logger.warn("Error setting widths for createStatusCell inner table: {}", e.getMessage());
            }

            PdfPCell yCell = buildOptionCell.apply("Yes", imgForY);
            PdfPCell space = new PdfPCell(new Phrase(""));
            space.setBorder(Rectangle.NO_BORDER);
            space.setFixedHeight(10f);
            PdfPCell nCell = buildOptionCell.apply("No", imgForN);

            inner.addCell(yCell);
            inner.addCell(space);
            inner.addCell(nCell);

            PdfPCell outer = new PdfPCell(inner);
            outer.setBorder(Rectangle.BOX);
            outer.setPadding(2);
            return outer;
        } catch (Exception e) {
            logger.error("Error creating status cell for status '{}': {}", statusObj, e.getMessage(), e);
            PdfPCell fallback = new PdfPCell(new Phrase(""));
            fallback.setBorder(Rectangle.BOX);
            return fallback;
        }
    }

    private CCTVBranchInfo mapDtoToEntity(CCTVBranchInfoRequestDTO dto) {
        logger.debug("Mapping request DTO to entity for branch: {}", dto != null ? dto.getBranchName() : "null");
        try {
            CCTVBranchInfo entity = new CCTVBranchInfo();

            entity.setCustomerId(dto.getCustomerId());
            entity.setInspectionDate(dto.getInspectionDate());
            entity.setScheduleVisitId(dto.getScheduleVisitId());
            entity.setServiceReportNumber(dto.getServiceReportNumber());
            entity.setFieldAssociateName(dto.getFieldAssociateName());
            entity.setConcernedPersonName(dto.getConcernedPersonName());
            entity.setConcernedPersonPhone(dto.getConcernedPersonPhone());
            entity.setConcernedPersonDesignation(dto.getConcernedPersonDesignation());
            entity.setCctvWorkingStatus(dto.getCctvWorkingStatus());
            entity.setTotalHddSlots(dto.getTotalHddSlots());
            entity.setHddInstalledQty(dto.getHddInstalledQty());
            entity.setTotalCapacityTb(dto.getTotalCapacityTb());
            entity.setTotalFreeSpaceTb(dto.getTotalFreeSpaceTb());
            entity.setIsAcSupplyUninterrupted(dto.getIsAcSupplyUninterrupted());
            entity.setIsSystemOnUps(dto.getIsSystemOnUps());
            entity.setAreAllCamerasFunctional(dto.getAreAllCamerasFunctional());
            entity.setIsRecordingAvailable(dto.getIsRecordingAvailable());
            entity.setIsOperationExplainedAndUnderstood(dto.getIsOperationExplainedAndUnderstood());
            entity.setIsMotionDetectionSet(dto.getIsMotionDetectionSet());
            entity.setIsSystemCleaned(dto.getIsSystemCleaned());
            entity.setIsPasswordChanged(dto.getIsPasswordChanged());
            entity.setEstimateProductDetails(dto.getEstimateProductDetails());
            entity.setFaRemarks(dto.getFaRemarks());
            entity.setEstimateNo(dto.getEstimateNo());
            entity.setEstimateDate(dto.getEstimateDate());
            entity.setEstimateAmount(dto.getEstimateAmount());
            entity.setBillToBeRaised(dto.getBillToBeRaised());
            entity.setBillNo(dto.getBillNo());
            entity.setBillDate(dto.getBillDate());
            entity.setIsInvoicePaymentFollowed(dto.getIsInvoicePaymentFollowed());
            entity.setPaymentRemarks(dto.getPaymentRemarks());
            entity.setCustomerRemarks(dto.getCustomerRemarks());
            entity.setProductType(dto.getProductType());
            entity.setStatus(dto.getStatus());

            if (dto.getCctvProductDetail() != null) {
                try {
                    CCTVProductDetail productDetail = new CCTVProductDetail();
                    productDetail.setCctvBranchInfo(entity);
                    productDetail.setCctvSystemType(dto.getCctvProductDetail().getCctvSystemType());
                    productDetail.setDvrNvrMake(dto.getCctvProductDetail().getDvrNvrMake());
                    productDetail.setDvrNvrModel(dto.getCctvProductDetail().getDvrNvrModel());
                    productDetail.setBuiltDate(dto.getCctvProductDetail().getBuiltDate());
                    productDetail.setTotalCameraPort(dto.getCctvProductDetail().getTotalCameraPort());
                    productDetail.setIsRackInstalled(dto.getCctvProductDetail().getIsRackInstalled());
                    productDetail.setDateOfInstallation(dto.getCctvProductDetail().getDateOfInstallation());
                    entity.setCctvProductDetail(productDetail);
                } catch (Exception e) {
                    logger.error("Error mapping CCTV product detail for branch '{}': {}", dto.getBranchName(), e.getMessage(), e);
                }
            }

            if (dto.getCctvCameraStatus() != null) {
                try {
                    List<CCTVCameraStatus> cameraStatusList = dto.getCctvCameraStatus().stream().map(cameraDto -> {
                        CCTVCameraStatus camera = new CCTVCameraStatus();
                        camera.setCctvBranchInfo(entity);
                        camera.setCameraLocation(cameraDto.getCameraLocation());
                        camera.setCameraType(cameraDto.getCameraType());
                        camera.setCameraWorkingStatus(cameraDto.getCameraWorkingStatus());
                        return camera;
                    }).collect(Collectors.toList());
                    entity.setCctvCameraStatus(cameraStatusList);
                } catch (Exception e) {
                    logger.error("Error mapping CCTV camera status list for branch '{}': {}", dto.getBranchName(), e.getMessage(), e);
                }
            }

            if (dto.getCctvHddStatus() != null) {
                try {
                    List<CCTVHddStatus> hddStatusList = dto.getCctvHddStatus().stream().map(hddDto -> {
                        CCTVHddStatus hdd = new CCTVHddStatus();
                        hdd.setCctvBranchInfo(entity);
                        hdd.setItemName(hddDto.getItemName());
                        hdd.setCapacity(hddDto.getCapacity());
                        hdd.setWorkingStatus(hddDto.getWorkingStatus());
                        return hdd;
                    }).collect(Collectors.toList());
                    entity.setCctvHddStatus(hddStatusList);
                } catch (Exception e) {
                    logger.error("Error mapping CCTV HDD status list for branch '{}': {}", dto.getBranchName(), e.getMessage(), e);
                }
            }

            if (dto.getMaterials() != null && !dto.getMaterials().isEmpty()) {
                try {
                    List<CCTVMaterial> materialsList = dto.getMaterials().stream().map(materialDto -> {
                        CCTVMaterial material = new CCTVMaterial();
                        material.setCctvBranchInfo(entity);
                        material.setItemDescription(materialDto.getItemDescription());
                        material.setQuantity(materialDto.getQuantity());
                        material.setIsChargeable(materialDto.getIsChargeable());
                        if (materialDto.getMaterialType() != null) {
                            material.setMaterialType(CCTVMaterial.MaterialType.valueOf(materialDto.getMaterialType().name()));
                        }
                        return material;
                    }).collect(Collectors.toList());
                    entity.setCctvMaterials(materialsList);
                } catch (Exception e) {
                    logger.error("Error mapping CCTV materials list for branch '{}': {}", dto.getBranchName(), e.getMessage(), e);
                }
            }

            if (dto.getStaffMembers() != null) {
                try {
                    List<CCTVStaffMember> staffMemberList = dto.getStaffMembers().stream().map(staffDto -> {
                        CCTVStaffMember staff = new CCTVStaffMember();
                        staff.setCctvBranchInfo(entity);
                        staff.setName(staffDto.getName());
                        staff.setDesignation(staffDto.getDesignation());
                        return staff;
                    }).collect(Collectors.toList());
                    entity.setCctvStaffMembers(staffMemberList);
                } catch (Exception e) {
                    logger.error("Error mapping staff members list for branch '{}': {}", dto.getBranchName(), e.getMessage(), e);
                }
            }

            if (dto.getCctvProductInstalledStatus() != null) {
                try {
                    List<CCTVProductInstalledStatus> statusList = dto.getCctvProductInstalledStatus().stream().map(statusDto -> {
                        CCTVProductInstalledStatus status = new CCTVProductInstalledStatus();
                        status.setCctvBranchInfo(entity);
                        status.setItemCategory(statusDto.getItemCategory());
                        status.setQuantity(statusDto.getQuantity());
                        status.setDetails(statusDto.getDetails());

                        if (statusDto.getSubItems() != null) {
                            try {
                                List<CCTVProductInstalledSubStatus> subList = statusDto.getSubItems().stream().map(subDto -> {
                                    CCTVProductInstalledSubStatus sub = new CCTVProductInstalledSubStatus();
                                    sub.setParentStatus(status);
                                    sub.setItemName(subDto.getItemName());
                                    sub.setQuantity(subDto.getQuantity());
                                    sub.setDetails(subDto.getDetails());
                                    sub.setWorkingStatus(subDto.getWorkingStatus());
                                    sub.setSafetyCabinetStatus(subDto.getSafetyCabinetStatus());
                                    return sub;
                                }).collect(Collectors.toList());
                                status.setSubItems(subList);
                            } catch (Exception e) {
                                logger.error("Error mapping sub-items for category '{}': {}", statusDto.getItemCategory(), e.getMessage(), e);
                            }
                        }
                        return status;
                    }).collect(Collectors.toList());
                    entity.setCctvProductInstalledStatuses(statusList);
                } catch (Exception e) {
                    logger.error("Error mapping CCTV installed product statuses for branch '{}': {}", dto.getBranchName(), e.getMessage(), e);
                }
            }

            logger.debug("DTO to entity mapping completed for branch: {}", dto.getBranchName());
            return entity;

        } catch (Exception e) {
            logger.error("Critical error in mapDtoToEntity for branch '{}': {}", dto != null ? dto.getBranchName() : "null", e.getMessage(), e);
            throw new RuntimeException("Failed to map DTO to entity: " + e.getMessage(), e);
        }
    }

    private CCTVBranchInfoResponseDTO mapEntityToResponseDto(CCTVBranchInfo entity,
            CustomerResponse customerDetailsPayload, CustomerResponse customerPayload) {

        logger.debug("Mapping entity to response DTO for ID: {}", entity != null ? entity.getId() : "null");
        try {
            CCTVBranchInfoResponseDTO dto = new CCTVBranchInfoResponseDTO();

            dto.setId(entity.getId());
            dto.setCustomerId(entity.getCustomerId());
            dto.setScheduleVisitId(entity.getScheduleVisitId());
            dto.setInspectionDate(entity.getInspectionDate());
            dto.setServiceReportNumber(entity.getServiceReportNumber());
            dto.setFieldAssociateName(entity.getFieldAssociateName());
            dto.setConcernedPersonName(entity.getConcernedPersonName());
            dto.setConcernedPersonPhone(entity.getConcernedPersonPhone());
            dto.setConcernedPersonDesignation(entity.getConcernedPersonDesignation());
            dto.setTotalHddSlots(entity.getTotalHddSlots());
            dto.setHddInstalledQty(entity.getHddInstalledQty());
            dto.setTotalCapacityTb(entity.getTotalCapacityTb());
            dto.setTotalFreeSpaceTb(entity.getTotalFreeSpaceTb());
            dto.setIsAcSupplyUninterrupted(entity.getIsAcSupplyUninterrupted());
            dto.setIsSystemOnUps(entity.getIsSystemOnUps());
            dto.setAreAllCamerasFunctional(entity.getAreAllCamerasFunctional());
            dto.setIsRecordingAvailable(entity.getIsRecordingAvailable());
            dto.setIsOperationExplainedAndUnderstood(entity.getIsOperationExplainedAndUnderstood());
            dto.setIsMotionDetectionSet(entity.getIsMotionDetectionSet());
            dto.setIsSystemCleaned(entity.getIsSystemCleaned());
            dto.setIsPasswordChanged(entity.getIsPasswordChanged());
            dto.setCctvWorkingStatus(entity.getCctvWorkingStatus());
            dto.setFaRemarks(entity.getFaRemarks());
            dto.setEstimateProductDetails(entity.getEstimateProductDetails());
            dto.setEstimateNo(entity.getEstimateNo());
            dto.setEstimateDate(entity.getEstimateDate());
            dto.setEstimateAmount(entity.getEstimateAmount());
            dto.setBillToBeRaised(entity.getBillToBeRaised());
            dto.setBillNo(entity.getBillNo());
            dto.setBillDate(entity.getBillDate());
            dto.setIsInvoicePaymentFollowed(entity.getIsInvoicePaymentFollowed());
            dto.setPaymentRemarks(entity.getPaymentRemarks());
            dto.setCustomerRemarks(entity.getCustomerRemarks());
            dto.setProductType(entity.getProductType());
            dto.setStatus(entity.getStatus());

            if (customerDetailsPayload != null) {
                dto.setBranchName(customerDetailsPayload.getCustomerName());
                dto.setBranchAddress(customerDetailsPayload.getAddress());
                dto.setRegionalOffice(customerDetailsPayload.getLevelName());
                dto.setBranchEmail(customerDetailsPayload.getEmail());
                dto.setCustomerCode(customerDetailsPayload.getCustomerCode());
            } else {
                logger.warn("customerDetailsPayload is null for entity ID: {}.", entity.getId());
            }

            if (customerPayload != null) {
                dto.setCustomerName(customerPayload.getCustomerName());
                dto.setDistrict(customerPayload.getDistrict());
                dto.setMinNoVisits(customerPayload.getMinNoVisits());
            } else {
                logger.warn("customerPayload is null for entity ID: {}.", entity.getId());
            }

            if (entity.getCctvProductDetail() != null) {
                try {
                    CCTVProductDetailDTO productDto = new CCTVProductDetailDTO();
                    productDto.setCctvSystemType(entity.getCctvProductDetail().getCctvSystemType());
                    productDto.setDvrNvrMake(entity.getCctvProductDetail().getDvrNvrMake());
                    productDto.setDvrNvrModel(entity.getCctvProductDetail().getDvrNvrModel());
                    productDto.setBuiltDate(entity.getCctvProductDetail().getBuiltDate());
                    productDto.setTotalCameraPort(entity.getCctvProductDetail().getTotalCameraPort());
                    productDto.setIsRackInstalled(entity.getCctvProductDetail().getIsRackInstalled());
                    productDto.setDateOfInstallation(entity.getCctvProductDetail().getDateOfInstallation());
                    dto.setCctvProductDetail(productDto);
                } catch (Exception e) {
                    logger.error("Error mapping product detail to DTO for ID '{}': {}", entity.getId(), e.getMessage(), e);
                }
            }

            if (entity.getCctvHddStatus() != null) {
                try {
                    dto.setCctvHddStatus(entity.getCctvHddStatus().stream().map(hdd -> {
                        CCTVHddStatusDTO hddDto = new CCTVHddStatusDTO();
                        hddDto.setItemName(hdd.getItemName());
                        hddDto.setCapacity(hdd.getCapacity());
                        hddDto.setWorkingStatus(hdd.getWorkingStatus());
                        return hddDto;
                    }).collect(Collectors.toList()));
                } catch (Exception e) {
                    logger.error("Error mapping HDD status to DTO for ID '{}': {}", entity.getId(), e.getMessage(), e);
                }
            }

            if (entity.getCctvCameraStatus() != null) {
                try {
                    dto.setCctvCameraStatus(entity.getCctvCameraStatus().stream().map(camera -> {
                        CCTVCameraStatusDTO cameraDto = new CCTVCameraStatusDTO();
                        cameraDto.setCameraLocation(camera.getCameraLocation());
                        cameraDto.setCameraType(camera.getCameraType());
                        cameraDto.setCameraWorkingStatus(camera.getCameraWorkingStatus());
                        return cameraDto;
                    }).collect(Collectors.toList()));
                } catch (Exception e) {
                    logger.error("Error mapping camera status to DTO for ID '{}': {}", entity.getId(), e.getMessage(), e);
                }
            }

            if (entity.getCctvMaterials() != null) {
                try {
                    dto.setMaterials(entity.getCctvMaterials().stream().map(material -> {
                        CCTVMaterialDTO materialDto = new CCTVMaterialDTO();
                        materialDto.setItemDescription(material.getItemDescription());
                        materialDto.setQuantity(material.getQuantity());
                        materialDto.setIsChargeable(material.getIsChargeable());
                        if (material.getMaterialType() != null) {
                            materialDto.setMaterialType(CCTVMaterialDTO.MaterialType.valueOf(material.getMaterialType().name()));
                        }
                        return materialDto;
                    }).collect(Collectors.toList()));
                } catch (Exception e) {
                    logger.error("Error mapping materials to DTO for ID '{}': {}", entity.getId(), e.getMessage(), e);
                }
            }

            if (entity.getCctvStaffMembers() != null) {
                try {
                    dto.setStaffMembers(entity.getCctvStaffMembers().stream().map(staff -> {
                        CCTVStaffMemberDTO staffDto = new CCTVStaffMemberDTO();
                        staffDto.setName(staff.getName());
                        staffDto.setDesignation(staff.getDesignation());
                        return staffDto;
                    }).collect(Collectors.toList()));
                } catch (Exception e) {
                    logger.error("Error mapping staff members to DTO for ID '{}': {}", entity.getId(), e.getMessage(), e);
                }
            }

            if (entity.getCctvProductInstalledStatuses() != null) {
                try {
                    dto.setCctvProductInstalledStatus(entity.getCctvProductInstalledStatuses().stream().map(status -> {
                        CCTVProductInstalledStatusDTO statusDto = new CCTVProductInstalledStatusDTO();
                        statusDto.setItemCategory(status.getItemCategory());
                        statusDto.setQuantity(status.getQuantity());
                        statusDto.setDetails(status.getDetails());

                        if (status.getSubItems() != null) {
                            try {
                                statusDto.setSubItems(status.getSubItems().stream().map(sub -> {
                                    CCTVProductInstalledSubStatusDTO subDto = new CCTVProductInstalledSubStatusDTO();
                                    subDto.setItemName(sub.getItemName());
                                    subDto.setQuantity(sub.getQuantity());
                                    subDto.setDetails(sub.getDetails());
                                    subDto.setWorkingStatus(sub.getWorkingStatus());
                                    subDto.setSafetyCabinetStatus(sub.getSafetyCabinetStatus());
                                    return subDto;
                                }).collect(Collectors.toList()));
                            } catch (Exception e) {
                                logger.error("Error mapping sub-items to DTO for status category '{}': {}", status.getItemCategory(), e.getMessage(), e);
                            }
                        }
                        return statusDto;
                    }).collect(Collectors.toList()));
                } catch (Exception e) {
                    logger.error("Error mapping installed product statuses to DTO for ID '{}': {}", entity.getId(), e.getMessage(), e);
                }
            }

            if (entity.getCctvBranchDocuments() != null && !entity.getCctvBranchDocuments().isEmpty()) {
                try {
                    List<CCTVBranchDocument> docs = cctvBranchDocumentRepository.findDocumentsByBranchInfoId(entity.getId());
                    if (docs != null && !docs.isEmpty()) {
                        Map<Long, String> base64Map = imageStorageService.readImagesAsBase64Parallel(docs);
                        List<CCTVBranchDocumentDTO> documentDTOs = docs.stream().filter(Objects::nonNull).map(doc -> {
                            try {
                                CCTVBranchDocumentDTO docDto = new CCTVBranchDocumentDTO();
                                docDto.setDocumentId(doc.getDocumentId());
                                docDto.setDocumentName(doc.getDocumentName());
                                docDto.setDocumentPath(doc.getDocumentPath());
                                docDto.setCreatedOn(doc.getCreatedOn());
                                docDto.setModifiedOn(doc.getCreatedOn());
                                docDto.setBase64Image(base64Map.getOrDefault(doc.getDocumentId(), null));
                                return docDto;
                            } catch (Exception e) {
                                logger.error("Error mapping document ID: {}, Error: {}", doc.getDocumentId(), e.getMessage());
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList());
                        dto.setDocuments(documentDTOs);
                    } else {
                        dto.setDocuments(Collections.emptyList());
                    }
                } catch (Exception e) {
                    logger.error("Error fetching documents for branchId {}: {}", entity.getId(), e.getMessage(), e);
                    dto.setDocuments(Collections.emptyList());
                }
            }

            logger.debug("Entity to response DTO mapping completed for ID: {}", entity.getId());
            return dto;

        } catch (Exception e) {
            logger.error("Critical error in mapEntityToResponseDto for ID '{}': {}", entity != null ? entity.getId() : "null", e.getMessage(), e);
            throw new RuntimeException("Failed to map entity to response DTO: " + e.getMessage(), e);
        }
    }

    private CCTVBranchInfoResponseDTO mapEntityToListResponseDto(CCTVBranchInfo entity,
            CustomerResponse customerDetailsPayload, CustomerResponse customerPayload) {

        if (entity == null) {
            logger.warn("CCTVBranchInfo entity is null in mapEntityToListResponseDto");
            return null;
        }
        try {
            CCTVBranchInfoResponseDTO dto = new CCTVBranchInfoResponseDTO();

            dto.setId(entity.getId());
            dto.setCustomerId(entity.getCustomerId());
            dto.setScheduleVisitId(entity.getScheduleVisitId());
            dto.setInspectionDate(entity.getInspectionDate());
            dto.setFieldAssociateName(entity.getFieldAssociateName());
            dto.setServiceReportNumber(entity.getServiceReportNumber());
            dto.setTotalHddSlots(entity.getTotalHddSlots());
            dto.setHddInstalledQty(entity.getHddInstalledQty());
            dto.setTotalCapacityTb(entity.getTotalCapacityTb());
            dto.setTotalFreeSpaceTb(entity.getTotalFreeSpaceTb());
            dto.setIsPasswordChanged(entity.getIsPasswordChanged());
            dto.setIsSystemCleaned(entity.getIsSystemCleaned());
            dto.setIsMotionDetectionSet(entity.getIsMotionDetectionSet());
            dto.setIsOperationExplainedAndUnderstood(entity.getIsOperationExplainedAndUnderstood());
            dto.setIsRecordingAvailable(entity.getIsRecordingAvailable());
            dto.setAreAllCamerasFunctional(entity.getAreAllCamerasFunctional());
            dto.setIsAcSupplyUninterrupted(entity.getIsAcSupplyUninterrupted());
            dto.setIsSystemOnUps(entity.getIsSystemOnUps());
            dto.setConcernedPersonName(entity.getConcernedPersonName());
            dto.setConcernedPersonPhone(entity.getConcernedPersonPhone());
            dto.setConcernedPersonDesignation(entity.getConcernedPersonDesignation());
            dto.setCctvWorkingStatus(entity.getCctvWorkingStatus());
            dto.setFaRemarks(entity.getFaRemarks());
            dto.setEstimateProductDetails(entity.getEstimateProductDetails());
            dto.setEstimateNo(entity.getEstimateNo());
            dto.setEstimateDate(entity.getEstimateDate());
            dto.setEstimateAmount(entity.getEstimateAmount());
            dto.setBillToBeRaised(entity.getBillToBeRaised());
            dto.setBillNo(entity.getBillNo());
            dto.setBillDate(entity.getBillDate());
            dto.setIsInvoicePaymentFollowed(entity.getIsInvoicePaymentFollowed());
            dto.setPaymentRemarks(entity.getPaymentRemarks());
            dto.setCustomerRemarks(entity.getCustomerRemarks());
            dto.setProductType(entity.getProductType());
            dto.setStatus(entity.getStatus());

            if (customerDetailsPayload != null) {
                dto.setBranchName(customerDetailsPayload.getCustomerName());
                dto.setBranchAddress(customerDetailsPayload.getAddress());
                dto.setRegionalOffice(customerDetailsPayload.getLevelName());
                dto.setBranchEmail(customerDetailsPayload.getEmail());
                dto.setCustomerCode(customerDetailsPayload.getCustomerCode());
            } else {
                logger.warn("customerDetailsPayload is null for entity ID: {} in list mapping.", entity.getId());
            }

            if (customerPayload != null) {
                dto.setCustomerName(customerPayload.getCustomerName());
                dto.setDistrict(customerPayload.getDistrict());
                dto.setMinNoVisits(customerPayload.getMinNoVisits());
            } else {
                logger.warn("customerPayload is null for entity ID: {} in list mapping.", entity.getId());
            }

            return dto;

        } catch (Exception e) {
            logger.error("Error mapping CCTVBranchInfo to list DTO for ID: {}. Error: {}", entity.getId(), e.getMessage(), e);
            throw e;
        }
    }

    public byte[] getPdfBytesById(Long id) {
        logger.info("Fetching PDF for CCTV ID: {}", id);
        try {
            CCTVBranchInfo entity = cctvBranchInfoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("CCTV record not found for ID: " + id));

            String pdfFilePath = entity.getPdfFilePath();

            if (pdfFilePath == null || pdfFilePath.isEmpty()) {
                logger.warn("PDF path is null or empty for ID: {}. Regenerating PDF.", id);
                return regenerateAndSavePdf(entity);
            }

            String fullPath = servletContext.getRealPath("/" + pdfFilePath);
            Path path = Paths.get(fullPath);

            if (!Files.exists(path)) {
                logger.warn("PDF file not found on disk: {}. Regenerating for ID: {}", fullPath, id);
                return regenerateAndSavePdf(entity);
            }

            byte[] pdfBytes = Files.readAllBytes(path);
            logger.info("PDF bytes read successfully for ID: {}. Size: {} bytes", id, pdfBytes.length);
            return pdfBytes;

        } catch (RuntimeException e) {
            logger.error("RuntimeException in getPdfBytesById for ID: {}. Error: {}", id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error in getPdfBytesById for ID: {}. Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch PDF for ID: " + id + ". " + e.getMessage(), e);
        }
    }

    private byte[] regenerateAndSavePdf(CCTVBranchInfo entity) {
        logger.info("Regenerating PDF for entity ID: {}", entity.getId());
        try {
            CCTVBranchInfoRequestDTO requestDTO = mapEntityToRequestDto(entity);

            CustomerResponse customerDetailsPayload = fetchCustomerDetailsByCustomerId(entity.getCustomerId());
            CustomerResponse customerPayload = fetchCustomerPayload(entity.getScheduleVisitId());

            if (customerDetailsPayload == null) {
                logger.warn("Could not fetch customer details for PDF regeneration. ID: {}, customerId: {}.", entity.getId(), entity.getCustomerId());
            }
            if (customerPayload == null) {
                logger.warn("Could not fetch customer details for PDF regeneration. ID: {}, scheduleVisitId: {}.", entity.getId(), entity.getScheduleVisitId());
            }

            requestDTO = enrichRequestDtoWithFeignData(requestDTO, customerDetailsPayload, customerPayload);

            ByteArrayOutputStream baos = generateCCTVServiceReport(requestDTO);
            if (baos == null || baos.size() == 0) {
                throw new RuntimeException("PDF could not be generated for ID: " + entity.getId());
            }

            byte[] pdfBytes = baos.toByteArray();

            try {
                String realPath = servletContext.getRealPath("/cctvPdf/");
                Path dirPath = Paths.get(realPath);
                Files.createDirectories(dirPath);

                String filename = "cctv_report_" + entity.getId() + ".pdf";
                Path filePath = dirPath.resolve(filename);
                Files.write(filePath, pdfBytes);

                entity.setPdfFilePath("cctvPdf/" + filename);
                cctvBranchInfoRepository.save(entity);
                logger.info("Regenerated PDF saved for ID: {} at: cctvPdf/{}", entity.getId(), filename);

                updateAssignmentStatusToCompleted(entity.getScheduleVisitId());

            } catch (Exception e) {
                logger.warn("Could not save regenerated PDF to disk for ID: {}. Error: {}", entity.getId(), e.getMessage());
            }

            return pdfBytes;

        } catch (Exception e) {
            logger.error("Failed to regenerate PDF for ID: {}. Error: {}", entity.getId(), e.getMessage(), e);
            throw new RuntimeException("PDF file missing and could not be regenerated for ID: " + entity.getId(), e);
        }
    }

    private CCTVBranchInfoRequestDTO mapEntityToRequestDto(CCTVBranchInfo entity) {
        if (entity == null) {
            logger.error("CCTVBranchInfo entity is null while mapping to RequestDTO");
            throw new RuntimeException("CCTVBranchInfo entity cannot be null");
        }
        try {
            CCTVBranchInfoRequestDTO dto = new CCTVBranchInfoRequestDTO();
            dto.setId(entity.getId());
            dto.setCustomerId(entity.getCustomerId());
            dto.setScheduleVisitId(entity.getScheduleVisitId());
            dto.setInspectionDate(entity.getInspectionDate());
            dto.setServiceReportNumber(entity.getServiceReportNumber());
            dto.setFieldAssociateName(entity.getFieldAssociateName());
            dto.setConcernedPersonName(entity.getConcernedPersonName());
            dto.setConcernedPersonPhone(entity.getConcernedPersonPhone());
            dto.setConcernedPersonDesignation(entity.getConcernedPersonDesignation());
            dto.setCctvWorkingStatus(entity.getCctvWorkingStatus());
            dto.setTotalHddSlots(entity.getTotalHddSlots());
            dto.setHddInstalledQty(entity.getHddInstalledQty());
            dto.setTotalCapacityTb(entity.getTotalCapacityTb());
            dto.setTotalFreeSpaceTb(entity.getTotalFreeSpaceTb());
            dto.setIsAcSupplyUninterrupted(entity.getIsAcSupplyUninterrupted());
            dto.setIsSystemOnUps(entity.getIsSystemOnUps());
            dto.setAreAllCamerasFunctional(entity.getAreAllCamerasFunctional());
            dto.setIsRecordingAvailable(entity.getIsRecordingAvailable());
            dto.setIsOperationExplainedAndUnderstood(entity.getIsOperationExplainedAndUnderstood());
            dto.setIsMotionDetectionSet(entity.getIsMotionDetectionSet());
            dto.setIsSystemCleaned(entity.getIsSystemCleaned());
            dto.setIsPasswordChanged(entity.getIsPasswordChanged());
            dto.setEstimateProductDetails(entity.getEstimateProductDetails());
            dto.setFaRemarks(entity.getFaRemarks());
            dto.setEstimateNo(entity.getEstimateNo());
            dto.setEstimateDate(entity.getEstimateDate());
            dto.setEstimateAmount(entity.getEstimateAmount());
            dto.setBillToBeRaised(entity.getBillToBeRaised());
            dto.setBillNo(entity.getBillNo());
            dto.setBillDate(entity.getBillDate());
            dto.setIsInvoicePaymentFollowed(entity.getIsInvoicePaymentFollowed());
            dto.setPaymentRemarks(entity.getPaymentRemarks());
            dto.setCustomerRemarks(entity.getCustomerRemarks());
            dto.setProductType(entity.getProductType());
            dto.setStatus(entity.getStatus());

            if (entity.getCctvProductDetail() != null) {
                CCTVProductDetailDTO pdto = new CCTVProductDetailDTO();
                pdto.setCctvSystemType(entity.getCctvProductDetail().getCctvSystemType());
                pdto.setDvrNvrMake(entity.getCctvProductDetail().getDvrNvrMake());
                pdto.setDvrNvrModel(entity.getCctvProductDetail().getDvrNvrModel());
                pdto.setBuiltDate(entity.getCctvProductDetail().getBuiltDate());
                pdto.setTotalCameraPort(entity.getCctvProductDetail().getTotalCameraPort());
                pdto.setIsRackInstalled(entity.getCctvProductDetail().getIsRackInstalled());
                pdto.setDateOfInstallation(entity.getCctvProductDetail().getDateOfInstallation());
                dto.setCctvProductDetail(pdto);
            }

            if (entity.getCctvCameraStatus() != null) {
                dto.setCctvCameraStatus(entity.getCctvCameraStatus().stream().map(c -> {
                    CCTVCameraStatusDTO cd = new CCTVCameraStatusDTO();
                    cd.setCameraLocation(c.getCameraLocation());
                    cd.setCameraType(c.getCameraType());
                    cd.setCameraWorkingStatus(c.getCameraWorkingStatus());
                    return cd;
                }).collect(Collectors.toList()));
            }

            if (entity.getCctvHddStatus() != null) {
                dto.setCctvHddStatus(entity.getCctvHddStatus().stream().map(h -> {
                    CCTVHddStatusDTO hd = new CCTVHddStatusDTO();
                    hd.setItemName(h.getItemName());
                    hd.setCapacity(h.getCapacity());
                    hd.setWorkingStatus(h.getWorkingStatus());
                    return hd;
                }).collect(Collectors.toList()));
            }

            if (entity.getCctvMaterials() != null) {
                dto.setMaterials(entity.getCctvMaterials().stream().map(m -> {
                    CCTVMaterialDTO md = new CCTVMaterialDTO();
                    md.setItemDescription(m.getItemDescription());
                    md.setQuantity(m.getQuantity());
                    md.setIsChargeable(m.getIsChargeable());
                    if (m.getMaterialType() != null) {
                        md.setMaterialType(CCTVMaterialDTO.MaterialType.valueOf(m.getMaterialType().name()));
                    }
                    return md;
                }).collect(Collectors.toList()));
            }

            if (entity.getCctvStaffMembers() != null) {
                dto.setStaffMembers(entity.getCctvStaffMembers().stream().map(s -> {
                    CCTVStaffMemberDTO sd = new CCTVStaffMemberDTO();
                    sd.setName(s.getName());
                    sd.setDesignation(s.getDesignation());
                    return sd;
                }).collect(Collectors.toList()));
            }

            if (entity.getCctvProductInstalledStatuses() != null) {
                dto.setCctvProductInstalledStatus(entity.getCctvProductInstalledStatuses().stream().map(ps -> {
                    CCTVProductInstalledStatusDTO psd = new CCTVProductInstalledStatusDTO();
                    psd.setItemCategory(ps.getItemCategory());
                    psd.setQuantity(ps.getQuantity());
                    psd.setDetails(ps.getDetails());
                    if (ps.getSubItems() != null) {
                        psd.setSubItems(ps.getSubItems().stream().map(sub -> {
                            CCTVProductInstalledSubStatusDTO subDto = new CCTVProductInstalledSubStatusDTO();
                            subDto.setItemName(sub.getItemName());
                            subDto.setQuantity(sub.getQuantity());
                            subDto.setDetails(sub.getDetails());
                            subDto.setWorkingStatus(sub.getWorkingStatus());
                            subDto.setSafetyCabinetStatus(sub.getSafetyCabinetStatus());
                            return subDto;
                        }).collect(Collectors.toList()));
                    }
                    return psd;
                }).collect(Collectors.toList()));
            }

            return dto;
        } catch (Exception e) {
            logger.error("Error mapping CCTVBranchInfo to RequestDTO. Error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to map CCTVBranchInfo to RequestDTO", e);
        }
    }
}