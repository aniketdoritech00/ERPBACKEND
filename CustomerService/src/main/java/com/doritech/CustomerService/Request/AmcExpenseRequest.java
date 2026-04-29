package com.doritech.CustomerService.Request;

import java.util.List;

public class AmcExpenseRequest {

	private Integer amcExpenseId;

	private String district;
	private Integer complaintFootageMultiplier;
	private Integer districtBasePrice;

	private List<RangeDto> ranges;

	public static class RangeDto {
		private Integer id;
		private Integer minRange;
		private Integer maxRange;
		private Integer multiplier;

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Integer getMinRange() {
			return minRange;
		}

		public void setMinRange(Integer minRange) {
			this.minRange = minRange;
		}

		public Integer getMaxRange() {
			return maxRange;
		}

		public void setMaxRange(Integer maxRange) {
			this.maxRange = maxRange;
		}

		public Integer getMultiplier() {
			return multiplier;
		}

		public void setMultiplier(Integer multiplier) {
			this.multiplier = multiplier;
		}
	}

	public Integer getAmcExpenseId() {
		return amcExpenseId;
	}

	public void setAmcExpenseId(Integer amcExpenseId) {
		this.amcExpenseId = amcExpenseId;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Integer getComplaintFootageMultiplier() {
		return complaintFootageMultiplier;
	}

	public void setComplaintFootageMultiplier(Integer complaintFootageMultiplier) {
		this.complaintFootageMultiplier = complaintFootageMultiplier;
	}

	public Integer getDistrictBasePrice() {
		return districtBasePrice;
	}

	public void setDistrictBasePrice(Integer districtBasePrice) {
		this.districtBasePrice = districtBasePrice;
	}

	public List<RangeDto> getRanges() {
		return ranges;
	}

	public void setRanges(List<RangeDto> ranges) {
		this.ranges = ranges;
	}

}