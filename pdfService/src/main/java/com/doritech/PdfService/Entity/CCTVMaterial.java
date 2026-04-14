package com.doritech.PdfService.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cctv_materials")
public class CCTVMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum MaterialType {
        REPLACED, REQUIRED
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cctv_branch_info_id", nullable = false)
    private CCTVBranchInfo cctvBranchInfo;

    @Column(name = "item_description", length = 500)
    private String itemDescription;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_chargeable")
    private Boolean isChargeable;

    private MaterialType materialType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CCTVBranchInfo getCctvBranchInfo() {
        return cctvBranchInfo;
    }

    public void setCctvBranchInfo(CCTVBranchInfo cctvBranchInfo) {
        this.cctvBranchInfo = cctvBranchInfo;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsChargeable() {
        return isChargeable;
    }

    public void setIsChargeable(Boolean isChargeable) {
        this.isChargeable = isChargeable;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }
    
}
