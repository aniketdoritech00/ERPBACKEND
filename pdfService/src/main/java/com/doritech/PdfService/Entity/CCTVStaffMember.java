package com.doritech.PdfService.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cctv_staff_members")
public class CCTVStaffMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cctv_branch_info_id", nullable = false)
	private CCTVBranchInfo cctvBranchInfo;

	@Column(name = "name", length = 255)
	private String name;

	@Column(name = "designation", length = 255)
	private String designation;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
}
