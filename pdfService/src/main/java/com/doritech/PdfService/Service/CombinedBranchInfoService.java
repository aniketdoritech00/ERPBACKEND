package com.doritech.PdfService.Service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.doritech.PdfService.Response.CCTVBranchInfoResponseDTO;
import com.doritech.PdfService.Response.FASBranchInfoResponseDTO;

@Service
public class CombinedBranchInfoService {

    private static final Logger logger = LoggerFactory.getLogger(CombinedBranchInfoService.class);

    @Autowired
    private CCTVBranchInfoService cctvBranchInfoService;

    @Autowired
    private FASBranchInfoService fasBranchInfoService;

    public Map<String, Object> getAllCombinedBranchInfo(int page, int size,
                                                        String sortBy, String sortDir) {

        logger.info("Fetching combined branch info | page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);

        try {
            Page<CCTVBranchInfoResponseDTO> cctvPage =
                    cctvBranchInfoService.getAllCCTVInfo(page, size, sortBy, sortDir);

            Page<FASBranchInfoResponseDTO> fasPage =
                    fasBranchInfoService.getAllFASInfo(page, size, sortBy, sortDir);

            Map<String, Object> cctvData = new HashMap<>();
            cctvData.put("content", cctvPage.getContent());
            cctvData.put("currentPage", cctvPage.getNumber());
            cctvData.put("totalPages", cctvPage.getTotalPages());
            cctvData.put("totalElements", cctvPage.getTotalElements());

            Map<String, Object> fasData = new HashMap<>();
            fasData.put("content", fasPage.getContent());
            fasData.put("currentPage", fasPage.getNumber());
            fasData.put("totalPages", fasPage.getTotalPages());
            fasData.put("totalElements", fasPage.getTotalElements());

            Map<String, Object> response = new HashMap<>();
            response.put("cctvData", cctvData);
            response.put("fasData", fasData);

            logger.info("Combined fetch successful | CCTV total={}, FAS total={}",
                    cctvPage.getTotalElements(), fasPage.getTotalElements());

            return response;

        } catch (Exception e) {
            logger.error("Error fetching combined branch info | {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch combined branch info: " + e.getMessage(), e);
        }
    }
}