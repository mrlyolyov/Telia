package com.optima.cms.model.theme;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Paginated theme-override payload ({@code docs} + Magnolia / Payload-style paging fields).
 */
@Getter
@Setter
public class ThemeOverrideResponse {

	private List<ThemeOverride> docs;
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

	public static ThemeOverrideResponse empty() {
		ThemeOverrideResponse r = new ThemeOverrideResponse();
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
