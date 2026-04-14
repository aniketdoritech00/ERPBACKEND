package com.doritech.PdfService.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.doritech.PdfService.Entity.CCTVBranchDocument;

@Repository
public interface CCTVBranchDocumentRepository extends JpaRepository<CCTVBranchDocument, Long> {

    @Query(value = "SELECT * FROM cctv_branch_documents WHERE cctv_branch_info_id = :branchInfoId",
           nativeQuery = true)
    List<CCTVBranchDocument> findDocumentsByBranchInfoId(@Param("branchInfoId") Long branchInfoId);
}