package com.doritech.PdfService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doritech.PdfService.Entity.CCTVBranchInfo;

import jakarta.transaction.Transactional;

@Repository
public interface CCTVBranchInfoRepository extends JpaRepository<CCTVBranchInfo, Long> {

	Optional<CCTVBranchInfo> findByScheduleVisitIdAndProductType(Integer scheduleVisitId, String productType);

	Optional<CCTVBranchInfo> findByCustomerIdAndScheduleVisitIdAndProductType(Integer customerId,
			Integer scheduleVisitId, String productType);

	@Modifying
	@Transactional
	@Query(value = "UPDATE employee_assignment SET status = 'Completed' WHERE assignment_id = :assignmentId", nativeQuery = true)
	int updateAssignmentStatusToCompleted(Integer assignmentId);
}
