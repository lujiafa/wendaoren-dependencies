package com.wendaoren.web.model.dto;

import com.wendaoren.web.model.BaseDTO;


/**
 * @author Jon
 * @email lujiafayx@163.com
 * @date 2021年3月18日
 * @Description 分页查询DTO
 */
public class PageQueryDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private static final long DEFAULT_PAGE_SIZE = 10;
	private static final long DEFAULT_CURRENT_PAGE = 1;

	// 分页大小（缺省默认10）
	private Long pageSize;
	// 当前页码（缺省默认1）
	private Long currentPage;

	public Long getPageSize() {
		if (pageSize == null) {
			return DEFAULT_PAGE_SIZE;
		}
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getCurrentPage() {
		if (currentPage == null) {
			return DEFAULT_CURRENT_PAGE;
		}
		return currentPage;
	}

	public void setCurrentPage(Long currentPage) {
		this.currentPage = currentPage;
	}

}
