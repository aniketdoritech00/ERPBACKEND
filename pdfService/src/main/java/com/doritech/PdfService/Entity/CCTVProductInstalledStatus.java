package com.doritech.PdfService.Entity;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "cctv_product_installed_status")
public class CCTVProductInstalledStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cctv_branch_info_id", nullable = false)
    private CCTVBranchInfo cctvBranchInfo;

    @Column(name = "item_category", length = 255)
    private String itemCategory;

    @Column(name = "quantity", length = 50)
    private String quantity;

    @Column(name = "details", length = 255)
    private String details;

    @OneToMany(mappedBy = "parentStatus", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CCTVProductInstalledSubStatus> subItems;

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

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<CCTVProductInstalledSubStatus> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<CCTVProductInstalledSubStatus> subItems) {
        this.subItems = subItems;
    }
}
