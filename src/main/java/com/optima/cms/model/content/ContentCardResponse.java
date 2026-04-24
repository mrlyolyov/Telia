package com.optima.cms.model.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Paginated content-card payload ({@code docs} + Magnolia-style paging fields).
 */
@Getter
@Setter
public class ContentCardResponse {

	private List<ContentCard> docs;
	private int totalDocs;
	private int limit;
	private int totalPages;
	private int page;
	private int pagingCounter;
	private boolean hasPrevPage;
	private boolean hasNextPage;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Integer prevPage;
	@JsonInclude(JsonInclude.Include.ALWAYS)
	private Integer nextPage;

	public static ContentCardResponse empty() {
		ContentCardResponse r = new ContentCardResponse();
		r.setDocs(List.of());
		r.setTotalDocs(0);
		r.setLimit(10);
		r.setTotalPages(0);
		r.setPage(1);
		r.setPagingCounter(0);
		r.setHasPrevPage(false);
		r.setHasNextPage(false);
		r.setPrevPage(null);
		r.setNextPage(null);
		return r;
	}
}
