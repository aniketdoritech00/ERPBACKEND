package com.doritech.PdfService.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fas_branch_document")
public class FASBranchDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_path")
    private String documentPath;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fas_branch_info_id")
    private FASBranchInfo fasBranchInfo;

    public FASBranchDocument() {
    }

    public FASBranchDocument(Long documentId, String documentName, String documentPath, LocalDateTime createdOn, LocalDateTime modifiedOn, FASBranchInfo fasBranchInfo) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentPath = documentPath;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.fasBranchInfo = fasBranchInfo;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public FASBranchInfo getFasBranchInfo() {
        return fasBranchInfo;
    }

    public void setFasBranchInfo(FASBranchInfo fasBranchInfo) {
        this.fasBranchInfo = fasBranchInfo;
    }
}