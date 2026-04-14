package com.doritech.PdfService.Response;

import org.springframework.data.domain.Page;

public class CombinedBranchInfoResponseDTO {
	private Page<CCTVBranchInfoResponseDTO> cctvData;
	private Page<FASBranchInfoResponseDTO> fasData;

	public Page<CCTVBranchInfoResponseDTO> getCctvData() {
		return cctvData;
	}

	public void setCctvData(Page<CCTVBranchInfoResponseDTO> cctvData) {
		this.cctvData = cctvData;
	}

	public Page<FASBranchInfoResponseDTO> getFasData() {
		return fasData;
	}

	public void setFasData(Page<FASBranchInfoResponseDTO> fasData) {
		this.fasData = fasData;
	}

	public CombinedBranchInfoResponseDTO(Page<CCTVBranchInfoResponseDTO> cctvData,
			Page<FASBranchInfoResponseDTO> fasData) {
		super();
		this.cctvData = cctvData;
		this.fasData = fasData;
	}

}