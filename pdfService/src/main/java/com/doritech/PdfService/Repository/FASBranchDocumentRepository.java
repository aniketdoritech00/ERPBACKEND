package com.doritech.PdfService.Repository;

import com.doritech.PdfService.Entity.FASBranchDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FASBranchDocumentRepository extends JpaRepository<FASBranchDocument, Long> {

    @Query("SELECT d FROM FASBranchDocument d WHERE d.fasBranchInfo.id = :branchInfoId")
    List<FASBranchDocument> findDocumentsByBranchInfoId(@Param("branchInfoId") Long branchInfoId);
}