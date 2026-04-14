package com.doritech.PdfService.Dto;

import java.util.List;

public class CCTVProductInstalledStatusDTO {

    private Long id;
    private Long cctvBranchInfoId;
    private String itemCategory;
    private String quantity;
    private String details;
    private List<CCTVProductInstalledSubStatusDTO> subItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCctvBranchInfoId() {
        return cctvBranchInfoId;
    }

    public void setCctvBranchInfoId(Long cctvBranchInfoId) {
        this.cctvBranchInfoId = cctvBranchInfoId;
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

    public List<CCTVProductInstalledSubStatusDTO> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<CCTVProductInstalledSubStatusDTO> subItems) {
        this.subItems = subItems;
    }
}
