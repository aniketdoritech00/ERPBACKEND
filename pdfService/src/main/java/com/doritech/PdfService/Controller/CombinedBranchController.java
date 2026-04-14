package com.doritech.PdfService.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.PdfService.Response.ResponseEntity;
import com.doritech.PdfService.Service.CombinedBranchInfoService;

@RestController
@RequestMapping("/pdf/api")
public class CombinedBranchController {

    @Autowired
    private CombinedBranchInfoService combinedBranchInfoService;

    private static final Logger logger =
            LoggerFactory.getLogger(CombinedBranchController.class);

    @GetMapping("/getAllBranchInfo")
    public ResponseEntity getAllBranchInfo(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "id") String sortBy,
                                           @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("GET /getAllBranchInfo | page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        try {
            Map<String, Object> data =
                    combinedBranchInfoService.getAllCombinedBranchInfo(page, size, sortBy, sortDir);
            return new ResponseEntity("Branch info fetched successfully",
                    HttpStatus.OK.value(), data);

        } catch (Exception e) {
            logger.error("GET /getAllBranchInfo | Error | {}", e.getMessage(), e);
            return new ResponseEntity("Failed to fetch branch info: "
                    + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }
}