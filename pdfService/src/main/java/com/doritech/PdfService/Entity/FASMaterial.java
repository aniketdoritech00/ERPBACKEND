package com.doritech.PdfService.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fas_material")
public class FASMaterial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "item_description")
	private String itemDescription;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "is_chargeable")
	private Boolean isChargeable;

	@Enumerated(EnumType.STRING)
	@Column(name = "material_type")
	private MaterialType materialType;

	@Column(name = "remarks")
	private String remarks;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fas_branch_info_id")
	private FASBranchInfo fasBranchInfo;

	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
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



	public String getRemarks() {
		return remarks;
	}



	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}



	public FASBranchInfo getFasBranchInfo() {
		return fasBranchInfo;
	}



	public void setFasBranchInfo(FASBranchInfo fasBranchInfo) {
		this.fasBranchInfo = fasBranchInfo;
	}



	public enum MaterialType {
		REPLACED, REQUIRED
	}
}