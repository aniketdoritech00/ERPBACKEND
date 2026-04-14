// ─────────────────────────────────────────────────────────────────────────────
// FILE 1: FASBranchInfoRepository.java
// ─────────────────────────────────────────────────────────────────────────────
package com.doritech.PdfService.Repository;

import com.doritech.PdfService.Entity.FASBranchInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FASBranchInfoRepository extends JpaRepository<FASBranchInfo, Long> {

    Optional<FASBranchInfo> findByScheduleVisitIdAndProductType(Integer scheduleVisitId, String productType);
}


// ─────────────────────────────────────────────────────────────────────────────
// FILE 2: FASBranchDocumentRepository.java
// ─────────────────────────────────────────────────────────────────────────────
