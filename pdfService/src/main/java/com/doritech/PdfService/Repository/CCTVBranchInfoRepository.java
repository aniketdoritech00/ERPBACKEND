package com.doritech.PdfService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.PdfService.Entity.CCTVBranchInfo;

@Repository
public interface CCTVBranchInfoRepository extends JpaRepository<CCTVBranchInfo, Long> {

    Optional<CCTVBranchInfo> findByScheduleVisitIdAndProductType(Integer scheduleVisitId, String productType);

	Optional<CCTVBranchInfo> findByCustomerIdAndScheduleVisitIdAndProductType(Integer customerId,
			Integer scheduleVisitId, String productType);
}
