package com.doritech.EmployeeService.response;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageResponseDTO<T> {

	private List<T> content;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;

	public PageResponseDTO(Page<T> page) {
		this.content = page.getContent();
		this.pageNumber = page.getNumber();
		this.pageSize = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}

	/**
	 * @return the content
	 */
	public List<T> getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(List<T> content) {
		this.content = content;
	}

	/**
	 * @return the pageNumber
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber
	 *            the pageNumber to set
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalElements
	 */
	public long getTotalElements() {
		return totalElements;
	}

	/**
	 * @param totalElements
	 *            the totalElements to set
	 */
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	/**
	 * @return the totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages
	 *            the totalPages to set
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	// getters/setters
}
