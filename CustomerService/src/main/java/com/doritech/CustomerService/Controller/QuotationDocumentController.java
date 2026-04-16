package com.doritech.CustomerService.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Request.QuotationDocumentRequest;
import com.doritech.CustomerService.Service.QuotationDocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer/api/quotation-document")
@Validated
public class QuotationDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(QuotationDocumentController.class);

    private final QuotationDocumentService service;

    public QuotationDocumentController(QuotationDocumentService service) {
        this.service = service;
    }

    @PostMapping(value = "/save-update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity saveUpdateDocument(
            @RequestHeader("X-User-Id") String userId,
            @RequestPart("data") @Valid List<@Valid QuotationDocumentRequest> requests,
            @RequestPart("files") List<MultipartFile> files) {

        int userIdInt = Integer.parseInt(userId);
        requests.forEach(r -> {
            r.setCreatedBy(userIdInt);
            r.setModifiedBy(userIdInt);
        });

        logger.info("API called: saveUpdateDocument by user {}", userIdInt);
        return service.saveUpdateDocument(requests, files);
    }

    @GetMapping("/getByDocumentId/{id}")
    public ResponseEntity getById(@PathVariable Integer id) {
        logger.info("API called: getByDocumentId for id {}", id);
        return service.getById(id);
    }

    @GetMapping("/getAllDocument")
    public ResponseEntity getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("API called: getAllDocument page {} size {}", page, size);
        return service.getAllDocument(page, size);
    }

    @GetMapping("/getByQuotationId/{quotationId}")
    public ResponseEntity getByQuotationId(@PathVariable Integer quotationId) {
        logger.info("API called: getByQuotationId for quotationId {}", quotationId);
        return service.getByQuotationId(quotationId);
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity deleteMultiple(@RequestBody List<Integer> ids) {
        logger.info("API called: deleteMultiple with ids {}", ids);
        return service.deleteMultiple(ids);
    }
}